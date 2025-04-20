package ng.ken.gamecalc;

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

import ng.ken.gamecalc.big2.Big2Fragment;
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
            if (position == 0) {
                tab.setText("鋤大D");
            } else if (position == 1) {
                tab.setText("拖拉機");
            }
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

        private final Context context;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            context = fragmentActivity;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new Big2Fragment(context); // 锄大D
            } else {
                return new TractorFragment(context); // 拖拉机
            }
        }

        @Override
        public int getItemCount() {
            return 2; // 两个标签页
        }
    }

}
