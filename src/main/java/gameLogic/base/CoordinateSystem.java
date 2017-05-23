package gameLogic.base;

import gameLogic.base.interfaces.ICoordinateSystem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;


public class CoordinateSystem implements ICoordinateSystem {
    protected RealVector origin;
    protected double angle;

    public CoordinateSystem(RealVector origin, double angle) {
        this.origin = origin;
        this.angle = angle;
    }

    public CoordinateSystem() {
        this(new ArrayRealVector(new double[]{0, 0}), 0);
    }

    @Override
    public RealVector getPosition() {
        return origin.copy();
    }

    @Override
    public double getRotation() {
        return angle;
    }

    @Override
    public void moveBy(RealVector offset) {
        origin = GeometryOperations.move(origin, offset);
    }

    @Override
    public void moveTo(RealVector position) {
        origin = position;
    }

    @Override
    public void rotateBy(double angularOffset) {
        angle += angularOffset;
    }

    @Override
    public void rotateTo(double newAngle) {
        angle = newAngle;
    }

    @Override
    public void rotateBy(double angularOffset, ICoordinateSystem rotationOrigin) {
        angle += angularOffset;
        origin = GeometryOperations.rotate(origin, angularOffset, rotationOrigin.getPosition());
    }

    @Override
    public void rotateTo(double newAngle, ICoordinateSystem rotationOrigin) {
        final double angularOffset = newAngle - angle;
        angle = newAngle;
        origin = GeometryOperations.rotate(origin, angularOffset, rotationOrigin.getPosition());
    }

    @Override
    public RealVector toLocalsWithoutOffset(RealVector globalPoint) {
        return GeometryOperations.getInverseRotationMatrix(angle).preMultiply(globalPoint);
    }

    @Override
    public RealVector toGlobalsWithoutOffset(RealVector localPoint) {
        return GeometryOperations.getRotationMatrix(angle).preMultiply(localPoint);
    }

    @Override
    public RealVector toLocals(RealVector globalPoint) {
        return GeometryOperations.getInverseRotationMatrix(angle).preMultiply(globalPoint.subtract(origin));
    }

    @Override
    public RealVector toGlobals(RealVector localPoint) {
        return origin.add(GeometryOperations.getRotationMatrix(angle).preMultiply(localPoint));
    }

}
