package hyltofthansen.ddhfregistrering.fragments.newitemfragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.Override;import java.lang.String;

import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.fragments.Frag_DatePicker;

public class Frag_NewItemInfo extends Fragment {

    private ListView lv;
    private BaseAdapter listAdapter;
    private ImageView imageView;
    private EditText titelTxt, beskrivelseTxt,
            modtagelsesDatoTxt, dateringFraTxt,
            dateringTilTxt, refDonatorTxt, refProducentTxt, postNrTxt;
    private static final String TAG = "Frag_NewItemInfo";
    private android.support.v4.app.FragmentManager fm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Registrer genstand");
        //Aktiver ActionBar menu med Opret knap
        setHasOptionsMenu(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top
        View root = inflater.inflate(R.layout.fr_newitem_info, container, false);
        titelTxt = (EditText) root.findViewById(R.id.titelTextEdit);
        beskrivelseTxt = (EditText) root.findViewById(R.id.beskrivelseTextEdit);
        refDonatorTxt = (EditText) root.findViewById(R.id.refDonatorTextEdit);
        refProducentTxt = (EditText) root.findViewById(R.id.refproducent);
        modtagelsesDatoTxt = (EditText) root.findViewById(R.id.modtagelsesDatoEditText);
        dateringFraTxt = (EditText) root.findViewById(R.id.dateringfraeditText);
        dateringTilTxt = (EditText) root.findViewById(R.id.dateringtileditText);
        postNrTxt = (EditText) root.findViewById(R.id.postnr);

        Sing_NewItemData.getInstance().setTitelTxt(titelTxt);
        Sing_NewItemData.getInstance().setBeskrivelseTxt(beskrivelseTxt);
        Sing_NewItemData.getInstance().setRefDonatorTxt(refDonatorTxt);
        Sing_NewItemData.getInstance().setRefProducentTxt(refProducentTxt);
        Sing_NewItemData.getInstance().setModtagelsesDatoTxt(modtagelsesDatoTxt);
        Sing_NewItemData.getInstance().setDateringFraTxt(dateringFraTxt);
        Sing_NewItemData.getInstance().setDateringTilTxt(dateringTilTxt);
        Sing_NewItemData.getInstance().setPostNrTxt(postNrTxt);

        setEditTextDatePicker(modtagelsesDatoTxt);
        setEditTextDatePicker(dateringFraTxt);
        setEditTextDatePicker(dateringTilTxt);

        fm = getFragmentManager();

        return root;
    }

    private void setEditTextDatePicker(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Frag_DatePicker newFragment = new Frag_DatePicker(editText);
                newFragment.show(fm, "datePicker");
            }
        });
    }
}
