package hyltofthansen.ddhfregistrering.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.Object;import java.lang.Override;import java.lang.String;import java.util.LinkedHashMap;
import java.util.Map;

import hyltofthansen.ddhfregistrering.dao.PostHTTP;
import hyltofthansen.ddhfregistrering.R;

public class NewItemFragment extends Fragment {

    String[] descriptions = {"Skriv betegnelse her", "Indtast beskrivelse", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
            "Referencenummer til donator", "Referencenummer til producent", "Indtast postnummer her"};

    ListView lv;
    BaseAdapter listAdapter;
    ImageView imageView;

    //Inflate ActionBar for Opret Genstand fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_createitem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_main: //Der blev trykket på "Opret" knappen i Opret Genstand actionbaren
                // evt. dialog her der spørger om man er sikker på at man vil oprette?
                //TODO Tjek at i det mindste Betegnelse er indskrevet, og om man evt. har oprettet flere gange i træk?
                Map<String,Object> postParams = new LinkedHashMap<>();
                postParams.put("itemheadline", descriptions[0]);
                postParams.put("itemdescription", descriptions[1]);
                postParams.put("itemreceived", descriptions[2]);
                postParams.put("itemdatingfrom", descriptions[3]);
                postParams.put("itemdatingto", descriptions[4]);
                postParams.put("donator", descriptions[5]);
                postParams.put("producer", descriptions[6]);
                postParams.put("postnummer", descriptions[7]);

                PostHTTP postHTTP = new PostHTTP(postParams, getActivity(), getFragmentManager());
                postHTTP.execute();
                break;
            case R.id.action_photo: //Der blev trykket på kamera ikonet
                getFragmentManager().beginTransaction().addToBackStack(null).
                        replace(R.id.fragmentContainer, new ImageBrowseFragment())
                        .commit();
                break;
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Registrer genstand");
        //Aktiver ActionBar menu med Opret knap
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top
        View root = inflater.inflate(R.layout.newitemlayout, container, false);
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { // fanger billede resultat fra camera intent
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public void showDatePickerFragment(final int position) {
        DialogFragment date = new DatePickerDialogFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                descriptions[position] = "" + day + "-" + (month + 1) + "-" + year;
                listAdapter.notifyDataSetChanged();
            }
        };
        date.show(getFragmentManager(), "datePicker");
    }
}
