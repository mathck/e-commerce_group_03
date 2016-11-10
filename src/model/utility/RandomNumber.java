package model.utility;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {

    private static final int standardDeviation = 1;

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static int nextGaussian(int mean) {
        return (int) Math.round(new Random().nextGaussian() * standardDeviation + mean);
    }
}
