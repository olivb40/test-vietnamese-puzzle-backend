package com.example.vietnamesepuzzle;

import com.example.vietnamesepuzzle.repository.AttemptRepository;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PuzzleSolveServiceTest {

    @Mock
    private AttemptRepository attemptRepository;

    @Mock
    private AttemptValidation attemptValidation;

    @Mockremoter
    private AttemptUtils attemptUtils;

    @InjectMocks
    private PuzzleSolveService puzzleSolveService;

    @BeforeEach
    void setUp() {
        Mockito.reset(attemptRepository, attemptValidation);
    }

    @Test
    void testEvaluateExpression_ValidCase_ShouldReturnTrueOrFalse() {
        // Suppose we have a certain arrangement that leads to 66.0 in the puzzle
        // We'll just provide an example
        List<Integer> digits = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // We'll simply check whether this returns false or true.
        // Adjust the assertion to the actual puzzle logic. For demonstration, we do:
        boolean result = puzzleSolveService.evaluateExpression(digits);
        // For a real puzzle, you can craft digits that definitely produce 66.
        // Example assertion:
        assertFalse(result, "Adjust this assertion based on actual puzzle correctness");
    }

    @Test
    void testEvaluateExpression_IncompleteList_ShouldReturnFalse() {
        List<Integer> digits = List.of(1, 2, 3, 4, 5, 6, 7, 8); // only 8 digits
        boolean result = puzzleSolveService.evaluateExpression(digits);
        assertFalse(result, "Should return false if the list is not size 9");
    }

    @Test
    void testGenerateSolutions_ShouldReturnMapWithSolutionAndDuration() {
        // We can mock attemptRepository.deleteAll() call
        willDoNothing().given(attemptRepository).deleteAll();

        // When
        Map<String, Object> result = puzzleSolveService.generateSolutions();

        // Then
        verify(attemptRepository, times(1)).deleteAll();
        assertNotNull(result, "Should not be null");
        assertTrue(result.containsKey("solution"), "Map should contain 'solution'");
        assertTrue(result.containsKey("durationMs"), "Map should contain 'durationMs'");
        // The value of 'solution' might be null if no solution was found
        // or a String with the solution. 'durationMs' should be a Long
    }

    @Test
    void testPermutations_ShouldProduceAllKPermutations() {
        List<Integer> input = List.of(1, 2, 3);
        // For a 2-permutation of [1,2,3], we expect 6 results
        List<List<Integer>> perms = puzzleSolveService.permutations(input, 2);

        assertEquals(6, perms.size());
        // We can check they contain [1,2], [1,3], [2,1], [2,3], [3,1], [3,2]
        assertTrue(perms.contains(List.of(1, 2)));
        assertTrue(perms.contains(List.of(1, 3)));
        assertTrue(perms.contains(List.of(2, 1)));
        assertTrue(perms.contains(List.of(2, 3)));
        assertTrue(perms.contains(List.of(3, 1)));
        assertTrue(perms.contains(List.of(3, 2)));
    }

    @Test
    void testFractionOperations() {
        // Test the nested Fraction class
        PuzzleSolveService.Fraction f1 = new PuzzleSolveService.Fraction(13, 2); // 13/2
        PuzzleSolveService.Fraction f2 = new PuzzleSolveService.Fraction(3, 2);  // 3/2

        PuzzleSolveService.Fraction sum = f1.add(f2);      // 13/2 + 3/2 = 16/2 = 8
        assertTrue(sum.isInteger());
        assertEquals(8, sum.intValue());

        PuzzleSolveService.Fraction diff = f1.subtract(f2); // 13/2 - 3/2 = 10/2 = 5
        assertTrue(diff.isInteger());
        assertEquals(5, diff.intValue());
    }

    // You could add more tests for backtracking logic if needed,
    // such as backtrackNaive or backtrackOptimizedStoreAll, but that can quickly become
    // a more complex test scenario (akin to an integration test).
}
