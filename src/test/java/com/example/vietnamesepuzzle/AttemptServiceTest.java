package com.example.vietnamesepuzzle;

import com.example.vietnamesepuzzle.model.Attempt;
import com.example.vietnamesepuzzle.repository.AttemptRepository;
import com.example.vietnamesepuzzle.service.AttemptService;
import com.example.vietnamesepuzzle.service.AttemptUtils;
import com.example.vietnamesepuzzle.service.AttemptValidation;
import com.example.vietnamesepuzzle.service.PuzzleSolveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class AttemptServiceTest {

    @Mock
    private AttemptRepository attemptRepository;

    // Since AttemptService calls AttemptValidation, AttemptUtils, and PuzzleSolveService,
    // we can mock them as well to ensure pure unit tests.
    @Mock
    private AttemptValidation attemptValidation;

    @Mock
    private AttemptUtils attemptUtils;

    @Mock
    private PuzzleSolveService puzzleSolveService;

    @InjectMocks
    private AttemptService attemptService;

    @BeforeEach
    void setUp() {
        Mockito.reset(attemptRepository, attemptValidation, attemptUtils, puzzleSolveService);
    }

    @Test
    void testCreateAttempt_ValidInput_ShouldCreateAndSaveAttempt() {
        // Given
        Attempt attempt = new Attempt();
        attempt.setAttemptInput("123456789");

        // Simulate successful validation (no exception thrown)
        willDoNothing().given(attemptValidation).validateAttemptInput("123456789");

        // Simulate converting the string into a list of Integers
        given(attemptUtils.convertStringToIntegerList("123456789"))
                .willReturn(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));

        // Simulate evaluateExpression returning false (not a valid puzzle solution)
        given(puzzleSolveService.evaluateExpression(anyList())).willReturn(false);

        // Simulate the repository saving the attempt
        Attempt savedAttempt = new Attempt();
        savedAttempt.setId(1L);
        savedAttempt.setAttemptInput("123456789");
        savedAttempt.setCorrect(false);
        given(attemptRepository.save(any(Attempt.class))).willReturn(savedAttempt);

        // When
        Attempt result = attemptService.createAttempt(attempt);

        // Then
        verify(attemptValidation).validateAttemptInput("123456789");
        verify(attemptUtils).convertStringToIntegerList("123456789");
        verify(puzzleSolveService).evaluateExpression(anyList());
        verify(attemptRepository).save(any(Attempt.class));

        assertNotNull(result.getId());
        assertEquals("123456789", result.getAttemptInput());
        // 'correct' is false as per the stub from puzzleSolveService
        assertFalse(result.getCorrect());
    }

    @Test
    void testCreateAttempt_EmptyInput_ShouldThrowException() {
        // Given
        Attempt attempt = new Attempt();
        attempt.setAttemptInput("   ");

        // Simulate that validation will throw an exception
        willThrow(new IllegalArgumentException("The attempt input must not be empty."))
                .given(attemptValidation)
                .validateAttemptInput("   ");

        // When / Then
        Throwable ex = assertThrows(IllegalArgumentException.class,
                () -> attemptService.createAttempt(attempt));
        assertEquals("The attempt input must not be empty.", ex.getMessage());

        // The repository should never be called in this scenario
        verify(attemptRepository, never()).save(any(Attempt.class));
    }

    @Test
    void testGetAttemptById_ExistingId_ShouldReturnAttempt() {
        // Given
        Attempt existingAttempt = new Attempt();
        existingAttempt.setId(1L);
        existingAttempt.setAttemptInput("123456789");
        existingAttempt.setCorrect(false);

        given(attemptRepository.findById(1L)).willReturn(Optional.of(existingAttempt));

        // When
        Attempt result = attemptService.getAttemptById(1L);

        // Then
        verify(attemptRepository).findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("123456789", result.getAttemptInput());
    }

    @Test
    void testGetAttemptById_NonExistingId_ShouldReturnNull() {
        // Given
        given(attemptRepository.findById(99L)).willReturn(Optional.empty());

        // When
        Attempt result = attemptService.getAttemptById(99L);

        // Then
        verify(attemptRepository).findById(99L);
        assertNull(result);
    }

    @Test
    void testUpdateAttempt_Existing_ShouldUpdateAndReturnAttempt() {
        // Given
        Attempt existingAttempt = new Attempt();
        existingAttempt.setId(1L);
        existingAttempt.setAttemptInput("oldValue");
        existingAttempt.setCorrect(false);

        Attempt updateDetails = new Attempt();
        updateDetails.setAttemptInput("987654321");

        // Repository findById
        given(attemptRepository.findById(1L)).willReturn(Optional.of(existingAttempt));

        // Validation
        willDoNothing().given(attemptValidation).validateAttemptInput("987654321");

        // Conversion
        given(attemptUtils.convertStringToIntegerList("987654321"))
                .willReturn(Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1));

        // Evaluate expression -> let's say it returns true now
        given(puzzleSolveService.evaluateExpression(anyList())).willReturn(true);

        // Save
        given(attemptRepository.save(any(Attempt.class))).willAnswer(inv -> inv.getArgument(0));

        // When
        Attempt updated = attemptService.updateAttempt(1L, updateDetails);

        // Then
        verify(attemptRepository).findById(1L);
        verify(attemptValidation).validateAttemptInput("987654321");
        verify(attemptUtils).convertStringToIntegerList("987654321");
        verify(puzzleSolveService).evaluateExpression(anyList());
        verify(attemptRepository).save(any(Attempt.class));

        assertNotNull(updated);
        assertEquals("987654321", updated.getAttemptInput());
        assertTrue(updated.getCorrect());
    }

    @Test
    void testUpdateAttempt_NonExisting_ShouldReturnNull() {
        // Given
        given(attemptRepository.findById(999L)).willReturn(Optional.empty());

        Attempt updateDetails = new Attempt();
        updateDetails.setAttemptInput("888888888");

        // When
        Attempt updated = attemptService.updateAttempt(999L, updateDetails);

        // Then
        verify(attemptRepository).findById(999L);
        verify(attemptRepository, never()).save(any(Attempt.class));
        assertNull(updated);
    }

    @Test
    void testDeleteAttempt_ShouldCallRepository() {
        // When
        attemptService.deleteAttempt(1L);

        // Then
        verify(attemptRepository).deleteById(1L);
    }

    @Test
    void testDeleteAllAttempts_ShouldCallRepository() {
        // When
        attemptService.deleteAllAttempts();

        // Then
        verify(attemptRepository).deleteAll();
    }
}
