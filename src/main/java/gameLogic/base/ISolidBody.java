package gameLogic.base;


import org.apache.commons.math3.linear.RealVector;

public interface ISolidBody extends ICoordinateSystem {
    RealVector getVelocity();

    void setVelocity(RealVector value);

    RealVector getRelativeVelocity(ISolidBody body);

    double getAngularVelocity();

    void setAngularVelocity(double value);
}
