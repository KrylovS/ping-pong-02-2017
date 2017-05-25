package gameLogic.base;

import gameLogic.TestHelper;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;


public class GeometryOperationsTest extends TestHelper {
    private final Logger logger = Logger.getLogger(GeometryOperationsTest.class.getName());

    @Test
    public void testProjection() {
        logger.info("Testing vector projection");
        final RealVector v1 = new ArrayRealVector(new double[]{1, 0});
        final RealVector v2 = new ArrayRealVector(new double[]{0.5, 0.5});

        final RealVector projection = GeometryOperations.getProjectionMatrix(v1).preMultiply(v2);
        final RealVector correctProjection = new ArrayRealVector(new double[]{0.5, 0});

        assertTrue(compare(projection, correctProjection));
        logger.info("OK");
    }

    @Test
    public void testReflection() {
        logger.info("Testing vector reflection");
        final RealVector v1 = new ArrayRealVector(new double[]{1, 0});
        final RealVector v2 = new ArrayRealVector(new double[]{0.5, 0.5});

        final RealVector reflection = GeometryOperations.getReflectionMatrix(v1).preMultiply(v2);
        final RealVector correctReflection = new ArrayRealVector(new double[]{-0.5, 0.5});

        assertTrue(compare(reflection, correctReflection));
        logger.info("OK");
    }
}


