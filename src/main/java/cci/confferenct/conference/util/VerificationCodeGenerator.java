package cci.confferenct.conference.util;

import java.security.SecureRandom;

public class VerificationCodeGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();


    public static String generate6DigitCode() {
        int number = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(number);
    }
}