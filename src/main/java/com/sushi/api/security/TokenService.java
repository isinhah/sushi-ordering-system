package com.sushi.api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.sushi.api.model.Customer;
import com.sushi.api.model.Employee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    private String createToken(String subject, String role) {
        try {
            return JWT.create()
                    .withIssuer("login-auth-api")
                    .withSubject(subject)
                    .withClaim("role", role)
                    .withExpiresAt(generateExpirationDate())
                    .sign(getAlgorithm());
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while creating token", exception);
        }
    }

    public String generateCustomerToken(Customer customer) {
        return createToken(customer.getEmail(), "USER");
    }

    public String generateEmployeeToken(Employee employee) {
        return createToken(employee.getEmail(), "ADMIN");
    }

    public boolean validateToken(String token) {
        try {
            JWT.require(getAlgorithm())
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return JWT.require(getAlgorithm())
                .withIssuer("login-auth-api")
                .build()
                .verify(token)
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return JWT.require(getAlgorithm())
                .withIssuer("login-auth-api")
                .build()
                .verify(token)
                .getClaim("role").asString();
    }

    public Instant getExpirationDateFromToken(String token) {
        return JWT.require(getAlgorithm())
                .withIssuer("login-auth-api")
                .build()
                .verify(token)
                .getExpiresAt()
                .toInstant();
    }

    public Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}