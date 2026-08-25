package br.com.jogatinastore.domain.iam.user.service;

import br.com.jogatinastore.domain.iam.role.code.RoleCode;
import br.com.jogatinastore.domain.iam.role.entity.Role;
import br.com.jogatinastore.domain.iam.role.service.RoleService;
import br.com.jogatinastore.domain.iam.user.dto.CreateEmployeeDTO;
import br.com.jogatinastore.domain.iam.user.dto.CreateUserDTO;
import br.com.jogatinastore.domain.iam.user.dto.UpdateUserRoleDTO;
import br.com.jogatinastore.domain.iam.user.dto.UserResponseDTO;
import br.com.jogatinastore.domain.iam.user.exception.UserErrors;
import br.com.jogatinastore.domain.iam.user.entity.User;
import br.com.jogatinastore.domain.iam.user.mapper.UserMapper;
import br.com.jogatinastore.domain.iam.user.repository.UserRepository;
import br.com.jogatinastore.infra.exception.ConflictException;
import br.com.jogatinastore.infra.exception.ResourceNotFoundException;
import br.com.jogatinastore.shared.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    private final RoleService roleService;

    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            RoleService roleService,
            UserMapper mapper,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.roleService = roleService;
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

        validateUniquenessByEmail(dto.email());

        User user = mapper.toEntity(dto);

        Role defaultRole = roleService.getByTitle(RoleCode.ROLE_CUSTOMER.name());

        user.assignRole(defaultRole);

        setEncodedPassword(user, dto.password());

        User savedUser = repository.save(user);

        logger.info("User created successfully. userId={}", savedUser.getId());

        return mapper.toResponse(savedUser);
    }

    @Transactional
    public UserResponseDTO createEmployee(CreateEmployeeDTO dto) {

        logger.debug("Creating new user employee entity. data={}", dto);

        validateUniquenessByEmail(dto.email());

        User user = mapper.toEmployeeEntity(dto);

        List<Role> roles = roleService.findAllByIdIn(dto.roleIds());

        user.assignRoles(roles);

        setEncodedPassword(user, dto.password());

        User savedUser = repository.save(user);

        logger.info("User employee created successfully. userId={}, roles={}",
                savedUser.getId(), dto.roleIds());

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

    @Transactional
    public void assignRoleToUser(UpdateUserRoleDTO dto) {
        logger.debug("Assigning role to user. userId={}, roleId={}", dto.userId(), dto.roleId());

        User user =  findByIdWithRoles(dto.userId());

        Role role = roleService.getEntityById(dto.roleId());

        user.assignRole(role);

        logger.info("Role Assigned to user successfully. userId={}, roleId={}", dto.userId(), dto.roleId());
    }

    @Transactional
    public void removeRoleFromUser(UpdateUserRoleDTO dto) {
        logger.debug("Removing role fom user. userId={}, roleId={}", dto.userId(), dto.roleId());

        User user =  findByIdWithRoles(dto.userId());

        user.removeRole(dto.roleId());

        logger.info("Role removed from User successfully. userId={}, roleId={}", dto.userId(), dto.roleId());
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

    private void validateUniquenessByEmail(String email) {
        if (repository.existsByEmail(email))
            throw new ConflictException(
                    UserErrors.Target.EMAIL,
                    UserErrors.Code.USER_EMAIL_ALREADY_EXISTS);
    }

    private void setEncodedPassword(User user, String password) {
        user.setPasswordHash(passwordEncoder.encode(password));
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