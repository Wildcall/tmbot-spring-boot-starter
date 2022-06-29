package ru.malygin.tmbot.cache;

import ru.malygin.tmbot.handler.AbstractHandler;

public interface BotState {
    Class<? extends AbstractHandler> getHandlerClass();

    String getPath();

    String name();
}
