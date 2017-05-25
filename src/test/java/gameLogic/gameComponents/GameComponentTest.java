package gameLogic.gameComponents;

import gameLogic.TestHelper;
import gameLogic.base.CoordinateSystemTest;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;


public class GameComponentTest extends TestHelper {
    private final Logger logger = Logger.getLogger(GameComponentTest.class.getName());

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
        logger.info("Testing GameComponent.moveBy");
        init();

        sector.moveBy(new ArrayRealVector(new double[]{0, 100}));
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        logger.info("OK");
    }

    @Test
    public void testMoveTo() {
        logger.info("Testing GameComponent.moveTo");
        init();

        sector.moveTo(new ArrayRealVector(new double[]{0, 100}));
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        logger.info("OK");
    }

    @Test
    public void testRotateBy() {
        logger.info("Testing GameComponent.rotateBy");
        init();

        sector.rotateBy(Math.PI / 4);
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        logger.info("OK");
    }

    @Test
    public void testRotateTo() {
        logger.info("Testing GameComponent.rotateTo");
        init();

        sector.rotateTo(Math.PI / 4);
        final RealVector platformPosition = platform.getPosition();
        final RealVector correctPlatformPosition = getCorrectPlatformPosition();

        assertTrue(compare(platformPosition, correctPlatformPosition));
        logger.info("OK");
    }

    @Test
    public void testMoveToWithConstraints() {
        logger.info("Testing GameComponent.moveToWithConstraints");
        init();

        final RealVector platformStartPos = platform.getPosition();

        platform.moveByWithConstraints(new ArrayRealVector(new double[]{1000, 0}));
        assertTrue(compare(platformStartPos, platform.getPosition()));
        logger.info("Movement fail OK");

        platform.moveByWithConstraints(new ArrayRealVector(new double[]{1, 0}));
        assertTrue(compare(platformStartPos.add(new ArrayRealVector(new double[]{1, 0})), platform.getPosition()));
        logger.info("Movement success OK");

        logger.info("OK");
    }
}
