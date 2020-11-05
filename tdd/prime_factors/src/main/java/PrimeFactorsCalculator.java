import java.util.ArrayList;
import java.util.List;

public class PrimeFactorsCalculator {

    private static final int SMALLEST_PRIME = 2;

    List<Integer> calculate(int n) {
        List<Integer> result = new ArrayList<Integer>();

        if(n >= SMALLEST_PRIME) {
            for(int divisor = SMALLEST_PRIME; n > divisor; divisor++) {
                while (n % divisor == 0) {
                    result.add(divisor);
                    n /= divisor;
                }
            }
            if(n != 1)
                result.add(n);
        }
        return result;
    }
}
