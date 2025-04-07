package com.TASS.dto;

public class UserDto {

    private String name;       // Nome dell'utente
    private String email;      // Email dell'utente
    private String password;   // Password dell'utente (non criptata)

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}