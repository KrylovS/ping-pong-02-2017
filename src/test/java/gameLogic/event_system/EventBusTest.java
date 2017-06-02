package gameLogic.event_system;

import gameLogic.game.EventBus;
import org.junit.Test;

import java.io.IOException;
import java.util.function.Function;
import java.util.logging.Logger;
import static org.junit.Assert.*;


public class EventBusTest {
    private static final Logger LOGGER = Logger.getLogger(EventBusTest.class.getName());

    private static class Event {
        private int value;

        Event(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @Test
    public void testSerialization() throws IOException {
        LOGGER.info("Testing event bus");

        final Function<Object, Void> callBack = event -> {
            assertEquals(((Event) event).getValue(), 100);
            return null;
        };

        EventBus.addEventListener(Event.class.getName(), callBack);
        EventBus.dispatchEvent(Event.class.getName(), new Event(100));

        LOGGER.info("OK");
    }
}
