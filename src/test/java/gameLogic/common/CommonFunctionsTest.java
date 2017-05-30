package gameLogic.common;

import gameLogic.TestHelper;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CommonFunctionsTest extends TestHelper {
    @Test
    public void testCircularValue() {
        final double positiveCircularVal = CommonFunctions.getCircularValue(5, 7, 10);
        assertEquals(positiveCircularVal, 2, delta);
        final double negativeCircularValue = CommonFunctions.getCircularValue(5, -7, 10);
        assertEquals(negativeCircularValue, 8, delta);
    }
}
