package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.iam.role.domain.model.Role;
import br.com.jogatinastore.iam.role.infrastructure.persistence.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleFixture {

    private final RoleRepository repository;

    public RoleFixture(RoleRepository repository) {
        this.repository = repository;
    }

    public Role customer() {
        return create("ROLE_CUSTOMER", "Common user");
    }

    public Role manager() {
        return create("ROLE_MANAGER", "Business manager");
    }

    public Role admin() {
        return create("ROLE_ADMIN", "Users and system manager");
    }

    private Role create(String title, String description) {
        return repository.save(new Role(title, description));
    }
}