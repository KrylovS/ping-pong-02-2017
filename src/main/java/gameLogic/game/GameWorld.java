package gameLogic.game;


import gameLogic.gameComponents.Ball;
import gameLogic.gameComponents.Platform;
import gameLogic.gameComponents.TriangleField;

import java.util.ArrayList;
import java.util.List;


public class GameWorld {
    private int userNum;
    private double sectorAngle;
    private double sectorHeight;
    private double ballRadius;
    private final double[] worldOrigin = new double[] {0, 0};

    double relativePlatformDistance;
    double relativePlatformLength;
    double platformWidth;

    private List<TriangleField> userSectors;
    private List<TriangleField> neutralSectors;

    private List<Platform> platforms;

    private Ball ball;

    public GameWorld(int userNum, double sectorHeight, double ballRadius,
                     double relativePlatformDistance, double relativePlatformLength,
                     double platformWidth) {
        this.userNum = userNum;
        this.sectorAngle = Math.PI / userNum;
        this.sectorHeight = sectorHeight;
        this.ballRadius = ballRadius;

        this.relativePlatformDistance = relativePlatformDistance;
        this.relativePlatformLength = relativePlatformLength;
        this.platformWidth = platformWidth;

        userSectors = new ArrayList<>();
        neutralSectors = new ArrayList<>();
        platforms = new ArrayList<>();

        initSectors();
        initBall();
        initPlatforms();
    }

    public Ball getBall() {
        return ball;
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

    private void initSectors() {
        for (int i = 0; i != userNum; ++i) {
            final TriangleField userSector = new TriangleField(sectorHeight, sectorAngle);
            userSector.setNeutral(false);
            userSector.rotateTo(2 * i * sectorAngle);
            userSector.moveTo(worldOrigin);

            final TriangleField neutralSector = new TriangleField(sectorHeight, sectorAngle);
            neutralSector.setNeutral(true);
            neutralSector.rotateTo((2 * i + 1) * sectorAngle);
            neutralSector.moveTo(worldOrigin);

            userSectors.add(userSector);
            neutralSectors.add(neutralSector);
        }
    }

    private void initPlatforms() {
        for (int i = 0; i != userNum; ++i) {
            platforms.add(
                    Platform.platformFromTriangleField(
                            userSectors.get(i), relativePlatformDistance, relativePlatformLength, platformWidth
                    )
            );
        }
    }

    private void initBall() {
        ball = new Ball(ballRadius);
        //ball.moveTo(worldOrigin);
        ball.moveTo(new double[] {0, 50});  // TODO remove (debug purpose only)
    }

}
