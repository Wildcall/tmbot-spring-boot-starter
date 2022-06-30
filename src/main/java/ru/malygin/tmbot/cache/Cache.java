package ru.malygin.tmbot.cache;

public interface Cache {
    BotState getBotState(Long userId);
}
