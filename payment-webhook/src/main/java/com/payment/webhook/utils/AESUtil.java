package com.payment.webhook.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESUtil {

    private static final String ALGO = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    // ✅ Base64 encoded 256-bit key
    private static final String secretKey = "2N3Cmqaf3i9pZz9xnIaXoaxcFT1zAoCxnIU5dcQz5uE=";

    public static SecretKey getKey(String base64Key) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key); // ✅ FIX
        return new SecretKeySpec(decodedKey, ALGO);
    }

    public static String encrypt(String plainText) {
        try {
            byte[] iv = new byte[IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            cipher.init(Cipher.ENCRYPT_MODE, getKey(secretKey), spec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    // 🔓 Decrypt
    public static String decrypt(String encryptedText, String secret) throws Exception {

        byte[] decoded = Base64.getDecoder().decode(encryptedText);

        byte[] iv = new byte[IV_LENGTH];
        byte[] encrypted = new byte[decoded.length - IV_LENGTH];

        System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
        System.arraycopy(decoded, IV_LENGTH, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

        cipher.init(Cipher.DECRYPT_MODE, getKey(secret), spec);

        byte[] decrypted = cipher.doFinal(encrypted);

        return new String(decrypted, StandardCharsets.UTF_8);
    }



}