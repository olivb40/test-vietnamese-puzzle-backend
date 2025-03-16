package com.example.vietnamesepuzzle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "attempts")
public class Attempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user-entered input for the attempt.
    // The unique constraint prevents duplicate values in the database.
    @Column(name = "attempt_input", unique = true)
    @JsonProperty("attemptInput")
    private String attemptInput;

    // Indicates whether the attempt is correct or not.
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty("isCorrect")
    private Boolean correct;

    // Default constructor.
    public Attempt() {
    }

    // Parameterized constructor.
    public Attempt(Long id, String attemptInput, Boolean correct) {
        this.id = id;
        this.attemptInput = attemptInput;
        this.correct = correct;
    }

    // Getters and setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttemptInput() {
        return attemptInput;
    }

    public void setAttemptInput(String attemptInput) {
        this.attemptInput = attemptInput;
    }

    public Boolean getCorrect() {
        return correct;
    }

    public void setCorrect(Boolean correct) {
        this.correct = correct;
    }
}
