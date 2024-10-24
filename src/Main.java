import java.util.Scanner;
import java.util.Stack;

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
                if (ch == '/' && expression.charAt(i+1) == '/') {
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
            } else if (ch == 's' || ch == 'c' || ch == 't') {
                if (ch == 's' && expression.charAt(i + 1) == 'i' && expression.charAt(i + 2) == 'n') {
                    System.out.println("sin");
                    operators.push('s');
                    i+=2;
                } else if (ch == 'c' && expression.charAt(i + 1) == 'o' && expression.charAt(i + 2) == 's') {
                    System.out.println("cos");
                    operators.push('c');
                    i += 2;
                } else if (ch == 't' && expression.charAt(i + 1) == 'a' && expression.charAt(i + 2) == 'n') {
                    System.out.println("tan");
                    operators.push('t');
                    i += 2;
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

            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '^') {
                double exponent = values.pop();
                double base = values.pop();
                values.push(applyOperator(operators.pop(), base, exponent));
            } else if (operators.peek() == 's' || operators.peek() == 'c' || operators.peek() == 't') {
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

//        return op1 == 's' || op1 == 'c' || op1 == 't';






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
