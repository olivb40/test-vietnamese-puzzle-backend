package com.example.vietnamesepuzzle.service;

import com.example.vietnamesepuzzle.model.Attempt;
import com.example.vietnamesepuzzle.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final AttemptValidation attemptValidation;
    private final AttemptUtils attemptUtils;
    private final PuzzleSolveService puzzleSolveService;

    public AttemptService(AttemptRepository attemptRepository,
                          AttemptValidation attemptValidation,
                          AttemptUtils attemptUtils,
                          PuzzleSolveService puzzleSolveService) {
        this.attemptRepository = attemptRepository;
        this.attemptValidation = attemptValidation;
        this.attemptUtils = attemptUtils;
        this.puzzleSolveService = puzzleSolveService;
    }

    // Retrieve all attempts.
    public List<Attempt> getAllAttempts() {
        return attemptRepository.findAll();
    }

    // Create a new attempt with business logic.
    public Attempt createAttempt(Attempt attempt) {
        // Validate that the input is correct.
        attemptValidation.validateAttemptInput(attempt.getAttemptInput());

        // Convert input to list of integers, evaluate correctness, set correct flag
        boolean isCorrect = this.puzzleSolveService.evaluateExpression(
                attemptUtils.convertStringToIntegerList(attempt.getAttemptInput())
        );
        attempt.setCorrect(isCorrect);

        return attemptRepository.save(attempt);
    }

    // Update an existing attempt.
    public Attempt updateAttempt(Long id, Attempt attemptDetails) {
        Attempt existingAttempt = attemptRepository.findById(id).orElse(null);
        if (existingAttempt != null) {
            // Validate the new input
            attemptValidation.validateAttemptInput(attemptDetails.getAttemptInput());

            existingAttempt.setAttemptInput(attemptDetails.getAttemptInput());
            boolean isCorrect = this.puzzleSolveService.evaluateExpression(
                    attemptUtils.convertStringToIntegerList(attemptDetails.getAttemptInput())
            );
            existingAttempt.setCorrect(isCorrect);

            return attemptRepository.save(existingAttempt);
        }
        return null;
    }

    // Retrieve an attempt by its id.
    public Attempt getAttemptById(Long id) {
        return attemptRepository.findById(id).orElse(null);
    }

    // Delete an attempt by its id.
    public void deleteAttempt(Long id) {
        attemptRepository.deleteById(id);
    }

    // Delete all attempts.
    public void deleteAllAttempts() {
        attemptRepository.deleteAll();
    }
}
