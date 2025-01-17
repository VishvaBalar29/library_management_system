package com.library_management.library_management.utility;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    @Value("${java-jwt-key}")
    private String JWT_KEY;

    private Key SECRET_KEY;

    // @PostConstruct is used to mark a method that should run once after the constructor has completed, but before the bean is used.
    // here, the SECRET_KEY needs to be computed after JWT_KEY is injected (because SECRET_KEY depends on JWT_KEY).
    @PostConstruct
    public void init(){
        SECRET_KEY = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }    

    public String generateToken(String username,boolean is_admin){
        return Jwts.builder()
            .setSubject(username)
            .claim("is_admin",is_admin)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
            .compact();
    }

    public UserInfo extractUserData(String userToken) {
        UserInfo userInfo = new UserInfo(extractUsername(userToken), extractClaims(userToken).get("is_admin",Boolean.class));
        return userInfo;
    }

    public String extractUsername(String userToken){
        return extractClaims(userToken).getSubject();
    }

    private Claims extractClaims(String token){
        return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }
}
