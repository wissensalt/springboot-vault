package com.wissensalt.springbootvault;

import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Ciphertext;
import org.springframework.vault.support.Plaintext;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter
public class EmployeeNameConverter implements AttributeConverter<String, String> {

    private static final String KEYRING_NAME = "app";
    
    @Override
    public String convertToDatabaseColumn(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            return null;
        }

        Plaintext plainText = null;
        try{
            plainText = Plaintext.of(firstName);
        } catch(Exception e) {
            log.error(">>>>>>>> ERROR {}", e.toString());

            return firstName;
        }

        if (plainText == null) {
            return null;
        }

        return getVaultOperations().opsForTransit().encrypt(KEYRING_NAME, plainText).getCiphertext();
    }

    @Override
    public String convertToEntityAttribute(String firstName) {        
        if (firstName == null || firstName.isEmpty()) {
            return null;
        }

        Ciphertext cipherText = null;
        try{
            cipherText = Ciphertext.of(firstName);
        } catch(Exception e) {
            log.error(">>>>>>>> ERROR {}", e.toString());
        }

        if (cipherText == null) {
            return null;
        }

        return getVaultOperations().opsForTransit().decrypt(KEYRING_NAME, cipherText).asString();
    }

    private VaultOperations getVaultOperations() {
        return ApplicationContextUtils
            .getApplicationContext()
            .getBean(VaultOperations.class);
    }
}
