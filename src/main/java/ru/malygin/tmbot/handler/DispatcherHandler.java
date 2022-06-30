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
import ru.malygin.tmbot.exception.TmbotException;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public final class DispatcherHandler {

    private final Cache cache;
    private final HandlersFactory handlersFactory;

    public ReplyPayload dispatch(Update update) throws Throwable {
        if (update == null)
            throw new TmbotException("Update must not be null");
        Long userId = TmbotUtils.getUserId(update);
        Long chatId = TmbotUtils.getChatId(update);

        BotState state = cache.getBotState(userId);

        AbstractHandler handler = handlersFactory
                .getHandlers()
                .get(state.getHandlerClass());
        if (handler != null) {
            return handler.handle(state, userId, chatId, TmbotUtils.getNotNullBotApiObject(update));
        } else
            throw new TmbotException(String.format("Handler for state: [%s] not found", state.name()));
    }
}
