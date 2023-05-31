package com.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "default.page")
@Data
public class PageConfigurationProperties {

    private Integer size;

    private Integer number;
}
