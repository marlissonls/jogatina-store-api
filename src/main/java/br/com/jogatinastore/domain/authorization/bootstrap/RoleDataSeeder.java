package br.com.jogatinastore.domain.authorization.bootstrap;

import br.com.jogatinastore.domain.authorization.entity.Role;
import br.com.jogatinastore.domain.authorization.repository.RoleRepository;
import br.com.jogatinastore.domain.authorization.code.RoleCode;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Transactional
public class RoleDataSeeder implements CommandLineRunner {

    private final RoleRepository repository;

    public RoleDataSeeder(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        List<Role> existing = repository.findAll();

        Set<String> titles = existing
            .stream()
            .map(Role::getTitle)
            .collect(Collectors.toSet());

        Map<String, String> required = Arrays.stream(RoleCode.values())
            .collect(Collectors.toConcurrentMap(
                RoleCode::key,
                RoleCode::description
            ));

        List<Role> toInsert = new ArrayList<>();

        for (var entry : required.entrySet()) {
            if (!titles.contains(entry.getKey())) {
                toInsert.add(
                    new Role(
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
