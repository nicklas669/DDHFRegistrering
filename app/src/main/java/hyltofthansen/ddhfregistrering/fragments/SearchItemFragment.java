package hyltofthansen.ddhfregistrering.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
        //Log.d(TAG, "onResume()!!");
        //Et midlertidigt fix for blankt søgeresultat efter back button
        //TODO: Gør så søgeresultat stadig gemmes
        if (inputSearch != null) {
            inputSearch.setText("");
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if(getHTTP.getStatus().equals(AsyncTask.Status.RUNNING)) {
            getHTTP.cancel(true);
            Log.d(TAG, "getHTTP køres fra SearchFragment");
        }
        Log.d(TAG, "Fragment er paused");
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreateView køres!");
        getActivity().setTitle("DDHF Registering");

        View root = inflater.inflate(R.layout.searchitemfragmentlayout, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        items = new ArrayList<ItemDTO>();

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og filtreres
        listAdapter = new CustomArrayAdapter(getActivity(), R.layout.searchlist_rowlayout, R.id.search_tvheadline, items);

        fetchItemsFromAPI(items);

        lv.setAdapter(listAdapter);

        // ** Når der klikkes på en række i listen, åbnes en aktivitet der viser genstandens detaljer **
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seeItemDetails = new Intent(getActivity(), ItemDetailsActivity.class);
                item = listAdapter.getItem(position);
                seeItemDetails.putExtra("itemid", item.getItemid());
                seeItemDetails.putExtra("itemheadline", item.getItemheadline());
                startActivity(seeItemDetails);
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

    /**
     * Henter items ned fra DB og gemmer dem i items listen
     * @param items
     */

    public void fetchItemsFromAPI(ArrayList<ItemDTO> items) {
        getHTTP = new GetHTTP(getActivity(), items, listAdapter);
        getHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_items: // Der er klikket på refresh knappen i toolbar
                //Log.d(TAG, "Trykket på refresh!");
                items = new ArrayList<ItemDTO>();
                fetchItemsFromAPI(items);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
