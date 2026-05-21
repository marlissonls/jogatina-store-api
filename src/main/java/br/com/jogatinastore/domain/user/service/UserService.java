package br.com.jogatinastore.domain.user.service;

import br.com.jogatinastore.domain.authorization.exception.AuthorizationErrors;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.domain.user.exception.UserErrors;
import br.com.jogatinastore.domain.authorization.entity.Permission;
import br.com.jogatinastore.domain.user.entity.User;
import br.com.jogatinastore.domain.user.dto.CreateUserDTO;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import br.com.jogatinastore.shared.PageResponse;
import br.com.jogatinastore.domain.user.dto.UpdateUserDTO;
import br.com.jogatinastore.domain.user.dto.UserResponseDTO;
import br.com.jogatinastore.domain.user.mapper.UserMapper;
import br.com.jogatinastore.domain.authorization.repository.PermissionRepository;
import br.com.jogatinastore.domain.user.repository.UserRepository;
import br.com.jogatinastore.domain.authorization.code.PermissionCode;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PermissionRepository permissionRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            PermissionRepository permissionRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResponse<UserResponseDTO> findAll(Pageable pageable) {

        logger.debug("Finding all User");

        var page = repository.findAll(pageable);

        var users = repository.findAllWithPermissionsByIdIn(
            page.getContent().stream().map(User::getId).toList()
        );

        var items = users.stream()
            .map(userMapper::toResponse)
            .toList();

        return new PageResponse<>(
            items,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }

    public UserResponseDTO findById(UUID id) {

        logger.debug("Starting findById userId={}", id);

        User user = findByIdWithPermissions(id);
        UserResponseDTO response = userMapper.toResponse(user);

        logger.info("Successfully retrieved user userId={}", id);

        return response;
    }

    public UserResponseDTO me(AuthenticatedUser auth) {

        UUID userId = UUID.fromString(auth.id());

        logger.debug("Fetching authenticated user profile for userId={}", userId);

        User user = findByIdWithPermissions(userId);
        UserResponseDTO response = userMapper.toResponse(user);

        logger.info("Successfully retrieved user profile for userId={}", userId);

        return response;
    }

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {

        logger.debug("Creating one User");

        User user = buildUser(dto);
        validateUserUniqueness(user);
        assignDefaultPermission(user);
        encodePassword(dto, user);
        User savedUser = repository.save(user);

        logger.info("Successfully created one User userId={}", savedUser.getId());

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDTO update(UUID id, UpdateUserDTO dto) {

        logger.debug("Updating user by id: {}", id);

        User entity = findByIdWithPermissions(id);
        applyUpdate(dto, entity);
        User updatedUser = repository.save(entity);

        logger.info("Successfully updated user userId={}", entity.getId());

        return userMapper.toResponse(updatedUser);
    }

    @Transactional
    public void delete(UUID id) {

        logger.debug("Deleting user by userId={}", id);

        User entity = findEntityById(id);

        logger.info("Successfully Deleted user userId={}", id);

        repository.delete(entity);
    }

    @Transactional
    public void deactivate(UUID id) {

        logger.debug("Deactivating user by userId={}", id);

        repository.deactivate(id);

        logger.info("Successfully deactivated user userId={}", id);
    }

    @Transactional
    public void activate(UUID id) {

        logger.debug("Activating user by userId={}", id);

        repository.activate(id);

        logger.info("Successfully activated user userId={}", id);
    }

    private User findEntityById(UUID id) {

        logger.debug("Fetching user userId={}", id);

        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warn("User not found userId={}", id);
                return new ResourceNotFoundException(UserErrors.Target.ID, UserErrors.Code.USER_NOT_FOUND);
            });
    }

    private User findByIdWithPermissions(UUID id) {

        logger.debug("Fetching user with permissions userId={}", id);

        return repository.findByIdWithPermissions(id)
            .orElseThrow(() -> {
                logger.warn("User not found userId={}", id);
                return new ResourceNotFoundException(UserErrors.Target.ID, UserErrors.Code.USER_NOT_FOUND);
            });
    }

    private User buildUser(CreateUserDTO dto) {
        return userMapper.toEntity(dto);
    }

    private void validateUserUniqueness(User user) {
        if (repository.existsAnyByEmailIncludingDeleted(user.getEmail()) > 0)
            throw new ConflictException(UserErrors.Target.EMAIL, UserErrors.Code.USER_EMAIL_ALREADY_EXISTS);

        if (repository.existsAnyByCpfIncludingDeleted(user.getCpf()) > 0)
            throw new ConflictException(UserErrors.Target.CPF, UserErrors.Code.USER_CPF_ALREADY_EXISTS);
    }

    private void assignDefaultPermission(User user) {
        Permission defaultPerm = permissionRepository.findByTitle(PermissionCode.ROLE_CUSTOMER.key())
            .orElseThrow(() -> new ResourceNotFoundException(
                AuthorizationErrors.Target.PERMISSION_TITLE,
                AuthorizationErrors.Code.PERMISSION_NOT_FOUND
        ));
        user.addPermission(defaultPerm);
    }

    private void encodePassword(CreateUserDTO dto, User user) {
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
    }

    private static void applyUpdate(UpdateUserDTO dto, User entity) {
        entity.setName(dto.name());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setBirthDate(dto.birthDate());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+ email +" not found!"));
    }
}