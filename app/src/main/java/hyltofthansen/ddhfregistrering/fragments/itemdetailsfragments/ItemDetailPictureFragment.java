package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * ItemDetailInfoFragment is showing detailed informaiton about a specific item which the user has clicked on
 */
public class ItemDetailPictureFragment extends Fragment {

    ItemDTO item;
    View root;
    ArrayList<ItemDTO> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.itemdetailspicturelayout, container, false);
        return root;
    }
}
