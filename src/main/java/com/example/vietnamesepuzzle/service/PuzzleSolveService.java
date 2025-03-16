package com.example.vietnamesepuzzle.service;

import com.example.vietnamesepuzzle.model.Attempt;
import com.example.vietnamesepuzzle.repository.AttemptRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PuzzleSolveService {

    private final AttemptRepository attemptRepository;
    private final AttemptUtils attemptUtils; // to convert strings/lists

    public PuzzleSolveService(AttemptRepository attemptRepository,
                              AttemptUtils attemptUtils) {
        this.attemptRepository = attemptRepository;
        this.attemptUtils = attemptUtils;
    }

    /**
     * Generates a solution using an optimized approach while storing
     * all attempts (correct or not) until the first valid solution is found.
     * The equation is:
     * A + 13*(B/C) + D + E + 12*F - G - 11 + (H*I)/G - 10 == 66
     *
     * @return a Map with keys "solution" (String) and "durationMs" (Long)
     */
    public Map<String, Object> generateSolutions() {
        // Clear previous attempts
        attemptRepository.deleteAll();
        // We'll store the first valid solution in a local variable
        List<String> firstSolution = new ArrayList<>(1);

        long startTime = System.currentTimeMillis();

        backtrackOptimizedStoreAll(firstSolution);

        long duration = System.currentTimeMillis() - startTime;

        Map<String, Object> result = new HashMap<>();
        result.put("solution", firstSolution.isEmpty() ? null : firstSolution.get(0));
        result.put("durationMs", duration);
        return result;
    }

    /**
     * Optimized backtracking that applies constraints but also stores each attempt
     * in the database (correct or incorrect) until the first valid solution is found.
     */
    private void backtrackOptimizedStoreAll(List<String> firstSolution) {
        List<Integer> digits = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // We pick 5 permutations for B, C, H, I, G
        List<List<Integer>> perm5 = permutations(digits, 5);

        outerLoop:
        for (List<Integer> list5 : perm5) {
            int B = list5.get(0);
            int C = list5.get(1);
            int H = list5.get(2);
            int I = list5.get(3);
            int G = list5.get(4);

            // Skip if C or G is 5 or 7 as denominators
            if (C == 5 || C == 7 || G == 5 || G == 7) {
                continue;
            }

            Fraction frac1 = new Fraction(13L * B, C);
            Fraction frac2 = new Fraction((long) H * I, G);

            if (!frac1.add(frac2).isInteger()) {
                continue;
            }

            // The remaining digits for A, D, E, F
            Set<Integer> used = new HashSet<>(list5);
            List<Integer> remaining = new ArrayList<>();
            for (int d : digits) {
                if (!used.contains(d)) {
                    remaining.add(d);
                }
            }

            List<List<Integer>> perm4 = permutations(remaining, 4);

            for (List<Integer> list4 : perm4) {
                int A = list4.get(0);
                int D = list4.get(1);
                int E = list4.get(2);
                int F = list4.get(3);

                if (E > 7) {
                    // store as incorrect
                    String attemptInput = attemptUtils.convertListToString(
                            Arrays.asList(A, B, C, D, E, F, G, H, I)
                    );
                    storeAttemptIfNotExists(attemptInput, false);
                    continue;
                }

                Fraction total = new Fraction(A, 1)
                        .add(frac1)
                        .add(new Fraction(D, 1))
                        .add(new Fraction(E, 1))
                        .add(new Fraction(12L * F, 1))
                        .subtract(new Fraction(G, 1))
                        .subtract(new Fraction(11, 1))
                        .add(frac2)
                        .subtract(new Fraction(10, 1));

                String attemptInput = attemptUtils.convertListToString(
                        Arrays.asList(A, B, C, D, E, F, G, H, I)
                );

                boolean correct = (total.isInteger() && total.intValue() == 66);
                storeAttemptIfNotExists(attemptInput, correct);

                if (correct) {
                    firstSolution.add(attemptInput);
                    break outerLoop;
                }
            }
        }
    }

    /**
     * Naive backtracking approach that tries all 9! permutations.
     * It is kept here for reference.
     */
    public void backtrackNaive(List<Integer> current, List<Integer> remaining, List<String> solutions) {
        // For instance, store solutions or just find the first
        if (remaining.isEmpty()) {
            // Evaluate
            boolean correct = evaluateExpression(current);
            String attemptInput = attemptUtils.convertListToString(current);
            storeAttemptIfNotExists(attemptInput, correct);
            if (correct) {
                solutions.add(attemptInput);
            }
            return;
        }
        for (Integer digit : new ArrayList<>(remaining)) {
            current.add(digit);
            List<Integer> newRemaining = new ArrayList<>(remaining);
            newRemaining.remove(digit);
            backtrackNaive(current, newRemaining, solutions);
            current.remove(current.size() - 1);
        }
    }

    // Evaluate the puzzle's equation for a 9-digit permutation
    public boolean evaluateExpression(List<Integer> values) {
        if (values.size() < 9) {
            return false;
        }
        int A = values.get(0);
        int B = values.get(1);
        int C = values.get(2);
        int D = values.get(3);
        int E = values.get(4);
        int F = values.get(5);
        int G = values.get(6);
        int H = values.get(7);
        int I = values.get(8);

        double result = A + 13 * ((double) B / C) + D + E
                + 12 * F - G - 11 + ((double) H * I) / G - 10;

        return Math.abs(result - 66.0) < 0.000001;
    }

    /**
     * Generate permutations of length k from the given list of digits
     */
    public List<List<Integer>> permutations(List<Integer> arr, int k) {
        List<List<Integer>> result = new ArrayList<>();
        backtrackPerm(arr, new ArrayList<>(), k, result);
        return result;
    }

    public void backtrackPerm(List<Integer> arr, List<Integer> temp, int k, List<List<Integer>> result) {
        if (temp.size() == k) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = 0; i < arr.size(); i++) {
            int val = arr.get(i);
            temp.add(val);
            List<Integer> remaining = new ArrayList<>(arr);
            remaining.remove(i);
            backtrackPerm(remaining, temp, k, result);
            temp.remove(temp.size() - 1);
        }
    }

    /**
     * Store the attempt in the database if it does not exist yet.
     *
     * @param attemptInput the 9-digit permutation as a string
     * @param isCorrect    whether the attempt is correct
     */
    private void storeAttemptIfNotExists(String attemptInput, boolean isCorrect) {
        if (attemptRepository.findByAttemptInput(attemptInput).isEmpty()) {
            Attempt attempt = new Attempt();
            attempt.setAttemptInput(attemptInput);
            attempt.setCorrect(isCorrect);
            attemptRepository.save(attempt);
        }
    }

    /**
     * A small Fraction class to avoid floating precision issues.
     */
    public static class Fraction {
        private final long num;
        private final long den;

        public Fraction(long num, long den) {
            if (den == 0) {
                throw new ArithmeticException("Denominator cannot be zero");
            }
            long g = gcd(Math.abs(num), Math.abs(den));
            if (den < 0) {
                den = -den;
                num = -num;
            }
            this.num = num / g;
            this.den = den / g;
        }

        private static long gcd(long a, long b) {
            if (b == 0) return a;
            return gcd(b, a % b);
        }

        public Fraction add(Fraction other) {
            long newNum = this.num * other.den + other.num * this.den;
            long newDen = this.den * other.den;
            return new Fraction(newNum, newDen);
        }

        public Fraction subtract(Fraction other) {
            long newNum = this.num * other.den - other.num * this.den;
            long newDen = this.den * other.den;
            return new Fraction(newNum, newDen);
        }

        public boolean isInteger() {
            return (num % den == 0);
        }

        public int intValue() {
            return (int) (num / den);
        }
    }
}
