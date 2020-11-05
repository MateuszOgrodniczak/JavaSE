package tdd.exercises;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class AdvancedStringCalculator {
    private static final String DOUBLE_REGEX = "\\d+(\\.\\d+)?";
    private static final String OPERATOR_REGEX = "[\\^*/+\\-]";
    private static final String NOT_OPERATOR_REGEX = "[^\\^*/+\\-]*";
    private static final String POWER = "\\^";
    private static final String MULTIPLICATION = "\\*";
    private static final String DIVISION = "\\/";
    private static final String ADDITION = "\\+";
    private static final String SUBTRACTION = "-";
    private static final double RETURN_ROUND_VALUE = 10000d;
    private static final String PERCENT_REGEX = "\\d\\d%";
    private static final String PERCENT_DIGIT_REGEX = "\\d%";

    double compute(String input) {
        double sum = 0.0;
        input = input.replaceAll("[ \n\t]", "");

        if (!input.isEmpty()) {
            Pattern pattern = Pattern.compile(OPERATOR_REGEX + OPERATOR_REGEX + "+");
            Matcher matcher = pattern.matcher(input);
            if (matcher.find()) {
                throw new IllegalArgumentException("Missing number between operators: " + matcher.group());
            } else if (StringUtils.countMatches(input, '(') != StringUtils.countMatches(input, ')')) {
                throw new IllegalArgumentException("An extra parenthesis detected");
            }

            if (input.contains("%")) {
                input = percentToNumberConverter(input, PERCENT_REGEX);
                input = percentToNumberConverter(input, PERCENT_DIGIT_REGEX);
            }

            while (input.contains("(")) {
                String substr = input.substring(input.lastIndexOf("("));
                substr = substr.substring(0, substr.indexOf(")") + 1);
                String temp = substr;
                substr = substr.replaceAll("[()]", "");
                substr = performAllOperations(substr);
                input = input.replace(temp, substr);
            }

            input = performAllOperations(input);
            sum = Double.parseDouble(input);
        }
        return Math.round(sum * RETURN_ROUND_VALUE) / RETURN_ROUND_VALUE;
    }

    private String performAllOperations(String inputString) {
        inputString = calculateOperation(POWER, inputString);
        inputString = calculateOperation(MULTIPLICATION, inputString);
        inputString = calculateOperation(DIVISION, inputString);
        inputString = calculateOperation(ADDITION, inputString);
        inputString = calculateOperation(SUBTRACTION, inputString);
        return inputString;
    }

    private String calculateOperation(String operator, String input) {
        while (input.contains(operator.replace("\\", ""))) {
            Pattern EXPRESSION = Pattern.compile(NOT_OPERATOR_REGEX + DOUBLE_REGEX + NOT_OPERATOR_REGEX + operator + NOT_OPERATOR_REGEX + DOUBLE_REGEX + NOT_OPERATOR_REGEX);
            Matcher m = EXPRESSION.matcher(input);
            while (m.find()) {
                String[] numbers = m.group().split(operator);
                Double result = 0.0;
                try {
                    switch (operator.replace("\\", "")) {
                        case "^": {
                            result = Math.pow(Double.parseDouble(numbers[0]), Double.parseDouble(numbers[1]));
                            break;
                        }
                        case "*": {
                            result = Double.parseDouble(numbers[0]) * Double.parseDouble(numbers[1]);
                            break;
                        }
                        case "/": {
                            if (Double.parseDouble(numbers[1]) == 0) {
                                throw new ArithmeticException("Division by zero");
                            }
                            result = Double.parseDouble(numbers[0]) / Double.parseDouble(numbers[1]);
                            break;
                        }
                        case "+": {
                            result = Double.parseDouble(numbers[0]) + Double.parseDouble(numbers[1]);
                            break;
                        }
                        case "-": {
                            result = Double.parseDouble(numbers[0]) - Double.parseDouble(numbers[1]);
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Invalid number detected: " + e.getMessage().replace("For input string: \"", "").replace("\"", ""));
                }
                input = input.replace(m.group(), result.toString());
            }
        }
        return input;
    }

    private String percentToNumberConverter(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String percentNumber = matcher.group();
            Double percentNumberAsDouble = Double.parseDouble(percentNumber.replace("%", "")) / 100d;
            input = input.replaceAll(percentNumber, percentNumberAsDouble.toString());
        }
        return input;
    }
}