package com.example.vietnamesepuzzle.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AttemptValidation {

    public void validateAttemptInput(String attemptInput) {
        if (attemptInput == null || attemptInput.trim().isEmpty()) {
            throw new IllegalArgumentException("The attempt input must not be empty.");
        }

        // Check length
        if (attemptInput.length() != 9) {
            throw new IllegalArgumentException("The attempt input must have exactly 9 characters.");
        }

        // Check digits are between '1'..'9', and are unique
        Set<Character> seen = new HashSet<>();
        for (char c : attemptInput.toCharArray()) {
            if (c < '1' || c > '9') {
                throw new IllegalArgumentException(
                        "All characters must be digits between 1 and 9. Invalid char: " + c
                );
            }
            if (!seen.add(c)) {
                throw new IllegalArgumentException(
                        "Duplicate digit detected in attempt input: " + c
                );
            }
        }
    }
}
