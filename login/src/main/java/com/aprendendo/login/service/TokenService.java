package com.aprendendo.login.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.aprendendo.login.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algoritimo = Algorithm.HMAC256(secret); // algoritimo para hash do token
            String token = JWT.create()
                    .withIssuer("authApi") // emisso(nome aplicação)
                    .withSubject(user.getUsername()) // usuário que receberá token
                    .withClaim("roles", user.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList())) // roles do usuário
                    .withExpiresAt(genExpirationDate()) // tempo de expiração
                    .sign(algoritimo); // assinatura
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public DecodedJWT verifyToken(String token){
        try {
            Algorithm algoritimo = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algoritimo)
                .withIssuer("authApi")
                .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            System.err.println("Token verification failed: " + e.getMessage());
            return null;
        }
        
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
