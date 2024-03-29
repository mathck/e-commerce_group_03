package model.utility;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {

    private static final int standardDeviation = 1;

    /*
    Generates a random number by minimum and maximum bounds.
     */
    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /*
    Generates a normal distributed random number with the parameter mean.
     */
    public static int nextGaussian(int mean) {
        return (int) Math.round(
                ThreadLocalRandom.current().nextGaussian() * standardDeviation + mean);
    }

    /*
    Generates a normal distributed random number with the parameters mean and stdDev.
     */
    public static int nextGaussian(int mean, int stdDev) {
        return (int) Math.abs(Math.round(
                ThreadLocalRandom.current().nextGaussian() * stdDev + mean));
    }

    /*
    Generates an ArrayList with all Rows, paired with all columns.
    The ArrayList is shuffled and added into a new ArrayList (result) by the number of resultAmount.
     */
    public static ArrayList<Pair<Integer, Integer>> nextIntPairUnique(int resultAmount, int minValue, int maxValue) {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();

        int index = 0;
        for (int rowCounter = minValue; rowCounter < maxValue; rowCounter++) {
            for (int colCounter = minValue; colCounter < maxValue; colCounter++) {
                pairs.add(index++, new Pair<>(rowCounter, colCounter));
            }
        }

        Collections.shuffle(pairs);

        for (int itemPosition = 0; itemPosition < resultAmount; itemPosition++) {
            result.add(pairs.get(itemPosition));
        }

        return result;
    }
}
