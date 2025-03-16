package com.example.vietnamesepuzzle;

import com.example.vietnamesepuzzle.service.AttemptUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AttemptUtilsTest {

    private AttemptUtils attemptUtils;

    @BeforeEach
    void setUp() {
        attemptUtils = new AttemptUtils();
    }

    @Test
    void testConvertStringToIntegerList() {
        String input = "123456789";
        List<Integer> result = attemptUtils.convertStringToIntegerList(input);

        assertEquals(9, result.size());
        // Verify it equals [1,2,3,4,5,6,7,8,9]
        for (int i = 0; i < 9; i++) {
            assertEquals(i + 1, result.get(i));
        }
    }

    @Test
    void testConvertListToString() {
        List<Integer> digits = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        String result = attemptUtils.convertListToString(digits);
        assertEquals("123456789", result);
    }

    // Add more edge case tests if needed (e.g., empty list -> "").
}
