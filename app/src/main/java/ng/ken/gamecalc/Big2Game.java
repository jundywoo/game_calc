package ng.ken.gamecalc;

import java.util.Arrays;

public class Big2Game {

    private int[][] scores = new int[0][4];
    private int[] sum = new int[4];


    public int[][] getScores() {
        return scores;
    }

    public int[] getSum() {
        return sum;
    }

    public void removeScore(int position) {
        if (position < 0 || position >= scores.length) return;
        int[][] newScores = new int[scores.length - 1][];
        for (int i = 0, j = 0; i < scores.length; i++) {
            if (i != position) {
                newScores[j++] = scores[i];
            }
        }
        scores = newScores;
        summarizeScores();
    }

    public void addScore(int[] score) {
        int[][] newScores = new int[scores.length + 1][];
        System.arraycopy(scores, 0, newScores, 0, scores.length);
        newScores[scores.length] = score;
        scores = newScores;
        summarizeScores();
    }

    public void cleanScores() {
        scores = new int[0][4];
        summarizeScores();
    }

    private void summarizeScores() {
        Arrays.fill(sum, 0);
        for (int[] score : scores) {
            for (int i = 0; i < sum.length; i++) {
                try {
                    sum[i] += score[i];
                } catch (Exception e) {
                    // Ignore if the score array is shorter than expected
                }
            }
        }
    }
}
