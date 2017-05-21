package gameLogic.base;


import gameLogic.base.interfaces.ISolidBody;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;


public class SolidBody extends CoordinateSystem implements ISolidBody {
    protected RealVector velocity;
    protected double angularVelocity;

    public SolidBody(RealVector origin, double angle, RealVector velocity, double angularVelocity) {
        super(origin, angle);
        this.velocity = velocity;
        this.angularVelocity = angularVelocity;
    }

    public SolidBody() {
        this(
                new ArrayRealVector(new double[] {0, 0}),
                0,
                new ArrayRealVector(new double[] {0, 0}),
                0
        );
    }

    public RealVector getVelocity() {
        return velocity.copy();
    }

    public void setVelocity(RealVector value) {
        velocity = value.copy();
    }

    public RealVector getRelativeVelocity(ISolidBody body) {
        return velocity.subtract(body.getVelocity());
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double value) {
        angularVelocity = value;
    }
}

