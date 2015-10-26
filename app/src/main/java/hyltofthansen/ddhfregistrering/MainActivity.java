package hyltofthansen.ddhfregistrering;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button b_newItem = (Button) findViewById(R.id.b_newItem);
        b_newItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        System.out.println("Du trykkede!");
        Intent i = new Intent(this, NewItem.class);
        startActivity(i);
    }
}
