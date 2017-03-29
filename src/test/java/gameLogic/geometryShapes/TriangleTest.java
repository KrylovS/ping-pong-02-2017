package gameLogic.geometryShapes;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TriangleTest {
    protected Triangle triangle;
    protected final double height = 100;
    protected final double angle = 1;
    protected final double delta = 0.001;

    @Before
    public void setUp() {
        triangle = new Triangle(height, angle);
    }

    @Test
    public void testGetters() {
        System.out.println("Testing triangle simple getters");

        assertEquals(triangle.getHeight(), height, delta);
        assertEquals(triangle.getSectorAngle(), angle, delta);
        assertEquals(triangle.getHalfWidth(), height * Math.tan(angle / 2), delta);

        System.out.println("OK");
    }

    @Test
    public void testPointArray() {
        System.out.println("Testing triangle point array creation");
        final double[][] pointArray = triangle.getPointArray();

        final double[][] correctpointArray = new double[][]{
                {0, 0},
                {-height * Math.tan(angle / 2), -height},
                {height * Math.tan(angle / 2), -height}
        };

        assertEquals(pointArray.length, correctpointArray.length);

        for (int i = 0; i != pointArray.length; ++i) {
            for (int j = 0; j != pointArray[i].length; ++j) {
                System.out.println(String.format("i = %d; j = %d", i, j));
                assertEquals(pointArray[i][j], correctpointArray[i][j], delta);
            }
        }

        System.out.println("OK");
    }

    @Test
    public void testWidthOnDistance() {
        System.out.println("Testing triangle width on distance");

        assertEquals(triangle.getWidthOnDistance(height), 0, delta);
        System.out.println("Correct on top");
        assertEquals(triangle.getWidthOnDistance(0), 2 * triangle.getHalfWidth(), delta);
        System.out.println("Correct on bottom");
        assertEquals(triangle.getWidthOnDistance(height / 2), triangle.getHalfWidth(), delta);
        System.out.println("Correct in the middle");
        System.out.println("OK");
    }

    @Test
    public void testContainsPoint() {
        System.out.println("Testing triangle contains point");

        final double[] innerPoint = new double[] {0, -triangle.getHeight() / 2};
        assertEquals(triangle.containsPoint(innerPoint), true);
        System.out.println("Correct inner point");

        final double[] outerPoint = new double[] {triangle.getHalfWidth(), 0};
        assertEquals(triangle.containsPoint(outerPoint), false);
        System.out.println("Correct outer point");

        final double[] boundaryPoint = new double[]{0, 0};
        assertEquals(triangle.containsPoint(boundaryPoint), true);
        System.out.println("Correct boundary point");

        System.out.println("OK");
    }
}
