package gameLogic.geometryShapes;

import gameLogic.base.CoordinateSystem;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;


public class Line {
    private RealVector startPoint;
    private RealVector endPoint;
    private CoordinateSystem coordinateSystem;

    public Line(RealVector startPoint, RealVector endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.coordinateSystem = new CoordinateSystem(startPoint, getAngle());   // TODO check if initializaion is correct
    }

    public RealVector getStartPoint() {
        return startPoint;
    }

    public RealVector getEndPoint() {
        return endPoint;
    }

    public double getAngle() {
        final RealVector direction = endPoint.subtract(startPoint);
        final double dx = direction.getEntry(0);
        final double dy = direction.getEntry(1);

        return Math.atan2(dy, dx);
    }

    public double getLength() {
        return endPoint.subtract(startPoint).getNorm();
    }

    public RealVector getClosestPoint(RealVector globalPoint) {
        final RealVector localPoint = coordinateSystem.toLocals(globalPoint);
        final double localX = localPoint.getEntry(0);

        if (localX < 0) {
            return startPoint;
        } else if (localX > getLength()) {
            return endPoint;
        } else {
            return coordinateSystem.toGlobals(new ArrayRealVector(new double[]{localX, 0}));
        }
    }

    public RealVector getPositiveNorm() {
        return coordinateSystem.toGlobalsWithoutOffset(new ArrayRealVector(new double[]{0, 1}));
    }

    public RealVector getNegativeNorm() {
        return coordinateSystem.toGlobalsWithoutOffset(new ArrayRealVector(new double[]{0, -1}));
    }
}