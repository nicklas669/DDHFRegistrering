package hyltofthansen.ddhfregistrering.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Override;import java.lang.String;

import hyltofthansen.ddhfregistrering.dao.PostHTTP;
import hyltofthansen.ddhfregistrering.R;

public class NewItemInfoFragment extends Fragment {

    private ListView lv;
    private BaseAdapter listAdapter;
    private ImageView imageView;
    private EditText titelTxt, beskrivelseTxt,
            modtagelsesDatoTxt, dateringFraTxt,
            dateringTilTxt, refDonatorTxt, refProducentTxt, postNrTxt;
    private static final String TAG = "NewItemInfoFragment";
    private android.support.v4.app.FragmentManager fm;

    //Inflate ActionBar for Opret Genstand fragment
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_createitem, menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_main: //Der blev trykket på "Opret" knappen i Opret Genstand actionbaren
                //TODO dialog her der spørger om man er sikker på at man vil oprette
                //TODO Tjek at i det mindste Betegnelse er indskrevet, og om man evt. har oprettet flere gange i træk?
                try {
                    JSONObject JSONitem = new JSONObject().put("itemheadline", titelTxt.getText().toString()).put("itemdescription", beskrivelseTxt.getText().toString()).put("itemreceived", modtagelsesDatoTxt.getText().toString())
                            .put("itemdatingfrom", dateringFraTxt.getText().toString()).put("itemdatingto", dateringTilTxt.getText().toString()).put("donator", refDonatorTxt.getText().toString()).put("producer",  refProducentTxt.getText().toString())
                            .put("postnummer", postNrTxt.getText().toString());
                    PostHTTP postHTTP = new PostHTTP(JSONitem, getActivity(), fm);
                    postHTTP.execute();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_photo: //Der blev trykket på kamera ikonet
                getFragmentManager().beginTransaction().addToBackStack(null).
                        replace(R.id.fragmentContainer, new NewItemPicturesFragment())
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
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top
        View root = inflater.inflate(R.layout.newitemdetailslayout, container, false);
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

        fm = getFragmentManager();

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
                DatePickerFragment newFragment = new DatePickerFragment(editText);
                newFragment.show(fm, "datePicker");
            }
        });
    }
}
