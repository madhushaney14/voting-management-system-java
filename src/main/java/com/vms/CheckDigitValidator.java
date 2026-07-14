package com.vms;

public class CheckDigitValidator {

    public static int calculateCheckDigit(String nineDigits) {
        if (nineDigits.length() != 9 || !nineDigits.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("Input must be exactly 9 digits");
        }

        int total = 0;
        for (int position = 1; position <= 9; position++) {
            int digit = Character.getNumericValue(nineDigits.charAt(position - 1));
            total += digit * position;
        }

        return total % 10;
    }

    public static boolean isValidId(String fullId) {
        if (fullId.length() != 10 || !fullId.chars().allMatch(Character::isDigit)) {
            return false;
        }

        String nineDigits = fullId.substring(0, 9);
        int providedCheck = Character.getNumericValue(fullId.charAt(9));
        int expectedCheck = calculateCheckDigit(nineDigits);

        return providedCheck == expectedCheck;
    }
}