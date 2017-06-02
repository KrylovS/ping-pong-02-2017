package gameLogic.event_system.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.util.logging.Logger;

public class GameTerminationTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(GameTermination.class.getName());

    @Test
    public void testSerialization() throws JsonProcessingException {
        LOGGER.info("Testing game termination serialization");
        final GameTermination gameTermination = new GameTermination(true, 100, System.currentTimeMillis());
        final String result = OBJECT_MAPPER.writeValueAsString(gameTermination);
        System.out.println(result);

        // TODO add real testing

        LOGGER.info("OK");
    }
}
