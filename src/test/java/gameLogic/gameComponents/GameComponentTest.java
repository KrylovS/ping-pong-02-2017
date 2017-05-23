package gameLogic.gameComponents;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import static org.junit.Assert.*;


public class GameComponentTest extends TestHelper {
    private TriangleField sector;
    private Platform platform;
    private double relDistance;
    private double relLength;
    private double aspectRatio;

    private void init() {
        sector = new TriangleField(100, Math.PI / 4, true);
        relDistance = 0.1;
        relLength = 0.25;
        aspectRatio = 0.1;

        platform = ComponentHelper.platformFromTriangleField(
                sector, relDistance, relLength, aspectRatio
        );
    }

    private RealVector getCorrectPlatformPosition() {
        final RealVector localPosition = new ArrayRealVector(new double[]{0, -sector.getHeight() * (1 - relDistance)});
        return sector.toGlobals(localPosition);
    }

    @Test
    public void testMoveBy() {
        System.out.println("Testing GameComponent.moveBy");
        init();

        sector.moveBy(new ArrayRealVector(new double[]{0, 100}));
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        System.out.println("OK");
    }

    @Test
    public void testMoveTo() {
        System.out.println("Testing GameComponent.moveTo");
        init();

        sector.moveTo(new ArrayRealVector(new double[]{0, 100}));
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        System.out.println("OK");
    }

    @Test
    public void testRotateBy() {
        System.out.println("Testing GameComponent.rotateBy");
        init();

        sector.rotateBy(Math.PI / 4);
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        System.out.println("OK");
    }

    @Test
    public void testRotateTo() {
        System.out.println("Testing GameComponent.rotateTo");
        init();

        sector.rotateTo(Math.PI / 4);
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        System.out.println("OK");
    }

    @Test
    public void testMoveToWithConstraints() {
        System.out.println("Testing GameComponent.moveToWithConstraints");
        init();

        final RealVector platformStartPos = platform.getPosition();

        platform.moveByWithConstraints(new ArrayRealVector(new double[]{1000, 0}));
        assertTrue(compare(platformStartPos, platform.getPosition()));
        System.out.println("Movement fail OK");

        platform.moveByWithConstraints(new ArrayRealVector(new double[]{1, 0}));
        assertTrue(compare(platformStartPos.add(new ArrayRealVector(new double[]{1, 0})), platform.getPosition()));
        System.out.println("Movement success OK");

        System.out.println("OK");
    }
}
