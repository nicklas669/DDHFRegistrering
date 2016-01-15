package hyltofthansen.ddhfregistrering.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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
    private Menu mymenu;


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
        if (getHTTP.getStatus().equals(AsyncTask.Status.RUNNING)) {
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

        View root = inflater.inflate(R.layout.search_layout, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

        items = new ArrayList<ItemDTO>();

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og filtreres
        listAdapter = new CustomArrayAdapter(getActivity(),
                R.layout.search_row, R.id.search_tvheadline, items);

        fetchItemsFromAPI(items, this);

        lv.setAdapter(listAdapter);

        // ** Når der klikkes på en række i listen, åbnes en aktivitet der viser genstandens detaljer **
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seeItemDetails = new Intent(getActivity(), ItemDetailsActivity.class);
                item = listAdapter.getItem(position);
                seeItemDetails.putExtra("itemid", item.getItemid());
                seeItemDetails.putExtra("itemheadline", item.getItemheadline());
                seeItemDetails.putExtra("images", item.getImageURLLists());
                seeItemDetails.putStringArrayListExtra("images", item.getImageURLLists());
                Log.d(TAG, item.getImageURLLists().toString());
                Log.d(TAG, item.getItemheadline().toString());
                Log.d(TAG, "Sætter data i extra");
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
     *
     * @param items
     * @param searchItemFragment
     */
    public void fetchItemsFromAPI(ArrayList<ItemDTO> items,
                                  SearchItemFragment searchItemFragment) {
        if (getHTTP == null) {
            getHTTP = new GetHTTP(getActivity(), items, listAdapter, searchItemFragment);
            getHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        if (getHTTP.getStatus() == AsyncTask.Status.FINISHED) {
            getHTTP = new GetHTTP(getActivity(), items, listAdapter, searchItemFragment);
            getHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        Log.d(TAG, getHTTP.getStatus().toString() + " onStatus()");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mymenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_items: // Der er klikket på refresh knappen i toolbar
                //Log.d(TAG, "Trykket på refresh!");
                items = new ArrayList<ItemDTO>();
                fetchItemsFromAPI(items, this);

                // Do animation start
                LayoutInflater inflater = getLayoutInflater(getArguments());
                ImageView iv = (ImageView) inflater.inflate(R.layout.act_main_renewitemsbutton, null);
                Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.searchlist_refresh_rotate);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                item.setActionView(iv);

                Toast.makeText(getContext(), "Opdaterer listen..",
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stopRefreshingAnimation() {
        try {
            MenuItem m = mymenu.findItem(R.id.action_refresh_items);
            if (m.getActionView() != null) {
                // Remove the animation.
                m.getActionView().clearAnimation();
                m.setActionView(null);
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }

    }
}
