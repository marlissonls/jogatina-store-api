package br.com.jogatinastore.domain.iam.user.service;

import br.com.jogatinastore.domain.iam.authorization.entity.Role;
import br.com.jogatinastore.domain.iam.authorization.exception.AuthorizationErrors;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import br.com.jogatinastore.domain.iam.user.entity.User;
import br.com.jogatinastore.domain.iam.user.dto.CreateUserDTO;
import br.com.jogatinastore.shared.PageResponse;
import br.com.jogatinastore.domain.iam.user.dto.UserResponseDTO;
import br.com.jogatinastore.domain.iam.user.mapper.UserMapper;
import br.com.jogatinastore.domain.iam.authorization.repository.RoleRepository;
import br.com.jogatinastore.domain.iam.user.repository.UserRepository;
import br.com.jogatinastore.domain.iam.authorization.code.RoleCode;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            RoleRepository roleRepository,
            UserMapper mapper,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResponse<UserResponseDTO> findAll(Pageable pageable) {

        logger.debug("Fetching all users. pagination={}", pageable);

        var page = repository.findAll(pageable);

        var users = repository.findAllWithRolesByIdIn(
            page.getContent().stream().map(User::getId).toList()
        );

        var items = users.stream()
            .map(mapper::toResponse)
            .toList();

        return new PageResponse<>(
            items,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }

    public UserResponseDTO findById(UUID id) {

        logger.debug("Fetching user. userId={}", id);

        User user = findByIdWithRoles(id);
        UserResponseDTO response = mapper.toResponse(user);

        logger.info("Successfully retrieved user. userId={}", id);

        return response;
    }

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {

        logger.debug("Creating new user entity. data={}", dto);

        User user = buildUser(dto);
        validateUserUniqueness(user);
        assignDefaultRole(user);
        encodePassword(dto, user);
        User savedUser = repository.save(user);

        logger.info("User created successfully. userId={}", savedUser.getId());

        return mapper.toResponse(savedUser);
    }

    @Transactional
    public void delete(UUID id) {

        logger.debug("Deleting user. userId={}", id);

        User entity = findEntityById(id);

        logger.info("User deleted successfully. userId={}", id);

        repository.delete(entity);
    }

    @Transactional
    public void deactivate(UUID id) {

        logger.debug("Deactivating user. userId={}", id);

        repository.deactivate(id);

        logger.info("User deactivated successfully. userId={}", id);
    }

    @Transactional
    public void activate(UUID id) {

        logger.debug("Activating user. userId={}", id);

        repository.activate(id);

        logger.info("User activated successfully. userId={}", id);
    }

    private User findEntityById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> {
                logger.warn("User not found. userId={}", id);

                return new ResourceNotFoundException(
                        UserErrors.Target.ID,
                        UserErrors.Code.USER_NOT_FOUND);
            });
    }

    private User findByIdWithRoles(UUID id) {

        logger.debug("Fetching user with roles. userId={}", id);

        return repository.findByIdWithRoles(id)
            .orElseThrow(() -> {
                logger.warn("User not found. userId={}", id);
                return new ResourceNotFoundException(
                        UserErrors.Target.ID,
                        UserErrors.Code.USER_NOT_FOUND);
            });
    }

    private User buildUser(CreateUserDTO dto) {
        return mapper.toEntity(dto);
    }

    private void validateUserUniqueness(User user) {
        if (repository.existsByEmail(user.getEmail()))
            throw new ConflictException(UserErrors.Target.EMAIL, UserErrors.Code.USER_EMAIL_ALREADY_EXISTS);
    }

    private void assignDefaultRole(User user) {
        Role defaultPerm = roleRepository.findByTitle(RoleCode.ROLE_CUSTOMER.key())
            .orElseThrow(() -> new ResourceNotFoundException(
                AuthorizationErrors.Target.ROLE_TITLE,
                AuthorizationErrors.Code.ROLE_NOT_FOUND
        ));
        user.addRole(defaultPerm);
    }

    private void encodePassword(CreateUserDTO dto, User user) {
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
    }

    public User getValidReference(UUID id) {
        logger.debug("Fetching user reference. id={}", id);

        validateEntity(id);

        return repository.getReferenceById(id);
    }

    private void validateEntity(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    UserErrors.Target.ID,
                    UserErrors.Code.USER_NOT_FOUND
            );
        }
    }
}