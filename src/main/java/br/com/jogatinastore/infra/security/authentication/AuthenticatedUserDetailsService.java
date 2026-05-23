package br.com.jogatinastore.infra.security.authentication;

import br.com.jogatinastore.domain.user.entity.User;
import br.com.jogatinastore.domain.user.repository.UserRepository;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public AuthenticatedUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = repository.findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                        "Username " + email + " not found!"
                ));

        var authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new AuthenticatedUser(
                user.getId().toString(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                user.isEnabled()
        );
    }
}