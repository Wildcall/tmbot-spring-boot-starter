package ru.malygin.tmbot.exception;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class TmbotExceptionHandlerFactory {

    private final AbstractTmbotExceptionHandler abstractTmbotExceptionHandler;

    public TmbotExceptionHandlerFactory(List<AbstractTmbotExceptionHandler> list) {
        if (list.size() > 1) throw new RuntimeException(
                "AbstractTmbotExceptionHandler must be one");
        if (list.isEmpty()) this.abstractTmbotExceptionHandler = null;
        else this.abstractTmbotExceptionHandler = list.get(0);
    }

    public AbstractTmbotExceptionHandler getExceptionHandler() {
        return abstractTmbotExceptionHandler;
    }
}
