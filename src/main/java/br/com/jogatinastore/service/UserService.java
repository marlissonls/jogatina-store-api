package br.com.jogatinastore.service;

import br.com.jogatinastore.exception.ConflictException;
import br.com.jogatinastore.exception.ResourceNotFoundException;
import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import br.com.jogatinastore.model.user.mapper.UserMapper;
import br.com.jogatinastore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    private final UserMapper userMapper;

    public UserService(UserRepository repository, UserMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    public List<UserResponseDTO> findAll() {
        return userMapper.toResponseList(repository.findAll());
    }

    public UserResponseDTO findById(UUID id) {

        return userMapper.toResponse(findEntityById(id));
    }

    public UserResponseDTO create(CreateUserDTO dto) {

        if (repository.existsByEmail(dto.email()))
            throw new ConflictException("Este e-mail já está vinculado a uma conta");

        if (repository.existsByCpf(dto.cpf()))
            throw new ConflictException("O CPF informado já possui cadastro");

        User user = userMapper.toEntity(dto);

        user.setPasswordHash(dto.password());

        User savedUser = repository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public UserResponseDTO update( UpdateUserDTO dto) {

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
}