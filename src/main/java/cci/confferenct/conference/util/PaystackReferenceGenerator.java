package cci.confferenct.conference.util;

import java.util.UUID;

public class PaystackReferenceGenerator {

    public static String generateReference() {

        String prefix = "CCI";

        String uuidPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        long timestamp = System.currentTimeMillis();

        return prefix + "_" + timestamp + "_" + uuidPart;
    }
}