package gameLogic.geometryShapes;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class LineTest extends TestHelper {
    private final Logger logger = Logger.getLogger(LineTest.class.getName());

    @Test
    public void testGetAngle() {
        logger.info("Testing Line.getAngle()");
        final double angle = Math.toRadians(30);
        final Line line = new Line(
                new ArrayRealVector(new double[]{0, 0}),
                new ArrayRealVector(new double[]{Math.cos(angle), Math.sin(angle)})
        );

        assertEquals(line.getAngle(), angle, delta);
        logger.info("OK");
    }

    @Test
    public void testGetLength() {
        logger.info("Testing Line.getLength()");
        final Line line = new Line(
                new ArrayRealVector(new double[]{0, 0}),
                new ArrayRealVector(new double[]{0, 1})
        );

        assertEquals(line.getLength(), 1, delta);
        logger.info("OK");
    }

    @Test
    public void testGetClosestPoint() {
        logger.info("Testing Line.getClosestPoint()");
        final RealVector start = new ArrayRealVector(new double[]{0, 0});
        final RealVector finish = new ArrayRealVector(new double[]{1, 0});
        final RealVector mid = new ArrayRealVector(new double[]{0.5, 0});

        final Line line = new Line(
                new ArrayRealVector(start),
                new ArrayRealVector(finish)
        );

        final RealVector leftPoint = new ArrayRealVector(new double[]{-1, 1});
        final RealVector midPoint = new ArrayRealVector(new double[]{0.5, 1});
        final RealVector rightPoint = new ArrayRealVector(new double[]{2, 1});

        assertTrue(compare(line.getClosestPoint(leftPoint), start));
        logger.info("OK left");
        assertTrue(compare(line.getClosestPoint(midPoint), mid));
        logger.info("OK mid");
        assertTrue(compare(line.getClosestPoint(rightPoint), finish));
        logger.info("OK right");

        logger.info("OK");
    }

    @Test
    public void testGetNorm() {
        logger.info("Testing Line normal vectors");
        final RealVector start = new ArrayRealVector(new double[]{0, 0});
        final RealVector finish = new ArrayRealVector(new double[]{1, 1});

        final Line line = new Line(
                new ArrayRealVector(start),
                new ArrayRealVector(finish)
        );

        final double angle = Math.PI / 4;
        final RealVector posiitveNorm = (new ArrayRealVector(new double[]{-Math.cos(angle), Math.sin(angle)}));
        final RealVector negativeNorm = posiitveNorm.mapMultiply(-1);

        assertTrue(compare(line.getPositiveNorm(), posiitveNorm));
        logger.info("OK positive");
        assertTrue(compare(line.getNegativeNorm(), negativeNorm));
        logger.info("OK negative");

        logger.info("OK");
    }
}
