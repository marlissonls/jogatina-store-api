package br.com.jogatinastore.controller;

import br.com.jogatinastore.model.User;
import br.com.jogatinastore.service.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    UserServices services;

    public UserController(UserServices services) {
        this.services = services;
    }

    @GetMapping
    public List<User> findAll() {

        return services.findAll();
    }

    @GetMapping(path="/{id}")
    public User findById(@PathVariable UUID id) {

        return services.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {

        return services.create(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {

        return services.update(user);
    }

    @DeleteMapping(path="/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        services.delete(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
