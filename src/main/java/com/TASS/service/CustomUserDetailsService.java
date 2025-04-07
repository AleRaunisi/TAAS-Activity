package com.TASS.service;

import com.TASS.model.Utente;
import com.TASS.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Email predefinita come amministratore
        if ("raunisi.alex@gmail.com".equals(email)) {
            return User.withUsername(email)
                    .password("{noop}password") // Password non codificata
                    .authorities("ADMIN") // Assegna il ruolo ADMIN
                    .build();
        }

        // Altrimenti, carica l'utente dal database
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));

        String ruolo = utente.getEmail().equals("raunisi.alex@gmail.com") ? "ADMIN" : utente.getRuolo();

        return new CustomUserDetails(
                utente.getEmail(),
                utente.getPassword(),
                User.withUsername(utente.getEmail())
                    .password(utente.getPassword())
                    .authorities(ruolo) // Usa il ruolo dal database
                    .build()
                    .getAuthorities(),
                utente.getNome()
        );
    }
}
