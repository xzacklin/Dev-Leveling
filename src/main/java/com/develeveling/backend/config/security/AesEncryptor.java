package com.develeveling.backend.config.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * A JPA AttributeConverter that automatically encrypts and decrypts a String entity attribute.
 */
@Converter
@Component
public class AesEncryptor implements AttributeConverter<String, String> {

    @Value("${encryption.aes.key}")
    private String secretKey;

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * Encrypts the attribute before it is saved to the database column.
     * @param attribute The raw string from the entity.
     * @return The Base64-encoded encrypted string.
     */
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {

            throw new IllegalStateException("Failed to encrypt attribute", e);
        }
    }

    /**
     * Decrypts the data from the database column when it is read into the entity.
     * @param dbData The encrypted, Base64-encoded string from the database.
     * @return The original raw string.
     */
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt attribute", e);
        }
    }
}