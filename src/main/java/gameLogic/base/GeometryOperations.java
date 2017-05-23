package gameLogic.base;

import org.apache.commons.math3.linear.*;

public class GeometryOperations {
    public static RealVector move(RealVector vector, RealVector offset) {
        return vector.add(offset);
    }

    public static RealMatrix getProjectionMatrix(RealVector normVec) {
        final RealVector normVec0 = normVec.unitVector();
        final double nx = normVec0.getEntry(0);
        final double ny = normVec0.getEntry(1);

        final double[][] matrixComponents = new double[][] {
                new double[] {nx * nx, nx * ny},
                new double[] {nx * ny, ny * ny}
        };
        return new Array2DRowRealMatrix(matrixComponents);
    }

    public static RealMatrix getReflectionMatrix(RealVector normVec) {
        return MatrixUtils.createRealIdentityMatrix(2).subtract(getProjectionMatrix(normVec).scalarMultiply(2));
    }

    public static RealMatrix getRotationMatrix(double angle) {
        final double[][] matrixComponents = new double[][] {
                new double[] {Math.cos(angle), Math.sin(angle)},
                new double[] {-Math.sin(angle), Math.cos(angle)}
        };

        return new Array2DRowRealMatrix(matrixComponents);
    }

    public static RealMatrix getInverseRotationMatrix(double angle) {
        final double[][] matrixComponents = new double[][] {
                new double[] {Math.cos(angle), -Math.sin(angle)},
                new double[] {Math.sin(angle), Math.cos(angle)}
        };

        return new Array2DRowRealMatrix(matrixComponents);
    }

    public static RealVector rotate(RealVector vector, double angle, RealVector origin) {
        final RealVector offset = vector.subtract(origin);
        final RealVector newOffset = getRotationMatrix(angle).preMultiply(offset);
        return origin.add(newOffset);
    }

    public static RealVector rotate(RealVector vector, double angle) {
        return rotate(vector, angle, new ArrayRealVector(new double[]{0, 0}));
    }

    public static RealVector scale(RealVector vector, double scaleFactor, RealVector origin) {
        final RealVector offset = vector.subtract(origin);
        final RealVector newOffset = offset.mapMultiply(scaleFactor);
        return origin.add(newOffset);
    }
}

