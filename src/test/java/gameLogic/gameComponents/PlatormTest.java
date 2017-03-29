package gameLogic.gameComponents;

import static org.junit.Assert.*;
import org.junit.Test;


public class PlatormTest {
    private final double delta = 0.0001;

    @Test
    public void testPointArray() {
        System.out.println("Testing Platform pointArray");

        final double length = 10;
        final double width = 1;
        final Platform platform = new Platform(length, width);

        final double verticalOffset = 1;
        platform.moveTo(new double[] {0, verticalOffset});

        final double[][] points = platform.getPointArray();
        final double[][] correctPoints = new double[][] {
                {-length / 2, verticalOffset - width / 2},
                {length / 2, verticalOffset - width / 2},
                {length / 2, verticalOffset + width / 2},
                {-length / 2, verticalOffset + width / 2}
        };

        for (int i = 0; i != points.length; ++i) {
            for (int j = 0; j != points[i].length; ++j) {
                System.out.println(String.format("i = %d; j = %d", i, j));
                assertEquals(points[i][j], correctPoints[i][j], delta);
            }
        }

        System.out.println("OK");
    }

    @Test
    public void testGetNorm() {
        System.out.println("Testing platform norm");

        final Platform platform = new Platform(10, 1);

        final double angle = Math.toRadians(45);
        platform.setAngle(angle);
        final double[] norm = platform.getNorm();
        final double[] correctNorm = new double[] {-Math.cos(angle), Math.sin(angle)};

        for (int i = 0; i != norm.length; ++i) {
            assertEquals(norm[i], correctNorm[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testInBounceZone() {
        System.out.println("Testing ball bouncing");

        final double length = 10;
        final double width = 1;
        final Platform platform = new Platform(length, width);
        platform.moveTo(new double[]{0, -width / 2});

        final double radius = 10;
        final Ball ball = new Ball(radius);

        ball.moveTo(new double[] {-length, radius});
        assertEquals(platform.inBounceZone(ball), false);

        ball.moveTo(new double[] {-length / 4, 2 * radius});
        assertEquals(platform.inBounceZone(ball), false);

        ball.moveTo(new double[] {-length / 4, radius});
        assertEquals(platform.inBounceZone(ball), true);

        System.out.println("OK");
    }
}
