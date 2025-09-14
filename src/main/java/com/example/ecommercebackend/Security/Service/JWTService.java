package com.example.ecommercebackend.Security.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class JWTService {


    @Value("${jwt.secret}")
    private String secretkey;




    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<String,Object>();
        return Jwts.builder()
                .claims().add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*60*20))
                .and()
                .signWith(getSecretKey())
                .compact();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims extractAllClaimsFromToken(String token){
        return Jwts.parser().
                verifyWith(getSecretKey()).
                build().parseSignedClaims(token).getPayload();
    }


    public String getUsernameFromTAoken(String jwttoken) {
        return  extractAllClaimsFromToken(jwttoken).getSubject();
    }

    public boolean validateToken(UserDetails userDetails, String jwttoken) {
        try {
            Claims c=extractAllClaimsFromToken(jwttoken);
            if(userDetails.getUsername().equals(c.getSubject())) return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token expired", e);
        } catch (SignatureException e) {
            throw new JwtException("Invalid token signature", e);
        } catch (Exception e) {
            throw new JwtException("Invalid token", e);
        }

        return false;

    }
}
