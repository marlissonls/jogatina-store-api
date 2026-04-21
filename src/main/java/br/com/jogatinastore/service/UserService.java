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
import jakarta.transaction.Transactional;
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

    public List<UserResponseDTO> findAll() {
        return userMapper.toResponseList(repository.findAll());
    }

    public UserResponseDTO findById(UUID id) {

        return userMapper.toResponse(findEntityById(id));
    }

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {

        if (dto == null)
            throw new RequiredObjectIsNullException("Os dados do usuário enviados não podem estar vazios");

        if (repository.existsByEmail(dto.email()))
            throw new ConflictException("Este e-mail já está vinculado a uma conta");

        if (repository.existsByCpf(dto.cpf()))
            throw new ConflictException("O CPF informado já possui cadastro");

        Permission defaultPerm = permissionRepository.findByTitle("ROLE_CUSTOMER");

        User user = userMapper.toEntity(dto);

        user.setPasswordHash(generateHashedPassword(dto.password()));

        user.addPermission(defaultPerm);

        User savedUser = repository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponseDTO update(UpdateUserDTO dto) {

        User entity = findEntityById(dto.id());

        userMapper.updateEntity(dto, entity);

        return userMapper.toResponse(repository.save(entity));
    }

    public void delete(UUID id) {

        User entity = findEntityById(id);

        repository.delete(entity);
    }

    private User findEntityById(UUID id) {

        return repository.findById(id)
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