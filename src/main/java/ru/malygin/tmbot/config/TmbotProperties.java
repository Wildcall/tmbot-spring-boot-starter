package ru.malygin.tmbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("spring.tmbot")
public class TmbotProperties {

    private String name;
    private String token;
    private String path;
}
