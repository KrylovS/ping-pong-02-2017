package gameLogic.geometryShapes;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;


public class TriangleTest extends TestHelper {
    protected Triangle triangle;
    protected final double height = 100;
    protected final double angle = 1;

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
        final List<RealVector> pointArray = triangle.getPointArray();

        final List<RealVector> correctPointArray = new ArrayList<>();
        correctPointArray.add(new ArrayRealVector(new double[]{0, 0}));
        correctPointArray.add(new ArrayRealVector(new double[]{-height * Math.tan(angle / 2), -height}));
        correctPointArray.add(new ArrayRealVector(new double[]{height * Math.tan(angle / 2), -height}));

        assertEquals(pointArray.size(), correctPointArray.size());

        for (int i = 0; i != pointArray.size(); ++i) {
            System.out.println(String.format("i = %d;", i));
            assertTrue(compare(pointArray.get(i), correctPointArray.get(i)));
        }

        System.out.println("OK");
    }

    @Test
    public void testBasePoints() {
        System.out.println("Testing triangle base points");
        final List<RealVector> basePoints = triangle.getBasePoints();

        final List<RealVector> correctBasePoints = new ArrayList<>();
        correctBasePoints.add(new ArrayRealVector(new double[]{-height * Math.tan(angle / 2), -height}));
        correctBasePoints.add(new ArrayRealVector(new double[]{height * Math.tan(angle / 2), -height}));

        assertEquals(basePoints.size(), correctBasePoints.size());

        for (int i = 0; i != basePoints.size(); ++i) {
            System.out.println(String.format("i = %d;", i));
            assertTrue(compare(basePoints.get(i), correctBasePoints.get(i)));
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
    public void testIsInSector() {
        System.out.println("Testing triangle isInSector");

        final RealVector inSector = new ArrayRealVector(new double[] {0, -triangle.getHeight() * 1000});
        assertEquals(triangle.isInSector(inSector), true);
        System.out.println("Correct in sector");

        final RealVector outOfSector = new ArrayRealVector(new double[] {10000, -1000});
        assertEquals(triangle.isInSector(outOfSector), false);
        System.out.println("Correct out of sector");

        System.out.println("OK");
    }

    @Test
    public void testContainsPoint() {
        System.out.println("Testing triangle contains point");

        final RealVector innerPoint = new ArrayRealVector(new double[] {0, -triangle.getHeight() / 2});
        assertEquals(triangle.contains(innerPoint), true);
        System.out.println("Correct inner point");

        final RealVector outerPoint = new ArrayRealVector(new double[] {triangle.getHalfWidth(), 0});
        assertEquals(triangle.contains(outerPoint), false);
        System.out.println("Correct outer point");

        final RealVector boundaryPoint = new ArrayRealVector(new double[]{0, 0});
        assertEquals(triangle.contains(boundaryPoint), true);
        System.out.println("Correct boundary point");

        System.out.println("OK");
    }
}

