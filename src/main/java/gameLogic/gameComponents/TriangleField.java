package gameLogic.gameComponents;

import gameLogic.collisionHandling.CircleCollider;
import gameLogic.collisionHandling.PolygonObstacle;
import gameLogic.gameComponents.interfaces.Area;
import gameLogic.gameComponents.interfaces.Shapefull;
import gameLogic.geometryShapes.Line;
import gameLogic.geometryShapes.Triangle;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;
import java.util.stream.Collectors;


public class TriangleField extends GameComponent implements Area, PolygonObstacle, Shapefull<Triangle> {
    private Triangle triangle;
    private boolean isNeutral;
    private boolean isLoser;

    public TriangleField(double height, double sectorAngle, boolean isNeutral) {
        super();
        this.triangle = new Triangle(height, sectorAngle);
        this.isNeutral = isNeutral;
        this.isLoser = false;
    }

    public boolean contains(RealVector point) {
        return triangle.contains(toLocals(point));
    }

    public double getHeight() {
        return triangle.getHeight();
    }

    public double getHalfWidth() {
        return triangle.getHalfWidth();
    }

    @Override
    public Triangle getShape() {
        return triangle;
    }

    public List<RealVector> getPointArray() {
        return triangle.getPointArray().stream()
                .map(this::toGlobals)
                .collect(Collectors.toList());
    }

    public RealVector getBottomNorm() {
        return toGlobalsWithoutOffset(new ArrayRealVector(new double[]{0, 1}));
    }

    public boolean getNeutral() {
        return isNeutral;
    }

    public boolean getLoser() {
        return isLoser;
    }

    public void setLoser(boolean isLoser) {
        this.isLoser = isLoser;
    }

    @Override
    public boolean containsGlobalPoint(RealVector point) {
        return triangle.contains(toLocals(point));
    }

    public boolean containsLocalPoint(RealVector point) {
        return triangle.contains(point);
    }

    public boolean isInSector(RealVector globalPoint) {
        return triangle.isInSector(toLocals(globalPoint));
    }

    public boolean reachesBottomLevel(CircleCollider collider) {
        final RealVector localBallPosition = toLocals(collider.getPosition());
        return triangle.getBottomDistance(localBallPosition) < collider.getRadius();
    }

    public double getWidthOnDistance(double bottomDistance) {
        return triangle.getWidthOnDistance(bottomDistance);
    }

    public double getWidthOnRelativeDistance(double relativeDistance) {
        return triangle.getWidthOnDistance(relativeDistance * triangle.getHeight());
    }

    @Override
    public RealVector getClosestPoint(RealVector referencePoint) {
        final List<RealVector> globalBasePoints = triangle.getBasePoints().stream()
                .map(this::toGlobals)
                .collect(Collectors.toList());
        final Line baseLine = new Line(globalBasePoints.get(0), globalBasePoints.get(1));
        return baseLine.getClosestPoint(referencePoint);

    }
}
