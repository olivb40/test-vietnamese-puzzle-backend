package com.example.vietnamesepuzzle.controller;

import com.example.vietnamesepuzzle.model.Attempt;
import com.example.vietnamesepuzzle.service.AttemptService;
import com.example.vietnamesepuzzle.service.PuzzleSolveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;
    private final PuzzleSolveService puzzleSolveService;

    public AttemptController(
            AttemptService attemptService,
            PuzzleSolveService puzzleSolveService
    ) {
        this.attemptService = attemptService;
        this.puzzleSolveService = puzzleSolveService;
    }

    // GET all attempts.
    @GetMapping
    public List<Attempt> getAllAttempts() {
        return attemptService.getAllAttempts();
    }

    // POST create a new attempt.
    @PostMapping
    public ResponseEntity<Attempt> createAttempt(@RequestBody Attempt attempt) {
        Attempt createdAttempt = attemptService.createAttempt(attempt);
        return new ResponseEntity<>(createdAttempt, HttpStatus.CREATED);
    }

    // GET an attempt by its id.
    @GetMapping("/{id}")
    public ResponseEntity<Attempt> getAttemptById(@PathVariable Long id) {
        Attempt attempt = attemptService.getAttemptById(id);
        return attempt != null ? new ResponseEntity<>(attempt, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // PUT update an existing attempt.
    @PutMapping("/{id}")
    public ResponseEntity<Attempt> updateAttempt(@PathVariable Long id, @RequestBody Attempt attemptDetails) {
        Attempt updatedAttempt = attemptService.updateAttempt(id, attemptDetails);
        return updatedAttempt != null ? new ResponseEntity<>(updatedAttempt, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // DELETE an attempt by its id.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable Long id) {
        attemptService.deleteAttempt(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // DELETE all attempts.
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllAttempts() {
        attemptService.deleteAllAttempts();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * POST /api/attempts/generateSolution
     * Generates a solution by evaluating all permutations until the equation is satisfied.
     * Returns a JSON object containing the found solution (or null if none) and the duration in milliseconds.
     */
    @PostMapping("/solutions")
    public ResponseEntity<Map<String, Object>> generateSolutions() {
        Map<String, Object> result = puzzleSolveService.generateSolutions();
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
