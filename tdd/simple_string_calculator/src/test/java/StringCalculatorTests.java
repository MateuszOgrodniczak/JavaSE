import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringCalculatorTests {
    private StringCalculator calculator = new StringCalculator();

    @Test
    void testZeroNumbers() {
        check(0, "");
    }

    @Test
    void testOneNumber() {
        check(1, "1");
        check(2, "2");
        check(11, "11");
        check(104, "104");
    }

    @Test
    void testTwoNumbers() {
        check(2, "1,1");
        check(3, "1,2");
        check(20, "15,5");
        check(441, "321,120");
    }

    @Test
    void testMultipleNumbers() {
        check(3, "1,1,1");
        check(11, "5,4,2");
        check(10 + 16 + 4 + 128, "10,16,4,128");
        check(125 + 164 + 42 + 129, "125,164,42,129");
    }

    @Test
    void testNewLines() {
        check(2, "1\n1");
        check(6, "1\n2,3");
        check(1+2+3+6+1+5+12, "1\n2\n3\n6\n1,5,12");
    }

    @Test
    void testDifferentDelimiters() {
        check(3, "//[;]\n1;2");
        check(10, "//[#]\n5#2#2#1");
        check(54+26+10, "//[.]\n54.26.10");
    }

    @Test
     void testNegativeNumbers() {
        checkNegatives("-1", Collections.singletonList(-1));
        checkNegatives("5,2,6,-10,2", Collections.singletonList(-10));
        checkNegatives("-9\n1\n1\n0", Collections.singletonList(-9));
    }

    @Test
    void testMultipleNegativeNumbers() {
        checkNegatives("-4,-2,-6,2,-7", Arrays.asList(-4, -2, -6, -7));
        checkNegatives("-100,-200,-10,-543", Arrays.asList(-100, -200, -10, -543));
        checkNegatives("-43\n21,-75\n81", Arrays.asList(-43, -75));
    }

    @Test
    void testLargeNumbers() {
        check(2, "2,1001");
        check(50, "5000,4,21,9,16,1001,10000");
        check(2+1000, "2,1000");
    }

    @Test
    void testStringDelimeters() {
        check(6, "//[***]\n1***2***3");
        check(4+74+3+1, "//[##@@!!]\n4##@@!!74##@@!!3##@@!!1");
    }

    @Test
    void testMultipleDelimiters() {
        check(6,  "//[*][%]\n1*2%3");
        check(10,  "//[..][###][@@]\n1@@2..3###2###2");
        check(6+7+8+9,  "//[%%%%%][.][*]\n6.7*8%%%%%9");
    }

    private void check(int expected, String input) {
        assertEquals(expected, calculator.add(input));
    }

    private void checkNegatives(String input, List<Integer> negativeNumbers) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> calculator.add(input));
        assertEquals("negatives not allowed: " + negativeNumbers, e.getMessage());
    }
}
