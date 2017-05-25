package gameLogic.geometryShapes;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;


public class TriangleTest extends TestHelper {
    private final Logger logger = Logger.getLogger(TriangleTest.class.getName());

    protected Triangle triangle;
    protected final double height = 100;
    protected final double angle = 1;

    @Before
    public void setUp() {
        triangle = new Triangle(height, angle);
    }

    @Test
    public void testGetters() {
        logger.info("Testing triangle simple getters");

        assertEquals(triangle.getHeight(), height, delta);
        assertEquals(triangle.getSectorAngle(), angle, delta);
        assertEquals(triangle.getHalfWidth(), height * Math.tan(angle / 2), delta);

        logger.info("OK");
    }

    @Test
    public void testPointArray() {
        logger.info("Testing triangle point array creation");
        final List<RealVector> pointArray = triangle.getPointArray();

        final List<RealVector> correctPointArray = new ArrayList<>();
        correctPointArray.add(new ArrayRealVector(new double[]{0, 0}));
        correctPointArray.add(new ArrayRealVector(new double[]{-height * Math.tan(angle / 2), -height}));
        correctPointArray.add(new ArrayRealVector(new double[]{height * Math.tan(angle / 2), -height}));

        assertEquals(pointArray.size(), correctPointArray.size());

        for (int i = 0; i != pointArray.size(); ++i) {
            logger.info(String.format("i = %d;", i));
            assertTrue(compare(pointArray.get(i), correctPointArray.get(i)));
        }

        logger.info("OK");
    }

    @Test
    public void testBasePoints() {
        logger.info("Testing triangle base points");
        final List<RealVector> basePoints = triangle.getBasePoints();

        final List<RealVector> correctBasePoints = new ArrayList<>();
        correctBasePoints.add(new ArrayRealVector(new double[]{-height * Math.tan(angle / 2), -height}));
        correctBasePoints.add(new ArrayRealVector(new double[]{height * Math.tan(angle / 2), -height}));

        assertEquals(basePoints.size(), correctBasePoints.size());

        for (int i = 0; i != basePoints.size(); ++i) {
            logger.info(String.format("i = %d;", i));
            assertTrue(compare(basePoints.get(i), correctBasePoints.get(i)));
        }

        logger.info("OK");
    }

    @Test
    public void testWidthOnDistance() {
        logger.info("Testing triangle width on distance");

        assertEquals(triangle.getWidthOnDistance(height), 0, delta);
        logger.info("Correct on top");
        assertEquals(triangle.getWidthOnDistance(0), 2 * triangle.getHalfWidth(), delta);
        logger.info("Correct on bottom");
        assertEquals(triangle.getWidthOnDistance(height / 2), triangle.getHalfWidth(), delta);
        logger.info("Correct in the middle");
        logger.info("OK");
    }

    @Test
    public void testIsInSector() {
        logger.info("Testing triangle isInSector");

        final RealVector inSector = new ArrayRealVector(new double[] {0, -triangle.getHeight() * 1000});
        assertEquals(triangle.isInSector(inSector), true);
        logger.info("Correct in sector");

        final RealVector outOfSector = new ArrayRealVector(new double[] {10000, -1000});
        assertEquals(triangle.isInSector(outOfSector), false);
        logger.info("Correct out of sector");

        logger.info("OK");
    }

    @Test
    public void testContainsPoint() {
        logger.info("Testing triangle contains point");

        final RealVector innerPoint = new ArrayRealVector(new double[] {0, -triangle.getHeight() / 2});
        assertEquals(triangle.contains(innerPoint), true);
        logger.info("Correct inner point");

        final RealVector outerPoint = new ArrayRealVector(new double[] {triangle.getHalfWidth(), 0});
        assertEquals(triangle.contains(outerPoint), false);
        logger.info("Correct outer point");

        final RealVector boundaryPoint = new ArrayRealVector(new double[]{0, 0});
        assertEquals(triangle.contains(boundaryPoint), true);
        logger.info("Correct boundary point");

        logger.info("OK");
    }
}

