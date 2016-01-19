package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;

/**
 * Frag_ItemDetailInfo is showing detailed informaiton about a specific item which the user has clicked on
 */
public class Frag_ItemDetailSound extends Fragment {

    DTO_Item item;
    View root;
    ArrayList<DTO_Item> items;
    private Menu activityMenu;
    private MenuItem editMenuItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fr_itemdetails_picture, container, false);
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        activityMenu = menu;
        editMenuItem = activityMenu.findItem(R.id.action_edit_item);
        editMenuItem.setVisible(false);
    }
}
