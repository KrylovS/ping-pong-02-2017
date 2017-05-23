package gameLogic;

import org.apache.commons.math3.linear.RealVector;

/**
 * Created by artem on 5/23/17.
 */
public class TestHelper {
    protected final double delta = 0.00001;

    public boolean compare(double x1, double x2) {
        return Math.abs(x1 - x2) < delta;
    }

    public boolean compare(RealVector v1, RealVector v2) {
        if (v1.getDimension() != v2.getDimension()) {
            return false;
        }

        for (int i = 0; i != v1.getDimension(); ++i) {
            if (!compare(v1.getEntry(i), v2.getEntry(i))) {
                return false;
            }
        }

        return true;
    }
}
