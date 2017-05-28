package gameLogic.event_system.messages;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PlatformStateTest extends TestHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(PlatformStateTest.class.getName());

    @Test
    public void testSerialization() throws IOException {
        LOGGER.info("Testing platform state serialization");
        final PlatformState platformState = new PlatformState(
                new ArrayRealVector(new double[]{1, 2}),
                Math.PI,
                new ArrayRealVector(new double[]{3, 4}),
                true
        );

        final String result = OBJECT_MAPPER.writeValueAsString(platformState);

        assertThat(result, containsString("position"));
        assertThat(result, containsString("velocity"));
        assertThat(result, containsString("angle"));
        assertThat(result, containsString("active"));
        LOGGER.info("Serialization OK");

        final PlatformState newPlatformState = OBJECT_MAPPER.readValue(result, PlatformState.class);

        assertTrue(compare(newPlatformState.getPosition(), platformState.getPosition()));
        assertTrue(compare(newPlatformState.getVelocity(), platformState.getVelocity()));
        assertEquals(newPlatformState.isActive(), platformState.isActive());
        assertEquals(newPlatformState.getAngle(), platformState.getAngle(), delta);
        LOGGER.info("Deserialization OK");
    }
}
