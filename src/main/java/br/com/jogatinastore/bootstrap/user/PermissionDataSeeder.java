package br.com.jogatinastore.bootstrap.user;

import br.com.jogatinastore.model.user.Permission;
import br.com.jogatinastore.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Transactional
public class PermissionDataSeeder implements CommandLineRunner {

    private final PermissionRepository repository;

    public PermissionDataSeeder(PermissionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        List<Permission> existing = repository.findAll();
        Set<String> titles = existing
            .stream()
            .map(Permission::getTitle)
            .collect(Collectors.toSet());

        Map<String, String> required = Map.of(
            "ROLE_ADMIN", "Full access to all system resources and administrative settings.",
            "ROLE_MANAGER", "Management access to oversee users, reports, and operational workflows.",
            "ROLE_CUSTOMER", "Standard customer access to view products and manage personal profile."
        );

        required.forEach((title, desc) -> {
            if (!titles.contains(title)) {
                repository.save(new Permission(UUID.randomUUID(), title, desc));
            }
        });
    }
}
