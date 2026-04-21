package br.com.jogatinastore.model.user.mapper;

import br.com.jogatinastore.model.user.UserPermission;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PermissionMapperUtils {

    public Set<String> mapUserPermissionsToTitles(Set<UserPermission> userPermissions) {
        if (userPermissions == null) {
            return Collections.emptySet();
        }
        return userPermissions.stream()
                .map(up -> up.getPermission().getTitle())
                .collect(Collectors.toSet());
    }
}
