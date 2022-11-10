package com.marcin.springboot_react.utils;

import com.marcin.springboot_react.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Configuration
@Slf4j
public class JwtUtils {

    @Value("${jwt.key.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date getExpirationFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        log.info("About to generate the auth token for user: '{}'", user.getUsername());
        return createToken(claims, user.getUsername());
    }

    public Boolean isTokenValid(String token, User user) {
        log.info("About to check if the passed in token for user: '{}' is still valid...", user.getUsername());
        String username = getUsernameFromToken(token);
        log.info("Username from token is: '{}'", username);
        //password should be stored as encrypted & then decrypted here
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, this.secret).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationFromToken(token).before(new Date());
    }
}
