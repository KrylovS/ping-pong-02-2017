package gameLogic.base;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.*;


public class CoordinateSystemTest extends TestHelper {
    @Test
    public void testMoveBy() {
        System.out.println("Testing coordinate system move by");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final RealVector offset = new ArrayRealVector(new double[]{10, 20});

        coordinateSystem.moveBy(offset);
        coordinateSystem.moveBy(offset);

        assertTrue(compare(coordinateSystem.getPosition(), offset.mapMultiply(2)));

        System.out.println("OK");
    }

    @Test
    public void testMoveTo() {
        System.out.println("Testing coordinate system move to");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final RealVector newPos = new ArrayRealVector(new double[]{10, 20});

        coordinateSystem.moveTo(newPos);
        coordinateSystem.moveTo(newPos);

        assertTrue(compare(coordinateSystem.getPosition(), newPos));

        System.out.println("OK");
    }

    @Test
    public void testRotateBy() {
        System.out.println("Testing coordinate system rotate by");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final double angularOffset = 1;

        coordinateSystem.rotateBy(angularOffset);
        coordinateSystem.rotateBy(angularOffset);

        assertEquals(coordinateSystem.getRotation(), angularOffset * 2, delta);

        System.out.println("OK");
    }

    @Test
    public void testRotateTo() {
        System.out.println("Testing coordinate system rotate to");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final double newAngle = 1;

        coordinateSystem.rotateTo(newAngle);
        coordinateSystem.rotateTo(newAngle);

        assertEquals(coordinateSystem.getRotation(), newAngle, delta);

        System.out.println("OK");
    }

    @Test
    public void testRotateByWithOrigin() {
        System.out.println("Testing coordinate system rotate by with origin");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final CoordinateSystem origin = new CoordinateSystem(new ArrayRealVector(new double[]{0, 1}), 0);

        final double angularOffset = Math.PI / 2;

        coordinateSystem.rotateBy(angularOffset, origin);
        coordinateSystem.rotateBy(angularOffset, origin);

        assertEquals(coordinateSystem.getRotation(), angularOffset * 2, delta);
        assertTrue(compare(coordinateSystem.getPosition(), origin.getPosition().mapMultiply(2)));

        System.out.println("OK");
    }

    @Test
    public void testRotateToWithOrigin() {
        System.out.println("Testing coordinate system rotate to with origin");

        final CoordinateSystem coordinateSystem = new CoordinateSystem();
        final CoordinateSystem origin = new CoordinateSystem(new ArrayRealVector(new double[]{0, 1}), 0);

        final double newAngle = Math.PI / 2;

        coordinateSystem.rotateTo(newAngle, origin);
        coordinateSystem.rotateTo(newAngle, origin);

        assertEquals(coordinateSystem.getRotation(), newAngle, delta);
        assertTrue(compare(coordinateSystem.getPosition(), new ArrayRealVector(new double[]{1, 1})));

        System.out.println("OK");
    }

    @Test
    public void testToLocals() {
        System.out.println("Testing coordinate system toLocals");
        final CoordinateSystem coordinateSystem = new CoordinateSystem(
                new ArrayRealVector(new double[]{0, 1}),
                Math.PI / 2
        );

        final RealVector globalPoint = new ArrayRealVector(new double[] {0, 0});
        final RealVector localPoint = new ArrayRealVector(new double[] {-1, 0});
        final RealVector gotLocalPoint = coordinateSystem.toLocals(globalPoint);

        assertTrue(compare(localPoint, gotLocalPoint));

        System.out.println("OK");
    }

    @Test
    public void testToLocalsWithoutOffset() {
        System.out.println("Testing coordinate system toLocalsWithouOffset");
        final double angle = Math.toRadians(30);

        final CoordinateSystem coordinateSystem = new CoordinateSystem(
                new ArrayRealVector(new double[]{1, 0}),
                angle
        );

        final RealVector globalPoint = new ArrayRealVector(new double[] {1, 0});
        final RealVector localPoint = new ArrayRealVector(new double[] {Math.cos(angle), -Math.sin(angle)});
        final RealVector gotLocalPoint = coordinateSystem.toLocalsWithoutOffset(globalPoint);

        assertTrue(compare(localPoint, gotLocalPoint));

        System.out.println("OK");
    }

    @Test
    public void testToGlobals() {
        System.out.println("Testing coordinate system toGlobals");
        final CoordinateSystem coordinateSystem = new CoordinateSystem(
                new ArrayRealVector(new double[]{0, 1}),
                Math.PI / 2
        );

        final RealVector localPoint = new ArrayRealVector(new double[] {-1, 0});
        final RealVector globalPoint = new ArrayRealVector(new double[] {0, 0});
        final RealVector gotGlobalPoint = coordinateSystem.toGlobals(localPoint);

        assertTrue(compare(globalPoint, gotGlobalPoint));

        System.out.println("OK");
    }

    @Test
    public void testToGlobalsWithoutOffset() {
        System.out.println("Testing coordinate system toGlobalsWithoutOffset");
        final double angle = Math.toRadians(30);

        final CoordinateSystem coordinateSystem = new CoordinateSystem(
                new ArrayRealVector(new double[]{1, 0}),
                angle
        );

        final RealVector localPoint = new ArrayRealVector(new double[] {Math.cos(angle), -Math.sin(angle)});
        final RealVector globalPoint = new ArrayRealVector(new double[] {1, 0});
        final RealVector gotGlobalPoint = coordinateSystem.toGlobalsWithoutOffset(localPoint);

        assertTrue(compare(globalPoint, gotGlobalPoint));

        System.out.println("OK");
    }
}
