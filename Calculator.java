import java.util.*;

import static java.lang.Double.NaN;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


/*
 *   A calculator for rather simple arithmetic expressions
 *
 *   This is not the program, it's a class declaration (with methods) in its
 *   own file (which must be named Calculator.java)
 *
 *   NOTE:
 *   - No negative numbers implemented
 */
public class Calculator {

    // Here are the only allowed instance variables!
    // Error messages (more on static later)
    final static String MISSING_OPERAND = "Missing or bad operand";
    final static String DIV_BY_ZERO = "Division with 0";
    final static String MISSING_OPERATOR = "Missing operator or parenthesis";
    final static String OP_NOT_FOUND = "Operator not found";

    // Definition of operators
    final static String OPERATORS = "+-*/^";

    // Method used in REPL
    double eval(String expr) {
        if (expr.length() == 0) {
            return NaN;
        }
        List<String> tokens = tokenize(expr);
        List<String> postfix = infix2Postfix(tokens);
        return evalPostfix(postfix);
    }

    // ------  Evaluate RPN expression -------------------

    double evalPostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        for (String s : postfix) {
            if (!OPERATORS.contains(s)) {
                try {
                    stack.push(Double.parseDouble(s));
                }
                catch (Exception e) {
                    throw new IllegalArgumentException(MISSING_OPERATOR);
                }
            } else {
                stack.push(applyOperator(s, stack.pop(), stack.pop()));
            }
        }
        return stack.lastElement();
    }
    double applyOperator(String op, double d1, double d2) {
        switch (op) {
            case "+":
                return d1 + d2;
            case "-":
                return d2 - d1;
            case "*":
                return d1 * d2;
            case "/":
                if (d1 == 0) {
                    throw new IllegalArgumentException(DIV_BY_ZERO);
                }
                return d2 / d1;
            case "^":
                return pow(d2, d1);
        }
        throw new RuntimeException(OP_NOT_FOUND);
    }

    // ------- Infix 2 Postfix ------------------------

    List<String> infix2Postfix(List<String> infix) {
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        for (String s : infix) {
            try {
                stackLogic(s, postfix, stack);
            }
            catch (Exception e) {
                throw new IllegalArgumentException(MISSING_OPERATOR);
            }
        }
        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        String valid = isValid(postfix);
        if (valid == "true") {
            return postfix;
        }
        else {
            throw new IllegalArgumentException(valid);
        }
    }
    void stackLogic (String op, List<String> postfix, Stack<String> stack) {
        if (op.equals("(")) {
            stack.push(op);
        }
        else if (op.equals(")")) {
            while (!stack.isEmpty() && !"(".equals(stack.lastElement())) {
                postfix.add(stack.pop());
            }
            stack.pop();
        }
        else if (OPERATORS.contains(op)) {
            while (!stack.isEmpty() && !"(".equals(stack.lastElement()) && getPrecedence(stack.lastElement()) >= getPrecedence(op) && getAssociativity(op) != Assoc.RIGHT) {
                postfix.add(stack.pop());
            }
            stack.push(op);
        }
        else {
            postfix.add(op);
        }
    }
    int getPrecedence(String op) {
        if ("+-".contains(op)) {
            return 2;
        } else if ("*/".contains(op)) {
            return 3;
        } else if ("^".contains(op)) {
            return 4;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }
    Assoc getAssociativity(String op) {
        if ("+-*/".contains(op)) {
            return Assoc.LEFT;
        } else if ("^".contains(op)) {
            return Assoc.RIGHT;
        } else {
            throw new RuntimeException(OP_NOT_FOUND);
        }
    }

    enum Assoc {
        LEFT,
        RIGHT
    }

    // ---------- Tokenize -----------------------

    // List String (not char) because numbers (with many chars)
    List<String> tokenize(String expr) {
        // TODO
        List<String> tokenized = new ArrayList<>();
        String toAdd = "";
        char[] chars = expr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (isNumber(chars[i])) {
                toAdd = toAdd + chars[i];
            }
            else if (isOperator(chars[i]) || isParenthesis(chars[i])) {
                if (!toAdd.equals("")) {
                    tokenized.add(toAdd);
                    toAdd = "";
                }
                toAdd = toAdd + chars[i];
                tokenized.add(toAdd);
                toAdd = "";
            }
            else if (i != 0 && chars[i] == ' ' && isNumber(chars[i - 1])) {
                tokenized.add(toAdd);
                toAdd = "";
            }
        }
        if (!toAdd.equals("")) {
            tokenized.add(toAdd);
        }
        return tokenized;
    }
    boolean isNumber (char c){
        if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6'
                || c == '7' || c == '8' || c == '9' || c == '0') {
            return true;
        }
        return false;
    }
    boolean isParenthesis (char c) {
        if (c == '(' || c == ')') {
            return true;
        }
        return false;
    }
    boolean isOperator (char c) {
        if (c == '+' || c == '*' || c == '/' || c == '^' || c == '-') {
            return true;
        }
        return false;
    }
    String isValid (List<String> postfix) {
        int ops = 0;
        int nums = 0;
        for (String s : postfix) {
            if (OPERATORS.contains(s)){
                ops++;
            }
            else {
                nums++;
            }
        }
        if (nums == ops + 1) {
            return "true";
        }
        else if (nums < ops + 1){
            return MISSING_OPERAND;
        }
        else {
            return MISSING_OPERATOR;
        }
    }
}
