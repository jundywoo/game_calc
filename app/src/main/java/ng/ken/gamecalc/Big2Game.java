package ng.ken.gamecalc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

public class Big2Game {

    private static final String PREFS_NAME = "Big2GamePrefs";
    private static final String SCORES_KEY = "scores";

    private int[][] scores = new int[0][4];
    private final int[] sum = new int[4];
    private final Context context;

    public Big2Game(Context context) {
        this.context = context;
    }


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
        saveScores();
    }

    private void saveScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        StringBuilder sb = new StringBuilder();
        for (int[] score : scores) {
            for (int s : score) {
                sb.append(s).append(",");
            }
            sb.append(";");
        }
        editor.putString(SCORES_KEY, sb.toString());
        editor.apply();
    }

    public void loadScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedScores = prefs.getString(SCORES_KEY, "");
        if (!savedScores.isEmpty()) {
            String[] scoreStrings = savedScores.split(";");
            scores = new int[scoreStrings.length][4];
            for (int i = 0; i < scoreStrings.length; i++) {
                String[] scoreParts = scoreStrings[i].split(",");
                for (int j = 0; j < scoreParts.length; j++) {
                    scores[i][j] = Integer.parseInt(scoreParts[j]);
                }
            }
            summarizeScores();
        }
    }
}
