package ru.malygin.tmbot.handler;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.cache.BotState;
import ru.malygin.tmbot.exception.TmbotException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractHandler {

    protected Map<Class<? extends BotApiObject>, Map<String, Method>> botApiMethodMap = new HashMap<>();

    public AbstractHandler() {
        init();
    }

    private void init() {
        //  @formatter:off
        Arrays.stream(this.getClass().getDeclaredMethods())
                .forEach(m -> {
                    ProcessUpdateWebHook annotation = m.getAnnotation(ProcessUpdateWebHook.class);
                    if (annotation != null) {
                        Class<? extends BotApiObject> type = annotation.type();
                        String path = annotation.path();
                        if (Arrays.equals(m.getParameterTypes(), new Class[]{Long.class, Long.class, type})) {
                            Map<String, Method> methodMap = botApiMethodMap.computeIfAbsent(type, k -> new HashMap<>());

                            Class<?> returnType = m.getReturnType();
                            if (!returnType.equals(ReplyPayload.class))
                                throw new RuntimeException(String.format(
                                        "In class: [%s], on method [%s]: return type of must be [ReplyPayload.class]",
                                        this.getClass().getName(),
                                        m.getName()));

                            if (!Modifier.isPublic(m.getModifiers()))
                                throw new RuntimeException(String.format(
                                        "In class: [%s], on method [%s]: must be public",
                                        this.getClass().getName(),
                                        m.getName()));

                            if (methodMap.put(path, m) != null)
                                throw new RuntimeException(String.format(
                                        "In class: [%s], on method [%s]: duplicate method",
                                        this.getClass().getName(),
                                        m.getName()));
                        } else
                            throw new RuntimeException(
                                    String.format(
                                            "In class: [%s], on method [%s]: declared annotation [%s] error",
                                            this.getClass().getName(),
                                            m.getName(),
                                            ProcessUpdateWebHook.class.getName()));
                    }
                });
        //  @formatter:on
    }

    public ReplyPayload handle(BotState state,
                               Long userId,
                               Long chatId,
                               BotApiObject botApiObject) throws Throwable {
        Class<? extends BotApiObject> aClass = botApiObject.getClass();
        Map<String, Method> methodMap = botApiMethodMap.get(aClass);
        if (methodMap == null) throw new TmbotException(String.format("Handler for class [%s] not found", aClass));
        try {
            Method method = methodMap.get(state.getPath());
            if (method == null) throw new TmbotException(
                    String.format("Handler for path [%s] not found", state.getPath()));
            return (ReplyPayload) method.invoke(this, userId, chatId, botApiObject);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Invoke error for method [%s]", e.getMessage()));
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof TmbotException) {
                throw e.getTargetException();
            }
            throw new RuntimeException(String.format("Invoke error for method [%s]", e.getMessage()));
        }
    }
}
