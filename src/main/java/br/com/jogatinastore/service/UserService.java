package br.com.jogatinastore.service;

import br.com.jogatinastore.exception.ConflictException;
import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.exception.messages.PermissionErrorCode;
import br.com.jogatinastore.exception.messages.PermissionErrorTarget;
import br.com.jogatinastore.exception.messages.UserErrorCode;
import br.com.jogatinastore.exception.messages.UserErrorTarget;
import br.com.jogatinastore.model.user.Permission;
import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import br.com.jogatinastore.model.user.mapper.UserMapper;
import br.com.jogatinastore.repository.PermissionRepository;
import br.com.jogatinastore.repository.UserRepository;
import br.com.jogatinastore.security.permission.RolePermissionEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UserResponseDTO> findAll() {

        logger.debug("Finding all User");

        return userMapper.toResponseList(repository.findAllWithPermissions());
    }

    public UserResponseDTO findById(UUID id) {

        logger.debug("Starting findById userId={}", id);

        User user = findByIdWithPermissions(id);
        UserResponseDTO response = userMapper.toResponse(user);

        logger.info("Successfully retrieved user userId={}", id);

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
    public UserResponseDTO update(UpdateUserDTO dto) {

        logger.debug("Updating user by id: {}", dto.id());

        User entity = findByIdWithPermissions(dto.id());
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

    private User findEntityById(UUID id) {

        logger.debug("Fetching user userId={}", id);

        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warn("User not found userId={}", id);
                return new ResourceNotFoundException(UserErrorTarget.ID, UserErrorCode.USER_NOT_FOUND);
            });
    }

    private User findByIdWithPermissions(UUID id) {

        logger.debug("Fetching user with permissions userId={}", id);

        return repository.findByIdWithPermissions(id)
            .orElseThrow(() -> {
                logger.warn("User not found userId={}", id);
                return new ResourceNotFoundException(UserErrorTarget.ID, UserErrorCode.USER_NOT_FOUND);
            });
    }

    private User buildUser(CreateUserDTO dto) {
        return userMapper.toEntity(dto);
    }

    private void validateUserUniqueness(User user) {
        if (repository.existsAnyByEmailIncludingDeleted(user.getEmail()) > 0)
            throw new ConflictException(UserErrorTarget.EMAIL, UserErrorCode.USER_EMAIL_ALREADY_EXISTS);

        if (repository.existsAnyByCpfIncludingDeleted(user.getCpf()) > 0)
            throw new ConflictException(UserErrorTarget.CPF, UserErrorCode.USER_CPF_ALREADY_EXISTS);
    }

    private void assignDefaultPermission(User user) {
        Permission defaultPerm = permissionRepository.findByTitle(RolePermissionEnum.ROLE_CUSTOMER.key())
            .orElseThrow(() -> new ResourceNotFoundException(
                PermissionErrorTarget.TITLE,
                PermissionErrorCode.PERMISSION_NOT_FOUND
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