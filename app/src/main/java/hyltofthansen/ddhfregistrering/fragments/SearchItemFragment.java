package hyltofthansen.ddhfregistrering.fragments;


import android.content.Intent;
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
import hyltofthansen.ddhfregistrering.activities.ItemDetailsActivity;
import hyltofthansen.ddhfregistrering.adapters.CustomArrayAdapter;
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
    private ItemDTO item;
    private Intent seeItemDetails;


    @Override
    public void onResume() {
        Log.d(TAG, "onResume()!!");
        //Et midlertidigt fix for blankt søgeresultat efter back button
        //TODO: Gør så søgeresultat stadig gemmes
        if (inputSearch != null) {
            inputSearch.setText("");
        }
        // Opdatér liste
        items = new ArrayList<ItemDTO>();
        fetchItemsFromAPI(items);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView køres!");
        getActivity().setTitle("DDHF Registering");

        View root = inflater.inflate(R.layout.searchitemfragmentlayout, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        items = new ArrayList<ItemDTO>();

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og filtreres
        listAdapter = new CustomArrayAdapter(getActivity(), R.layout.searchlist_rowlayout, R.id.search_tvheadline, items);

        //fetchItemsFromAPI(items);

        lv.setAdapter(listAdapter);

        // ** Når der klikkes på en række i listen, åbnes et fragment der viser genstandens detaljer **
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seeItemDetails = new Intent(getActivity(), ItemDetailsActivity.class);
                item = listAdapter.getItem(position);
                putInExtra();
                startActivity(seeItemDetails);
//                ItemDetailInfoFragment itemfragment = new ItemDetailInfoFragment();
//                itemfragment.setItem(listAdapter.getItem(position));
//
//                android.support.v4.app.FragmentManager fm = getFragmentManager();
//                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
//                ft.addToBackStack(null);
//                ft.replace(R.id.fragmentContainer, itemfragment);
//                ft.commit();
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

    private void putInExtra() {
        seeItemDetails.putExtra("itemid", item.getItemid());
        seeItemDetails.putExtra("itemheadline", item.getItemheadline());
//        seeItemDetails.putExtra("itemdescription", item.getItemdescription());
//        seeItemDetails.putExtra("itemreceived", item.getItemreceived());
//        seeItemDetails.putExtra("itemdatingfrom", item.getItemdatingfrom());
//        seeItemDetails.putExtra("itemdatingto", item.getItemdatingfrom());
//        seeItemDetails.putExtra("donator", item.getDonator());
//        seeItemDetails.putExtra("producer", item.getProducer());
//        seeItemDetails.putExtra("postnummer", item.getpostnummer());
    }

    /**
     * Henter items ned fra DB og gemmer dem i items listen
     * @param items
     */

    public void fetchItemsFromAPI(ArrayList<ItemDTO> items) {
        getHTTP = new GetHTTP(getActivity(), items, listAdapter);
        getHTTP.fetchItems();
    }
}
