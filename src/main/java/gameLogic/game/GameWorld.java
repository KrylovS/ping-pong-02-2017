package gameLogic.game;


import gameLogic.collisionHandling.CollisionHandling;
import gameLogic.collisionHandling.CollisionInfo;
import gameLogic.collisionHandling.interfaces.PolygonObstacle;
import gameLogic.common.Pair;
import gameLogic.config_models.GameConfig;
import gameLogic.config_models.PlatformConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.gameComponents.*;
import gameLogic.gameComponents.interfaces.Statefull;
import org.apache.commons.math3.linear.RealVector;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class GameWorld implements Statefull<GameWorldState> {
    //TODO add event bus wiring

    private int userNum;
    private double sectorAngle;
    private double sectorHeight;
    private double ballRadius;
    private List<TriangleField> userSectors;
    private List<TriangleField> neutralSectors;
    private List<Platform> platforms;

    private Ball ball;

    //TODO add config loading

    private PolygonObstacle lastCollidedObject;

    public GameWorld(int userNum, double sectorHeight, double ballRadius) {
        this.userNum = userNum;
        this.sectorAngle = Math.PI / userNum;
        this.sectorHeight = sectorHeight;
        this.ballRadius = ballRadius;

        this.userSectors = new ArrayList<>();
        this.neutralSectors = new ArrayList<>();
        this.platforms = new ArrayList<>();
        this.ball = null;

        initSectors();
        initPlatforms();
        initBall();
    }

    public List<TriangleField> getUserSectors() {
        return userSectors;
    }

    public List<TriangleField> getNeutralSectors() {
        return neutralSectors;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Ball getBall() {
        return ball;
    }

    @Override
    public GameWorldState getState() {
        return new GameWorldState(
                ball.getState(),
                platforms.stream()
                        .map(Platform::getState)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void setState(GameWorldState state) {
        ball.setState(state.getBallState());
        IntStream.range(0, platforms.size()).boxed()
                .forEach(i -> platforms.get(i).setState(state.getPlatformsState().get(i)));
    }

    public void movePlatform(Platform platform, RealVector localOffsetVec, RealVector velocityVector) {
        final RealVector globalOffset = platform.toGlobalsWithoutOffset(localOffsetVec);
        platform.moveByWithConstraints(globalOffset, velocityVector);
    }

    public void makeIteration(double time) {
        Double restTime = time;
        Double updateTime = innerMakeIteration(restTime);

        while (updateTime != null) {
            restTime -= updateTime;
            if (restTime <= 0) {
                break;
            }

            updateTime = innerMakeIteration(restTime);
        }
    }

    @Nullable
    private Double innerMakeIteration(double time) {
        final CollisionInfo platformCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, platforms, 0, time, ballRadius / GameConfig.PLATFORM_COLLISION_ACCURACY
        );
        final CollisionInfo userSectorCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, userSectors, 0, time, ballRadius / GameConfig.SECTOR_COLLISION_ACCURACY
        );
        final CollisionInfo neutralSectorCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, neutralSectors, 0, time, ballRadius / GameConfig.SECTOR_COLLISION_ACCURACY
        );


        final Pair<CollisionInfo, String> firstCollisionData = getFirstCollision(Arrays.asList(
                new Pair<>(platformCollision, "platform"),
                new Pair<>(userSectorCollision, "userSector"),
                new Pair<>(neutralSectorCollision, "neutralSector")
        ));

        if (firstCollisionData != null) {
            ball.moveBy(ball.getVelocity().mapMultiply(firstCollisionData.getFirst().getTime()));

            switch (firstCollisionData.getSecond()) {
                case "platform":
                    handlePlatformCollision(
                            firstCollisionData.getFirst().getObstacle(),
                            ball,
                            firstCollisionData.getFirst().getDirection()
                    );
                    break;
                case "userSector":
                    handleUserSectorCollision(
                            firstCollisionData.getFirst().getObstacle(),
                            ball,
                            firstCollisionData.getFirst().getDirection()
                    );
                    // todo add event dispatching
                    break;
                case "neutralSector":
                    handleNeutralSectorCollision(
                            firstCollisionData.getFirst().getObstacle(),
                            ball,
                            firstCollisionData.getFirst().getDirection()
                    );
                    break;
            }

            return firstCollisionData.getFirst().getTime();
        }

        ball.moveBy(ball.getVelocity().mapMultiply(time));
        return null;
    }

    @Nullable
    private Pair<CollisionInfo, String> getFirstCollision(List<Pair<CollisionInfo, String>> pairs) {
        final List<Pair<CollisionInfo, String>> collisionsData = pairs.stream()
                .filter(pair -> pair.getFirst() != null)
                .sorted((left, right) -> {
                    if (left.getFirst().getTime() < right.getFirst().getTime()) {
                        return -1;
                    } else {
                        return 1;
                    }
                })
                .collect(Collectors.toList());

        if (collisionsData.isEmpty()) {
            return null;
        } else {
            return collisionsData.get(0);
        }


    }

    private void initSectors() {
        userSectors = IntStream.range(0, userNum).boxed()
                .map(i -> {
                    final TriangleField field = new TriangleField(sectorHeight, sectorAngle, false);
                    field.rotateBy(2 * sectorAngle * i);
                    return field;
                })
                .collect(Collectors.toList());

        neutralSectors = IntStream.range(0, userNum).boxed()
                .map(i -> {
                    final TriangleField field = new TriangleField(sectorHeight, sectorAngle, true);
                    field.rotateBy(sectorAngle * (2 * i + 1));
                    return field;
                })
                .collect(Collectors.toList());
    }

    private void initPlatforms() {
        platforms = userSectors.stream()
                .map(sector -> ComponentHelper.platformFromTriangleField(
                        sector, PlatformConfig.RELATIVE_DISTANCE, PlatformConfig.RELATIVE_LENGTH, PlatformConfig.ASPECT_RATIO
                ))
                .collect(Collectors.toList());
    }

    private void initBall() {
        ball = new Ball(ballRadius);
    }

    private void handleUserSectorCollision(PolygonObstacle sector, Ball collidingBall, RealVector norm) {
        if (!sector.equals(lastCollidedObject)) {
            collidingBall.bounceNorm(norm, sector.getVelocity());
            lastCollidedObject = sector;

            //TODO add event dispatching
        }
    }

    private void handleNeutralSectorCollision(PolygonObstacle sector, Ball collidingBall, RealVector norm) {
        if (!sector.equals(lastCollidedObject)) {
            collidingBall.bounceNorm(norm, sector.getVelocity());
            lastCollidedObject = sector;
        }
    }

    private void handlePlatformCollision(PolygonObstacle platform, Ball collidingBall, RealVector norm) {
        if (!platform.equals(lastCollidedObject)) {
            collidingBall.bounceNorm(norm, platform.getVelocity());
            lastCollidedObject = platform;

            //TODO add event dispatching
        }
    }
}