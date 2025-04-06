package ng.ken.gamecalc;

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

public class ScoresAdapter extends BaseAdapter {

    private final Context context;
    private final Big2Game big2Game;

    public ScoresAdapter(Context context, Big2Game big2Game) {
        this.context = context;
        this.big2Game = big2Game;
    }

    private static int getAnInt(EditText input1) {
        try {
            return Integer.parseInt(input1.getText().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    private static void setSummary(TextView scoreC, int sum) {
        scoreC.setText(String.valueOf(sum));
        if (70 < sum && sum <= 85) {
            scoreC.setBackgroundColor(0xFFFFFF00);
        } else if (85 < sum && sum <= 100) {
            scoreC.setBackgroundColor(0xFFFFA500);
        } else if (sum > 100) {
            scoreC.setBackgroundColor(0xFFFF0000);
        } else {
            scoreC.setBackgroundColor(0x00000000);
        }
    }

    @Override
    public int getCount() {
        return big2Game.getScores().length + 3;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return null; // 标题行
        } else if (position <= big2Game.getScores().length) {
            return big2Game.getScores()[position - 1];
        } else if (position == big2Game.getScores().length + 1) {
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
        if (position == 0) {
            // 标题行
            convertView = LayoutInflater.from(context).inflate(R.layout.title_item, parent, false);
            Button newButton = convertView.findViewById(R.id.new_game_button);
            newButton.setOnClickListener(v -> confirm("开始新游戏？", (a, b) -> {
                big2Game.cleanScores();
                notifyDataSetChanged();
            }));
        } else if (position <= big2Game.getScores().length) {
            // 分数行
            convertView = LayoutInflater.from(context).inflate(R.layout.score_item, parent, false);
            TextView scoreC = convertView.findViewById(R.id.score_c);
            TextView scoreK = convertView.findViewById(R.id.score_k);
            TextView scoreT = convertView.findViewById(R.id.score_t);
            TextView scoreH = convertView.findViewById(R.id.score_h);
            Button deleteButton = convertView.findViewById(R.id.delete_button);

            int[] scores = big2Game.getScores()[position - 1];
            scoreC.setText(String.valueOf(scores[0]));
            scoreK.setText(String.valueOf(scores[1]));
            scoreT.setText(String.valueOf(scores[2]));
            scoreH.setText(String.valueOf(scores[3]));
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(v -> confirm("确定删除？", (a, b) -> {
                big2Game.removeScore(position - 1);
                notifyDataSetChanged();
            }));
        } else if (position == big2Game.getScores().length + 1) {
            // 总分行
            convertView = LayoutInflater.from(context).inflate(R.layout.sum_item, parent, false);
            convertView.setBackgroundColor(0xFFD3D3D3);
            TextView scoreC = convertView.findViewById(R.id.score_c);
            TextView scoreK = convertView.findViewById(R.id.score_k);
            TextView scoreT = convertView.findViewById(R.id.score_t);
            TextView scoreH = convertView.findViewById(R.id.score_h);

            int[] sum = big2Game.getSum();
            setSummary(scoreC, sum[0]);
            setSummary(scoreK, sum[1]);
            setSummary(scoreT, sum[2]);
            setSummary(scoreH, sum[3]);
        } else {
            // 输入框行
            convertView = LayoutInflater.from(context).inflate(R.layout.input_item, parent, false);
            EditText input1 = convertView.findViewById(R.id.input1);
            EditText input2 = convertView.findViewById(R.id.input2);
            EditText input3 = convertView.findViewById(R.id.input3);
            EditText input4 = convertView.findViewById(R.id.input4);
            Button addButton = convertView.findViewById(R.id.add_button);

            addButton.setOnClickListener(v -> {
                int[] newScore = new int[4];
                newScore[0] = getAnInt(input1);
                newScore[1] = getAnInt(input2);
                newScore[2] = getAnInt(input3);
                newScore[3] = getAnInt(input4);
                if (!Arrays.stream(newScore).anyMatch(a -> a == 0)) {
                    new AlertDialog.Builder(context)
                            .setTitle("至少一个赢家分数为零")
                            .setPositiveButton("明白", null)
                            .show();
                    return;
                }
                big2Game.addScore(newScore);
                notifyDataSetChanged();
            });
        }

        return convertView;
    }

    private void confirm(String title, OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton("是", listener)
                .setNegativeButton("否", null)
                .show();
    }
}