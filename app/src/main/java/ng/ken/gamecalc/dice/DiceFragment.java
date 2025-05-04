package ng.ken.gamecalc.dice;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ng.ken.gamecalc.R;

public class DiceFragment extends Fragment {

    public static final int ROLL_TIMES = 10;
    public static final int ROLL_INTERVAL = 20;
    public static final String PREFS_NAME = "DiceGamePrefs";
    public static final int MIN_DICE_COUNT = 1;
    public static final int MAX_DICE_COUNT = 20;
    private static final String DICE_COUNT_KEY = "dice_count";
    private static final int[] DICES = new int[]{-1,
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6,
    };
    private final Random random = new Random();
    private final Context context;
    private ImageView[] dices;
    private int diceCount;
    private Button rollDiceButton;
    private ViewGroup diceLayout;
    private View reduceDiceButton;
    private View addDiceButton;

    public DiceFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dice_fragment, container, false);

        diceLayout = view.findViewById(R.id.dice_layout);

        rollDiceButton = view.findViewById(R.id.roll_dice_button);
        rollDiceButton.setOnClickListener(v -> rollDice());

        reduceDiceButton = view.findViewById(R.id.reduce_dice_button);
        reduceDiceButton.setOnClickListener(v -> modifyDiceCount(diceCount - 1));

        addDiceButton = view.findViewById(R.id.add_dice_button);
        addDiceButton.setOnClickListener(v -> modifyDiceCount(diceCount + 1));

        loadDiceCount();

        return view;
    }

    private void modifyDiceCount(int count) {
        if (MIN_DICE_COUNT <= count && count <= MAX_DICE_COUNT) {
            updateDiceCount(count);
            saveDiceCount(count);
        }
    }


    private void updateDiceCount(int count) {
        diceCount = count;
        rollDiceButton.setText(String.format(Locale.getDefault(), "碌 (%d)", count));
        reduceDiceButton.setVisibility(VISIBLE);
        addDiceButton.setVisibility(VISIBLE);
        if (count == MIN_DICE_COUNT) {
            reduceDiceButton.setVisibility(INVISIBLE);
        } else if (count == MAX_DICE_COUNT) {
            addDiceButton.setVisibility(INVISIBLE);
        }

        diceLayout.removeAllViews();
        dices = new ImageView[count];
        for (int i = 0; i < count; i++) {
            ImageView diceImage = new ImageView(getContext());
            diceImage.setId(View.generateViewId());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            diceImage.setLayoutParams(params);
            diceImage.setImageResource(DICES[1]);
            diceLayout.addView(diceImage);
            dices[i] = diceImage;
        }
    }

    private void rollDice() {
        rollDiceButton.setEnabled(false);
        new Thread(() -> {
            for (int d = 0; d < diceCount; d++) {
                for (int i = 0; i < ROLL_TIMES; i++) {
                    rollOneDice(Arrays.copyOfRange(dices, d, diceCount));
                    try {
                        TimeUnit.MILLISECONDS.sleep(ROLL_INTERVAL);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
            }
            rollDiceButton.post(() -> rollDiceButton.setEnabled(true));
        }).start();
    }

    private void rollOneDice(ImageView... diceImages) {
        Arrays.stream(diceImages).parallel().forEach(diceImage ->
                diceImage.post(() -> diceImage.setImageResource(DICES[random.nextInt(6) + 1]))
        );

    }

    private void loadDiceCount() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedCount = prefs.getString(DICE_COUNT_KEY, "");
        try {
            updateDiceCount(Integer.parseInt(savedCount));
        } catch (Exception e) {
            updateDiceCount(2);
        }
    }

    public void saveDiceCount(int count) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(DICE_COUNT_KEY, String.valueOf(count));
        editor.apply();
    }
}