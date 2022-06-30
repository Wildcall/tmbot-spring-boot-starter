package ru.malygin.tmbot;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.validation.constraints.NotNull;

public class TmbotUtils {

    public static Long getUserId(@NotNull Message message) {
        return message
                .getFrom()
                .getId();
    }

    public static Long getUserId(@NotNull CallbackQuery query) {
        return query
                .getFrom()
                .getId();
    }

    public static Long getChatId(@NotNull Message message) {
        return message.getChatId();
    }

    public static String getStringChatId(@NotNull Message message) {
        return message
                .getChatId()
                .toString();
    }

    public static Long getChatId(@NotNull CallbackQuery query) {
        return query
                .getMessage()
                .getChatId();
    }

    public static String getStringChatId(@NotNull CallbackQuery query) {
        return query
                .getMessage()
                .getChatId()
                .toString();
    }

    public static Long getUserId(@NotNull Update update) {
        if (update.hasMessage()) {
            return update
                    .getMessage()
                    .getFrom()
                    .getId();
        }

        if (update.hasInlineQuery()) {
            return update
                    .getInlineQuery()
                    .getFrom()
                    .getId();
        }

        if (update.hasChosenInlineQuery()) {
            return update
                    .getChosenInlineQuery()
                    .getFrom()
                    .getId();
        }

        if (update.hasCallbackQuery()) {
            return update
                    .getCallbackQuery()
                    .getFrom()
                    .getId();
        }

        if (update.hasEditedMessage()) {
            return update
                    .getEditedMessage()
                    .getFrom()
                    .getId();
        }

        if (update.hasChannelPost()) {
            return update
                    .getChannelPost()
                    .getFrom()
                    .getId();
        }

        if (update.hasEditedChannelPost()) {
            return update
                    .getEditedChannelPost()
                    .getFrom()
                    .getId();
        }

        if (update.hasShippingQuery()) {
            return update
                    .getShippingQuery()
                    .getFrom()
                    .getId();
        }

        if (update.hasPreCheckoutQuery()) {
            return update
                    .getPreCheckoutQuery()
                    .getFrom()
                    .getId();
        }

        if (update.hasPollAnswer()) {
            return update
                    .getPollAnswer()
                    .getUser()
                    .getId();
        }

        if (update.hasMyChatMember()) {
            return update
                    .getMyChatMember()
                    .getFrom()
                    .getId();
        }

        if (update.hasChatMember()) {
            return update
                    .getChatMember()
                    .getFrom()
                    .getId();
        }

        if (update.hasChatJoinRequest()) {
            return update
                    .getChatJoinRequest()
                    .getUser()
                    .getId();
        }

        return null;
    }

    public static String getStringUserId(@NotNull Update update) {
        Long userId = getUserId(update);
        return userId == null ? null : String.valueOf(userId);
    }

    public static Long getChatId(@NotNull Update update) {
        if (update.hasMessage()) {
            return update
                    .getMessage()
                    .getChatId();
        }

        if (update.hasCallbackQuery()) {
            return update
                    .getCallbackQuery()
                    .getMessage()
                    .getChatId();
        }

        if (update.hasEditedMessage()) {
            return update
                    .getEditedMessage()
                    .getChatId();
        }

        if (update.hasChannelPost()) {
            return update
                    .getChannelPost()
                    .getChatId();
        }

        if (update.hasEditedChannelPost()) {
            return update
                    .getEditedChannelPost()
                    .getChatId();
        }

        if (update.hasMyChatMember()) {
            return update
                    .getMyChatMember()
                    .getChat()
                    .getId();
        }

        if (update.hasChatMember()) {
            return update
                    .getChatMember()
                    .getChat()
                    .getId();
        }

        if (update.hasChatJoinRequest()) {
            return update
                    .getChatJoinRequest()
                    .getChat()
                    .getId();
        }

        return null;
    }

    public static String getStringChatId(@NotNull Update update) {
        Long chatId = getChatId(update);
        return chatId == null ? null : String.valueOf(chatId);
    }

    public static BotApiObject getNotNullBotApiObject(@NotNull Update update) {
        if (update.hasMessage()) return update.getMessage();
        if (update.hasInlineQuery()) return update.getInlineQuery();
        if (update.hasChosenInlineQuery()) return update.getChosenInlineQuery();
        if (update.hasCallbackQuery()) return update.getCallbackQuery();
        if (update.hasEditedMessage()) return update.getEditedMessage();
        if (update.hasChannelPost()) return update.getChannelPost();
        if (update.hasEditedChannelPost()) return update.getEditedChannelPost();
        if (update.hasShippingQuery()) return update.getShippingQuery();
        if (update.hasPreCheckoutQuery()) return update.getPreCheckoutQuery();
        if (update.hasPoll()) return update.getPoll();
        if (update.hasPollAnswer()) return update.getPollAnswer();
        if (update.hasMyChatMember()) return update.getMyChatMember();
        if (update.hasChatMember()) return update.getChatMember();
        if (update.hasChatJoinRequest()) return update.getChatJoinRequest();
        throw new RuntimeException("Update must contain least one BotApiObject");
    }
}
