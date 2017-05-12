package gameLogic.gameComponents;

import gameLogic.collisionHandling.PolygonObstacle;
import gameLogic.common.Pair;
import gameLogic.event_system.messages.PlatformState;
import gameLogic.gameComponents.interfaces.Polygon;
import gameLogic.gameComponents.interfaces.Shapefull;
import gameLogic.gameComponents.interfaces.Statefull;
import gameLogic.geometryShapes.Line;
import gameLogic.geometryShapes.Rectangle;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Platform extends GameComponent implements Polygon, PolygonObstacle, Statefull<PlatformState>, Shapefull<Rectangle> {
    private Rectangle rectangle;
    private boolean isActive;

    public Platform(double length, double width, boolean isActive) {
        super();
        this.rectangle = new Rectangle(length, width);
        this.isActive = isActive;
    }

    public Platform(double length, double width) {
        this(length, width, false);
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getActive() {
        return isActive;
    }

    @Override
    public PlatformState getState() {
        return new PlatformState(
                getPosition(),
                getRotation(),
                velocity,
                isActive
        );
    }

    @Override
    public void setState(PlatformState state) {
        origin = state.getPosition();
        angle = state.getAngle();
        velocity = state.getVelocity();
        isActive = isActive;
    }

    @Override
    public List<RealVector> getPointArray() {
        return rectangle.getPointArray().stream()
                .map(this::toGlobals)
                .collect(Collectors.toList());
    }

    public List<Line> getLineArray() {
        final List<RealVector> pointArray = rectangle.getPointArray().stream()
                .map(this::toGlobals)
                .collect(Collectors.toList());

        return IntStream.range(0, pointArray.size()).boxed()
                .map(i -> new Line(
                        pointArray.get(i % pointArray.size()),
                        pointArray.get((i + 1) % pointArray.size())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Rectangle getShape() {
        return rectangle;
    }

    @Override
    public RealVector getClosestPoint(RealVector referencePoint) {
        return getLineArray().stream()
                .map(line -> line.getClosestPoint(referencePoint))
                .map(point -> new Pair<>(
                        point,
                        point.subtract(referencePoint).getNorm()
                ))
                .sorted((left, right) -> {
                        if (left.getSecond() < right.getSecond()) {
                            return -1;
                        } else {
                            return 1;
                        }
                })
                .collect(Collectors.toList())
                .get(0)
                .getFirst();
    }
}
