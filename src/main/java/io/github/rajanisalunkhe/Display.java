package io.github.rajanisalunkhe;

public class Display {

    private Display() {
        throw new IllegalStateException("Utility class");
    }

    public static int sum(int a, int b) {

        if (a > 0 && b > Integer.MAX_VALUE - a) {
            throw new ArithmeticException("Positiv sum");
        }
        if (a < 0 && b < Integer.MIN_VALUE - a) {
            throw new ArithmeticException("Negative sum");
        }
        return a + b;
    }
}
