package com.TASS.service;

import com.TASS.dto.UserDto;
import com.TASS.model.Utente;
import com.TASS.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    
   

    private  PasswordEncoder passwordEncoder;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository, PasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserDto userDto) {
        // Controlla se l'email esiste già nel database
        if (utenteRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email già in uso");
        }
        
        // Crea il nuovo utente
        Utente user = new Utente();
        user.setNome(userDto.getName());
        user.setEmail(userDto.getEmail());
        
        // Imposta la password cifrata
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        // Imposta la data di registrazione come la data corrente
        user.setDataRegistrazione(new Date());
        
        // Imposta il provider OAuth (se applicabile)
        user.setOauthProvider("GOOGLE");  // O puoi usare un valore dinamico se stai utilizzando OAuth2
        
        // Salva l'utente nel database
        utenteRepository.save(user);
    }

    public boolean authenticateUser(String email, String password) {
        Utente user = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    // Metodo per recuperare tutti gli utenti
    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    // Metodo per recuperare un utente per ID
    public Utente getUtenteById(Long id) {
        return utenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato!"));
    }

    // Metodo per recuperare un utente per email
    public Utente getUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Metodo per creare un nuovo utente
    public Utente createUtente(Utente utente) {
        return utenteRepository.save(utente);
    }

    // Metodo per eliminare un utente per ID
    public void deleteUtente(Long id) {
        utenteRepository.deleteById(id);
    }

    public Long findIdByEmail(String email) {
        return utenteRepository.findByEmail(email)
            .map(Utente::getId)
            .orElseGet(() -> {
                // 🆕 Se l'utente non esiste, lo creiamo
                Utente nuovoUtente = new Utente();
                nuovoUtente.setEmail(email);
                nuovoUtente.setNome("Utente Google"); // 👈 Nome di default, se non fornito
                nuovoUtente.setOauthProvider("GOOGLE"); // 👈 Imposta il provider OAuth
                nuovoUtente.setRuolo("USER"); // 👈 Ruolo predefinito
                nuovoUtente.setDataRegistrazione(new Date()); // 👈 Data di registrazione
    
                Utente utenteSalvato = utenteRepository.save(nuovoUtente);
                return utenteSalvato.getId();
            });
    }
    
}