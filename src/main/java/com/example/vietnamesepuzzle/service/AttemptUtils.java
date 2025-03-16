package com.example.vietnamesepuzzle.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttemptUtils {

    /**
     * Converts a string of 9 digits (e.g. '123456789') into a List of Integers
     */
    public List<Integer> convertStringToIntegerList(String attemptInput) {
        List<Integer> numbers = new ArrayList<>();
        for (char ch : attemptInput.toCharArray()) {
            numbers.add(Character.getNumericValue(ch));
        }
        return numbers;
    }

    /**
     * Converts a list of integers to a concatenated string (e.g. [1,2,3] -> '123').
     */
    public String convertListToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer digit : list) {
            sb.append(digit);
        }
        return sb.toString();
    }
}
