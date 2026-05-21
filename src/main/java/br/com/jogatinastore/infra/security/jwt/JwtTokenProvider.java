package br.com.jogatinastore.infra.security.jwt;

import br.com.jogatinastore.infra.exception.InvalidJwtTokenException;
import br.com.jogatinastore.domain.authentication.exception.AuthErrors;
import br.com.jogatinastore.domain.authentication.dto.TokenDTO;
import br.com.jogatinastore.infra.security.principal.AuthenticatedUser;
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
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JwtTokenProvider {

    private final long validityInMilliseconds;
    private final long refreshValidityInMilliseconds;
    private final Algorithm algorithm;
    private final JWTVerifier accessTokenVerifier;
    private final JWTVerifier refreshTokenVerifier;
    private static final Pattern JWT_PATTERN =
            Pattern.compile("[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+");

    public JwtTokenProvider(
        @Value("${security.jwt.token.secret:secret}") String secret,
        @Value("${secret.jwt.token.expire-length:3600000}") long validityInMilliseconds,
        @Value("${secret.jwt.token.refresh-expire-length:10800000}") long refreshValidityInMilliseconds
    ) {
        this.validityInMilliseconds = validityInMilliseconds;
        this.refreshValidityInMilliseconds = refreshValidityInMilliseconds;
        this.algorithm = Algorithm.HMAC256(secret);
        this.accessTokenVerifier = JWT
                .require(algorithm)
                .withClaim("type", "access")
                .build();

        this.refreshTokenVerifier = JWT
                .require(algorithm)
                .withClaim("type", "refresh")
                .build();
    }

    public TokenDTO createAccessToken(String userId, String email, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        Date refreshValidity = new Date(now.getTime() + refreshValidityInMilliseconds);
        String accessToken = getAccessToken(userId, email, roles, now, validity);
        String refreshToken = getRefreshToken(userId, email, roles, now, refreshValidity);

        return new TokenDTO(email, true, now, validity, accessToken, refreshToken);
    }

    private String getAccessToken(String userId, String email, List<String> roles, Date now, Date validity) {

        String issuerUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId)
                .withClaim("roles", roles)
                .withClaim("type", "access")
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    private String getRefreshToken(String userId, String email, List<String> roles, Date now, Date refreshValidity) {

        String jti = UUID.randomUUID().toString();

        return JWT.create()
                .withSubject(email)
                .withClaim("id", userId)
                .withClaim("roles", roles)
                .withClaim("jti", jti)
                .withClaim("type", "refresh")
                .withIssuedAt(now)
                .withExpiresAt(refreshValidity)
                .sign(algorithm);
    }



    public Optional<String> resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        return extractToken(bearerToken);
    }

    private static Optional<String> extractToken(String value) {
        if (value == null || value.isBlank()) {
            return Optional.empty();
        }

        Matcher matcher = JWT_PATTERN.matcher(value);

        return matcher.find()
            ? Optional.of(matcher.group())
            : Optional.empty();
    }

    public Authentication getAccessAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = accessTokenVerifier.verify(token);

            String id = decodedJWT.getClaim("id").asString();
            String email = decodedJWT.getSubject();

            var roles = decodedJWT.getClaim("roles").asList(String.class);
            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            AuthenticatedUser principal = new AuthenticatedUser(id, email, roles);

            return new UsernamePasswordAuthenticationToken(principal, null, authorities);

        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException(
                    AuthErrors.Target.ACCESS_TOKEN,
                    AuthErrors.Code.ACCESS_TOKEN_INVALID
            );
        }
    }

    public TokenDTO refreshToken(String bearerToken) {
        try {
            Optional<String> refreshToken = extractToken(bearerToken);

            if (refreshToken.isEmpty()) {
                throw new InvalidJwtTokenException(
                        AuthErrors.Target.REFRESH_TOKEN,
                        AuthErrors.Code.REFRESH_TOKEN_INVALID
                );
            }

            DecodedJWT decodedJWT = refreshTokenVerifier.verify(refreshToken.get());

            String id = decodedJWT.getClaim("id").asString();
            String email = decodedJWT.getSubject();
            var roles = decodedJWT.getClaim("roles").asList(String.class);

            return createAccessToken(id, email, roles);
        } catch (JWTVerificationException e) {
            throw new InvalidJwtTokenException(
                    AuthErrors.Target.REFRESH_TOKEN,
                    AuthErrors.Code.REFRESH_TOKEN_INVALID
            );
        }
    }
}
