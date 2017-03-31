package gameLogic.gameComponents;


public class Platform extends SolidBody {
    private double length;
    private double width;
    private boolean isActive;

    private SectorPositionInfo sectorPositionInfo;

    private static class SectorPositionInfo {
        private double[] originalPosition;
        private double maxOffset;

        public double[] getOriginalPosition() {
            return originalPosition;
        }

        public void setOriginalPosition(double[] originalPosition) {
            this.originalPosition = originalPosition;
        }

        public double getMaxOffset() {
            return maxOffset;
        }

        public void setMaxOffset(double maxOffset) {
            this.maxOffset = maxOffset;
        }

        SectorPositionInfo(double[] originalPosition, double maxOffset) {
            this.originalPosition = originalPosition;
            this.maxOffset = maxOffset;
        }

        SectorPositionInfo() {
            originalPosition = new double[]{0, 0};
            maxOffset = Double.POSITIVE_INFINITY;
        }
    }

    public Platform(double length, double width, boolean isActive) {
        this.length = length;
        this.width = width;
        this.isActive = isActive;
        this.sectorPositionInfo = new SectorPositionInfo();
    }

    public Platform(double length, double width) {
        this.length = length;
        this.width = width;
        this.isActive = false;
        this.sectorPositionInfo = new SectorPositionInfo();
    }

    public static Platform platformFromTriangleField(
            TriangleField sector, double relativeDistance, double relativeLength, double width
    ) {

        final double[] position = sector.toGlobals(
                new double[]{0, sector.getHeight() * (relativeDistance - 1)}
        );  // using such coordinates because triangleField coordinate system origin is in the topmost corner.

        final double totalLength = sector.getWidthOnRelativeDistance(relativeDistance);

        final Platform platform = new Platform(totalLength * relativeLength, width);
        platform.moveTo(position);
        platform.rotateTo(sector.getAngle());

        platform.updateSectorPositioning(platform.getOrigin(), totalLength * (1 - relativeLength) / 2);

        return platform;
    }

    public void updateSectorPositioning(double[] originalPosition, double maxOffset) {
        sectorPositionInfo.setOriginalPosition(originalPosition);
        sectorPositionInfo.setMaxOffset(maxOffset);
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setActive() {
        isActive = true;
    }

    public void setPassive() {
        isActive = false;
    }

    public double[][] getPointArray() {
        final double[][] localPoints = new double[][] {
                {getLeftBorder(), getLowerBorder()},
                {getRightBorder(), getLowerBorder()},
                {getRightBorder(), getUpperBorder()},
                {getLeftBorder(), getUpperBorder()}
        };

        final double[][] result = new double[localPoints.length][2];
        for (int i = 0; i != localPoints.length; ++i) {
            result[i] = toGlobals(localPoints[i]);
        }

        return result;
    }

    public double[] getNorm() {
        return toGlobalsWithoutOffset(new double[]{0, 1});
    }

    public boolean inBounceZone(Ball ball) {
        final double[] localBallPosition = toLocals(ball.getOrigin());
        final double[] checkPoint = new double[]{
                localBallPosition[0], localBallPosition[1] - ball.getRadius()
        };

        if (getLeftBorder() <= checkPoint[0]) if (checkPoint[0] <= getRightBorder())
            if (getLowerBorder() <= checkPoint[1]) if (checkPoint[1] <= getUpperBorder()) return true;
        return false;
    }

    private double getLeftBorder() {
        return -length / 2;
    }

    private double getRightBorder() {
        return length / 2;
    }

    private double getLowerBorder() {
        return -width / 2;
    }

    private double getUpperBorder() {
        return width / 2;
    }

}
