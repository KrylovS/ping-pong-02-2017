package gameLogic.geometryShapes;


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

    public double[][] getPointArray() {
        return new double[][]{
                {0., 0.},
                {-height * Math.tan(sectorAngle / 2), -height},
                {height * Math.tan(sectorAngle / 2), -height}
        };
    }

    public double getBottomDistance(double[] point) {
        return point[1] + height;
    }

    /**
     *
     * @param bottomDistance
     * @return number (width of the triangle on the specified distance from bottom)
     */
    public double getWidthOnDistance(double bottomDistance) {
        return 2 * (1 - bottomDistance / height) * halfWidth;
    }

    public boolean containsPoint(double[] point) {
        // offsets below move coordinate system origin to the lower left corner
        // because it is easier to write conditions in this coordinate system
        final double x = point[0] + halfWidth;
        final double y = point[1] + height;

        return (y > 0) && (y <= height / halfWidth * x) && (y <= height * (2 - x / halfWidth));
    }

}
