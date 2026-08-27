package br.com.jogatinastore.iam.user.presentation.http;

import br.com.jogatinastore.iam.user.presentation.docs.UserControllerDocs;
import br.com.jogatinastore.iam.user.application.dto.request.CreateEmployeeInput;
import br.com.jogatinastore.iam.user.application.dto.request.CreateUserInput;
import br.com.jogatinastore.iam.user.application.dto.request.UpdateUserRoleInput;
import br.com.jogatinastore.shared.pagination.PageResponse;
import br.com.jogatinastore.iam.user.application.dto.response.UserResponseOutput;
import br.com.jogatinastore.iam.user.application.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "Users", description = "Endpoints for Users management")
public class UserController implements UserControllerDocs {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Override
    public ResponseEntity<PageResponse<UserResponseOutput>> findAll(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {
        return ResponseEntity.ok().body(service.findAll(pageable));
    }

    @Override
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseOutput> findById(@PathVariable UUID id) {

        return ResponseEntity.ok().body(service.findById(id));
    }

    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseOutput> create(@RequestBody @Valid CreateUserInput dto) {
        UserResponseOutput response = service.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @Override
    @PostMapping(path = "/employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponseOutput> createEmployee(@RequestBody @Valid CreateEmployeeInput dto) {
        UserResponseOutput response = service.createEmployee(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }


    @Override
    @PutMapping(path = "/assignRole", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> assignRoleToUser(@RequestBody @Valid UpdateUserRoleInput dto) {
        service.assignRoleToUser(dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(path = "/removeRole", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> removeRoleFromUser(@RequestBody @Valid UpdateUserRoleInput dto) {
        service.removeRoleFromUser(dto);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id.toString() == authentication.principal.getId()")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping(path = "/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {

        service.deactivate(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    @PatchMapping(path = "/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {

        service.activate(id);
        return ResponseEntity.noContent().build();
    }
}