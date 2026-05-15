package com.ghosted.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    /**
     * JWT signing secret. MUST be supplied via configuration / env var
     * (e.g. GHOSTED_APP_JWTSECRET). No insecure default is provided —
     * the application will fail to start if this is not configured with
     * sufficient entropy. Minimum length: 64 bytes (HS512 requirement).
     */
    @Value("${ghosted.app.jwtSecret:}")
    private String jwtSecret;

    @Value("${ghosted.app.jwtExpirationMs:86400000}")
    private int jwtExpirationMs; // default 24 hours

    private Key signingKey;

    @PostConstruct
    public void init() {
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException(
                "JWT secret is not configured. Set ghosted.app.jwtSecret or " +
                "the GHOSTED_APP_JWTSECRET environment variable to a strong random " +
                "value (>= 64 bytes / 512 bits)."
            );
        }
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 64) {
            throw new IllegalStateException(
                "JWT secret too short: " + keyBytes.length + " bytes. " +
                "HS512 requires at least 64 bytes (512 bits) of entropy."
            );
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        logger.info("JWT signing key initialized ({} bytes).", keyBytes.length);
    }

    private Key key() {
        return signingKey;
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            // parseClaimsJws enforces signature verification (vs. parse which accepts unsigned)
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }
        return false;
    }
}
