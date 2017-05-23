package gameLogic.gameComponents;

import gameLogic.TestHelper;
import gameLogic.base.GeometryOperations;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import static org.junit.Assert.*;


public class TriangleFieldTest extends TestHelper {
    @Test
    public void testGetBottomNorm() {
        System.out.println("Testing TriangleField.getBottomNorm");
        final TriangleField sector = new TriangleField(100, Math.PI / 2, true);
        sector.rotateBy(Math.PI / 4);

        final RealVector bottomNorm = sector.getBottomNorm();
        final RealVector correctBottomNorm = GeometryOperations
                .getRotationMatrix(Math.PI / 4)
                .preMultiply(new ArrayRealVector(new double[]{0, 1}));

        assertTrue(compare(bottomNorm, correctBottomNorm));
        System.out.println("OK");
    }

    @Test
    public void testReachesBottomLevel() {
        System.out.println("Testing TriangleField.reachesBottomLevel");
        final TriangleField sector = new TriangleField(100, Math.PI / 2, true);
        final Ball ball = new Ball(20);

        ball.moveTo(new ArrayRealVector(new double[]{0, -90}));
        assertTrue(sector.reachesBottomLevel(ball));
        System.out.println("Reached OK");

        ball.moveTo(new ArrayRealVector(new double[]{0, 0}));
        assertFalse(sector.reachesBottomLevel(ball));
        System.out.println("Not reached OK");

        System.out.println("OK");
    }
}
