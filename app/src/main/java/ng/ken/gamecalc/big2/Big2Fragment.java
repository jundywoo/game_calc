package ng.ken.gamecalc.big2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import ng.ken.gamecalc.R;

public class Big2Fragment extends Fragment {

    private final Big2Game big2Game;
    private final Big2ScoresAdapter adapter;

    public Big2Fragment(Context context) {
        big2Game = new Big2Game(context);
        adapter = new Big2ScoresAdapter(context, big2Game);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.big2_fragment, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(inflate.findViewById(R.id.scores_grid), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        big2Game.loadScores();
        inflate.<ListView>findViewById(R.id.scores_grid).setAdapter(adapter);

        return inflate;
    }
}