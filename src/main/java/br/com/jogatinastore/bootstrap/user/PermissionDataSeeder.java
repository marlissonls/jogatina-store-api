package br.com.jogatinastore.bootstrap.user;

import br.com.jogatinastore.model.user.Permission;
import br.com.jogatinastore.repository.PermissionRepository;
import br.com.jogatinastore.security.permission.RolePermissionEnum;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
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

        Map<String, String> required = Arrays.stream(RolePermissionEnum.values())
            .collect(Collectors.toConcurrentMap(
                RolePermissionEnum::key,
                RolePermissionEnum::description
            ));

        List<Permission> toInsert = new ArrayList<>();

        for (var entry : required.entrySet()) {
            if (!titles.contains(entry.getKey())) {
                toInsert.add(
                    new Permission(
                        UUID.randomUUID(),
                        entry.getKey(),
                        entry.getValue()
                    )
                );
            }
        }

        repository.saveAll(toInsert);
    }
}
