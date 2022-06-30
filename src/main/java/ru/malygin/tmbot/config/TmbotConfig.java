package ru.malygin.tmbot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.malygin.tmbot.cache.Cache;
import ru.malygin.tmbot.cache.DefaultCache;
import ru.malygin.tmbot.exception.TmbotExceptionHandler;

@Configuration
public class TmbotConfig {

    @Bean
    @ConditionalOnMissingBean
    public Cache cache() {
        return new DefaultCache();
    }

    @Bean
    @ConditionalOnMissingBean
    public TmbotExceptionHandler tmbotExceptionHandler() {
        return new TmbotExceptionHandler();
    }
}
