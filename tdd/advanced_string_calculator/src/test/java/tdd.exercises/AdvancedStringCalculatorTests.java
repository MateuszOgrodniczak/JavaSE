package tdd.exercises;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedStringCalculatorTests {
    private final AdvancedStringCalculator calculator = new AdvancedStringCalculator();
    private static final double RETURN_ROUND_VALUE = 10000d;

    @Test
    void testSingleNumber() {
        check(0.0, "");
        check(1.0, "1.0");
        check(43.251, "43.251");
    }

    @Test
    void testOneOperation() {
        check(3.5, "1+2.5");
        check(4.0, "6.2-2.2");
        check(15, "5*3");
        check(2, "8/4");
        check(25, "5^2");
    }

    @Test
    void testIgnoredWhiteCharacters() {
        check(6.6, "2.2*   3");
        check(6.6, "2.2\n*3");
        check(6.6, "2.2\t*3");
        check(6.6, "2.2   * \t  3 \n");
    }

    @Test
    void testMultipleOperations() {
        check(2.6 + 3.251 + 4.12, "2.6 + 3.251 + 4.12");
        check(1.5 + 2 * 9, "1.5 + 2 * 3^2");
        check(2.0 * 2.0 * 1.0 + 2.0 / 2.0 / 1.0, "2.0 * 2.0 * 1.0 + 2.0 / 2.0 / 1.0");
        check(39.0625 + 5 - 2 * 4.21 - 6.2, "2.5 ^ 4 + 5 - 2 * 4.21 - 6.2");
    }

    @Test
    void testParenthesis() {
        check(((2.4 - 0.4) * 3) * 2, "((2.4-0.4)*3)*2");
        check((1.5 + 2 * 2) * 9, "(1.5 + 2 *2) * 3^2");
        check(Math.round((1.2 * 25.12 * (4 + 5) * ((5 - 2.1) * 2) * 2.5 + 5.12 / 3.0) * RETURN_ROUND_VALUE) / RETURN_ROUND_VALUE, "1.2*25.12*(4+5)*((5-2.1)*2)*2.5+5.12/3.0");
    }

    @Test
    void testMissingNumberExceptions() {
        checkThrows(IllegalArgumentException.class, "Missing number between operators: -*", "1 -* 3");
        checkThrows(IllegalArgumentException.class, "Missing number between operators: -+", "5.134 ^ 3 -+");
        checkThrows(IllegalArgumentException.class, "Missing number between operators: +^", "1 + 3 * 5.123 +^ 2 + 5 *-2");
    }

    @Test
    void testInvalidNumberException() {
        checkThrows(NumberFormatException.class, "Invalid number detected: 2a", "2a * 3");
        checkThrows(NumberFormatException.class, "Invalid number detected: 4.2'", "2.513 + (1.3 - 4.2') / 3");
        checkThrows(NumberFormatException.class, "Invalid number detected: 12c", "4.13 * 5b -2 ^ 12c");
    }

    @Test
    void testExtraParenthesis() {
        checkThrows(IllegalArgumentException.class, "An extra parenthesis detected", "2 * (3.4 + 6))");
        checkThrows(IllegalArgumentException.class, "An extra parenthesis detected", "6.12 + (2.3 - 42) * 5 + ((2.5+2) / (1.4^2-4)");
        checkThrows(IllegalArgumentException.class, "An extra parenthesis detected", "5.21( * 5.135");
    }

    @Test
    void testDivisionByZero() {
        checkThrows(ArithmeticException.class, "Division by zero", "1 / (2-2)");
        checkThrows(ArithmeticException.class, "Division by zero", "5.2 - 3.12 * 2.1 / 0.0");
    }

    @Test
    void testPercentages() {
        check(Math.round((2 + 0.05 - 2.1 * 0.75 / 4) * RETURN_ROUND_VALUE) / RETURN_ROUND_VALUE, "2 + 5% - 2.1 * 75% / 4");
        check(0.1 * 0.88 + 0.66 - 0.25, "10%*88%+66%-25%");
        check(0, "(12.531*51^3-(51+1.6-2.00)*7.25)*0%");
    }

    private void check(double expected, String input) {
        assertEquals(expected, calculator.compute(input));
    }

    private void checkThrows(Class<? extends Exception> e, String message, String input) {
        Exception thrown = assertThrows(e, () -> calculator.compute(input));
        assertEquals(message, thrown.getMessage());
    }
}