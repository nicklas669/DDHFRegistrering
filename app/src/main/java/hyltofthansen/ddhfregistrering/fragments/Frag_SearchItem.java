package hyltofthansen.ddhfregistrering.fragments;

import android.content.Intent;
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

import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;
import hyltofthansen.ddhfregistrering.activities.Act_ItemDetails;
import hyltofthansen.ddhfregistrering.adapters.Adapter_SearchList;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;
import hyltofthansen.ddhfregistrering.R;

public class Frag_SearchItem extends Fragment {

    private ListView lv;
    private Adapter_SearchList listAdapter;
    private GetHTTP getHTTP;
    private ArrayList<DTO_Item> items;
    private EditText inputSearch;
    private static final String TAG = "Frag_SearchItem";
    private DTO_Item item;
    private Intent itemDetails;
    private Menu mymenu;
    private Sing_AsyncTasks singAsyncTasks;


    @Override
    public void onResume() {
        //Log.d(TAG, "onResume()!!");
        //Et midlertidigt fix for blankt søgeresultat efter back button
        //TODO: Gør så søgeresultat stadig gemmes
        if (inputSearch != null) {
            inputSearch.setText("");
        }
        Sing_AsyncTasks.getInstance().fetchItemsFromAPI();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        Log.d(TAG, "onCreateView køres!");
        singAsyncTasks = Sing_AsyncTasks.getInstance();

        View root = inflater.inflate(R.layout.search_layout, container, false);

        lv = (ListView) root.findViewById(R.id.search_lv);
        inputSearch = (EditText) root.findViewById(R.id.inputSearch);

//        items = new ArrayList<DTO_Item>();
        items = singAsyncTasks.getInstance().getItems();

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises og filtreres
        listAdapter = new Adapter_SearchList(getActivity(),
                R.layout.search_row, R.id.search_tvheadline, items);

        lv.setAdapter(listAdapter);

//        singAsyncTasks.fetchItemsFromAPI(getActivity(), listAdapter, this);
        Sing_AsyncTasks.getInstance().setSearchFragment(this);
        Sing_AsyncTasks.getInstance().setSearchListAdapter(listAdapter);
        Sing_AsyncTasks.getInstance().fetchItemsFromAPI();



        // ** Når der klikkes på en række i listen, åbnes en aktivitet der viser genstandens detaljer **
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemDetails = new Intent(getActivity(), Act_ItemDetails.class);
                item = listAdapter.getItem(position);
                Sing_AsyncTasks.getInstance().setClickedItem(item);
                Log.d(TAG, "onClick " + item.toString());
                startActivity(itemDetails);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mymenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_refresh_items: // Der er klikket på refresh knappen i toolbar
                //Log.d(TAG, "Trykket på refresh!");
//                singAsyncTasks.fetchItemsFromAPI(getActivity(), listAdapter, this);
                singAsyncTasks.fetchItemsFromAPI();

                // Do animation start
                LayoutInflater inflater = getLayoutInflater(getArguments());
                ImageView iv = (ImageView) inflater.inflate(R.layout.act_main_renewitemsbutton, null);
                Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.searchlist_refresh_rotate);
                rotation.setRepeatCount(Animation.INFINITE);
                iv.startAnimation(rotation);
                menuItem.setActionView(iv);

                Toast.makeText(getContext(), "Opdaterer listen..",
                        Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
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
