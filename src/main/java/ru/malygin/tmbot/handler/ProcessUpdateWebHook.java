package ru.malygin.tmbot.handler;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessUpdateWebHook {
    String path();

    Class<? extends BotApiObject> type();
}
