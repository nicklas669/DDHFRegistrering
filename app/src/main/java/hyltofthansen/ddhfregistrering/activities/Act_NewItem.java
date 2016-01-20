package hyltofthansen.ddhfregistrering.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONObject;

import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;
import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;
import hyltofthansen.ddhfregistrering.adapters.Adapter_NewItemPager;
import hyltofthansen.ddhfregistrering.R;

/**
 * SwipeView to create item with info, pictures and sound
 */
public class Act_NewItem extends AppCompatActivity {

    private static final String TAG = "Act_NewItem";
    private ViewPager viewPager;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_newitem_swipeview);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_info_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_image_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mic_black));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);


        final Adapter_NewItemPager adapter = new Adapter_NewItemPager
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideKeyboard();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_item: //Der blev trykket på "Opret" knappen i Opret Genstand actionbaren
                //TODO dialog her der spørger om man er sikker på at man vil oprette

                EditText titelTxt = Sing_NewItemData.getInstance().getTitelTxt();

                    if (Sing_NewItemData.getInstance().getTitelTxt().getText().toString().trim().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Genstand mangler en titel").setTitle("Titel mangler");
                        AlertDialog alert = builder.create();
                        alert.show();
                } else {
                    JSONObject JSONitem = Sing_NewItemData.getInstance().getJSONitem();
                    Sing_AsyncTasks.getInstance().callPostHTTPController(JSONitem, this);
                    break;
                }
        }
        return true;
    }

    //From http://stackoverflow.com/questions/11818916/programmatically-hide-soft-keyboard-in-viewpager-onpagechangelistener-onpagesele/12422905#12422905
    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newitem, menu);
        return true;
    }
}
