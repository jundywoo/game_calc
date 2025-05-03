package ng.ken.gamecalc.tractor;

import static ng.ken.gamecalc.utils.Constants.COLOR_CYAN;
import static ng.ken.gamecalc.utils.Constants.COLOR_WHITE;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_CONFIRM_SKIP_LEVEL;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_CONFIRM_SWITCH_HOST;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_CONFIRM_UPGRADE;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_HOST_LEFT;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_HOST_RIGHT;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_LEFT;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_LEVELS;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_LEVEL_10;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_LEVEL_PATTERN;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_MUST_GREATER_THAN_ZERO;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_MUST_INPUT_SCORE;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_MUST_SCORE_TIMES_5;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_PLAYERS;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_RESET_GAME;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_RIGHT;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_SWITCH_HOST;
import static ng.ken.gamecalc.utils.Constants.TRACTOR_TEAMS;
import static ng.ken.gamecalc.utils.Constants.getNameById;
import static ng.ken.gamecalc.utils.DialogUtils.confirm;
import static ng.ken.gamecalc.utils.DialogUtils.warning;
import static ng.ken.gamecalc.utils.StringHelper.getLevelUpTitle;

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

    private final Context context;
    private final TractorGame tractorGame;

    public TractorLevelAdapter(Context context, TractorGame tractorGame) {
        this.context = context;
        this.tractorGame = tractorGame;
    }

    private static int toOpposite(int side) {
        return (side + 1) % 2;
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
            view = LayoutInflater.from(context).inflate(TRACTOR_TEAMS[line], parent, false);
            registerPlayerListener(view, line);
            showLevel(view, line);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tractor_actions, parent, false);
            Button reset = view.findViewById(R.id.reset_game_button);
            reset.setOnClickListener(v -> confirm(context, TRACTOR_RESET_GAME, (a, b) -> {
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
            warning(context, TRACTOR_MUST_INPUT_SCORE);
            return;
        }
        int score = Integer.parseInt(text);
        if (score % 5 != 0) {
            warning(context, TRACTOR_MUST_SCORE_TIMES_5);
        } else if (score < 0) {
            warning(context, TRACTOR_MUST_GREATER_THAN_ZERO);
        } else if (score < 80) {
            hostUpgrade(score);
        } else {
            hostSwitchAndUpgrade(score);
        }
    }

    private void hostSwitchAndUpgrade(int score) {
        int team = toOpposite(tractorGame.getTeam());
        int side = toOpposite(tractorGame.getSide(team));
        String host = getNameById(TRACTOR_PLAYERS[team][side]);
        int levelUp = (score - 80) / 40;
        String title = host + (levelUp == 0 ? TRACTOR_SWITCH_HOST : getLevelUpTitle(levelUp));
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
        String host = getNameById(TRACTOR_PLAYERS[team][side]);
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
        TextView text = view.findViewById(TRACTOR_LEVELS[position]);
        int side = tractorGame.getSide(position);
        String level = tractorGame.getLevels()[position];
        text.setText(String.format(TRACTOR_LEVEL_PATTERN,
                TRACTOR_LEFT == side ? TRACTOR_HOST_LEFT : "  ",
                TRACTOR_LEVEL_10.equals(level) ? level : " " + level,
                TRACTOR_RIGHT == side ? TRACTOR_HOST_RIGHT : " "
        ));
    }

    private void registerPlayerListener(View view, int position) {
        IntStream.range(0, 2).forEach(side -> {
            int player = TRACTOR_PLAYERS[position][side];
            String playerName = getNameById(player);
            TextView text = view.findViewById(player);
            text.setText(playerName);

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
                text.setBackgroundColor(COLOR_CYAN);
            } else {
                text.setBackgroundColor(COLOR_WHITE);
            }
        });
    }

    @NonNull
    private String getTitle(String playerName, int position) {
        if (tractorGame.getHost().equals(playerName)) {
            return String.format(TRACTOR_CONFIRM_SKIP_LEVEL, playerName);
        } else if (tractorGame.getTeam() == position) {
            return String.format(TRACTOR_CONFIRM_UPGRADE, playerName);
        } else {
            return String.format(TRACTOR_CONFIRM_SWITCH_HOST, playerName);
        }
    }

}