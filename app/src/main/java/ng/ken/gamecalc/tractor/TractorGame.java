package ng.ken.gamecalc.tractor;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import lombok.Getter;
import ng.ken.gamecalc.R;

@Getter
public class TractorGame {

    private static final String PREFS_NAME = "TractorGamePrefs";
    private static final String LEVEL_KEY = "level";
    private static final String HOST_KEY = "host";
    private static final String HOST_TEAM = "team";
    private static final String HOST_SIDE = "side";

    private static final Map<String, String> LEVEL_MAP = new HashMap<>(13) {{
        put("A", "2");
        put("2", "3");
        put("3", "4");
        put("4", "5");
        put("5", "6");
        put("6", "7");
        put("7", "8");
        put("8", "9");
        put("9", "10");
        put("10", "J");
        put("J", "Q");
        put("Q", "K");
        put("K", "A");
    }};

    private final String[] levels = new String[2];
    private final Context context;
    private final int[] sides = new int[2];
    private String host;
    private int team;

    public TractorGame(Context context) {
        this.context = context;
    }

    public void upgrade(String player, int team, int side) {
        if (this.team == team) {
            levels[team] = LEVEL_MAP.get(levels[team]);
        }
        this.host = player;
        this.sides[team] = side;
        this.team = team;

        saveScores();
    }

    public void resetLevel() {
        levels[0] = levels[1] = "2";
        saveScores();
    }

    private void saveScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(LEVEL_KEY, levels[0] + "," + levels[1]);
        editor.putString(HOST_KEY, host);
        editor.putInt(HOST_TEAM, team);
        editor.putString(HOST_SIDE, sides[0] + "," + sides[1]);
        editor.apply();
    }

    public void loadScores() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        host = prefs.getString(HOST_KEY, context.getResources().getString(R.string.kennie));
        team = prefs.getInt(HOST_TEAM, 0);
        String[] split = prefs.getString(HOST_SIDE, "0,0").split(",");
        IntStream.range(0, 2).forEach(i -> sides[i] = Integer.parseInt(split[i]));
        String savedLevel = prefs.getString(LEVEL_KEY, "2,2");
        String[] levels = savedLevel.split(",");
        this.levels[0] = levels[0];
        this.levels[1] = levels[1];
    }

    public int getSide(int team) {
        return sides[team];
    }
}
