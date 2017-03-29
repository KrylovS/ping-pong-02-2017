package gameLogic.game;



public class GameConfig {
    private int playerNum;
    private int frameRate;
    private double ballRelativeRadius;
    private double[] ballRelativeVelocity;
    private double sectorHeight;
    private double relativePlatformDistance;
    private double relativePlatformLength;
    private double platformWidth;

    public static final int DEFAULT_PLAYERS_NUM = 4;
    public static final int DEFAULT_FRAME_RATE = 60;
    public static final double DEFAULT_BALL_RELATIVE_RADIUS = 0.05;
    public static final double[] DEFAULT_RELATIVE_BALL_VELOCITY = new double[]{0.025, 0};


    public static GameConfig getDefaultConfig() {
        final GameConfig defaultConfig = new GameConfig();
        defaultConfig.playerNum = DEFAULT_PLAYERS_NUM;
        defaultConfig.frameRate = DEFAULT_FRAME_RATE;
        defaultConfig.ballRelativeRadius = DEFAULT_BALL_RELATIVE_RADIUS;
        defaultConfig.ballRelativeVelocity = DEFAULT_RELATIVE_BALL_VELOCITY;

        return defaultConfig;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public double getBallRelativeRadius() {
        return ballRelativeRadius;
    }

    public void setBallRelativeRadius(double ballRelativeRadius) {
        this.ballRelativeRadius = ballRelativeRadius;
    }

    public double[] getBallRelativeVelocity() {
        return ballRelativeVelocity;
    }

    public void setBallRelativeVelocity(double[] ballRelativeVelocity) {
        this.ballRelativeVelocity = ballRelativeVelocity;
    }

    public double getSectorHeight() {
        return sectorHeight;
    }

    public void setSectorHeight(double sectorHeight) {
        this.sectorHeight = sectorHeight;
    }

    public double getRelativePlatformDistance() {
        return relativePlatformDistance;
    }

    public void setRelativePlatformDistance(double relativePlatformDistance) {
        this.relativePlatformDistance = relativePlatformDistance;
    }

    public double getRelativePlatformLength() {
        return relativePlatformLength;
    }

    public void setRelativePlatformLength(double relativePlatformLength) {
        this.relativePlatformLength = relativePlatformLength;
    }

    public double getPlatformWidth() {
        return platformWidth;
    }

    public void setPlatformWidth(double platformWidth) {
        this.platformWidth = platformWidth;
    }
}
