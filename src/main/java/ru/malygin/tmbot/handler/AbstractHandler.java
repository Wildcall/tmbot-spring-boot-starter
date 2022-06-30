package ru.malygin.tmbot.handler;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import ru.malygin.tmbot.ReplyPayload;
import ru.malygin.tmbot.cache.BotState;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class AbstractHandler {

    protected Map<Class<? extends BotApiObject>, Map<String, Method>> botApiMethodMap = new HashMap<>();

    public <T extends AbstractHandler> void init(T o) {
        //  @formatter:off
        Arrays.stream(o.getClass().getDeclaredMethods())
                .forEach(m -> {
                    ProcessUpdateWebHook annotation = m.getAnnotation(ProcessUpdateWebHook.class);
                    if (annotation != null) {
                        Class<? extends BotApiObject> type = annotation.type();
                        String path = annotation.path();
                        if (Arrays.equals(m.getParameterTypes(), new Class[]{Long.class, Long.class, type})) {
                            Map<String,Method> methodMap = botApiMethodMap.computeIfAbsent(type, k -> new HashMap<>());
                            if (methodMap.put(path, m) != null)
                                throw new RuntimeException(String.format(
                                        "Duplicate path in class: [%s], on method [%s]",
                                        o.getClass().getName(),
                                        m.getName()));
                        } else
                            throw new RuntimeException(
                                    String.format(
                                            "Declared annotation [%s] error, in class: [%s], on method [%s]",
                                            ProcessUpdateWebHook.class.getName(),
                                            o.getClass().getName(),
                                            m.getName()));
                    }
                });
        //  @formatter:on
    }

    public ReplyPayload handle(BotState state,
                               Long userId,
                               Long chatId,
                               BotApiObject botApiObject) {
        Class<? extends BotApiObject> aClass = botApiObject.getClass();
        Map<String, Method> methodMap = botApiMethodMap.get(aClass);
        if (methodMap == null) {
            log.error("Handler for class [{}] not found", aClass);
            return null;
        }
        try {
            Method method = methodMap.get(state.getPath());
            if (method == null) {
                log.error("Handler for path [{}] not found", state.getPath());
                return null;
            }
            return (ReplyPayload) method.invoke(this, userId, chatId, botApiObject);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("ERROR LOGGING: {}", e.getMessage());
        }
        return null;
    }
}
