import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimeFactorsCalculatorTests {

    private PrimeFactorsCalculator calculator = new PrimeFactorsCalculator();

    @Test
    public void testEmptyList() {
        test(Collections.<Integer>emptyList(), 0);
        test(Collections.<Integer>emptyList(), 1);
    }

    @Test
    public void testSmallDigits() {
        test(Arrays.asList(2), 2);
        test(Arrays.asList(3), 3);
    }

    @Test
    public void testSimpleCases() {
        test(Arrays.asList(2, 2), 4);
        test(Arrays.asList(5), 5);
        test(Arrays.asList(2, 3), 6);
        test(Arrays.asList(7), 7);
    }

    @Test
    public void testHarderCases() {
        test(Arrays.asList(2, 2, 2), 8);
        test(Arrays.asList(3, 3), 9);
        test(Arrays.asList(2, 5), 10);
        test(Arrays.asList(2, 2, 3), 12);
    }

    @Test
    public void testLargeNumbers() {
        test(Arrays.asList(5, 5), 25);
        test(Arrays.asList(2, 3, 5), 30);
        test(Arrays.asList(2, 2, 5, 5), 100);
        test(Arrays.asList(7, 11, 13), 7 * 11 * 13);
        test(Arrays.asList(17, 19, 19, 53, 107), 17 * 19 * 19 * 53 * 107);
    }

    private void test(List<Integer> factors, int number) {
        assertEquals(factors, calculator.calculate(number));
    }
}
