package com.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfigProperties {

    private String host;

    private Integer port;

    private String username;

    private String password;
}
