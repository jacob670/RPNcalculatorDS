import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
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

                if (ch == '/' && expression.charAt(i+1) == '/') {
                    if (expression.charAt(i + 1) == '/') {
                        System.out.println("integer divison");
                        operators.push("//")
                    }
                } else {

                    System.out.println("Opposite of isStackEmpty?: " + !operators.isEmpty());
                    System.out.println();

                    // checks precedence when the stack is building up, then pushes new values
                    while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
                        System.out.println("CHECKING THE PRECEDENCE IN LOOP for " + ch + " vs. " + operators.peek() + " and value is " + hasPrecedence(ch, operators.peek()));
                        System.out.println();

                        System.out.println(values);
                        System.out.println(operators);
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()));

                        System.out.println(values);
                        System.out.println(operators);
                    }
                    operators.push(ch);
                }

//                System.out.println("Opposite of isStackEmpty?: " + !operators.isEmpty());
//                System.out.println();
//
//                // checks precedence when the stack is building up, then pushes new values
//                while (!operators.isEmpty() && hasPrecedence(ch, operators.peek())) {
//                    System.out.println("CHECKING THE PRECEDENCE IN LOOP for " + ch + " vs. " + operators.peek() + " and value is " + hasPrecedence(ch, operators.peek()));
//                    System.out.println();
//
//                    System.out.println(values);
//                    System.out.println(operators);
//                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
//
//                    System.out.println(values);
//                    System.out.println(operators);
//                }
//                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            if (operators.peek() == '^') {
                double exponent = values.pop();
                double base = values.pop();
                values.push(applyOperator(operators.pop(), base, exponent));
            } else {
                System.out.println("Final values: " + values);
                System.out.println("Final operators: " + operators);
                double rightOperand = values.pop();
                double leftOperand = values.pop();
                values.push(applyOperator(operators.pop(), leftOperand, rightOperand));
            }

        }
        System.out.println("Final return value: " + values.peek());
        return values.pop();
    }


    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if (op1 == '^') {
            return op2 != '^';
        }

        return (op1 != '*' && op1 != '/' && op1 != '%') || (op2 != '+' && op2 != '-');
    }

    private static double applyOperator(char operator, double a, double b) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            case '%':
                // values are switched around on stack, messes up mod -> a mod b instead of b mod a
                return b % a;
            default:
                return 0;
        }
    }
}
