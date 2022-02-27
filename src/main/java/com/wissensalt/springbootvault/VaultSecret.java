package com.wissensalt.springbootvault;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("thirdparty")
public class VaultSecret {

    private String id;
    private String name;
    private String secret;
}
