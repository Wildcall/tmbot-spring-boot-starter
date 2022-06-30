package ru.malygin.tmbot.filter;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;

public abstract class AbstractFilter {

    public AbstractFilter() {
        init();
    }

    public abstract ReplyPayload filter(Update update);

    protected void init() {
    }
}
