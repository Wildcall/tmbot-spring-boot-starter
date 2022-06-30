package ru.malygin.tmbot.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.exception.TmbotException;
import ru.malygin.tmbot.exception.TmbotExceptionHandler;
import ru.malygin.tmbot.handler.DispatcherHandler;

import java.util.List;

@Slf4j
@Service
public final class DispatcherFilter {

    private final DispatcherHandler dispatcherHandler;
    private final TmbotExceptionHandler tmbotExceptionHandler;
    private final List<Class<? extends AbstractFilter>> filters;
    private final FilterFactory filterFactory;

    public DispatcherFilter(DispatcherHandler dispatcherHandler,
                            TmbotExceptionHandler tmbotExceptionHandler,
                            FilterFactory filterFactory) {
        this.dispatcherHandler = dispatcherHandler;
        this.tmbotExceptionHandler = tmbotExceptionHandler;
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
        } catch (TmbotException e) {
            log.error("Error logging: {}", e.getMessage());
            return tmbotExceptionHandler.handle(e);
        }
    }
}
