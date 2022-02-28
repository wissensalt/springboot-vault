package com.wissensalt.springbootvault;

import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.Ciphertext;
import org.springframework.vault.support.Plaintext;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter
public class IdentityNumberConverter implements AttributeConverter<String, String> {

    private static final String KEYRING_NAME = "app";
    
    @Override
    public String convertToDatabaseColumn(String identityNumber) {
        if (identityNumber == null || identityNumber.isEmpty()) {
            return null;
        }

        return getVaultOperations().opsForTransit().encrypt(KEYRING_NAME, Plaintext.of(identityNumber)).getCiphertext();
    }

    @Override
    public String convertToEntityAttribute(String identityNumber) {
        if (identityNumber == null || identityNumber.isEmpty()) {
            return null;
        }

        return getVaultOperations().opsForTransit().decrypt(KEYRING_NAME, Ciphertext.of(identityNumber)).asString();
    }

    private VaultOperations getVaultOperations() {
        return ApplicationContextUtils
            .getApplicationContext()
            .getBean(VaultOperations.class);
    }
}
