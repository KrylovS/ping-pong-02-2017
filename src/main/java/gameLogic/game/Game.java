package gameLogic.game;


import gameLogic.common.CommonFunctions;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.gameComponents.Platform;
import gameLogic.gameComponents.TriangleField;
import gameLogic.geometryShapes.Rectangle;
import gameLogic.geometryShapes.interfaces.Rectangular;
import org.apache.commons.math3.linear.RealVector;


//public class Game implements Runnable {
//    public static final double SECTOR_HEIGHT = 100;
//    public static final int MILLISECONDS_PER_SECOND = 1000;
//
//    private GameConfig gameConfig;
//    private GameWorld gameWorld;
//    private SolidBody lastCollidedObject;
//
//    Game(GameConfig gameConfig) {
//        this.gameConfig = gameConfig;
//        gameWorld = getInitializedWorld();
//        setInitialBallVelocity();
//    }
//
//    Game() {
//        gameConfig = GameConfig.getDefaultConfig();
//        gameWorld = getInitializedWorld();
//        setInitialBallVelocity();
//    }
//
//    @Override
//    public void run() {
//        final Timer timer = new Timer();
//
//        final Game game = this;
//        final int updatePeriod = MILLISECONDS_PER_SECOND / gameConfig.getFrameRate();
//        final TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                game.makeIteration(updatePeriod);
//            }
//        };
//
//        timer.schedule(task, 0, updatePeriod);
//
//        //this._setListeners();
//        //this._initWorld();
//
//        //final double time = MILLISECONDS_PER_SECOND / gameConfig.getFrameRate();
//        //this._setIntervalID = setInterval(() => this._makeIteration(time), time);
//    }
//
//    public void stop() {
//        //clearInterval(this._setIntervalID);
//    }
//
//    private void makeIteration(double time) {
//        gameWorld.getBall().moveBy(
//                Arrays.stream(gameWorld.getBall().getVelocity()).map(item -> item * time).toArray()
//        );
//
//        final Ball ball = gameWorld.getBall();
//
//        for (TriangleField sector : gameWorld.getUserSectors()) {
//            if (sector.containsGlobalPoint(ball.getOrigin()) &&
//                    sector.reachesBottomLevel(ball)) {
//                handleUserSectorCollision(sector, ball);
//            }
//        }
//
//        for (TriangleField sector : gameWorld.getNeutralSectors()) {
//            if (sector.containsGlobalPoint(ball.getOrigin()) &&
//                    sector.reachesBottomLevel(ball)) {
//                handleNeutralSectorCollision(sector, ball);
//            }
//        }
//
//        for (Platform platform : gameWorld.getPlatforms()) {
//            if (platform.inBounceZone(ball)) {
//                handlePlatformCollision(platform, ball);
//            }
//        }
//    }
//
//    private GameWorld getInitializedWorld() {
//        return new GameWorld(
//                gameConfig.getPlayerNum(),
//                SECTOR_HEIGHT,
//                gameConfig.getBallRelativeRadius() * SECTOR_HEIGHT, // TODO replace with dimless values
//                gameConfig.getRelativePlatformDistance(),
//                gameConfig.getRelativePlatformLength(),
//                gameConfig.getPlatformWidth()   // TODO replace with dimless values
//        );
//    }
//
//    private void setInitialBallVelocity() {
//        final double[] ballVelocity = Arrays.stream(gameConfig.getBallRelativeVelocity())
//                .map(item -> item * SECTOR_HEIGHT / gameConfig.getFrameRate())
//                .toArray();
//        gameWorld.getBall().setVelocity(ballVelocity);
//    }
//
//}


public class Game {
    //todo wire event bus

    //todo load game config

    private RealVector platformVelocityDirection;
    private GameWorld world;
    private boolean running;

    public Game() {
        running = false;
    }

    public void init() {
        initWorld();
        setListeners();
    }

    public void run() {
        // TODO take run from previous implementation
    }

    public void stop() {
        //this._running = false;
        //clearInterval(this._setIntervalID);
    }

    public GameWorldState getWorldState() {
        return world.getState();
    }

    public Platform getPlatformByIndex(int index) {
        return CommonFunctions.getByCircularIndex(world.getPlatforms(), index);
    }

    public TriangleField getUserSectorByIndex(int index) {
        return CommonFunctions.getByCircularIndex(world.getUserSectors(), index);
    }

    private void initWorld() {
        final double sectorHeight = GameConfig.FIELD_SIZE * GameConfig.FILL_FACTOR / 2;
        final double ballRadius = GameConfig.BALL_RELATIVE_RADIUS * sectorHeight;

        world = new GameWorld(GameConfig.PLAYERS_NUM, sectorHeight, ballRadius);
        world.getBall().setVelocity(GameConfig.BALL_VELOCITY);  // TODO add randomization
    }

    private void setListeners() {
        // TODO set listeners
    }

    private void makeIteration(double time) {
        world.makeIteration(time);
    }
}