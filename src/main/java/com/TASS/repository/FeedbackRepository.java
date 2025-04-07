package com.TASS.repository;

import com.TASS.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Metodo personalizzato per trovare feedback relativi a un'attività
    List<Feedback> findByAttivitaId(Long attivitaId);

    // Metodo personalizzato per trovare feedback dati da un utente
    List<Feedback> findByUtenteId(Long utenteId);
}