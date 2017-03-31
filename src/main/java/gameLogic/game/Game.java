package gameLogic.game;


import gameLogic.gameComponents.Ball;
import gameLogic.gameComponents.Platform;
import gameLogic.gameComponents.SolidBody;
import gameLogic.gameComponents.TriangleField;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class Game implements Runnable {
    public static final double SECTOR_HEIGHT = 100;
    public static final int MILLISECONDS_PER_SECOND = 1000;

    private GameConfig gameConfig;
    private GameWorld gameWorld;
    private SolidBody lastCollidedObject;

    Game(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        gameWorld = getInitializedWorld();
        setInitialBallVelocity();
    }

    Game() {
        gameConfig = GameConfig.getDefaultConfig();
        gameWorld = getInitializedWorld();
        setInitialBallVelocity();
    }

    @Override
    public void run() {
        final Timer timer = new Timer();

        final Game game = this;
        final int updatePeriod = MILLISECONDS_PER_SECOND / gameConfig.getFrameRate();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                game.makeIteration(updatePeriod);
            }
        };

        timer.schedule(task, 0, updatePeriod);

        //this._setListeners();
        //this._initWorld();

        //final double time = MILLISECONDS_PER_SECOND / gameConfig.getFrameRate();
        //this._setIntervalID = setInterval(() => this._makeIteration(time), time);
    }

    public void stop() {
        //clearInterval(this._setIntervalID);
    }

    private void makeIteration(double time) {
        gameWorld.getBall().moveBy(
                Arrays.stream(gameWorld.getBall().getVelocity()).map(item -> item * time).toArray()
        );

        final Ball ball = gameWorld.getBall();

        for (TriangleField sector : gameWorld.getUserSectors()) {
            if (sector.containsGlobalPoint(ball.getOrigin()) &&
                    sector.reachesBottomLevel(ball)) {
                handleUserSectorCollision(sector, ball);
            }
        }

        for (TriangleField sector : gameWorld.getNeutralSectors()) {
            if (sector.containsGlobalPoint(ball.getOrigin()) &&
                    sector.reachesBottomLevel(ball)) {
                handleNeutralSectorCollision(sector, ball);
            }
        }

        for (Platform platform : gameWorld.getPlatforms()) {
            if (platform.inBounceZone(ball)) {
                handlePlatformCollision(platform, ball);
            }
        }
    }

    private GameWorld getInitializedWorld() {
        return new GameWorld(
                gameConfig.getPlayerNum(),
                SECTOR_HEIGHT,
                gameConfig.getBallRelativeRadius() * SECTOR_HEIGHT, // TODO replace with dimless values
                gameConfig.getRelativePlatformDistance(),
                gameConfig.getRelativePlatformLength(),
                gameConfig.getPlatformWidth()   // TODO replace with dimless values
        );
    }

    private void setInitialBallVelocity() {
        final double[] ballVelocity = Arrays.stream(gameConfig.getBallRelativeVelocity())
                .map(item -> item * SECTOR_HEIGHT / gameConfig.getFrameRate())
                .toArray();
        gameWorld.getBall().setVelocity(ballVelocity);
    }

    private void handleUserSectorCollision(TriangleField sector, Ball ball) {
        if (!Objects.equals(sector, lastCollidedObject)) {
            this.stop();
            sector.setLoser(true);

            ball.bounce(sector.getBottomNorm());    // TODO remove (for debug purpose only)
            lastCollidedObject = sector;
        }

        System.out.println("User sector bump!");
    }

    private void handleNeutralSectorCollision(TriangleField sector, Ball ball) {
        if (!Objects.equals(sector, lastCollidedObject)) {
            ball.bounce(sector.getBottomNorm());
            lastCollidedObject = sector;
        }

        System.out.println("Neutral sector bump!");
    }

    private void handlePlatformCollision(Platform platform, Ball ball) {
        if (!Objects.equals(platform, lastCollidedObject)) {
            ball.bounce(platform.getNorm());
            lastCollidedObject = platform;
        }

        System.out.println("Platform bump!");
    }

}
