package gameLogic.collisionHandling;


import gameLogic.base.interfaces.ISolidBody;
import gameLogic.collisionHandling.interfaces.CircleCollider;
import gameLogic.collisionHandling.interfaces.PolygonObstacle;
import gameLogic.common.Pair;
import org.apache.commons.math3.linear.RealVector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollisionHandling {
    @Nullable
    public static CollisionInfo getNearestCollisionMultiObstacle(
            CircleCollider collider,
            List<? extends PolygonObstacle> obstacles,
            double timeStart,
            double timeFinish,
            double maxStep
    ) {
        final List<Pair<Integer, CollisionInfo>> collisionPairs = IntStream.range(0, obstacles.size())
                .boxed()
                .map(i -> new Pair<>(
                        i,
                        getCheckPoints(collider, obstacles.get(i), timeStart, timeFinish, maxStep)
                ))
                .map(pair -> {
                    final CollisionInfo collision = getNearestCollisionSingleObstacle(
                            collider,
                            obstacles.get(pair.getFirst()),
                            pair.getSecond()
                    );

                    if (collision == null) {
                        return null;
                    } else {
                        return new Pair<>(pair.getFirst(), collision);
                    }
                })
                .filter(Objects::nonNull)
                .sorted((left, right) -> {
                    if (left.getSecond().getTime() < right.getSecond().getTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());

        if (collisionPairs.isEmpty()) {
            return null;
        } else {
            final Pair<Integer, CollisionInfo> collisionPair = collisionPairs.get(0);
            final int index = collisionPair.getFirst();
            final CollisionInfo collision = collisionPair.getSecond();
            collision.setObstacle(obstacles.get(index));

            return collision;
        }
    }


    @Nullable
    public static CollisionInfo getNearestCollisionSingleObstacle(
            CircleCollider collider,
            PolygonObstacle obstacle,
            List<Double> checkPoints
    ) {
        CollisionInfo result = null;
        for (double checkPoint : checkPoints) {
            result = getCollision(collider, obstacle, checkPoint);
            if (result != null) {
                break;
            }
        }

        return result;
    }

    @Nullable
    public static CollisionInfo getCollision(
            CircleCollider collider,
            PolygonObstacle obstacle,
            double time
    ) {
        final RealVector relVelocity = collider.getRelativeVelocity(obstacle);
        final RealVector relPosition = relVelocity.mapMultiply(time).add(collider.getPosition());
        final RealVector closestPoint = obstacle.getClosestPoint(relPosition);
        final RealVector norm = relPosition.subtract(closestPoint);
        final double distance = norm.getNorm();

        if (distance <= collider.getRadius()) {
            return new CollisionInfo(time, closestPoint, norm);
        } else {
            return null;
        }
    }

    public static List<Double> getCheckPoints(
            ISolidBody body1,
            ISolidBody body2,
            double timeStart,
            double timeFinish,
            double maxStep
    ) {
        final RealVector relVelocity = body1.getRelativeVelocity(body2);
        final double totalPathLength = relVelocity.mapMultiply(timeFinish - timeStart).getNorm();
        return divideTime(totalPathLength, timeStart, timeFinish, maxStep);
    }

    public static List<Double> divideTime(
            double pathLength,
            double timeStart,
            double timeFinish,
            double maxStep
    ) {
        final double timeStep = (timeFinish - timeStart) / pathLength * maxStep;
        return arange(timeStart, timeFinish, timeStep);
    }

    public static List<Double> arange(double begin, double end, double step) {
        final double range = end - begin;
        final int checkPointNum = (int) Math.round(Math.floor(range / step));
        final List<Double> checkPointArray;

        if (checkPointNum == 0) {
            checkPointArray = new ArrayList<>();
        } else {
            checkPointArray = IntStream.range(0, checkPointNum)
                    .asDoubleStream()
                    .map(index -> begin + range / checkPointNum * (index + 1))
                    .boxed()
                    .collect(Collectors.toList());
        }

        if (checkPointArray.isEmpty() || checkPointArray.get(0) < end) {
            checkPointArray.add(end);
        }

        return checkPointArray;
    }
}
