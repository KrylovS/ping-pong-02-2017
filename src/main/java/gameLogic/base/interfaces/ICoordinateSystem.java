package gameLogic.base.interfaces;

import org.apache.commons.math3.linear.RealVector;


public interface ICoordinateSystem {
    RealVector getPosition();

    double getRotation();

    void moveBy(RealVector position);

    void moveTo(RealVector position);

    void rotateBy(double angularOffset);

    void rotateTo(double newAngle);

    void rotateBy(double angularOffset, ICoordinateSystem rotationOrigin);

    void rotateTo(double newAngle, ICoordinateSystem rotationOrigin);

    RealVector toLocalsWithoutOffset(RealVector globalPoint);

    RealVector toGlobalsWithoutOffset(RealVector localPoint);

    RealVector toLocals(RealVector globalPoint);

    RealVector toGlobals(RealVector localPoint);
}
