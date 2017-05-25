package gameLogic.game;

import gameLogic.TestHelper;
import gameLogic.base.CoordinateSystem;
import gameLogic.base.CoordinateSystemTest;
import gameLogic.gameComponents.Ball;
import gameLogic.gameComponents.Platform;
import gameLogic.gameComponents.TriangleField;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static org.junit.Assert.*;


public class GameWorldTest extends TestHelper {
    public static final int USER_NUM = 4;
    public static final int SECTOR_HEIGHT = 50;
    public static final int BALL_RADIUS = 5;

    private final Logger logger = Logger.getLogger(CoordinateSystemTest.class.getName());

    private GameWorld getNewWorld() {
        return new GameWorld(USER_NUM, SECTOR_HEIGHT, BALL_RADIUS);
    }

    @Test
    public void testGetUserSectors() {
        logger.info("Testing GameWorld.GetUserSectors");
        final GameWorld world = getNewWorld();
        final List<TriangleField> userSectors = world.getUserSectors();
        final List<Double> angles = userSectors.stream()
                .map(CoordinateSystem::getRotation)
                .collect(Collectors.toList());
        final List<Double> correctAngles = Arrays.asList(0., Math.PI / 2, Math.PI, 3 * Math.PI / 2);

        for (int i = 0; i != correctAngles.size(); ++i) {
            assertEquals(angles.get(i), correctAngles.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testGetNeutralSectors() {
        logger.info("Testing GameWorld.GetNeutralSectors");
        final GameWorld world = getNewWorld();
        final List<TriangleField> neutralSectors = world.getNeutralSectors();
        final List<Double> angles = neutralSectors.stream()
                .map(CoordinateSystem::getRotation)
                .collect(Collectors.toList());
        final List<Double> correctAngles = Arrays.asList(Math.PI / 4, 3 * Math.PI / 4, 5 * Math.PI / 4, 7 * Math.PI / 4);

        for (int i = 0; i != correctAngles.size(); ++i) {
            assertEquals(angles.get(i), correctAngles.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testGetPlatforms() {
        logger.info("Testing GameWorld.GetPlatforms");
        final GameWorld world = getNewWorld();
        final List<Platform> platform = world.getPlatforms();
        final List<Double> angles = platform.stream()
                .map(CoordinateSystem::getRotation)
                .collect(Collectors.toList());
        final List<Double> correctAngles = Arrays.asList(0., Math.PI / 2, Math.PI, 3 * Math.PI / 2);

        for (int i = 0; i != correctAngles.size(); ++i) {
            assertEquals(angles.get(i), correctAngles.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testBallInitialPosition() {
        logger.info("Testing ball initial position");
        final GameWorld world = getNewWorld();

        final Ball ball = world.getBall();
        assertTrue(compare(ball.getPosition(), new ArrayRealVector(new double[]{0, 0})));

        logger.info("OK");
    }

    @Test
    public void testBallMove() {
        logger.info("Testing ball move");
        final GameWorld world = getNewWorld();
        final RealVector ballVelocity = new ArrayRealVector(new double[]{100, 0});
        world.getBall().setVelocity(ballVelocity);

        final double time = 0.01;
        world.makeIteration(time);

        final RealVector ballPosition = world.getBall().getPosition();
        final RealVector correctBallPosition = ballVelocity.mapMultiply(time);

        assertTrue(compare(ballPosition, correctBallPosition));

        logger.info("OK");
    }

    @Test
    public void testBallCollide() {
        // TODO may be need to make more exhaustive test
        logger.info("Testing ball collide");
        final GameWorld world = getNewWorld();
        final RealVector ballVelocity = new ArrayRealVector(new double[]{0, -38});
        world.getBall().setVelocity(ballVelocity);

        final double time = 1;
        world.makeIteration(time);
        world.makeIteration(time);

        final RealVector newBallVelocity = world.getBall().getVelocity();
        final RealVector correctNewBallVelocity = ballVelocity.mapMultiply(-1);

        assertTrue(compare(newBallVelocity, correctNewBallVelocity));

        logger.info("OK");
    }

}
