import java.util.*;

public class Main {
    public static void main(String[] args) {
        /* RPN Calculator
        * Enter ^ for exponents
        * Enter % for modulus operator and '\\' for integer division
        * Enter N for negative integers or numbers
        * Enter S, C, T for trig functions representing sin, cos, tan
        * Example Expressions: S30+C30; 10+N7; 3^5-124*3; 10\\3; 100%13;
        * */

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter an expression: ");
        String expression = scanner.nextLine();

        try {
            double result = evaluateExpression(expression);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            System.out.println("Invalid expression: " + e.getMessage());
        }
    }

    private static double evaluateExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        // easier to check and read. just call .contains if user inputs trig function
        List<Character> trigCharacters = Arrays.asList('s', 'c', 't', 'S', 'C', 'T');

        // length of expression
        for (int i = 0; i < expression.length(); i++) {
            // each token of expression
            char ch = expression.charAt(i);

            // if the token is a digit
            if (Character.isDigit(ch)) {
                StringBuilder num = new StringBuilder();
                // loops when i is less than length of string and the char at index i is either a digit or decimal
                // Builds the string expression
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    num.append(expression.charAt(i++));
                }
                i--;
                double val = Double.parseDouble(num.toString());
                values.push(val);

            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    double result = applyOperator(operators.pop(), values.pop(), values.pop());
                    values.push(result);
                }
                operators.pop();

            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '%' || ch == '\\') {
                // reads the next character ahead -> '\\' represent integer division in expression
                // saves the $ symbol as integer division (having no remainder) because stack is char based and not string based
                if (ch == '\\' && i + 1 < expression.length() && expression.charAt(i + 1) == '\\' ) {
                    operators.push('$');
                    i++;
                } else {
                    // checks precedence when the stack is building up, then pushes new values
                    while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                    }
                    operators.push(ch);
                }

            } else if (trigCharacters.contains(ch)) {
                char trigFunc = Character.toLowerCase(ch);
                operators.push(trigFunc);

                if (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    i++;
                    StringBuilder num = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i++));
                    }
                    i--;
                    double val = Double.parseDouble(num.toString());
                    values.push(applyTrigOperators(trigFunc, val));

                    operators.pop();
                }
            } else if (ch == 'N') {
                if (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    // Probably could have implemented some type of function to reduce code
                    // How it builds the trig values is the same as the N values for using StringBuilder. Could have used method but bugs
                    i++;
                    StringBuilder num = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i++));
                    }
                    i--;

                    double val = Double.parseDouble(num.toString());
                    val *= -1;
                    values.push(val);
                }
            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '^') {
                double exponent = values.pop();
                double base = values.pop();
                values.push(applyOperator(operators.pop(), base, exponent));
            }
            else {
                double rightOperand = values.pop();
                double leftOperand = values.pop();
                values.push(applyOperator(operators.pop(), rightOperand, leftOperand));
            }

        }
        return values.pop();
    }

    private static double buildValue(int i, String expression, boolean isNegative) {
        /* This method was not used in the final production of the code
        * It would have been added to reduce the code and make readability better
        * Could not figure out a small bug when testing the function
        * */
        StringBuilder num = new StringBuilder();

        while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
            num.append(expression.charAt(i++));
        }
        double val = Double.parseDouble(num.toString());

        if (isNegative) {
            return -val;
        } else {
            return val;
        }
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if (op2 == '^') {
            return true;
        }
        if ( op2 == 's' || op2 == 'c' || op2 == 't') {
            return true;
        }
        if (op2 == '*' || op2 == '/' || op2 == '%' || op2 == '$') {
            return op1 != '+' && op1 != '-';
        }
        return false;
    }

    private static double applyOperator(char operator, double a, double b) {
        switch (operator) {
            case '+':
                return b + a;
            case '-':
                return b - a;
            case '*':
                return b * a;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return b / a;
            case '^':
                return Math.pow(b, a);
            case '%':
                // values are switched around on stack, messes up mod -> a mod b instead of b mod a
                return b % a;
            case '$':
                return Math.floor(b/a);
            default:
                return 0;
        }
    }

    private static double applyTrigOperators(char operator, double value) {
        switch (operator) {
            case 's':
                return Math.sin(value);
            case 'c':
                return Math.cos(value);
            case 't':
                return Math.tan(value);
            default:
                return 0;
        }
    }
}
