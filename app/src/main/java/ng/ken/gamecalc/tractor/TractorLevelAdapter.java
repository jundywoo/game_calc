package ng.ken.gamecalc.tractor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
    private static int LEFT = 0;
    private static int RIGHT = 1;
    private final Context context;
    private final TractorGame tractorGame;

    public TractorLevelAdapter(Context context, TractorGame tractorGame) {
        this.context = context;
        this.tractorGame = tractorGame;
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
        if (position < 2) {
            view = LayoutInflater.from(context).inflate(TEAMS[position], parent, false);
            registerPlayerListener(view, position);
            showLevel(view, position);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tractor_reset, parent, false);
            Button reset = view.findViewById(R.id.reset_game_button);
            reset.setOnClickListener(v -> confirm("确定重置游戏？", (a, b) -> {
                tractorGame.resetLevel();
                notifyDataSetChanged();
            }));
        }

        return view;
    }

    private void showLevel(View view, int position) {
        TextView text = view.findViewById(LEVELS[position]);
        int side = tractorGame.getSide(position);
        text.setText(String.format("%s   %s   %s",
                LEFT == side ? "<" : " ",
                tractorGame.getLevels()[position],
                RIGHT == side ? ">" : " "
        ));
    }

    private void registerPlayerListener(View view, int position) {
        IntStream.range(0, 2).forEach(side -> {
            int player = PLAYERS[position][side];
            TextView text = view.findViewById(player);
            String playerName = text.getText().toString();

            text.setOnClickListener(v -> confirm(getTitle(playerName, position), (a, b) -> {
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
        return String.format("确定%s %s？", tractorGame.getTeam() == position ? "升级" : "换庄", playerName);
    }

    private void confirm(String title, OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("是", listener)
                .setNegativeButton("否", null)
                .show();
    }
}