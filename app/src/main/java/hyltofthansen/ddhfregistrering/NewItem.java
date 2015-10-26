package hyltofthansen.ddhfregistrering;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class NewItem extends FragmentActivity {

    String[] fields = {"Genstand nr.", "Betegnelse", "Modtagelsesdato", "Datering fra", "Datering til",
            "Beskrivelse", "Ref. til donator", "Ref. til producent", "Geografisk område", "Ref. til emnegruppe(r)"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);
        ListView lv = (ListView) findViewById(R.id.newItem_listview);
        lv.setAdapter(new ArrayAdapter(this, R.layout.listview_layout, R.id.listHeader, fields));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("DU TRYKKEDE SGU PÅ ENTRY "+position+"!!");
                if (position == 2) { // Modtagelsesdato
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getFragmentManager(), "datePicker");
                }
            }
        }
        );
    }
}
