import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

class StringCalculator {
    private static final String STARTING_DELIMITERS = ",|\n";
    private static final String CUSTOM_DELIMITERS_REGEX = "^//\\[.+]\n.*";
    private static final String DELIMITER_START_SYMBOL = "[";
    private static final String DELIMITER_END_SYMBOL = "]";
    private static final String EMPTY_STRING = "";
    private static final String NEGATIVE_NUMBER_EXCEPTION = "negatives not allowed: ";
    private static final int LARGEST_NUMBER_ANALYZED = 1000;

    int add(String s) {
        int sum = 0;
        String delimiters = STARTING_DELIMITERS;

        if(s.matches(CUSTOM_DELIMITERS_REGEX)) {
        	delimiters = changeDelimiters(s, delimiters);
        	s = s.substring(s.indexOf('\n')+1);
        }

        if(s.isEmpty()) {
            return sum;
        }

        String[] numbers = s.split(delimiters);
        List<Integer> negativeNumbers = new ArrayList<>();

        for(String number : numbers) {
            int parsedNumber = Integer.parseInt(number);
            if(parsedNumber < 0) {
                negativeNumbers.add(parsedNumber);
            }
            if(parsedNumber <= LARGEST_NUMBER_ANALYZED) {
                sum += Integer.parseInt(number);
            }
        }

        if(!negativeNumbers.isEmpty()) {
            throw new IllegalArgumentException(NEGATIVE_NUMBER_EXCEPTION + negativeNumbers);
        }

        return sum;
    }
    
    private String changeDelimiters(String s, String delimiters) {
    	StringBuilder customDelimiters = new StringBuilder();
        while(s.contains(DELIMITER_START_SYMBOL)) {
            customDelimiters.append(Pattern.quote(s.substring(s.indexOf(DELIMITER_START_SYMBOL)+1, s.indexOf(DELIMITER_END_SYMBOL))));
            s = s.replaceFirst("\\" + DELIMITER_START_SYMBOL, EMPTY_STRING);
            s = s.replaceFirst(DELIMITER_END_SYMBOL, EMPTY_STRING);
            if(s.contains(DELIMITER_START_SYMBOL)) {
                customDelimiters.append("|");
            }
        }
        return customDelimiters.toString();
    }
}
