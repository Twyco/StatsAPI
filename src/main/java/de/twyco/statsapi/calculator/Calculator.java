package de.twyco.statsapi.calculator;

public abstract class Calculator {


    public static double calculateStringTerm(String term) {
        if (!validateTerm(term)) {
            throw new IllegalArgumentException("The String term is not a valid Term!");
        }
        while (containsNonTerminal(term)) {
            term = calculateInnermostNonTerminal(term);
        }
        return Double.parseDouble(term);
    }

    private static String calculateInnermostNonTerminal(String term) {
        int openInnermostBracket = -1;
        int closeInnermostBracket = -1;

        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            if (c == '(') {
                openInnermostBracket = i;
            } else if (c == ')') {
                closeInnermostBracket = i;
                break;
            }
        }
        if (openInnermostBracket == -1) {
            double terminal = calculateNonTerminal(term);
            return String.valueOf(terminal);
        }else {
            String innermostNonTerminal = term.substring(openInnermostBracket + 1, closeInnermostBracket);
            String innermostNonTerminalWithBrackets = term.substring(openInnermostBracket, closeInnermostBracket + 1);
            double terminal = calculateNonTerminal(innermostNonTerminal);
            return term.replace(innermostNonTerminalWithBrackets, String.valueOf(terminal));
        }
    }

    private static double calculateNonTerminal(String term) {
        StringBuilder fstOperand = new StringBuilder();     //x = 1
        StringBuilder op = new StringBuilder();       //x = 2
        StringBuilder sndOperand = new StringBuilder();     //x = 3
        int x = 1;
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);
            if (x == 1 || x == 3) {
                if (c == '$') {
                    x++;
                    continue;
                }
                if (x == 1) {
                    fstOperand.append(c);
                } else {
                    sndOperand.append(c);
                }
            } else if (x == 2) {
                if (c == '$') {
                    x++;
                    continue;
                }
                op.append(c);
            }
        }
        double firstOperand = Double.parseDouble(fstOperand.toString());
        if (op.isEmpty()) {
            return firstOperand;
        }
        Operator operator = Operator.valueOf(op.toString().toUpperCase());
        double secondOperand = Double.parseDouble(sndOperand.toString());
        return operator.performAction(firstOperand, secondOperand);
    }

    private static boolean containsNonTerminal(String term) {
        boolean containsNonTerminal = false;
        containsNonTerminal |= term.contains("(") || term.contains(")");
        containsNonTerminal |= term.contains("$");

        return containsNonTerminal;
    }

    private static boolean validateTerm(String term) {
        int openBrackets = 0;
        boolean lastCharOperator = false;
        boolean lastCharDecimalPoint = false;
        boolean lastCharMinus = false;
        boolean operandIsDecimal = false;
        for (int i = 0; i < term.length(); i++) {
            char c = term.charAt(i);

            if (Character.isDigit(c)) {
                lastCharDecimalPoint = false;
                lastCharOperator = false;
                lastCharMinus = false;
            } else if (c == '.') {
                if (operandIsDecimal || lastCharMinus) {
                    System.out.println(1);
                    return false;
                }
                lastCharDecimalPoint = true;
                operandIsDecimal = true;
            } else if (c == '-') {
                if (lastCharMinus || (!lastCharOperator && i != 0)) {
                    System.out.println(2);
                    return false;
                }
                lastCharMinus = true;
            } else if (c == '(') {
                if (lastCharDecimalPoint || (!lastCharOperator && !lastCharMinus && i != 0)) {    //Vor ( kein  '.' und muss operator oder - sein, mit ausnahme index 0
                    System.out.println(3);
                    return false;
                }
                operandIsDecimal = false;
                openBrackets++;
            } else if (c == ')') {
                if (openBrackets == 0 || lastCharOperator || lastCharMinus || lastCharDecimalPoint) {
                    System.out.println(4);
                    return false;
                }
                operandIsDecimal = false;
                openBrackets--;
            } else if (c == '$') {
                operandIsDecimal = false;
                if (lastCharDecimalPoint || lastCharMinus || lastCharOperator) {
                    System.out.println(5);
                    return false;
                }
                i++;
                StringBuilder stringBuilder = new StringBuilder();
                while (term.charAt(i) != '$') {
                    stringBuilder.append(term.charAt(i));
                    i++;
                    if (i == term.length()) {
                        System.out.println(6);
                        return false;
                    }
                }
                lastCharOperator = true;
                if (i == (term.length() - 1)) {
                    System.out.println(7);
                    return false;
                }
                boolean validOperator = false;
                for (Operator operator : Operator.values()) {
                    if (operator.toString().equalsIgnoreCase(stringBuilder.toString())) {
                        validOperator = true;
                    }
                }
                if (!validOperator) {
                    System.out.println(8);
                    return false;
                }
            }
        }
        return openBrackets == 0;
    }

}
