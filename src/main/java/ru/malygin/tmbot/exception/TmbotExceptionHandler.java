package ru.malygin.tmbot.exception;

import lombok.extern.slf4j.Slf4j;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.handler.ProcessUpdateWebHook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TmbotExceptionHandler {

    private final Map<Class<? extends TmbotException>, Method> exceptionMethodMap = new HashMap<>();

    public TmbotExceptionHandler() {
        init();
    }

    public ReplyPayload handle(TmbotException e) {
        Class<? extends TmbotException> eClass = e.getClass();
        Method method = exceptionMethodMap.get(eClass);
        if (method == null) return defaultHandle(e);
        try {
            return (ReplyPayload) method.invoke(this, e);
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException(String.format("Invoke error for method [%s]", e.getMessage()));
        }
    }

    protected ReplyPayload defaultHandle(TmbotException e) {
        return e.replyWithError();
    }

    private void init() {
        //  @formatter:off
        Arrays
                .stream(this.getClass().getDeclaredMethods())
                .forEach(m -> {
                    HandleTmbotException annotation = m.getAnnotation(HandleTmbotException.class);
                    if (annotation != null) {
                        Class<? extends TmbotException> value = annotation.value();
                        if (Arrays.equals(m.getParameterTypes(), new Class[]{value})) {
                            if (exceptionMethodMap.put(value, m) != null)
                                throw new RuntimeException(String.format(
                                        "Duplicate exception handler in class: [%s], on method [%s]",
                                        this.getClass().getName(),
                                        m.getName()));
                        } else
                            throw new RuntimeException(
                                    String.format(
                                            "Declared annotation [%s] error, in class: [%s], on method [%s]",
                                            ProcessUpdateWebHook.class.getName(),
                                            this.getClass().getName(),
                                            m.getName()));
                    }
                });
        //  @formatter:on
    }
}
