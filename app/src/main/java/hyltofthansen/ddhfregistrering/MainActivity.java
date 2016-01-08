package hyltofthansen.ddhfregistrering;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import hyltofthansen.ddhfregistrering.fragments.NewItemFragment;
import hyltofthansen.ddhfregistrering.fragments.SearchItemFragment;

public class MainActivity extends AppCompatActivity {

    private SearchItemFragment searchFragment;
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ryd gemt billede fra app's data
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        prefs.edit().remove("chosenImage").commit();

        searchFragment = new SearchItemFragment();
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//
//        ft.add(searchFragment, null);
//        ft.addToBackStack(null);
//        ft.commit();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, searchFragment).commit();
            getFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (getFragmentManager().getBackStackEntryCount() == 0) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false); // deaktivér "tilbage"-pil i venstre top når landing page vises
                        setTitle("DDHF Registrering"); // Hvis landing page fragment vises, skiftes hovedmenu tekst
                    }
                }
            });
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0 ){
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_create_main:   //Hvis man klikker på + knappen i action bar
                NewItemFragment newItemFragment = new NewItemFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentContainer, newItemFragment);
                ft.commit();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
