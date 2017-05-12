package gameLogic.base;

import org.apache.commons.math3.linear.RealVector;


public class CoordinateSystem implements ICoordinateSystem {
    protected RealVector origin;
    protected double angle;

    public CoordinateSystem(RealVector origin, double angle) {
        this.origin = origin;
        this.angle = angle;
    }

    public RealVector getPosition() {
        return origin.copy();
    }

    public double getRotation() {
        return angle;
    }

    public void moveBy(RealVector offset) {
        origin = GeometryOperations.move(origin, offset);
    }

    public void moveTo(RealVector position) {
        origin = position;
    }

    public void rotateBy(double angularOffset) {
        angle += angularOffset;
    }

    public void rotateTo(double newAngle) {
        angle = newAngle;
    }

    public void rotateBy(double angularOffset, ICoordinateSystem rotationOrigin) {
        angle += angularOffset;
        origin = GeometryOperations.rotate(origin, angularOffset, rotationOrigin.getPosition());
    }

    public void rotateTo(double newAngle, ICoordinateSystem rotationOrigin) {
        final double angularOffset = angle = newAngle;
        angle = newAngle;
        origin = GeometryOperations.rotate(origin, angularOffset, rotationOrigin.getPosition());
    }

    public RealVector toLocalsWithoutOffset(RealVector globalPoint) {
        return GeometryOperations.getRotationMatrix(angle).preMultiply(globalPoint);
    }

    public RealVector toGlobalsWithoutOffset(RealVector localPoint) {
        return GeometryOperations.getInverseRotationMatrix(angle).preMultiply(localPoint);
    }

    public RealVector toLocals(RealVector globalPoint) {
        return GeometryOperations.getRotationMatrix(angle).preMultiply(globalPoint.subtract(origin));
    }

    public RealVector toGlobals(RealVector localPoint) {
        return origin.add(GeometryOperations.getInverseRotationMatrix(angle).preMultiply(localPoint));
    }

}
