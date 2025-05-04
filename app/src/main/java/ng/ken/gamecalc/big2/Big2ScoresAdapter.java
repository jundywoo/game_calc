package ng.ken.gamecalc.big2;

import static ng.ken.gamecalc.big2.Big2Helper.getAnInt;
import static ng.ken.gamecalc.big2.Big2Helper.setScore;
import static ng.ken.gamecalc.big2.Big2Helper.setSummary;
import static ng.ken.gamecalc.big2.Big2Helper.setTitle;
import static ng.ken.gamecalc.big2.Big2Helper.sumUpView;
import static ng.ken.gamecalc.utils.Constants.BIG2_AT_LEASE_ONE_SCORED;
import static ng.ken.gamecalc.utils.Constants.BIG2_AT_LEASE_ONE_ZERO;
import static ng.ken.gamecalc.utils.Constants.BIG2_CONFIRM_REMOVE;
import static ng.ken.gamecalc.utils.Constants.BIG2_NEW_A_GAME;
import static ng.ken.gamecalc.utils.Constants.BIG2_SEE_MORE;
import static ng.ken.gamecalc.utils.Constants.UNDERSTOOD;
import static ng.ken.gamecalc.utils.DialogUtils.confirm;
import static ng.ken.gamecalc.utils.DialogUtils.confirmWithView;
import static ng.ken.gamecalc.utils.DialogUtils.warning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.Arrays;

import ng.ken.gamecalc.R;
import static ng.ken.gamecalc.utils.StringHelper.getBig2Title;

public class Big2ScoresAdapter extends BaseAdapter {

    private final Context context;
    private final Big2Game big2Game;

    public Big2ScoresAdapter(Context context, Big2Game big2Game) {
        this.context = context;
        this.big2Game = big2Game;
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
            setTitle(view);
            Button newButton = view.findViewById(R.id.sum_up_button);
            newButton.setOnClickListener(this::sumUpGame);
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
                if (Arrays.stream(newScore).noneMatch(a -> a == 0)) {
                    warning(context, BIG2_AT_LEASE_ONE_ZERO, UNDERSTOOD);
                    return;
                } else if (Arrays.stream(newScore).noneMatch(a -> a > 0)) {
                    warning(context, BIG2_AT_LEASE_ONE_SCORED, UNDERSTOOD);
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
            deleteButton.setOnClickListener(v -> confirm(context, BIG2_CONFIRM_REMOVE, (a, b) -> {
                big2Game.removeScore(line);
                notifyDataSetChanged();
            }));
        }

        return view;
    }


    private void sumUpGame(View view) {
        double avg = Arrays.stream(big2Game.getSum()).average().orElse(0);
        confirmWithView(
                context,
                getBig2Title(avg),
                sumUpView(context, big2Game.getSum(), avg),
                BIG2_NEW_A_GAME,
                (a, b) -> {
                    big2Game.cleanScores();
                    notifyDataSetChanged();
                },
                BIG2_SEE_MORE,
                null
        );
    }

}