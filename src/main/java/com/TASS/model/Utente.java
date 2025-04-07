package com.TASS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nome") // Nome tabella nel database
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "partecipanti")
    private List<Attivita> attivita;

    @NotNull(message = "Il nome non può essere nullo.")
    @Size(min = 2, max = 50, message = "Il nome deve avere tra 2 e 50 caratteri.")
    private String nome;

    @NotNull(message = "L'email è obbligatoria.")
    @Email(message = "Inserire un'email valida.")
    private String email;

    @Column(nullable = false)
    private String password;

    @NotNull(message = "Il provider OAuth è obbligatorio.")
    @Pattern(regexp = "GOOGLE|FACEBOOK|TWITTER", message = "Il provider deve essere GOOGLE, FACEBOOK o TWITTER.")
    private String oauthProvider;

    @NotNull(message = "La data di registrazione è obbligatoria.")
    @Temporal(TemporalType.DATE)
    private Date dataRegistrazione;

    @Column(nullable = false)
    private String ruolo = "USER";

     // Getter e Setter
     public String getRuolo() {
         return ruolo;
     }
 
     public void setRuolo(String ruolo) {
         this.ruolo = ruolo;
     }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}