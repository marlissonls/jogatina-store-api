package br.com.jogatinastore.integrationtests.fixture;

import br.com.jogatinastore.iam.role.domain.model.Role;
import br.com.jogatinastore.iam.user.domain.model.User;
import br.com.jogatinastore.iam.user.infrastructure.persistence.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserFixture {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleFixture roleFixture;

    private String email = "user@test.com";
    private String password = "password";
    private List<Role> roles;

    public UserFixture(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            RoleFixture roleFixture
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleFixture = roleFixture;
    }

    public UserFixture withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserFixture withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserFixture withRole(Role role) {
        this.roles = List.of(role);
        return this;
    }

    public UserFixture withRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Transactional
    public User create() {
        User user = new User();

        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));

        List<Role> rolesToAssign = roles != null
                ? roles
                : List.of(roleFixture.customer());

        user.assignRoles(rolesToAssign);

        return repository.save(user);
    }
}
