package ng.ken.gamecalc;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.view_pager);

        // 设置 ViewPager 的适配器
        viewPager.setAdapter(new ViewPagerAdapter(this));

        // 将 TabLayout 和 ViewPager2 绑定
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("锄大D");
            } else if (position == 1) {
                tab.setText("拖拉机");
            }
        }).attach();
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
