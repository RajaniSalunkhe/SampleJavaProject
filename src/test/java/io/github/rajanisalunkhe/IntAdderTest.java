package io.github.rajanisalunkhe;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DisplayTest {

    @Test
    void testSumWithPositiveOverflow() {
        // Test positive overflow
        assertThrows(ArithmeticException.class, () -> Display.sum(Integer.MAX_VALUE, 1),
                "Sum of Integer.MAX_VALUE and 1 should throw ArithmeticException due to positive overflow");
    }

    @Test
    void testSumWithNegativeOverflow() {
        // Test negative overflow
        assertThrows(ArithmeticException.class, () -> Display.sum(Integer.MIN_VALUE, -1),
                "Sum of Integer.MIN_VALUE and -1 should throw ArithmeticException due to negative overflow");
    }

}
