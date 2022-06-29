package ru.malygin.tmbot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.cache.BotState;
import ru.malygin.tmbot.cache.Cache;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public final class DispatcherHandler {

    private final Cache cache;
    private final HandlersFactory handlersFactory;

    public ReplyPayload dispatch(Update update) {
        try {
            if (update == null) return null;
            Long userId = getUserId(update);
            BotState state = cache.getBotState(userId);

            AbstractHandler handler = handlersFactory
                    .getHandlers()
                    .get(state.getHandlerClass());
            if (handler != null) {
                return handler.handle(state, getNotNullProperty(update));
            } else
                throw new RuntimeException(String.format("Handler for state: [%s] not found", state.name()));
        } catch (RuntimeException e) {
            log.error("ERROR LOGGING: {}", e.getMessage());
        }
        return null;
    }

    private Long getUserId(Update update) {
        Message message = update.getMessage();
        if (message != null)
            return message
                    .getFrom()
                    .getId();
        CallbackQuery query = update.getCallbackQuery();
        if (query != null)
            return query
                    .getFrom()
                    .getId();
        throw new IllegalArgumentException("Wrong update format, userId not found!");
    }

    private BotApiObject getNotNullProperty(Update update) {
        return Arrays
                .stream(update
                                .getClass()
                                .getDeclaredFields())
                .map(f -> {
                    if (BotApiObject.class.isAssignableFrom(f.getType())) {
                        try {
                            if (f.trySetAccessible()) {
                                BotApiObject o = (BotApiObject) f.get(update);
                                if (o != null)
                                    return o;
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Update must contain least one BotApiObject"));
    }
}
