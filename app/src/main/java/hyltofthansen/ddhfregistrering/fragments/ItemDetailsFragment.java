package hyltofthansen.ddhfregistrering.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * Created by Nicklas on 04-01-2016.
 */
public class ItemDetailsFragment extends Fragment {

    ItemDTO item;
    View root;
    ArrayList<ItemDTO> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.itemdetails, container, false);
        getActivity().setTitle(item.getItemheadline().toString()); // TODO: FRÆKT AT SÆTTE GENSTANDSNAVN HER
        items = new ArrayList<ItemDTO>();
        GetHTTPDetails getHTTPDetails = new GetHTTPDetails(getActivity(), item.getItemid(), items, this);
        getHTTPDetails.fetchItems();
        return root;
    }

    public void updateTextViews() {
        TextView tv_headline = (TextView) root.findViewById(R.id.tv_itemheadline);
        tv_headline.setText(items.get(0).getItemheadline().toString());

        TextView tv_descript = (TextView) root.findViewById(R.id.tv_itemdescr);
        tv_descript.setText(items.get(0).getItemdescription().toString());
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }
}
