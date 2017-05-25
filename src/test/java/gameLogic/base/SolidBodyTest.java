package gameLogic.base;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class SolidBodyTest extends TestHelper {
    private final Logger logger = Logger.getLogger(SolidBodyTest.class.getName());

    @Test
    public void testGetRelativeVelocity() {
        logger.info("Testing solid body getRelativeVelocity");

        final SolidBody solidBody = new SolidBody();
        solidBody.setVelocity(new ArrayRealVector(new double[]{1, 0}));

        final SolidBody refSolidBody = new SolidBody();
        refSolidBody.setVelocity(new ArrayRealVector(new double[]{0, 1}));

        assertTrue(compare(
                refSolidBody.getRelativeVelocity(solidBody),
                new ArrayRealVector(new double[]{-1, 1}))
        );

        logger.info("OK");
    }
}
