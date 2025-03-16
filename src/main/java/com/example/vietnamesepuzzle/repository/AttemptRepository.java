package com.example.vietnamesepuzzle.repository;

import com.example.vietnamesepuzzle.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    // Find an attempt by its attemptInput
    Optional<Attempt> findByAttemptInput(String attemptInput);
}
