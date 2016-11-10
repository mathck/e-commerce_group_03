package model.utility;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {

    private static final int standardDeviation = 1;

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static int nextGaussian(int mean) {
        return (int) Math.round(
                ThreadLocalRandom.current().nextGaussian() * standardDeviation + mean);
    }
}
