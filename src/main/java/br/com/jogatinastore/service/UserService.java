package br.com.jogatinastore.service;

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

        // TODO: create repository methods
        //if (repository.existsByEmail(dto.email()))
        //    throw new RuntimeException("E-mail já cadastrado!");

        //if (repository.existsByCpf(dto.cpf()))
        //    throw new RuntimeException("CPF já cadastrado!");

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
                .orElseThrow(() -> new RuntimeException("No records found for this ID!"));
    }
}