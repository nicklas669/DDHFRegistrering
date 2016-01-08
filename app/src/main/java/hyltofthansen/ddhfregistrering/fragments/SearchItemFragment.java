package hyltofthansen.ddhfregistrering.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.CustomArrayAdapter;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.R;

public class SearchItemFragment extends Fragment {

    private ListView lv;
    private CustomArrayAdapter listAdapter;
    private GetHTTP getHTTP;
    private ArrayList<ItemDTO> items;
    private EditText inputSearch;
    private static final String TAG = "SearchItemFragment";


    @Override
    public void onResume() {
        //Et midlertidigt fix for blankt søgeresultat efter back button
        //TODO Gør så søgeresultat stadig gemmes
        if (inputSearch != null) {
            inputSearch.setText("");
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "OnCreateView kørt");

        items = new ArrayList<ItemDTO>();

        getActivity().setTitle("DDHF Registering");
        View root = inflater.inflate(R.layout.searchitem, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og søges i
        Log.d(TAG, "CustomArrayAdapter initialiseres!!");
        listAdapter = new CustomArrayAdapter(getActivity(), R.layout.searchlist, R.id.search_tvheadline, items);

        //Henter items ned fra DB
        Log.d(TAG, "Items hentes fra DB!!");
        getHTTP = new GetHTTP(getActivity(), items, listAdapter);
        getHTTP.fetchItems();

        Log.d(TAG, "Listview adapter sættes!!");
        lv.setAdapter(listAdapter);

        // ** Når der klikkes på en række i listen, åbnes et fragment der viser genstandens detaljer **
        Log.d(TAG, "Listview clickListener sættes!!");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDetailsFragment itemfragment = new ItemDetailsFragment();
                itemfragment.setItem(listAdapter.getItem(position));

                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentContainer, itemfragment);
                ft.commit();
            }
        });

        Log.d(TAG, "inputSearch textChangeDListener sættes!!");
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                listAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });
        return root;
    }
}
