package gameLogic.geometryShapes;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;

public class LineTest extends TestHelper {
    @Test
    public void testGetAngle() {
        System.out.println("Testing Line.getAngle()");
        final double angle = Math.toRadians(30);
        final Line line = new Line(
                new ArrayRealVector(new double[]{0, 0}),
                new ArrayRealVector(new double[]{Math.cos(angle), Math.sin(angle)})
        );

        assertEquals(line.getAngle(), angle, delta);
        System.out.println("OK");
    }

    @Test
    public void testGetLength() {
        System.out.println("Testing Line.getLength()");
        final Line line = new Line(
                new ArrayRealVector(new double[]{0, 0}),
                new ArrayRealVector(new double[]{0, 1})
        );

        assertEquals(line.getLength(), 1, delta);
        System.out.println("OK");
    }

    @Test
    public void testGetClosestPoint() {
        System.out.println("Testing Line.getClosestPoint()");
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
        System.out.println("OK left");
        assertTrue(compare(line.getClosestPoint(midPoint), mid));
        System.out.println("OK mid");
        assertTrue(compare(line.getClosestPoint(rightPoint), finish));
        System.out.println("OK right");

        System.out.println("OK");
    }

    @Test
    public void testGetNorm() {
        System.out.println("Testing Line normal vectors");
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
        System.out.println("OK positive");
        assertTrue(compare(line.getNegativeNorm(), negativeNorm));
        System.out.println("OK negative");

        System.out.println("OK");
    }
}
