package com.example.spring_crud.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.spring_crud.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JWTService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            Algorithm algorithm= Algorithm.HMAC256(secret);

            String token= JWT.create()
                    .withIssuer("spring-crud")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);

            return  token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro durante autenticação");
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm= Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("spring-crud")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return  null;
        }
    }

    private Instant generateExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }
}
