package br.com.jogatinastore.service;

import br.com.jogatinastore.exception.ConflictException;
import br.com.jogatinastore.exception.RequiredObjectIsNullException;
import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.model.user.Permission;
import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import br.com.jogatinastore.model.user.mapper.UserMapper;
import br.com.jogatinastore.repository.PermissionRepository;
import br.com.jogatinastore.repository.UserRepository;
import br.com.jogatinastore.security.permission.RolePermissionEnum;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final PermissionRepository permissionRepository;

    private final UserMapper userMapper;

    public UserService(
            UserRepository repository,
            PermissionRepository permissionRepository,
            UserMapper userMapper
    ) {
        this.repository = repository;
        this.permissionRepository = permissionRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public List<UserResponseDTO> findAll() {

        logger.info("Finding all User");

        return userMapper.toResponseList(repository.findAllWithPermissions());
    }

    @Transactional
    public UserResponseDTO findById(UUID id) {

        logger.info("Finding one User");

        return userMapper.toResponse(findByIdWithPermissions(id));
    }

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {

        logger.info("Creating one User");

        if (dto == null)
            throw new RequiredObjectIsNullException("Os dados do usuário enviados não podem estar vazios");

        if (repository.existsAnyByEmailIncludingDeleted(dto.email()) > 0)
            throw new ConflictException("Este e-mail já está vinculado a uma conta");

        if (repository.existsAnyByCpfIncludingDeleted(dto.cpf()) > 0)
            throw new ConflictException("O CPF informado já possui cadastro");

        Permission defaultPerm = permissionRepository.findByTitle(RolePermissionEnum.ROLE_CUSTOMER.key());

        User user = userMapper.toEntity(dto);

        user.setPasswordHash(generateHashedPassword(dto.password()));

        user.addPermission(defaultPerm);

        return userMapper.toResponse(repository.save(user));
    }

    @Transactional
    public UserResponseDTO update(UpdateUserDTO dto) {

        logger.info("Updating one User");

        if (dto == null)
            throw new RequiredObjectIsNullException("Os dados do usuário enviados não podem estar vazios");

        User entity = findByIdWithPermissions(dto.id());

        userMapper.updateEntity(dto, entity);

        return userMapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {

        logger.info("Deleting one User");

        User entity = findEntityById(id);

        repository.delete(entity);
    }

    private User findEntityById(UUID id) {

        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No user found with the given ID"));
    }

    private User findByIdWithPermissions(UUID id) {

        return repository.findByIdWithPermissions(id)
            .orElseThrow(() -> new ResourceNotFoundException("No user found with the given ID"));
    }

    private String generateHashedPassword(String password) {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
            "", 8, 185000,
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put("pbkdf2", pbkdf2Encoder);

        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);

        return passwordEncoder.encode(password);
    }
}