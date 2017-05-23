package gameLogic.gameComponents;


import gameLogic.gameComponents.interfaces.Area;
import gameLogic.gameComponents.interfaces.Polygon;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.util.function.Function;

public class ComponentHelper {
    public static Platform platformFromTriangleField(
            TriangleField triangleField,
            double relativeDistance,
            double relativeLength,
            double aspectRatio
    ) {
        final double totalLength = triangleField.getWidthOnRelativeDistance(relativeDistance);
        final double platformLength = totalLength * relativeLength;
        final double platformWidth = aspectRatio * platformLength;

        final Platform platform = new Platform(platformLength, platformWidth);

        // using such coordinates because triangleField coordinate system origin is in the topmost corner.
        final RealVector position = triangleField.toGlobals(
                new ArrayRealVector(new double[]{0, -triangleField.getHeight() * (1 - relativeDistance)})
        );
        final double rotation = triangleField.getRotation();

        platform.moveTo(position);
        platform.rotateTo(rotation);

        platform.setPositionValidator(getOffsetChecker(platform, triangleField));
        triangleField.addChild(platform);

        return platform;
    }

    public static Function<RealVector, Boolean> getOffsetChecker(Polygon polygon, Area area) {
        return offset -> {
            return polygon.getPointArray().stream()
                    .map(point -> area.containsGlobalPoint(point.add(offset)))
                    .reduce(true, (curr, next) -> curr && next);    // TODO check if correct
        };
    }
}
