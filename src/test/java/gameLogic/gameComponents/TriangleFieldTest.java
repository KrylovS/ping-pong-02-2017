package gameLogic.gameComponents;

import gameLogic.TestHelper;
import gameLogic.base.GeometryOperations;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;


public class TriangleFieldTest extends TestHelper {
    private final Logger logger = Logger.getLogger(TriangleFieldTest.class.getName());

    @Test
    public void testContains() {
        logger.info("Testing TriangleField.contains");
        final TriangleField sector = new TriangleField(100, Math.PI / 4, true);

        final RealVector topPointIn = new ArrayRealVector(new double[]{0, -0.1});
        assertTrue(sector.containsGlobalPoint(topPointIn));
        logger.info("Top point in OK");

        final RealVector topPointOut = new ArrayRealVector(new double[]{0, 0.1});
        assertFalse(sector.containsGlobalPoint(topPointOut));
        logger.info("Top point out OK");


        logger.info("OK");
    }

    @Test
    public void testGetBottomNorm() {
        logger.info("Testing TriangleField.getBottomNorm");
        final TriangleField sector = new TriangleField(100, Math.PI / 2, true);
        sector.rotateBy(Math.PI / 4);

        final RealVector bottomNorm = sector.getBottomNorm();
        final RealVector correctBottomNorm = GeometryOperations
                .getRotationMatrix(Math.PI / 4)
                .preMultiply(new ArrayRealVector(new double[]{0, 1}));

        assertTrue(compare(bottomNorm, correctBottomNorm));
        logger.info("OK");
    }

    @Test
    public void testReachesBottomLevel() {
        logger.info("Testing TriangleField.reachesBottomLevel");
        final TriangleField sector = new TriangleField(100, Math.PI / 2, true);
        final Ball ball = new Ball(20);

        ball.moveTo(new ArrayRealVector(new double[]{0, -90}));
        assertTrue(sector.reachesBottomLevel(ball));
        logger.info("Reached OK");

        ball.moveTo(new ArrayRealVector(new double[]{0, 0}));
        assertFalse(sector.reachesBottomLevel(ball));
        logger.info("Not reached OK");

        logger.info("OK");
    }
}
