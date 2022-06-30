package ru.malygin.tmbot.exception;

import lombok.Getter;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.malygin.tmbot.ReplyPayload;

import javax.validation.constraints.NotNull;

@Getter
public class TmbotException extends RuntimeException {

    private final Long chatId;
    private final Long userId;
    private final String text;

    public TmbotException(@NotNull String message,
                          @NotNull Long chatId,
                          @NotNull Long userId,
                          @NotNull String text) {
        super(message);
        this.chatId = chatId;
        this.userId = userId;
        this.text = text;
    }

    public TmbotException(@NotNull String message) {
        super(message);
        this.chatId = null;
        this.text = null;
        this.userId = null;
    }

    public ReplyPayload replyWithError() {
        if (this.chatId == null || this.text == null || this.userId == null) return null;
        SendMessage errorMessage = new SendMessage();
        errorMessage.setChatId(chatId);
        errorMessage.setText(text);
        return new ReplyPayload(errorMessage);
    }
}
