package com.TASS.repository;

import com.TASS.model.Utente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    // Metodo personalizzato per trovare un utente per email
    boolean existsByEmail(String email);
    Optional<Utente> findByEmail(String email);
}