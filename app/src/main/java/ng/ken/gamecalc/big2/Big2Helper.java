package ng.ken.gamecalc.big2;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.graphics.Typeface.BOLD;
import static android.view.Gravity.CENTER;
import static ng.ken.gamecalc.utils.Constants.COLOR_AMBER;
import static ng.ken.gamecalc.utils.Constants.COLOR_RED;
import static ng.ken.gamecalc.utils.Constants.COLOR_WHITE;
import static ng.ken.gamecalc.utils.Constants.COLOR_YELLOW;
import static ng.ken.gamecalc.utils.Constants.BIG2_PLAYERS;
import static ng.ken.gamecalc.utils.Constants.SCORES;
import static ng.ken.gamecalc.utils.Constants.getName;
import static ng.ken.gamecalc.utils.Constants.getNameById;
import static ng.ken.gamecalc.utils.StringHelper.toHumanAvgAndSum;
import static ng.ken.gamecalc.utils.StringHelper.toHumanDigits;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Big2Helper {

    public static int[] getAnInt(View view) {
        int[] scores = new int[4];
        for (int i = 0; i < SCORES.length; i++) {
            EditText input = view.findViewById(SCORES[i]);
            try {
                scores[i] = Integer.parseInt(input.getText().toString());
            } catch (Exception e) {
                scores[i] = 0;
            }
        }
        return scores;
    }

    public static void setTitle(View view) {
        for (int i = 0; i < 4; i++) {
            int playerId = BIG2_PLAYERS[i];
            TextView text = view.findViewById(playerId);
            text.setText(getNameById(playerId));
        }
    }

    public static void setSummary(View view, int[] sums) {
        for (int i = 0; i < SCORES.length; i++) {
            TextView text = view.findViewById(SCORES[i]);
            int sum = sums[i];
            text.setText(String.valueOf(sum));
            setRAG(text, sum, 70, 85, 100);
        }
    }

    public static void setScore(View view, int[] scores) {
        for (int i = 0; i < SCORES.length; i++) {
            TextView text = view.findViewById(SCORES[i]);
            int score = scores[i];
            text.setText(score == 0 ? "🏆" : String.valueOf(score));
            setRAG(text, score, 16, 30, 52);
        }
    }

    private static void setRAG(TextView text, int score, int green, int amber, int red) {
        if (green <= score && score < amber) {
            text.setBackgroundColor(COLOR_YELLOW);
        } else if (amber <= score && score < red) {
            text.setBackgroundColor(COLOR_AMBER);
        } else if (score >= red) {
            text.setBackgroundColor(COLOR_RED);
        } else {
            text.setBackgroundColor(COLOR_WHITE);
        }
    }


    public static View sumUpView(Context context, int[] sums, double avg) {
        LinearLayout rootLayout = new LinearLayout(context);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(16, 16, 16, 16);

        GradientDrawable border = new GradientDrawable();
        border.setColor(WHITE); // 背景颜色
        border.setStroke(2, BLACK); // 边框宽度和颜色
        border.setCornerRadius(8); // 圆角
        rootLayout.setBackground(border);

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setStretchAllColumns(true);
        rootLayout.addView(tableLayout);

        addRow(context, tableLayout, "玩家", "分數", true);

        for (int i = 0; i < sums.length; i++) {
            int sum = sums[i];
            addRow(context, tableLayout, getName(i), toHumanAvgAndSum(avg, sum), false);
        }

        return rootLayout;
    }


    private static void addRow(Context context, TableLayout tableLayout, String name, String score, boolean header) {
        TableRow row = new TableRow(context);

        TextView nameTextView = new TextView(context);
        nameTextView.setText(name);
        nameTextView.setGravity(CENTER);
        nameTextView.setWidth(row.getWidth() / 3);
        nameTextView.setPadding(16, 16, 16, 16);
        nameTextView.setTextColor(header ? WHITE : BLACK);
        nameTextView.setBackgroundColor(header ? BLACK : WHITE);

        TextView scoreTextView = new TextView(context);
        scoreTextView.setText(score);
        scoreTextView.setPadding(16, 16, 16, 16);
        scoreTextView.setTextColor(header ? WHITE : BLACK);
        scoreTextView.setBackgroundColor(header ? BLACK : WHITE);

        row.addView(nameTextView);
        row.addView(scoreTextView);

        if (header) {
            nameTextView.setTypeface(null, BOLD);
            scoreTextView.setTypeface(null, BOLD);

            View divider = new View(context);
            TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT, 2);
            params.setMargins(0, 0, 0, 0);
            divider.setLayoutParams(params);
            divider.setBackgroundColor(Color.GRAY);
            tableLayout.addView(divider);
        }
        tableLayout.addView(row);
    }

}
