package com.rarediseaseapp.security;

import com.rarediseaseapp.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Key secretKey;

    public JwtUtil() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(User user) {
        return Jwts.builder().claim("username", user.getUsername()).claim("userId", user.getId()).claim("role", user.getRole()).setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000L)).signWith(this.secretKey).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        Claims claims = (Claims)Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.get("userId").toString());
    }

    public String extractRole(String token) {
        return (String)this.extractClaim(token, (claims) -> (String)claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, ClaimsResolver<T> claimsResolver) {
        Claims claims = this.extractAllClaims(token);
        return claimsResolver.resolve(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims)Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token).getBody();
    }

    @FunctionalInterface
    public interface ClaimsResolver<T> {
        T resolve(Claims claims);
    }
}

