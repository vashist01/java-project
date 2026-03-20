package com.payment.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void main(String[] args) throws Exception {

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");

        // 🔥 Use 256 for production (if supported)
        keyGen.init(256);

        SecretKey secretKey = keyGen.generateKey();

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated Key: " + encodedKey);
    }
}