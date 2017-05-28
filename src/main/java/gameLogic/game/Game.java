package gameLogic.game;

import gameLogic.common.CommonFunctions;
import gameLogic.config_models.GameConfig;
import gameLogic.event_system.messages.GameWorldState;
import gameLogic.gameComponents.Platform;


public class Game {
    //todo wire event bus

    //todo load game config

    private GameWorld world;
    private long lastUpdateTime;
    private long lastTransmitTime;
    private int id;
    private boolean running;

    public Game(int id) {
        this.id = id;
        running = true;

        final long timestamp = System.currentTimeMillis();
        lastUpdateTime = timestamp;    // TODO check if correct
        lastTransmitTime = timestamp;
        init();
    }

    public void init() {
        initWorld();
        setListeners();

    }

    public int getId() {
        return id;
    }

    public boolean isRunning() {
        return running;
    }

    public void pause() {
        running = false;
    }

    public void play() {
        running = true;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public long getLastTransmitTime() {
        return lastTransmitTime;
    }

    public void setLastTransmitTime(long transmitTime) {
        lastTransmitTime = transmitTime;
    }

    public int getUserNum() {
        return world.getUserNum();
    }

    public GameWorldState getWorldState() {
        return world.getState();
    }

    public Platform getPlatformByIndex(int index) {
        return CommonFunctions.getByCircularIndex(world.getPlatforms(), index);
    }

    public synchronized void makeIteration(double time) {
        world.makeIteration(time);
        lastUpdateTime = System.currentTimeMillis();
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
}