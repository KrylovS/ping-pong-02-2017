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


public class RectangleTest extends TestHelper {
    private final Logger logger = Logger.getLogger(RectangleTest.class.getName());

    protected Rectangle rectangle;
    protected final double length = 100;
    protected final double width = 200;

    @Before
    public void setUp() {
        rectangle = new Rectangle(length, width);
    }

    @Test
    public void testPointArray() {
        logger.info("Testing rectangle point array creation");
        final List<RealVector> pointArray = rectangle.getPointArray();

        final List<RealVector> correctPointArray = new ArrayList<>();
        correctPointArray.add(new ArrayRealVector(new double[]{-length / 2, -width / 2}));
        correctPointArray.add(new ArrayRealVector(new double[]{length / 2, -width / 2}));
        correctPointArray.add(new ArrayRealVector(new double[]{length / 2, width / 2}));
        correctPointArray.add(new ArrayRealVector(new double[]{-length / 2, width / 2}));

        assertEquals(pointArray.size(), correctPointArray.size());

        for (int i = 0; i != pointArray.size(); ++i) {
            logger.info(String.format("i = %d;", i));
            assertTrue(compare(pointArray.get(i), correctPointArray.get(i)));
        }

        logger.info("OK");
    }

    @Test
    public void testContainsPoint() {
        logger.info("Testing rectangle contains point");

        final RealVector innerPoint = new ArrayRealVector(new double[] {0, 0});
        assertEquals(rectangle.contains(innerPoint), true);
        logger.info("Correct inner point");

        final RealVector outerPoint = new ArrayRealVector(new double[] {0, 1000});
        assertEquals(rectangle.contains(outerPoint), false);
        logger.info("Correct outer point");

        final RealVector boundaryPoint = new ArrayRealVector(new double[]{length / 2, width / 2});
        assertEquals(rectangle.contains(boundaryPoint), true);
        logger.info("Correct boundary point");

        logger.info("OK");
    }
}
