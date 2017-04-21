package gameLogic.gameComponents;

import gameLogic.geometryShapes.Triangle;


public class TriangleField extends SolidBody {
    private Triangle triangle;
    private Boolean isNeutral;
    private Boolean isLoser;

    public TriangleField(double height, double sectorAngle) {
        triangle = new Triangle(height, sectorAngle);
        isNeutral = false;
        isLoser = false;

        setOrigin(new double[] {0, 0});
    }

    public double getHeight() {
        return triangle.getHeight();
    }

    public double getHalfWidth() {
        return triangle.getHalfWidth();
    }

    public double[][] getPointArray() {
        final double[][] localPoints = triangle.getPointArray();

        final double[][] result = new double[localPoints.length][2];
        for (int i = 0; i != localPoints.length; ++i) {
            result[i] = toGlobals(localPoints[i]);
        }

        return result;
    }

    public double[] getBottomNorm() {
        return this.toGlobalsWithoutOffset(new double[]{0, 1});
    }

    public boolean isNeutral() {
        return isNeutral;
    }

    public boolean isLoser() {
        return isLoser;
    }

    public void setLoser(boolean value) {
        isLoser = value;
    }

    public void setNeutral(boolean value) {
        isNeutral = value;
    }

    public boolean containsGlobalPoint(double[] point) {
        return triangle.containsPoint(toLocals(point));
    }

    public boolean containsLocalPoint(double[] point) {
        return triangle.containsPoint(point);
    }

    public boolean reachesBottomLevel(Ball ball) {
        final double[] localBottomPosition = toLocals(ball.getOrigin());
        final double ballRadius = ball.getRadius();

        return triangle.getBottomDistance(localBottomPosition) < ballRadius;
    }

    public double getWidthOnDistance(double bottomDistance) {
        return triangle.getWidthOnDistance(bottomDistance);
    }

    public double getWidthOnRelativeDistance(double relativeDistance) {
        return triangle.getWidthOnDistance(relativeDistance * triangle.getHeight());
    }

}
