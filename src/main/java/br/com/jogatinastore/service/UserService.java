package br.com.jogatinastore.service;

import br.com.jogatinastore.model.user.User;
import br.com.jogatinastore.repository.UserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(UUID id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No records found for this ID!"));
    }

    public User create(User user) {

        return repository.save(user);
    }

    public User update(@Nonnull User user) {

        var entity = findById(user.getId());

        entity.setName(user.getName());
        entity.setCpf(user.getCpf());
        entity.setBirthDate(user.getBirthDate());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setEmail(user.getEmail());

        return repository.save(entity);
    }

    public void delete(UUID id) {

        var entity = findById(id);

        repository.delete(entity);
    }
}