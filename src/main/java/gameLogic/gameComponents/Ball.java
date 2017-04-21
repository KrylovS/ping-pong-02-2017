package gameLogic.gameComponents;


import gameLogic.geometryShapes.Circle;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class Ball extends SolidBody {
    private Circle circle;

    public Ball(double radius) {
        super();

        circle = new Circle(radius);
    }

    public double getRadius() {
        return this.circle.getRadius();
    }

    public void bounce(double[] surfaceNormVec) {
        final RealVector normVec = new ArrayRealVector(surfaceNormVec);
        final RealVector normVec0 = normVec.mapDivide(normVec.getNorm());

        final RealMatrix normMatrix = MatrixUtils.createRealMatrix(
                new double[][]{
                        {normVec0.getEntry(0) * normVec0.getEntry(0), normVec0.getEntry(0) * normVec0.getEntry(1)},
                        {normVec0.getEntry(0) * normVec0.getEntry(1), normVec0.getEntry(1) * normVec0.getEntry(1)}
                }
        );

        final RealMatrix identity = MatrixUtils.createRealIdentityMatrix(2);
        final RealMatrix transformationMatrix = identity.subtract(normMatrix.scalarMultiply(2));

        final double[] newVelocity = transformationMatrix.operate(getVelocity());

        setVelocity(newVelocity);
    }
}
