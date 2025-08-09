package ng.ken.gamecalc;

import static ng.ken.gamecalc.utils.Constants.GAMES;
import static ng.ken.gamecalc.utils.Constants.GAME_BIG2;
import static ng.ken.gamecalc.utils.Constants.GAME_DICE;
import static ng.ken.gamecalc.utils.Constants.GAME_POINTS_24;
import static ng.ken.gamecalc.utils.Constants.GAME_TRACTOR;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Map;
import java.util.Optional;

import ng.ken.gamecalc.big2.Big2Fragment;
import ng.ken.gamecalc.dice.DiceFragment;
import ng.ken.gamecalc.points24.Points24Fragment;
import ng.ken.gamecalc.tractor.TractorFragment;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPreferences";
    private static final String LAST_TAB_INDEX = "LastTabIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // 设置 ViewPager 的适配器
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // 读取上次保存的标签页索引
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int lastTabIndex = preferences.getInt(LAST_TAB_INDEX, 0); // 默认加载第一个标签页
        viewPager.setCurrentItem(lastTabIndex, false);

        // 将 TabLayout 和 ViewPager2 绑定
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(GAMES[position]);
        }).attach();

        // 监听标签页切换并保存当前索引
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(LAST_TAB_INDEX, position);
                editor.apply();
            }
        });
    }

    // ViewPager2 的适配器
    private static class ViewPagerAdapter extends FragmentStateAdapter {

        private final Map<String, Fragment> fragments;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            fragments = Map.of(
                    GAME_BIG2, new Big2Fragment(fragmentActivity),
                    GAME_TRACTOR, new TractorFragment(fragmentActivity),
                    GAME_POINTS_24, new Points24Fragment(fragmentActivity),
                    GAME_DICE, new DiceFragment(fragmentActivity)
            );
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return Optional.ofNullable(fragments.get(GAMES[position])).orElse(new Fragment());
        }

        @Override
        public int getItemCount() {
            return GAMES.length; // 两个标签页
        }
    }

}
