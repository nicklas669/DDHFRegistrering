package hyltofthansen.ddhfregistrering.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.activities.NewItemActivity;
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_create_main:   //Hvis man klikker på + knappen i action bar
                Intent opretItem = new Intent(this, NewItemActivity.class);
                startActivity(opretItem);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
