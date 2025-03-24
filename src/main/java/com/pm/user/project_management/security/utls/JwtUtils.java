package com.pm.user.project_management.security.utls;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtils {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);


    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 )) // Token valid for 1 hour
                .signWith(key, SignatureAlgorithm.HS256) // Use the secure key
                .compact();
    }

//    public String generateTokenForGuest(String phoneNumber) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("role", "ROLE_GUEST");
//        return Jwts.builder()
//                .setSubject(phoneNumber)
//                .claim("role", "ROLE_GUEST") // Include guest role
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
//                .signWith(key,SignatureAlgorithm.HS256)
//                .compact();
//    }


    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}

