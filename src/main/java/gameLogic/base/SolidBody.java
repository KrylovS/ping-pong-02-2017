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

    @Override
    public RealVector getVelocity() {
        return velocity.copy();
    }

    @Override
    public void setVelocity(RealVector value) {
        velocity = value.copy();
    }

    @Override
    public RealVector getRelativeVelocity(ISolidBody body) {
        return velocity.subtract(body.getVelocity());
    }

    @Override
    public double getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public void setAngularVelocity(double value) {
        angularVelocity = value;
    }
}

