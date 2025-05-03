package ng.ken.gamecalc.utils;

import java.util.Map;

import ng.ken.gamecalc.R;

public class Constants {

    public static final String UNDERSTOOD = "明白";
    public static final String YES = "是";
    public static final String NO = "否";


    public static final String BIG2_SCORE_SUMMARY = "結算分數";
    public static final String BIG2_NEW_A_GAME = "重开";
    public static final String BIG2_SEE_MORE = "睇多眼";
    public static final String BIG2_CONFIRM_REMOVE = "確定刪除？";
    public static final String BIG2_AT_LEASE_ONE_ZERO = "最少一個贏家分數為零";
    public static final String BIG2_AT_LEASE_ONE_SCORED = "最少一個赢家被記分数";

    public static final String TRACTOR_SWITCH_HOST = " 換莊?";
    public static final String TRACTOR_RESET_GAME = "確定重置遊戲？";
    public static final String TRACTOR_MUST_INPUT_SCORE = "請輸入分數";
    public static final String TRACTOR_MUST_SCORE_TIMES_5 = "分數必須為5的倍數";
    public static final String TRACTOR_MUST_GREATER_THAN_ZERO = "分數必須大於零";
    public static final String TRACTOR_CONFIRM_SKIP_LEVEL = "确定跳级 %s？";
    public static final String TRACTOR_CONFIRM_UPGRADE = "确定升级 %s？";
    public static final String TRACTOR_CONFIRM_SWITCH_HOST = "确定换庄 %s？";
    public static final String TRACTOR_LEVEL_10 = "10";
    public static final String TRACTOR_LEVEL_PATTERN = "  %s  %s  %s ";
    public static final String TRACTOR_HOST_LEFT = "<";
    public static final String TRACTOR_HOST_RIGHT = ">";
    public static final int TRACTOR_LEFT = 0;
    public static final int TRACTOR_RIGHT = 1;


    public static final String NAME_CISSY = "C";
    public static final String NAME_KENNIE = "K";
    public static final String NAME_THOMAS = "T";
    public static final String NAME_HINKS = "H";

    public static final int[] BIG2_PLAYERS = new int[]{
            R.id.player_c, R.id.player_k, R.id.player_t, R.id.player_h
    };
    public static final int[] SCORES = new int[]{
            R.id.score_c, R.id.score_k, R.id.score_t, R.id.score_h
    };
    public static final int[] TRACTOR_TEAMS = new int[]{
            R.layout.tractor_team_kt, R.layout.tractor_team_ch
    };
    public static final int[] TRACTOR_LEVELS = new int[]{
            R.id.level_kt, R.id.level_ch
    };
    public static final int[][] TRACTOR_PLAYERS = new int[][]{
            new int[]{R.id.player_k, R.id.player_t},
            new int[]{R.id.player_c, R.id.player_h}
    };
    public static final Map<Integer, String> NAMES = Map.of(
            R.id.player_c, NAME_CISSY,
            R.id.player_k, NAME_KENNIE,
            R.id.player_t, NAME_THOMAS,
            R.id.player_h, NAME_HINKS
    );
    public static final int COLOR_CYAN = 0xFF00FFFF;
    public static final int COLOR_YELLOW = 0xFFFFFF00;
    public static final int COLOR_AMBER = 0xFFFFA500;
    public static final int COLOR_RED = 0xFFFF0000;
    public static final int COLOR_WHITE = 0x00000000;

    public static String getName(int idx) {
        return getNameById(BIG2_PLAYERS[idx]);
    }

    public static String getNameById(int id) {
        return NAMES.get(id);
    }
}
