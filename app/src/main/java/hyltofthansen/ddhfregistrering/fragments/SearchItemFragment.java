package hyltofthansen.ddhfregistrering.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
    // Search EditText
    private EditText inputSearch;
    private static final String TAG = "SearchItemFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        items = new ArrayList<ItemDTO>();

        getActivity().setTitle("DDHF Registering");
        View root = inflater.inflate(R.layout.searchitem, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og søges i
        listAdapter = new CustomArrayAdapter(getActivity(), R.layout.searchlist, R.id.search_tvheadline, items);
        lv.setAdapter(listAdapter);

        getHTTP = new GetHTTP(getActivity(), items, listAdapter);
        getHTTP.fetchItems();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemDetailsFragment itemfragment = new ItemDetailsFragment();
                itemfragment.setItem(listAdapter.getItem(position));

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragmentContainer, itemfragment);
                ft.commit();
            }
        });


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
