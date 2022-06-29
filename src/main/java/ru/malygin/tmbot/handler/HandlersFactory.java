package ru.malygin.tmbot.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class HandlersFactory {

    private final Map<Class<? extends AbstractHandler>, AbstractHandler> handlers;

    public HandlersFactory(List<AbstractHandler> handlers) {
        this.handlers = handlers
                .stream()
                .collect(Collectors.toMap(AbstractHandler::getClass, Function.identity()));
        initHandlers(this.handlers.values());
    }

    public Map<Class<? extends AbstractHandler>, AbstractHandler> getHandlers() {
        return handlers;
    }

    private void initHandlers(Collection<? extends AbstractHandler> handlers) {
        handlers.forEach(handler -> handler.init(handler));
    }
}
