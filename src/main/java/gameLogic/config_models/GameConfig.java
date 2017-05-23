package gameLogic.config_models;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Created by artem on 5/13/17.
 */
public class GameConfig {
    public static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int FRAME_RATE = 60;
    public static final double FIELD_SIZE = 100;
    public static final int PLAYERS_NUM = 4;
    public static final double FILL_FACTOR = 0.8;
    public static final double TIME = MILLISECONDS_PER_SECOND / FRAME_RATE;
    public static final double BALL_RELATIVE_RADIUS = 0.05;
    public static final RealVector BALL_VELOCITY = new ArrayRealVector(new double[]{0, 0.08});
    public static final double PLATFORM_VELOCITY = 0.03;
    public static final double MINIMAL_OFFSET = 1;
    public static final double PLATFORM_COLLISION_ACCURACY = 1;
    public static final double SECTOR_COLLISION_ACCURACY = 0.5;
}