package com.example.vietnamesepuzzle;

import com.example.vietnamesepuzzle.service.AttemptValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttemptValidationTest {

    private AttemptValidation attemptValidation;

    @BeforeEach
    void setUp() {
        attemptValidation = new AttemptValidation();
    }

    @Test
    void testValidateAttemptInput_Valid_ShouldNotThrowException() {
        // 9 distinct digits between 1..9
        assertDoesNotThrow(() -> attemptValidation.validateAttemptInput("123456789"));
    }

    @Test
    void testValidateAttemptInput_Empty_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> attemptValidation.validateAttemptInput("   ")
        );
        assertEquals("The attempt input must not be empty.", ex.getMessage());
    }

    @Test
    void testValidateAttemptInput_LengthNot9_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> attemptValidation.validateAttemptInput("12345678") // only 8 chars
        );
        assertEquals("The attempt input must have exactly 9 characters.", ex.getMessage());
    }

    @Test
    void testValidateAttemptInput_OutOfRangeDigit_ShouldThrowException() {
        // Contains '0' or some invalid char
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> attemptValidation.validateAttemptInput("123456780")
        );
        assertTrue(ex.getMessage().contains("All characters must be digits between 1 and 9"));
    }

    @Test
    void testValidateAttemptInput_DuplicateDigit_ShouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> attemptValidation.validateAttemptInput("112345678")
        );
        assertTrue(ex.getMessage().contains("Duplicate digit detected"));
    }
}
