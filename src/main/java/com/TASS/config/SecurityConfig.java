package com.TASS.config;

import com.TASS.service.CustomUserDetails;
import com.TASS.service.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
                                                                                              
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/mobile-page", "/mobile").permitAll() 
                .requestMatchers("/esplora", "/supporto", "/**").permitAll() 
                .requestMatchers(HttpMethod.DELETE, "/attivita/**", "/home2", "/activity", "/create-activity").permitAll() 
                .requestMatchers("/images/**").permitAll() 
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/oauth2/**").permitAll()  // Permetti l'accesso alle risorse statiche
                .requestMatchers("/user", "/booking").authenticated()  // Proteggi le rotte sensibili
                .anyRequest().authenticated()  // Tutte le altre rotte devono essere protette
            )
            .oauth2Login(login -> login   
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/home");
                })
                .failureUrl("/login?error=true")
            )
            .formLogin(login -> login
                .loginPage("/login")
                .successHandler((request, response, authentication) -> {                    
                    response.sendRedirect("/home"); // Redirect per tutti gli altri
                })
                .failureUrl("/login?error=true")
            )
            .csrf(csrf -> csrf.disable());  // Disabilita CSRF per test, abilitalo in produzione

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}