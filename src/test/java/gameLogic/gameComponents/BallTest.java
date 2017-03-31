package gameLogic.gameComponents;

import static org.junit.Assert.*;
import org.junit.Test;


public class BallTest {
    private final double delta = 0.0001;

    @Test
    public void testBounce() {
        System.out.println("Testing ball bounce");

        final Ball ball = new Ball(100);
        ball.setVelocity(new double[] {0, -1});

        final double normAngle = Math.toRadians(45);
        ball.bounce(new double[] {Math.cos(normAngle), Math.sin(normAngle)});

        final double[] newVelocity = ball.getVelocity();
        final double[] correctVelocity = new double[] {1, 0};

        for (int i = 0; i != newVelocity.length; ++i) {
            assertEquals(newVelocity[i], correctVelocity[i], delta);
        }

        System.out.println("OK");
    }
}
