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
public class ItemDetailInfoFragment extends Fragment {

    ItemDTO item;
    View root;
    ArrayList<ItemDTO> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.itemdetailsinfolayout, container, false);
        getActivity().setTitle(item.getItemheadline().toString()); //FRÆKT AT SÆTTE GENSTANDSNAVN HER
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

        TextView tv_received = (TextView) root.findViewById(R.id.tv_receivedDate);
        tv_received.setText(items.get(0).getItemreceived().toString());

        TextView tv_datingFrom = (TextView) root.findViewById(R.id.tv_datingFrom);
        tv_datingFrom.setText(items.get(0).getItemdatingfrom().toString());

        TextView tv_datingTo = (TextView) root.findViewById(R.id.tv_datingTo);
        tv_datingTo.setText(items.get(0).getItemdatingfrom().toString());

        TextView tv_donator = (TextView) root.findViewById(R.id.tv_donator);
        tv_donator.setText(items.get(0).getDonator().toString());

        TextView tv_producer = (TextView) root.findViewById(R.id.tv_producer);
        tv_producer.setText(items.get(0).getProducer().toString());

        TextView tv_postnr = (TextView) root.findViewById(R.id.tv_postnr);
        tv_postnr.setText(String.valueOf(items.get(0).getpostnummer()));


    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }
}
