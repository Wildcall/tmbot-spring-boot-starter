package ru.malygin.tmbot.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.exception.TmbotException;
import ru.malygin.tmbot.exception.AbstractTmbotExceptionHandler;
import ru.malygin.tmbot.exception.TmbotExceptionHandlerFactory;
import ru.malygin.tmbot.handler.DispatcherHandler;

import java.util.List;

@Slf4j
@Service
public final class DispatcherFilter {

    private final DispatcherHandler dispatcherHandler;
    private final AbstractTmbotExceptionHandler abstractTmbotExceptionHandler;
    private final List<Class<? extends AbstractFilter>> filters;
    private final FilterFactory filterFactory;

    public DispatcherFilter(DispatcherHandler dispatcherHandler,
                            TmbotExceptionHandlerFactory tmbotExceptionHandlerFactory,
                            FilterFactory filterFactory) {
        this.dispatcherHandler = dispatcherHandler;
        this.abstractTmbotExceptionHandler = tmbotExceptionHandlerFactory.getExceptionHandler();
        this.filterFactory = filterFactory;
        this.filters = filterFactory.getFilters();
    }

    public ReplyPayload doFilter(Update update) {
        try {
            for (Class<? extends AbstractFilter> f : filters) {
                AbstractFilter filter = filterFactory.getFilter(f);
                ReplyPayload reply = filter.filter(update);
                if (reply != null)
                    return reply;
            }
            return dispatcherHandler.dispatch(update);
        } catch (Throwable e) {
            log.error("Error logging: {}", e.getMessage());
            if (e instanceof TmbotException) {
                if (abstractTmbotExceptionHandler != null)
                    return abstractTmbotExceptionHandler.handle((TmbotException) e);
            }
            return null;
        }
    }
}
