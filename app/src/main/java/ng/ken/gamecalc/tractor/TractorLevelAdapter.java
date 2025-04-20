package ng.ken.gamecalc.tractor;

import static ng.ken.gamecalc.utils.DialogUtils.confirm;
import static ng.ken.gamecalc.utils.DialogUtils.warning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.stream.IntStream;

import ng.ken.gamecalc.R;

public class TractorLevelAdapter extends BaseAdapter {

    private static final int[] TEAMS = new int[]{
            R.layout.tractor_team_kt, R.layout.tractor_team_ch
    };
    private static final int[] LEVELS = new int[]{
            R.id.level_kt, R.id.level_ch
    };
    private static final int[][] PLAYERS = new int[][]{
            new int[]{R.id.player_k, R.id.player_t},
            new int[]{R.id.player_c, R.id.player_h}
    };
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private final String[][] NAMES;
    private final Context context;
    private final TractorGame tractorGame;

    public TractorLevelAdapter(Context context, TractorGame tractorGame) {
        this.context = context;
        this.tractorGame = tractorGame;
        NAMES = new String[][]{
                {context.getString(R.string.kennie), context.getString(R.string.thomas)},
                {context.getString(R.string.cissy), context.getString(R.string.hinks)}
        };
    }

    private static int toOpposite(int side) {
        return (side + 1) % 2;
    }

    @NonNull
    private static String getLevelUpTitle(int levelUp) {
        return " 升" + (levelUp == 1 ? "" : levelUp) + "級?";
    }

    @Override
    public int getCount() {
        return tractorGame.getLevels().length + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < 2) {
            return new String[]{
                    tractorGame.getHost(),
                    tractorGame.getLevels()[position]
            };
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position > 0) {
            int line = position - 1;
            view = LayoutInflater.from(context).inflate(TEAMS[line], parent, false);
            registerPlayerListener(view, line);
            showLevel(view, line);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tractor_actions, parent, false);
            Button reset = view.findViewById(R.id.reset_game_button);
            reset.setOnClickListener(v -> confirm(context, "確定重置遊戲？", (a, b) -> {
                tractorGame.resetLevel();
                notifyDataSetChanged();
            }));
            Button upgrade = view.findViewById(R.id.upgrade_button);
            upgrade.setOnClickListener(v -> onUpgradeButtonClicked(view));
        }

        return view;
    }

    private void onUpgradeButtonClicked(View view) {
        EditText input = view.findViewById(R.id.upgrade_score);
        String text = input.getText().toString();
        if (text.trim().isEmpty()) {
            warning(context, "請輸入分數");
            return;
        }
        int score = Integer.parseInt(text);
        if (score % 5 != 0) {
            warning(context, "分數必須為5的倍數");
        } else if (score < 0) {
            warning(context, "分數必須大於零");
        } else if (score < 80) {
            hostUpgrade(score);
        } else {
            hostSwitchAndUpgrade(score);
        }
    }

    private void hostSwitchAndUpgrade(int score) {
        int team = toOpposite(tractorGame.getTeam());
        int side = toOpposite(tractorGame.getSide(team));
        String host = NAMES[team][side];
        int levelUp = (score - 80) / 40;
        String title = host + (levelUp == 0 ? " 換莊?" : getLevelUpTitle(levelUp));
        confirm(context, title, (a, b) -> {
            for (int i = -1; i < levelUp; i++) {
                tractorGame.upgrade(host, team, side);
            }
            notifyDataSetChanged();
        });
    }

    private void hostUpgrade(int score) {
        int team = tractorGame.getTeam();
        int side = toOpposite(tractorGame.getSide(team));
        String host = NAMES[team][side];
        int levelUp = score == 0 ? 3 : 2 - score / 40;
        String title = host + getLevelUpTitle(levelUp);
        confirm(context, title, (a, b) -> {
            for (int i = 0; i < levelUp; i++) {
                tractorGame.upgrade(host, team, side);
            }
            notifyDataSetChanged();
        });
    }

    private void showLevel(View view, int position) {
        TextView text = view.findViewById(LEVELS[position]);
        int side = tractorGame.getSide(position);
        String level = tractorGame.getLevels()[position];
        text.setText(String.format("%s    %s   %s",
                LEFT == side ? "<" : "  ",
                "10".equals(level) ? level : " " + level,
                RIGHT == side ? ">" : " "
        ));
    }

    private void registerPlayerListener(View view, int position) {
        IntStream.range(0, 2).forEach(side -> {
            int player = PLAYERS[position][side];
            TextView text = view.findViewById(player);
            String playerName = text.getText().toString();

            text.setOnClickListener(v -> confirm(context,
                    getTitle(playerName, position),
                    (a, b) -> {
                        tractorGame.upgrade(
                                playerName,
                                position,
                                side
                        );
                        notifyDataSetChanged();
                    }));

            if (tractorGame.getHost().equals(playerName)) {
                text.setBackgroundColor(0xFF00FFFF);
            } else {
                text.setBackgroundColor(0x00000000);
            }
        });
    }

    @NonNull
    private String getTitle(String playerName, int position) {
        if (tractorGame.getHost().equals(playerName)) {
            return String.format("确定跳级 %s？", playerName);
        } else if (tractorGame.getTeam() == position) {
            return String.format("确定升级 %s？", playerName);
        } else {
            return String.format("确定换庄 %s？", playerName);
        }
    }

}