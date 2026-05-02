package br.com.jogatinastore.controller;

import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import br.com.jogatinastore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    private final UserService services;

    public UserController(UserService services) {
        this.services = services;
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {

        return services.findAll();
    }

    @GetMapping(path="/{id}")
    public UserResponseDTO findById(@PathVariable UUID id) {

        return services.findById(id);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserDTO dto) {
        UserResponseDTO response = services.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestBody @Valid UpdateUserDTO dto) {

        return ResponseEntity.ok().body(services.update(dto));
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        services.delete(id);
        return ResponseEntity.noContent().build();
    }
}