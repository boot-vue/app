package com.bootvue.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

    private boolean swagger;
    private List<String> allowList;
}
