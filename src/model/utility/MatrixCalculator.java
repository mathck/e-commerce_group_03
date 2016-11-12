package model.utility;

public class MatrixCalculator {

    public static <T> void rotateInPlace90DegreesClockwise(T[][] matrix) {
        int n = matrix.length;
        int half = n / 2;

        for (int layer = 0; layer < half; layer++) {
            int last = n - 1 - layer;

            for (int i = layer; i < last; i++) {
                int offset = i - layer;
                int j = last - offset;
                T top = matrix[layer][i];
                matrix[layer][i] = matrix[j][layer];
                matrix[j][layer] = matrix[last][j];
                matrix[last][j] = matrix[i][last];
                matrix[i][last] = top;
            }
        }
    }
}
