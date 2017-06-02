package gameLogic.event_system.messages;


import com.fasterxml.jackson.databind.ObjectMapper;
import gameLogic.TestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class WorldStateTest extends TestHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = Logger.getLogger(WorldStateTest.class.getName());

    @Test
    public void testSerialization() throws IOException {
        LOGGER.info("Testing world state serialization");
        final BallState ballState = new BallState(
                new double[]{1, 2},
                new double[]{3, 4}
        );

        final List<PlatformState> platformStateList = Arrays.asList(
                new PlatformState(
                        new double[]{5, 6},
                        1,
                        new double[]{49, 90},
                        false,
                        1,
                        1
                ),
                new PlatformState(
                        new double[]{5, 6},
                        1,
                        new double[]{49, 90},
                        false,
                        1,
                        1
                )
        );

        final GameWorldState gameWorldState = new GameWorldState(ballState, platformStateList);
        final String result = OBJECT_MAPPER.writeValueAsString(gameWorldState);

        assertThat(result, containsString("ballState"));
        assertThat(result, containsString("platformsState"));
        LOGGER.info("Serialization OK");

        final GameWorldState newGameWorldState = OBJECT_MAPPER.readValue(result, GameWorldState.class);
        assertEquals(newGameWorldState.getPlatformsState().size(), gameWorldState.getPlatformsState().size());

        IntStream.range(0, newGameWorldState.getPlatformsState().size()).boxed()
                .forEach(i -> {
                    final PlatformState platformState = gameWorldState.getPlatformsState().get(i);
                    final PlatformState newPlatformState = newGameWorldState.getPlatformsState().get(i);

                    assertTrue(compare(newPlatformState.getPosition(), platformState.getPosition()));
                    assertTrue(compare(newPlatformState.getVelocity(), platformState.getVelocity()));
                    assertEquals(newPlatformState.isActive(), platformState.isActive());
                    assertEquals(newPlatformState.getAngle(), platformState.getAngle(), delta);
                });

        assertTrue(compare(newGameWorldState.getBallState().getPosition(), ballState.getPosition()));
        assertTrue(compare(newGameWorldState.getBallState().getVelocity(), ballState.getVelocity()));
        LOGGER.info("Deserialization OK");

        LOGGER.info("OK");
    }

}
