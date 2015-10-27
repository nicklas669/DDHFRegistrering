package hyltofthansen.ddhfregistrering;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class NewItem extends FragmentActivity {

    String[] fields = {"Genstand nr.", "Betegnelse", "Modtagelsesdato", "Datering fra", "Datering til",
            "Beskrivelse", "Ref. til donator", "Ref. til producent", "Geografisk område", "Ref. til emnegruppe(r)"};

    String[] descriptions = {"Skriv genstand nummer her", "Skriv betegnelse her", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
    "Indtast beskrivelse", "Referencenummer til donator", "Referencenummer til producent", "Vælg kommune her", "Referencenummer til emnegruppe(r)"};

    ListView lv;
    BaseAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_item);
        lv = (ListView) findViewById(R.id.newItem_listview);

        listAdapter = new ArrayAdapter(this, R.layout.listview_layout, R.id.listHeader, fields){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView beskrivelse = (TextView) view.findViewById(R.id.listDescription);
                beskrivelse.setText(descriptions[position]);
                return view;
            }
        };

        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("DU TRYKKEDE SGU PÅ ENTRY "+position+"!!");
                switch (position) {
                    case 0: // Genstand nr.
                        break;
                    case 1: // Betegnelse
                        break;
                    case 2: // Modtagelsesdato
                        DialogFragment dateFragment = new DatePickerFragment() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                descriptions[2] = ""+day+"-"+(month+1)+"-"+year;
                                listAdapter.notifyDataSetChanged();
                            }
                        };
                        dateFragment.show(getFragmentManager(), "datePicker");
                        break;
                    case 3: // Datering fra
                        break;
                    case 4: // Datering til
                        break;
                    case 5: // Beskrivelse
                        break;
                    case 6: // Ref. til donator
                        break;
                    case 7: // Ref. til producent
                        break;
                    case 8: // Geografisk område
                        break;
                    case 9: // Ref. til emnegruppe(r)
                        break;
                }
            }
        }
        );
    }
}
