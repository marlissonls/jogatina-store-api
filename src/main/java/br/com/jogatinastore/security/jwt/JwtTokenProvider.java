package br.com.jogatinastore.security.jwt;

import br.com.jogatinastore.exception.InvalidJwtTokenException;
import br.com.jogatinastore.exception.messages.AuthErrorCode;
import br.com.jogatinastore.exception.messages.AuthErrorTarget;
import br.com.jogatinastore.security.dto.TokenDTO;
import br.com.jogatinastore.security.principal.AuthenticatedUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    private final long validityInMilliseconds;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public JwtTokenProvider(
        @Value("${security.jwt.token.secret:secret}") String secret,
        @Value("${secret.jwt.token.expire-length:3600000}") long validityInMilliseconds
    ) {
        this.validityInMilliseconds = validityInMilliseconds;
        String secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
        this.jwtVerifier = JWT.require(this.algorithm).build();
    }

    public TokenDTO createAccessToken(String userId, String email, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        String accessToken = getAccessToken(userId, email, roles, now, validity);
        String refreshToken = getRefreshToken(userId, email, roles, now);

        return new TokenDTO(email, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(String userId, String email, List<String> roles, Date now, Date validity) {

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId)
                .withClaim("roles", roles)
                .withClaim("type", "accessToken")
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withIssuer(issuerUrl)
            .sign(algorithm);
    }

    private String getRefreshToken(String userId, String email, List<String> roles, Date now) {

        Date refreshTokenValidity = new Date(now.getTime() + validityInMilliseconds * 3);
        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId)
                .withClaim("roles", roles)
                .withClaim("type", "refreshToken")
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
            .sign(algorithm);
    }



    public String resolveToken(HttpServletRequest request) {

        return request.getHeader("Authorization");
    }

    public DecodedJWT decodeToken(String token) {
        try {
            return jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException(AuthErrorTarget.TOKEN, AuthErrorCode.TOKEN_INVALID);
        }
    }

    public DecodedJWT handleToken(String token, String path) {
        if (!token.startsWith("Bearer "))
            throw new InvalidJwtTokenException(AuthErrorTarget.TOKEN, AuthErrorCode.TOKEN_INVALID);

        String actualToken = token.substring(7);
        DecodedJWT decodedJWT = decodeToken(actualToken);
        String type = decodedJWT.getClaim("type").asString();

        boolean isRefreshPath = path.contains("/auth/refresh");

        if (isRefreshPath && !"refreshToken".equals(type))
            throw new InvalidJwtTokenException(AuthErrorTarget.REFRESH_TOKEN, AuthErrorCode.REFRESH_TOKEN_INVALID);

        if (!isRefreshPath && !"accessToken".equals(type))
            throw new InvalidJwtTokenException(AuthErrorTarget.ACCESS_TOKEN, AuthErrorCode.ACCESS_TOKEN_INVALID);

        return decodedJWT;
    }

    public Authentication getAuthentication(String token, String path) {

        DecodedJWT decodedJWT = handleToken(token, path);

        String id = decodedJWT.getClaim("id").asString();
        String email = decodedJWT.getSubject();

        var roles = decodedJWT.getClaim("roles").asList(String.class);
        var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        AuthenticatedUser principal = new AuthenticatedUser(id, email, roles);

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }

    public TokenDTO refreshToken() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        AuthenticatedUser user = (AuthenticatedUser) auth.getPrincipal();

        return createAccessToken(user.id(), user.email(), user.roles());
    }
}
