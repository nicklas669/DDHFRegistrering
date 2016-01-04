package hyltofthansen.ddhfregistrering.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.Object;import java.lang.Override;import java.lang.String;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import hyltofthansen.ddhfregistrering.dao.PostHTTP;
import hyltofthansen.ddhfregistrering.R;

public class NewItemFragment extends Fragment {

    private String[] descriptions = {"Skriv betegnelse her", "Indtast beskrivelse", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
            "Referencenummer til donator", "Referencenummer til producent", "Indtast postnummer her"};

    private ListView lv;
    private BaseAdapter listAdapter;
    private ImageView imageView;
    private EditText titelTxt, beskrivelseTxt,
            modtagelsesDatoTxt, dateringFraTxt,
            dateringTilTxt, refDonatorTxt, refProducentTxt, postNrTxt;
    private static final String TAG = "NewItemFragment";

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
                postParams.put("itemheadline", titelTxt.getText().toString());
                postParams.put("itemdescription", beskrivelseTxt.getText().toString());
                postParams.put("itemreceived", modtagelsesDatoTxt.getText().toString());
                postParams.put("itemdatingfrom", dateringFraTxt.getText().toString());
                postParams.put("itemdatingto", dateringTilTxt.getText().toString());
                postParams.put("donator", refDonatorTxt.getText().toString());
                postParams.put("producer", refProducentTxt.getText().toString());
                postParams.put("postnummer", postNrTxt.getText().toString());

                Log.d(TAG, "Hejsa!");
                for (Map.Entry<String, Object> param : postParams.entrySet()) {
                    Log.d(TAG, param.getKey());
                }
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
        titelTxt = (EditText) root.findViewById(R.id.titelTextEdit);
        beskrivelseTxt = (EditText) root.findViewById(R.id.beskrivelseTextEdit);
        refDonatorTxt = (EditText) root.findViewById(R.id.refDonatorTextEdit);
        refProducentTxt = (EditText) root.findViewById(R.id.refproducent);
        modtagelsesDatoTxt = (EditText) root.findViewById(R.id.modtagelsesDatoEditText);
        dateringFraTxt = (EditText) root.findViewById(R.id.dateringfraeditText);
        dateringTilTxt = (EditText) root.findViewById(R.id.dateringtileditText);
        postNrTxt = (EditText) root.findViewById(R.id.postnr);

        setEditTextDatePicker(modtagelsesDatoTxt);
        setEditTextDatePicker(dateringFraTxt);
        setEditTextDatePicker(dateringTilTxt);

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

    private void setEditTextDatePicker(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment(editText);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }
}