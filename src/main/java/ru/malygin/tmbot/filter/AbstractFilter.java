package ru.malygin.tmbot.filter;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;

public abstract class AbstractFilter {
    public abstract ReplyPayload filter(Update update);

    public <T extends AbstractFilter> void init(T t) {}
}
