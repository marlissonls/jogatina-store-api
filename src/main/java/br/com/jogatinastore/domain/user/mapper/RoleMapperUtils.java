package br.com.jogatinastore.domain.user.mapper;

import br.com.jogatinastore.domain.authorization.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapperUtils {

    public Set<String> mapUserRolesToTitles(Set<UserRole> userRoles) {
        if (userRoles == null) {
            return Collections.emptySet();
        }
        return userRoles.stream()
                .map(up -> up.getRole().getTitle())
                .collect(Collectors.toSet());
    }
}
