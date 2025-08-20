package com.critiquehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for simplicity in APIs (recommended to keep disabled in token-based auth)
        http.csrf(csrf -> csrf.disable());

        // Define our authorization rules
        http.authorizeHttpRequests(auth -> auth
                // Allow user APIs (for registration/testing without auth)
                .requestMatchers("/api/users/**").permitAll()
                // Allow Swagger and API docs (optional)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                // Everything else will require authentication
                .anyRequest().authenticated()
        );

        // Enable basic auth temporarily (we will replace this with JWT later)
        http.httpBasic(Customizer.withDefaults());

        // If you want form login for testing in browser, you can also enable:
        // http.formLogin(Customizer.withDefaults());

        System.out.println("✅ SecurityConfig loaded successfully!");

        return http.build();
    }
}
