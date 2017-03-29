package gameLogic;

import gameLogic.gameComponents.SolidBody;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by artem on 3/25/17.
 */
public class SolidBodyTest {
    final double delta = 0.0001;

    @Test
    public void testOrigin() {
        System.out.println("Testing solid body origin get/set");
        final SolidBody solidBody = new SolidBody();

        final double[] origin = new double[] {10, 10};
        solidBody.setOrigin(origin);
        final double[] gotOrigin = solidBody.getOrigin();

        for (int i = 0; i != origin.length; ++i) {
            assertEquals(origin[i], gotOrigin[i], delta);
        }
        System.out.println("OK");
    }

    @Test
    public void testAngle() {
        System.out.println("Testing solid body angle get/set");
        final SolidBody solidBody = new SolidBody();

        final double angle = 10;
        solidBody.setAngle(angle);
        assertEquals(solidBody.getAngle(), angle, delta);
        System.out.println("OK");
    }

    @Test
    public void testVelocity() {
        System.out.println("Testing solid body velocity get/set");
        final SolidBody solidBody = new SolidBody();

        final double[] velocity = new double[] {10, 10};
        solidBody.setVelocity(velocity);
        final double[] gotVelocity = solidBody.getVelocity();

        for (int i = 0; i != velocity.length; ++i) {
            assertEquals(velocity[i], gotVelocity[i], delta);
        }
        System.out.println("OK");
    }

    @Test
    public void testAngularVelocity() {
        System.out.println("Testing solid body angular velocity get/set");
        final SolidBody solidBody = new SolidBody();

        final double angularVelocity = 10;
        solidBody.setAngularVelocity(angularVelocity);
        assertEquals(solidBody.getAngularVelocity(), angularVelocity, delta);
        System.out.println("OK");
    }

    @Test
    public void testToLocals() {
        System.out.println("Testing solid body toLocals");
        final SolidBody solidBody = new SolidBody();

        final double[] origin = new double[] {0, 1};
        solidBody.setOrigin(origin);

        final double angle = Math.toRadians(90);
        solidBody.setAngle(angle);

        final double[] globalPoint = new double[] {0, 0};
        final double[] localPoint = new double[] {-1, 0};
        final double[] gotLocalPoint = solidBody.toLocals(globalPoint);

        for (int i = 0; i != globalPoint.length; ++i) {
            assertEquals(localPoint[i], gotLocalPoint[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testToLocalsWithoutOffset() {
        System.out.println("Testing solid body toLocalsWithoutOffset");
        final SolidBody solidBody = new SolidBody();

        final double angle = Math.toRadians(30);
        solidBody.setAngle(angle);

        final double[] globalPoint = new double[] {1, 0};
        final double[] localPoint = new double[] {Math.cos(angle), -Math.sin(angle)};
        final double[] gotLocalPoint = solidBody.toLocalsWithoutOffset(globalPoint);

        for (int i = 0; i != globalPoint.length; ++i) {
            assertEquals(localPoint[i], gotLocalPoint[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testToGlobalsWithoutOffset() {
        System.out.println("Testing solid body toGlobalsWithoutOffset");
        final SolidBody solidBody = new SolidBody();

        final double angle = Math.toRadians(30);
        solidBody.setAngle(angle);

        final double[] localPoint = new double[] {Math.cos(angle), -Math.sin(angle)};
        final double[] globalPoint = new double[] {1, 0};
        final double[] gotGlobalPoint = solidBody.toGlobalsWithoutOffset(localPoint);

        for (int i = 0; i != globalPoint.length; ++i) {
            assertEquals(globalPoint[i], gotGlobalPoint[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testToGlobals() {
        System.out.println("Testing solid body toGlobals");
        final SolidBody solidBody = new SolidBody();

        final double[] origin = new double[] {0, 1};
        solidBody.setOrigin(origin);

        final double angle = Math.toRadians(90);
        solidBody.setAngle(angle);

        final double[] localPoint = new double[] {-1, 0};
        final double[] globalPoint = new double[] {0, 0};
        final double[] gotGlobalPoint = solidBody.toGlobals(localPoint);

        for (int i = 0; i != globalPoint.length; ++i) {
            assertEquals(globalPoint[i], gotGlobalPoint[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testMoveBy() {
        System.out.println("Testing solid body MoveBy");
        final SolidBody solidBody = new SolidBody();

        final double[] origin = new double[] {0, 1};
        solidBody.setOrigin(origin);
        solidBody.moveBy(new double[] {1, 1});

        final double[] newOrigin = solidBody.getOrigin();
        final double[] correctOrigin = new double[] {1, 2};

        for (int i = 0; i != newOrigin.length; ++i) {
            assertEquals(newOrigin[i], correctOrigin[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testMoveTo() {
        System.out.println("Testing solid body toMoveTo");
        final SolidBody solidBody = new SolidBody();

        final double[] origin = new double[] {0, 1};
        solidBody.setOrigin(origin);
        solidBody.moveTo(new double[] {1, 1});

        final double[] newOrigin = solidBody.getOrigin();
        final double[] correctOrigin = new double[] {1, 1};

        for (int i = 0; i != newOrigin.length; ++i) {
            assertEquals(newOrigin[i], correctOrigin[i], delta);
        }

        System.out.println("OK");
    }

    @Test
    public void testRotateBy() {
        System.out.println("Testing solid body RotateBy");
        final SolidBody solidBody = new SolidBody();

        final double angle = 1;
        solidBody.setAngle(angle);
        solidBody.rotateBy(2);

        assertEquals(solidBody.getAngle(), 3, delta);
        System.out.println("OK");
    }

    @Test
    public void testRotateTo() {
        System.out.println("Testing solid body RotateTo");
        final SolidBody solidBody = new SolidBody();

        final double angle = 1;
        solidBody.setAngle(angle);
        solidBody.rotateTo(10);

        final double correctAngle = 10;

        assertEquals(solidBody.getAngle(), correctAngle, delta);
        System.out.println("OK");
    }

}