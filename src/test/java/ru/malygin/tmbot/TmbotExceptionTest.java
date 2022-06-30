package ru.malygin.tmbot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.malygin.tmbot.exception.HandleTmbotException;
import ru.malygin.tmbot.exception.TmbotException;
import ru.malygin.tmbot.exception.TmbotExceptionHandler;

@Slf4j
public class TmbotExceptionTest {

    @Test
    public void exceptionMapHandler() {
        TestClass testClass = new TestClass();
        try {
            throw new CustomTmbotExceptionWithHandler("error");
        } catch (TmbotException e) {
            ReplyPayload withHandle = testClass.handle(e);
            Assertions.assertNotNull(withHandle);
        }

        try {
            throw new CustomTmbotExceptionWithoutHandler("error");
        } catch (TmbotException e) {
            ReplyPayload withoutHandle = testClass.handle(e);
            Assertions.assertNull(withoutHandle);
        }
    }

    public static class TestClass extends TmbotExceptionHandler {

        @HandleTmbotException(CustomTmbotExceptionWithHandler.class)
        public ReplyPayload handleCustomTmbotException(CustomTmbotExceptionWithHandler e) {
            return new ReplyPayload();
        }
    }

    public static class CustomTmbotExceptionWithHandler extends TmbotException {

        public CustomTmbotExceptionWithHandler(String message,
                                               Long chatId,
                                               String text) {
            super(message, chatId, text);
        }

        public CustomTmbotExceptionWithHandler(String message) {
            super(message);
        }
    }

    public static class CustomTmbotExceptionWithoutHandler extends TmbotException {

        public CustomTmbotExceptionWithoutHandler(String message,
                                                  Long chatId,
                                                  String text) {
            super(message, chatId, text);
        }

        public CustomTmbotExceptionWithoutHandler(String message) {
            super(message);
        }
    }
}
