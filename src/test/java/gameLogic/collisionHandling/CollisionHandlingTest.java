package gameLogic.collisionHandling;

import gameLogic.TestHelper;
import gameLogic.base.interfaces.ISolidBody;
import gameLogic.collisionHandling.interfaces.CircleCollider;
import gameLogic.collisionHandling.interfaces.PolygonObstacle;
import gameLogic.gameComponents.Ball;
import gameLogic.gameComponents.Platform;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;


public class CollisionHandlingTest extends TestHelper {
    private final Logger logger = Logger.getLogger(CollisionHandling.class.getName());

    @Test
    public void testArange() {
        logger.info("Testing arange");
        final double begin = 0;
        final double end = 10;
        final double step = 3;

        final List<Double> arange = CollisionHandling.arange(begin, end, step);
        final List<Double> correctArange = Arrays.asList(3., 6., 9., 10.);

        assertEquals(arange.size(), correctArange.size());
        for (int i = 0; i != arange.size(); ++i) {
            assertEquals(arange.get(i), correctArange.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testDivideTime() {
        logger.info("Testing divide time");
        final double pathLength = 100;
        final double timeStart = 0;
        final double timeFinish = 10;
        final double maxStep = 30;

        final List<Double> checkPoints = CollisionHandling.divideTime(pathLength, timeStart, timeFinish, maxStep);
        final List<Double> correctCheckPoints = Arrays.asList(3., 6., 9., 10.);

        assertEquals(checkPoints.size(), correctCheckPoints.size());
        for (int i = 0; i != checkPoints.size(); ++i) {
            assertEquals(checkPoints.get(i), correctCheckPoints.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testGetCheckPoints() {
        logger.info("Testing get checkpoints");
        final ISolidBody body1 = new Ball(10);
        body1.setVelocity(new ArrayRealVector(new double[]{5, 0}));

        final ISolidBody body2 = new Ball(10);
        body2.setVelocity(new ArrayRealVector(new double[]{-5, 0}));

        final double timeStart = 0;
        final double timeFinish = 1;

        final double maxStep = 1;
        final List<Double> checkPoints = CollisionHandling.getCheckPoints(body1, body2, timeStart, timeFinish, maxStep);
        final List<Double> correctCheckPoints = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0);

        assertEquals(checkPoints.size(), correctCheckPoints.size());
        for (int i = 0; i != checkPoints.size(); ++i) {
            assertEquals(checkPoints.get(i), correctCheckPoints.get(i), delta);
        }
        logger.info("OK");
    }

    @Test
    public void testGetCollision() {
        logger.info("Testing getCollision");

        final CircleCollider collider = new Ball(10);
        final PolygonObstacle obstacle = new Platform(10, 10);

        obstacle.moveTo(new ArrayRealVector(new double[]{10, 0}));
        collider.setVelocity(new ArrayRealVector(new double[]{0, 100}));

        final CollisionInfo failedCollision = CollisionHandling.getCollision(collider, obstacle, 1);
        assertNull(failedCollision);
        logger.info("Collision failed OK");

        collider.setVelocity(new ArrayRealVector(new double[]{100, 0}));
        final CollisionInfo successCollision = CollisionHandling.getCollision(collider, obstacle, 0.095);

        assertNotNull(successCollision);
        assertTrue(compare(
                successCollision.getDirection().projection(new ArrayRealVector(new double[]{0, 1})).getNorm(),
                0));
        logger.info("Collision success OK");

        logger.info("OK");
    }

    @Test
    public void testGetNearestCollisionOneObstacle() {
        logger.info("Testing getNearestCollisionOneObstacle");

        final CircleCollider collider = new Ball(10);
        final PolygonObstacle obstacle = new Platform(10, 10);

        obstacle.moveTo(new ArrayRealVector(new double[]{100, 0}));
        collider.setVelocity(new ArrayRealVector(new double[]{0, 10}));

        final CollisionInfo failedCollision = CollisionHandling.getNearestCollisionOneObstacle(
                collider, obstacle, Arrays.asList(0., 0.1, 0.5)
        );
        assertNull(failedCollision);
        logger.info("Collision failed OK");


        collider.setVelocity(new ArrayRealVector(new double[]{100, 0}));
        final double firstCollisionTime = 0.96;
        final CollisionInfo successCollision = CollisionHandling.getNearestCollisionOneObstacle(
                collider, obstacle, Arrays.asList(firstCollisionTime, firstCollisionTime + 0.01, firstCollisionTime + 0.02)
        );
        logger.info("Collision success OK");

        assertNotNull(successCollision);
        assertEquals(successCollision.getTime(), firstCollisionTime, delta);

        logger.info("OK");
    }

    @Test
    public void testGetNearestCollisionMultiObstacle() {
        logger.info("Testing getNearestCollisionOneObstacle");

        final CircleCollider collider = new Ball(10);
        final PolygonObstacle obstacle1 = new Platform(10, 10);
        final PolygonObstacle obstacle2 = new Platform(10, 10);

        final double xPos = 100;
        obstacle1.moveTo(new ArrayRealVector(new double[]{xPos, 0}));
        obstacle2.moveTo(new ArrayRealVector(new double[]{xPos + 10, 0}));
        collider.setVelocity(new ArrayRealVector(new double[]{100, 0}));

        final List<? extends PolygonObstacle> obstacles = Arrays.asList(obstacle1, obstacle2);
        final CollisionInfo collisionInfo = CollisionHandling.getNearestCollisionMultiObstacle(
                collider, obstacles, 0, 2, 5
        );

        assertNotNull(collisionInfo);
        assertEquals(collisionInfo.getObstacle(), obstacle1);
        logger.info("OK");
    }
}
