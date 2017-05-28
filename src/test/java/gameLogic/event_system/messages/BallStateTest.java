package gameLogic.event_system.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gameLogic.TestHelper;
import gameLogic.gameComponents.Ball;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;


public class BallStateTest extends TestHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(BallStateTest.class.getName());

    @Test
    public void testSerialization() throws IOException {
        LOGGER.info("Testing ball state serialization");
        final BallState ballState = new BallState(
                new ArrayRealVector(new double[]{1, 2}),
                new ArrayRealVector(new double[]{3, 4})
        );

        final String result = OBJECT_MAPPER.writeValueAsString(ballState);

        assertThat(result, containsString("position"));
        assertThat(result, containsString("velocity"));
        LOGGER.info("Serialization OK");

        final BallState newBallState = OBJECT_MAPPER.readValue(result, BallState.class);
        assertTrue(compare(newBallState.getPosition(), ballState.getPosition()));
        assertTrue(compare(newBallState.getVelocity(), ballState.getVelocity()));
        LOGGER.info("Deserialization OK");

        LOGGER.info("OK");
    }
}
