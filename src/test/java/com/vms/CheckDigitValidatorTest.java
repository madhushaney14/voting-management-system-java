package com.vms;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckDigitValidatorTest {

    @Test
    public void testCalculateCheckDigit_correctValue() {
        int result = CheckDigitValidator.calculateCheckDigit("314159265");
        assertEquals(7, result);
    }

    @Test
    public void testIsValidId_validId_returnsTrue() {
        boolean result = CheckDigitValidator.isValidId("3141592657");
        assertTrue(result);
    }

    @Test
    public void testIsValidId_tamperedId_returnsFalse() {
        boolean result = CheckDigitValidator.isValidId("3141592658");
        assertFalse(result);
    }

    @Test
    public void testIsValidId_wrongLength_returnsFalse() {
        boolean result = CheckDigitValidator.isValidId("12345");
        assertFalse(result);
    }

    @Test
    public void testIsValidId_nonDigitCharacters_returnsFalse() {
        boolean result = CheckDigitValidator.isValidId("31415926A7");
        assertFalse(result);
    }
}