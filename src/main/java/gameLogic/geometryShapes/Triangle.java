package gameLogic.geometryShapes;


import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Triangle {
    private double height;
    private double sectorAngle;
    private double halfWidth;

    public Triangle(double height, double sectorAngle) {
        this.height = height;
        this.sectorAngle = sectorAngle;
        this.halfWidth = height * Math.tan(sectorAngle / 2);
    }

    public double getHeight() {
        return height;
    }

    public double getSectorAngle() {
        return sectorAngle;
    }

    public double getHalfWidth() {
        return halfWidth;
    }

    public List<RealVector> getPointArray() {
        final double[][] points = new  double[][]{
                {0., 0.},
                {-height * Math.tan(sectorAngle / 2), -height},
                {height * Math.tan(sectorAngle / 2), -height}
        };

        return Arrays.stream(points)
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());
    }

    public List<RealVector> getBasePoints() {
        final double[][] points = new double[][] {
                {-height * Math.tan(sectorAngle / 2), -height},
                {height * Math.tan(sectorAngle / 2), -height}
        };

        return Arrays.stream(points)
                .map(ArrayRealVector::new)
                .collect(Collectors.toList());
    }

    public double getBottomDistance(RealVector point) {
        return point.getEntry(1) + height;
    }

    /**
     *
     * @param bottomDistance
     * @return number (width of the triangle on the specified distance from bottom)
     */
    public double getWidthOnDistance(double bottomDistance) {
        return 2 * (1 - bottomDistance / height) * halfWidth;
    }

    public boolean contains(RealVector point) {
        final double x = point.getEntry(0);
        final double y = point.getEntry(1) + height;

        return isInSector(point) && aboveBottom(y) && inHorRange(x);
    }

    public boolean isInSector(RealVector point) {
        return underLeftSide(point) && underRightSide(point);
    }

    private boolean inHorRange(double x) {
        return halfWidth <= x && x <= halfWidth;
    }

    private boolean aboveBottom(double y) {
        return y > 0;
    }

    private boolean underLeftSide(RealVector point) {
       final double x = point.getEntry(0);
       final double y = point.getEntry(1);

       return y < height * (1 + x / halfWidth);
    }

    private boolean underRightSide(RealVector point) {
        final double x = point.getEntry(0);
        final double y = point.getEntry(1);

        return y < height * (1 - x / halfWidth);
    }
}
