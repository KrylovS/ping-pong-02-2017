package gameLogic.geometryShapes;

import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;


public class CircleTest {
    private final Logger logger = Logger.getLogger(CircleTest.class.getName());

    @Test
    public void testRadius() {
        logger.info("Testing circle");

        final double radius = 100;
        final double delta = 0.001;
        final Circle circle = new Circle(radius);

        assertEquals(radius, circle.getRadius(), delta);

        logger.info("OK");
    }
}
