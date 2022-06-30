package ru.malygin.tmbot.exception;

import ru.malygin.tmbot.ReplyPayload;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractTmbotExceptionHandler {

    private final Map<Class<? extends TmbotException>, Method> exceptionMethodMap = new HashMap<>();

    public AbstractTmbotExceptionHandler() {
        init();
    }

    private void init() {
        //  @formatter:off
        Arrays
                .stream(this.getClass().getDeclaredMethods())
                .forEach(m -> {
                    HandleTmbotException annotation = m.getAnnotation(HandleTmbotException.class);
                    if (annotation != null) {
                        Class<? extends TmbotException> value = annotation.value();
                        if (!Arrays.equals(m.getParameterTypes(), new Class[]{value}))
                            throw new RuntimeException(String.format(
                                    "In class: [%s], on method [%s]: declared parameter must be [%s]",
                                    this.getClass().getName(), m.getName(),
                                    value));

                        if (!m.getReturnType().equals(ReplyPayload.class))
                            throw new RuntimeException(String.format(
                                    "In class: [%s], on method [%s]: return type of must be [ReplyPayload.class]",
                                    this.getClass().getName(),
                                    m.getName()));

                        if (!Modifier.isPublic(m.getModifiers()))
                            throw new RuntimeException(String.format(
                                    "In class: [%s], on method [%s]: must be public",
                                    this.getClass().getName(),
                                    m.getName()));

                        if (exceptionMethodMap.put(value, m) != null)
                            throw new RuntimeException(String.format(
                                    "In class: [%s], on method [%s]: duplicate method",
                                    this.getClass().getName(),
                                    m.getName()));
                    }
                });
        //  @formatter:on
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
}
