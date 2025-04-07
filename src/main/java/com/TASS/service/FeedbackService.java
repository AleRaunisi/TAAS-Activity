package com.TASS.service;

import com.TASS.model.Feedback;
import com.TASS.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Recupera tutti i feedback
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    // Recupera un feedback per ID
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trovato!"));
    }

    // Crea un nuovo feedback
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    // Elimina un feedback per ID
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
}