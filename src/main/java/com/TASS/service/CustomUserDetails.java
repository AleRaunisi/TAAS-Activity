package com.TASS.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final String nome;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String nome) {
        super(username, password, authorities);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
    
}
