package br.com.jogatinastore.config.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "security.jwt.token")
@Validated
public class JwtProperties {

    @NotBlank
    private String secret;

    @NotNull
    private Duration expireLength;

    @NotNull
    private Duration refreshExpireLength;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Duration getExpireLength() {
        return expireLength;
    }

    public void setExpireLength(Duration expireLength) {
        this.expireLength = expireLength;
    }

    public Duration getRefreshExpireLength() {
        return refreshExpireLength;
    }

    public void setRefreshExpireLength(Duration refreshExpireLength) {
        this.refreshExpireLength = refreshExpireLength;
    }
}
