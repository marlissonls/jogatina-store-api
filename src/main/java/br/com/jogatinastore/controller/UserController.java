package br.com.jogatinastore.controller;

import br.com.jogatinastore.model.user.dto.CreateUserDTO;
import br.com.jogatinastore.model.PageResponse;
import br.com.jogatinastore.model.user.dto.UpdateUserDTO;
import br.com.jogatinastore.model.user.dto.UserResponseDTO;
import br.com.jogatinastore.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    private final UserService services;

    public UserController(UserService services) {
        this.services = services;
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserResponseDTO>> findAll(
            @PageableDefault(size = 12, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(services.findAll(pageable));
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