package gameLogic.gameComponents;

import static org.junit.Assert.*;

import gameLogic.TestHelper;
import gameLogic.event_system.messages.BallState;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Logger;


public class BallTest extends TestHelper {
    private final Logger logger = Logger.getLogger(BallTest.class.getName());

    private Ball ball;

    @Before
    public void setUp() {
        ball = new Ball(100);
    }

    @Test
    public void testBounceStatic() {
        logger.info("Testing ball bounce static");
        ball.setVelocity(new ArrayRealVector(new double[]{0, -1}));

        final double normAngle = Math.toRadians(45);
        ball.bounceNorm(
                new ArrayRealVector(new double[] {Math.cos(normAngle), Math.sin(normAngle)}),
                new ArrayRealVector(new double[] {0, 0})
        );

        final RealVector newVelocity = ball.getVelocity();
        final RealVector correctVelocity = new ArrayRealVector(new double[] {1, 0});

        assertTrue(compare(newVelocity, correctVelocity));

        logger.info("OK");
    }

    @Test
    public void testBounceDynamic() {
        logger.info("Testing ball bounce dynamic");
        ball.setVelocity(new ArrayRealVector(new double[]{0, -1}));

        ball.bounceNorm(
                new ArrayRealVector(new double[] {0, 1}),
                new ArrayRealVector(new double[] {0, 1})
        );

        final RealVector newVelocity = ball.getVelocity();
        final RealVector correctVelocity = new ArrayRealVector(new double[] {0, 3});

        assertTrue(compare(newVelocity, correctVelocity));

        logger.info("OK");
    }

    @Test
    public void testStateLoading() {
        logger.info("Testing ball state loading");
        final BallState ballState = new BallState(
                new ArrayRealVector(new double[]{1, 0}),
                new ArrayRealVector(new double[]{100, 200})
        );

        ball.setState(ballState);

        assertTrue(compare(ballState.getPosition(), ball.getPosition()));
        assertTrue(compare(ballState.getVelocity(), ball.getVelocity()));

        logger.info("OK");
    }

    @Test
    public void testStateExtraction() {
        logger.info("Testing ball state extraction");
        final BallState ballState = new BallState(
                new ArrayRealVector(new double[]{1, 0}),
                new ArrayRealVector(new double[]{100, 200})
        );

        ball.setState(ballState);
        final BallState newState = ball.getState();

        assertTrue(compare(ballState.getPosition(), newState.getPosition()));
        assertTrue(compare(ballState.getVelocity(), newState.getVelocity()));

        logger.info("OK");
    }
}
