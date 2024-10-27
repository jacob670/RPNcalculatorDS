import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean testing = true;
        while (testing) {
            System.out.print("Enter an expression: ");
            String expression = scanner.nextLine();


            try {
                double result = evaluateExpression(expression);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Invalid expression: " + e.getMessage());
            }
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

            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '%') {
                // reads the next character ahead -> '//' represent integer division in expression
                // saves the $ symbol as integer division (having no remainder) because stack is char based and not string based
                if (ch == '/' && expression.charAt(i + 1) == '/') {
                    operators.push('$');
                    System.out.println(operators);
                    i++;
                } else {

                    System.out.println("Opposite of isStackEmpty?: " + !operators.isEmpty());
                    System.out.println();

                    // checks precedence when the stack is building up, then pushes new values
                    while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                        System.out.println("CHECKING THE PRECEDENCE IN LOOP for " + ch + " vs. " + operators.peek() + " and value is " + hasPrecedence(ch, operators.peek()));
                        System.out.println();

                        System.out.println("Current values: " + values);
                        System.out.println("Current operators: " + operators);
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));

                        System.out.println(values);
                        System.out.println(operators);
                    }
                    operators.push(ch);
                }
            } else if (trigCharacters.contains(ch)) {
                char trigFunc = Character.toUpperCase(ch);
                if (trigFunc == 'S') {
                    operators.push('s');
                } else if (trigFunc == 'C') {
                    operators.push('c');
                } else if (trigFunc == 'T') {
                    operators.push('t');
                }

                // Here we expect the next token to be a number
                if (i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1))) {
                    i++;
                    StringBuilder num = new StringBuilder();
                    while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        num.append(expression.charAt(i++));
                    }
                    i--;
                    double val = Double.parseDouble(num.toString());
                    // Push the result of the trig function directly
                    values.push(applyTrigOperators(trigFunc, val));
                }
//                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
//                    System.out.println("CHECKING THE PRECEDENCE IN LOOP for " + ch + " vs. " + operators.peek() + " and value is " + hasPrecedence(ch, operators.peek()));
//                    System.out.println();
//
//                    System.out.println("Current values: " + values);
//                    System.out.println("Current operators: " + operators);
//                    values.push(applyTrigOperators(operators.pop(), values.pop()));
//
//                    System.out.println(values);
//                    System.out.println(operators);
//                }
                // operators.push(ch);
            } else if (ch == 'N') {
                if (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)))) {
                    i++;
                    String conversion = String.valueOf(expression.charAt(i));
                    Double val = Double.parseDouble(conversion);
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
            else if (operators.peek() == 's' || operators.peek() == 'c' || operators.peek() == 't') {
                System.out.println("Final values: " + values);
                System.out.println("Final operators: " + operators);
                values.push(applyTrigOperators(operators.pop(), values.pop()));
            }
            else {
                System.out.println("Final values: " + values);
                System.out.println("Final operators: " + operators);
                double rightOperand = values.pop();
                double leftOperand = values.pop();
                values.push(applyOperator(operators.pop(), rightOperand, leftOperand));
            }

        }
        System.out.println("Final return value: " + values.peek());
        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        // # *+-/ trig function works
        // trig function +-/* # does not work thinks expression is null

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

        //return op1 == 's' || op1 == 'c' || op1 == 't';






//        if (op2 == '(' || op2 == ')') {
//            return false;
//        }
//        if (op2 == '^') {
//            return true;
//        }
//        if (op1 == '*' || op1 == '/' || op1 == '%' || op1 == '$') {
//            return op2 != '+' && op2 != '-';
//        }
//
//        if (op1 == '+' || op1 == '-') {
//            return false;
//        }
//
//        return op1 == 's' || op1 == 'c' || op1 == 't';


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
