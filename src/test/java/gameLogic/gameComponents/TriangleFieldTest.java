package gameLogic.gameComponents;

import org.junit.Test;

import static org.junit.Assert.*;


public class TriangleFieldTest {
    @Test
    public void testReachesBottomLevel() {
        System.out.println("Testing triangleField.reachesBottomLevel");

        final double sectorHeight = 100;
        final TriangleField triangleField = new TriangleField(sectorHeight, Math.toRadians(90));
        triangleField.moveTo(new double[] {0, 0});

        final double ballRadius = 10;
        final Ball ball = new Ball(ballRadius);
        ball.moveTo(new double[]{0, -sectorHeight + ballRadius / 2});

        assertEquals(triangleField.reachesBottomLevel(ball), true);

        ball.moveBy(new double[]{0, 3 * ballRadius});
        assertEquals(triangleField.reachesBottomLevel(ball), false);

        System.out.println("OK");
    }
}