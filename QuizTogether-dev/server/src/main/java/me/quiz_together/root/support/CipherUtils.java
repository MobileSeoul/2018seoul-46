package me.quiz_together.root.support;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CipherUtils {
    private static final String ALGORITHM = "AES";

    public static byte[] encryptAes(byte[] plainText, byte[] cipherSecretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(cipherSecretKey, ALGORITHM);
        Cipher cipher = getCipherInstance(ALGORITHM);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher.doFinal(plainText);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypteAes(byte[] cipherText, byte[] cipherSecretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(cipherSecretKey, ALGORITHM);
        Cipher cipher = getCipherInstance(ALGORITHM);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(cipherText);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static Cipher getCipherInstance(String transforms) {
        Cipher cipher = null;
        try {
            return cipher = Cipher.getInstance(transforms);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
