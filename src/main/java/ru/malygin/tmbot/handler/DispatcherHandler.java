package ru.malygin.tmbot.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.TmbotUtils;
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
        if (update == null) return null;
        Long userId = TmbotUtils.getUserId(update);
        Long chatId = TmbotUtils.getChatId(update);

        BotState state = cache.getBotState(userId);

        AbstractHandler handler = handlersFactory
                .getHandlers()
                .get(state.getHandlerClass());
        if (handler != null) {
            return handler.handle(state, userId, chatId, TmbotUtils.getNotNullBotApiObject(update));
        } else
            log.info("Handler for state: [{}] not found", state.name());
        return null;
    }
}
