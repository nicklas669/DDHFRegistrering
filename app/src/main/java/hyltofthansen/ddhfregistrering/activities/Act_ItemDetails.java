package hyltofthansen.ddhfregistrering.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.adapters.Adapter_ItemDetailsPager;
import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;

/**
 * SwipeView of the item's details with info, pictures and sound
 */
public class Act_ItemDetails extends AppCompatActivity {
    private static final String TAG = "Act_ItemDetails";
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_itemdetails_swipeview);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_info_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_image_black));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mic_black));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);
        final Adapter_ItemDetailsPager adapter = new Adapter_ItemDetailsPager(
                getSupportFragmentManager(), tabLayout.getTabCount());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_itemdetails, menu);
        return true;
    }

    //From http://stackoverflow.com/questions/11818916/programmatically-hide-soft-keyboard-in-viewpager-onpagechangelistener-onpagesele/12422905#12422905
    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_item:   //Hvis man klikker på skraldespanden
                Bundle extras = this.getIntent().getExtras();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Er du sikker på at du vil slette genstanden?").setTitle("Verifikation");
                dialogBuilder.setPositiveButton("Ja", dialogClickListener);
                dialogBuilder.setNegativeButton("Nej", dialogClickListener);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            switch (id){
                case DialogInterface.BUTTON_POSITIVE:
                    Sing_AsyncTasks.getInstance().deleteHTTP(Act_ItemDetails.this);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };
}
