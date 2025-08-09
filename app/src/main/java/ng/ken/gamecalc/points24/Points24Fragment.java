package ng.ken.gamecalc.points24;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ng.ken.gamecalc.R;

public class Points24Fragment extends Fragment {
    //private final Context context;

    public Points24Fragment(Context context) {
        //this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.points24_fragment, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, Points24Calculator.CARDS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner card1 = view.findViewById(R.id.card_1);
        Spinner card2 = view.findViewById(R.id.card_2);
        Spinner card3 = view.findViewById(R.id.card_3);
        Spinner card4 = view.findViewById(R.id.card_4);

        card1.setAdapter(adapter);
        card2.setAdapter(adapter);
        card3.setAdapter(adapter);
        card4.setAdapter(adapter);

        Button calculateButton = view.findViewById(R.id.point24_calculate_button);
        Button resetButton = view.findViewById(R.id.point24_reset_button);
        TextView resultText = view.findViewById(R.id.point24_result_text);

        calculateButton.setOnClickListener(v -> {
            String value1 = card1.getSelectedItem().toString();
            String value2 = card2.getSelectedItem().toString();
            String value3 = card3.getSelectedItem().toString();
            String value4 = card4.getSelectedItem().toString();

            // 调用计算方法
            var calc = new Points24Calculator(value1, value2, value3, value4);

            // 显示结果
            if (calc.isSolved()) {
                resultText.setText(String.join("\n", calc.getSolutions()));
            } else {
                resultText.setText(calc.cardsToString() + " 無解");
            }
        });
        resetButton.setOnClickListener(v -> {
            resultText.setText(R.string.waiting_result);
        });

        return view;
    }

}