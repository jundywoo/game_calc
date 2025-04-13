package ng.ken.gamecalc.tractor;

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

public class TractorFragment extends Fragment {

    private final TractorGame tractorGame;
    private final TractorLevelAdapter adapter;

    public TractorFragment(Context context) {
        tractorGame = new TractorGame(context);
        adapter = new TractorLevelAdapter(context, tractorGame);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.tractor_fragment, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(inflate.findViewById(R.id.tractor_grid), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tractorGame.loadScores();
        inflate.<ListView>findViewById(R.id.tractor_grid).setAdapter(adapter);

        return inflate;
    }
}