package ru.malygin.tmbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.malygin.tmbot.config.TmbotProperties;
import ru.malygin.tmbot.filter.DispatcherFilter;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramBot extends TelegramWebhookBot {

    private final TmbotProperties tmbotProperties;
    private final DispatcherFilter dispatcherFilter;

    @Override
    public String getBotUsername() {
        return tmbotProperties.getName();
    }

    @Override
    public String getBotToken() {
        return tmbotProperties.getToken();
    }

    @Override
    public String getBotPath() {
        return tmbotProperties.getPath();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        ReplyPayload replyPayload = dispatcherFilter.doFilter(update);
        if (replyPayload == null) return null;

        try {
            executePayload(replyPayload);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            return null;
        }
        return replyPayload.getMessage();
    }

    private void executePayload(ReplyPayload payload) throws TelegramApiException {
        for (SendPhoto item : payload.getSendPhotoList()) execute(item);

        for (SendDocument item : payload.getSendDocumentList()) execute(item);

        for (SendVideo item : payload.getSendVideoList()) execute(item);

        for (SendVideoNote item : payload.getSendVideoNoteList()) execute(item);

        for (SendSticker item : payload.getSendStickerList()) execute(item);

        for (SendAudio item : payload.getSendAudioList()) execute(item);

        for (SendVoice item : payload.getSendVoiceList()) execute(item);

        for (SendAnimation item : payload.getSendAnimationList()) execute(item);
    }
}
