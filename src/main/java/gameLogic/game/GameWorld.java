package gameLogic.game;


import gameLogic.collisionHandling.CollisionHandling;
import gameLogic.collisionHandling.CollisionInfo;
import gameLogic.common.Pair;
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
import java.util.stream.Stream;


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

    private GameComponent lastCollidedObject;

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
        double restTime = time;
        double updateTime = innerMakeIteration(restTime);

        while (updateTime > 0) {
            restTime -= updateTime;
            if (restTime == 0) {
                break;
            }

            updateTime = innerMakeIteration(restTime);
        }
    }

    private double innerMakeIteration(double time) {
        final double COLLISION_ACCURACY = 2;    //TODO move to config;
        final CollisionInfo platformCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, platforms, 0, time, ballRadius / COLLISION_ACCURACY
        );
        final CollisionInfo userSectorCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, userSectors, 0, time, ballRadius / COLLISION_ACCURACY
        );
        final CollisionInfo neutralSectorCollision = CollisionHandling.getNearestCollisionMultiObstacle(
                ball, neutralSectors, 0, time, ballRadius / COLLISION_ACCURACY
        );


        final Pair<CollisionInfo, String> firstCollisionData = getFirstCollision(Arrays.asList(
                new Pair<>(platformCollision, "platform"),
                new Pair<>(userSectorCollision, "userSector"),
                new Pair<>(neutralSectorCollision, "neutralSector")
        ));

        if (firstCollisionData != null) {
            ball.moveBy(ball.getVelocity().mapMultiply(firstCollisionData.getFirst().getTime()));

            if ()
        }
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
//
//        if (firstCollisionData) {
//            this.ball.moveBy(math.multiply(this.ball.velocity, firstCollisionData.collision.time));
//
//            if (firstCollisionData.tag === 'platform') {
//                this._handlePlatformCollision(
//                        firstCollisionData.collision.obstacle,
//                        this.ball,
//                        firstCollisionData.collision.direction
//                );
//            } else if (firstCollisionData.tag === 'userSector') {
//                this._handleUserSectorCollision(firstCollisionData.collision.obstacle, this.ball);
//            } else if (firstCollisionData.tag === 'neutralSector') {
//                this._handleNeutralSectorCollision(firstCollisionData.collision.obstacle, this.ball);
//            }
//
//            return firstCollisionData.collision.time;
//        }
//
//        this.ball.moveBy(math.multiply(this.ball.velocity, time));
//        return null;
//    }

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
        platforms = userSectors.stream().map(sector -> ComponentHelper.platformFromTriangleField(sector));  // TODO get rest of data from config
    }

    private void initBall() {
        ball = new Ball(ballRadius);
    }
}


//export class GameWorld implements Drawable, Stateful<GameWorldState> {

//
//    private _handleUserSectorCollision(sector: TriangleField, ball: Ball) {
//        if (sector !== this._lastCollidedObject) {
//            ball.bounceNorm(sector.getBottomNorm());
//            this._lastCollidedObject = sector;
//
//            this.eventBus.dispatchEvent(events.gameEvents.ClientDefeatEvent.create(sector.id));
//        }
//    }
//
//    private _handleNeutralSectorCollision(sector, ball) {
//        if (sector !== this._lastCollidedObject) {
//            ball.bounceNorm(sector.getBottomNorm());
//            this._lastCollidedObject = sector;
//        }
//    }
//
//    private _handlePlatformCollision(platform, ball, norm) {
//        if (platform !== this._lastCollidedObject) {
//            ball.bounceNorm(norm, platform.velocity);
//            this._lastCollidedObject = platform;
//
//            this.eventBus.dispatchEvent(events.gameEvents.BallBounced.create(platform.id));
//        }
//    }
//}