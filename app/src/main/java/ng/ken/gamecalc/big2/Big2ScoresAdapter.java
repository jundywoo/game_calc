package ng.ken.gamecalc.big2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

import ng.ken.gamecalc.R;

public class Big2ScoresAdapter extends BaseAdapter {

    private final static int[] PLAYERS = new int[]{
            R.id.score_c, R.id.score_k, R.id.score_t, R.id.score_h
    };

    private final Context context;
    private final Big2Game big2Game;

    public Big2ScoresAdapter(Context context, Big2Game big2Game) {
        this.context = context;
        this.big2Game = big2Game;
    }

    private static int[] getAnInt(View view) {
        int[] scores = new int[4];
        for (int i = 0; i < PLAYERS.length; i++) {
            EditText input = view.findViewById(PLAYERS[i]);
            try {
                scores[i] = Integer.parseInt(input.getText().toString());
            } catch (Exception e) {
                scores[i] = 0;
            }
        }
        return scores;
    }

    private static void setSummary(View view, int[] sums) {
        for (int i = 0; i < 4; i++) {
            TextView text = view.findViewById(PLAYERS[i]);
            int sum = sums[i];
            text.setText(String.valueOf(sum));
            if (70 <= sum && sum < 85) {
                text.setBackgroundColor(0xFFFFFF00);
            } else if (85 <= sum && sum < 100) {
                text.setBackgroundColor(0xFFFFA500);
            } else if (sum >= 100) {
                text.setBackgroundColor(0xFFFF0000);
            } else {
                text.setBackgroundColor(0x00000000);
            }
        }
    }

    private static void setScore(View view, int[] scores) {
        for (int i = 0; i < PLAYERS.length; i++) {
            TextView text = view.findViewById(PLAYERS[i]);
            int score = scores[i];
            text.setText(String.valueOf(score));
            if (16 <= score && score < 30) {
                text.setBackgroundColor(0xFFFFFF00);
            } else if (30 <= score && score < 52) {
                text.setBackgroundColor(0xFFFFA500);
            } else if (score >= 52) {
                text.setBackgroundColor(0xFFFF0000);
            } else {
                text.setBackgroundColor(0x00000000);
            }
        }
    }

    @Override
    public int getCount() {
        return big2Game.getScores().length + 3;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0 || position == 2) {
            return null; // 标题行
        } else if (position > 2) {
            return big2Game.getScores()[position - 1];
        } else if (position == 1) {
            return big2Game.getSum(); // sum 行
        } else {
            return null; // 输入框行
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (position == 0) {
            // Title
            view = LayoutInflater.from(context).inflate(R.layout.big2_title_item, parent, false);
            Button newButton = view.findViewById(R.id.new_game_button);
            newButton.setOnClickListener(v -> confirm("开始新游戏？", (a, b) -> {
                big2Game.cleanScores();
                notifyDataSetChanged();
            }));
        } else if (position == 1) {
            // Line of summary
            view = LayoutInflater.from(context).inflate(R.layout.big2_sum_item, parent, false);
            view.setBackgroundColor(0xFFD3D3D3);

            int[] sum = big2Game.getSum();
            setSummary(view, sum);
        } else if (position == 2) {
            // Line of input
            view = LayoutInflater.from(context).inflate(R.layout.big2_input_item, parent, false);
            Button addButton = view.findViewById(R.id.add_button);

            addButton.setOnClickListener(v -> {
                int[] newScore = getAnInt(view);
                if (!Arrays.stream(newScore).anyMatch(a -> a == 0)) {
                    new AlertDialog.Builder(context)
                            .setTitle("至少一个赢家分数为零")
                            .setPositiveButton("明白", null)
                            .show();
                    return;
                } else if (!Arrays.stream(newScore).anyMatch(a -> a > 0)) {
                    new AlertDialog.Builder(context)
                            .setTitle("至少一个赢家被记分数")
                            .setPositiveButton("明白", null)
                            .show();
                    return;
                }
                big2Game.addScore(newScore);
                notifyDataSetChanged();
            });
        } else {
            // Lines of scores
            view = LayoutInflater.from(context).inflate(R.layout.big2_score_item, parent, false);

            int line = position - 3;
            setScore(view, big2Game.getScores()[line]);

            Button deleteButton = view.findViewById(R.id.delete_button);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> confirm("确定删除？", (a, b) -> {
                big2Game.removeScore(line);
                notifyDataSetChanged();
            }));
        }

        return view;
    }

    private void confirm(String title, OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("是", listener)
                .setNegativeButton("否", null)
                .show();
    }
}