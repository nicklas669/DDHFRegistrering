package hyltofthansen.ddhfregistrering;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hyltofthansen.ddhfregistrering.fragments.LandingScreenFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Ryd gemt billede fra app's data
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        prefs.edit().remove("chosenImage").commit();

        if (savedInstanceState == null) {
            Fragment fragment = new LandingScreenFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainer, fragment).commit();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
