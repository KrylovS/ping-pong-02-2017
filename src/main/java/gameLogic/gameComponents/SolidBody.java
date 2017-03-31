package gameLogic.gameComponents;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class SolidBody {
    private double[] origin;
    private double angle;
    private double[] velocity;
    private double angularVelocity;

    public double[] getOrigin() {
        return origin;
    }

    public void setOrigin(double[] origin) {
        this.origin = origin;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double[] getVelocity() {
        return velocity;
    }

    public void setVelocity(double[] velocity) {
        this.velocity = velocity;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public double[] toLocals(double[] globalCoord) {
        final RealVector globalPoint = new ArrayRealVector(globalCoord);
        final RealVector originPoint = new ArrayRealVector(origin);

        return this.getRotationMatrix().operate(
                globalPoint.subtract(originPoint)
        ).toArray();
    }

    public double[] toLocalsWithoutOffset(double[] globalCoord) {
        return getRotationMatrix().operate(globalCoord);
    }

    public double[] toGlobalsWithoutOffset(double[] localCoord) {
        return getInverseRotationMatrix().operate(localCoord);
    }

    public double[] toGlobals(double[] localCoord) {
        final RealVector localPoint = new ArrayRealVector(localCoord);
        final RealVector originPoint = new ArrayRealVector(origin);

        return getInverseRotationMatrix().operate(localPoint).add(originPoint).toArray();
    }

    public void moveBy(double[] offset) {
        final RealVector offsetVec = new ArrayRealVector(offset);
        final RealVector originPoint = new ArrayRealVector(origin);

        origin = originPoint.add(offsetVec).toArray();
    }

    public void moveTo(double[] position) {
        origin = position;
    }

    public void rotateBy(double angularOffset) {
        angle += angularOffset;
    }

    public void rotateTo(double newAngle) {
        angle = newAngle;
    }

    protected RealMatrix getRotationMatrix() {
        return MatrixUtils.createRealMatrix(
                new double[][]{
                        {Math.cos(angle), Math.sin(angle)}  ,
                        {-Math.sin(angle), Math.cos(angle)}
                }
        );
    }

    protected RealMatrix getInverseRotationMatrix() {
        return MatrixUtils.createRealMatrix(
                new double[][]{
                        {Math.cos(angle), -Math.sin(angle)}  ,
                        {Math.sin(angle), Math.cos(angle)}
                }
        );
    }
}

