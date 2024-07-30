package com.sushi.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/customers/login", "/api/auth/customers/register").permitAll()
                        .requestMatchers("/api/auth/employees/login", "/api/auth/employees/register").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/list", "/api/categories/{id}", "/api/categories/find/by-name").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/list", "/api/products/{id}", "/api/products/find/by-name").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/customers", "/api/orders").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/customers/{id}", "/api/orders/{id}").hasAnyAuthority("USER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/{id}").hasAnyAuthority("USER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/employees", "/api/employees/list", "/api/employees/find/by-email").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/customers", "/api/customers/{id}", "/api/customers/find/by-name", "/api/customers/find/by-email").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders", "/api/orders/list").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/categories", "/api/products", "/api/employees").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories", "/api/products", "/api/orders", "/api/employees").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}", "/api/products/{id}", "/api/employees/{id}").hasAuthority("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
