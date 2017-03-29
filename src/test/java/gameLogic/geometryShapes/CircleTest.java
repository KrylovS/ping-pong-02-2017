package gameLogic.geometryShapes;

import org.junit.Test;
import static org.junit.Assert.*;


public class CircleTest {
    @Test
    public void testRadius() {
        System.out.println("Testing circle");

        final double radius = 100;
        final double delta = 0.001;
        final Circle circle = new Circle(radius);

        assertEquals(radius, circle.getRadius(), delta);

        System.out.println("OK");
    }
}
