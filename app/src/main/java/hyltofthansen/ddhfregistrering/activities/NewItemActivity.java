package hyltofthansen.ddhfregistrering.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import hyltofthansen.ddhfregistrering.adapters.NewItemPagerAdapter;
import hyltofthansen.ddhfregistrering.R;

/**
 * Created by hylle on 07-01-2016.
 */
public class NewItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newitem_swipeview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Info"));
//        tabLayout.addTab(tabLayout.newTab().setText("Billeder"));
//        tabLayout.addTab(tabLayout.newTab().setText("Lyd"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_info_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_image_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mic_black));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final NewItemPagerAdapter adapter = new NewItemPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newitem, menu);
        return true;
    }
}
