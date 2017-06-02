package sample.websocket;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by artem on 5/28/17.
 */
public class RandomUtils {
    public static final int RADIX = 32;
    public static final int NUM_BITS = 130;
    private static final Random RANDOM = new Random();

    public static String getRandomString() {
        return new BigInteger(NUM_BITS, RANDOM).toString(RADIX);
    }

    public static String getRandomString(int numBits) {
        return new BigInteger(numBits, RANDOM).toString(RADIX);
    }
}
