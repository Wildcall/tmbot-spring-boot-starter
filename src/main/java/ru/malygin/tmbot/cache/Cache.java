package ru.malygin.tmbot.cache;

public interface Cache {
    BotState getBotState(long userId);
}
