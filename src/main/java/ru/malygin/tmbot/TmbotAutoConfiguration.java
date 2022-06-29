package ru.malygin.tmbot;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.malygin.tmbot.config.TmbotProperties;

@ComponentScan
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(TmbotProperties.class)
public class TmbotAutoConfiguration {
}
