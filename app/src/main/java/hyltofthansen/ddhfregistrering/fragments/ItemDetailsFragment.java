package hyltofthansen.ddhfregistrering.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * Created by Nicklas on 04-01-2016.
 */
public class ItemDetailsFragment extends Fragment {

    ItemDTO item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.itemdetails, container, false);
        getActivity().setTitle("Genstand detaljer"); // TODO: FRÆKT AT SÆTTE GENSTANDSNAVN HER
        TextView tv_headline = (TextView) root.findViewById(R.id.tv_itemheadline);
        tv_headline.setText(item.getItemheadline());

        TextView tv_descript = (TextView) root.findViewById(R.id.tv_itemdescr);
        tv_descript.setText(item.getItemdescription());

        TextView tv_received = (TextView) root.findViewById(R.id.tv_itemreceived);
        tv_received.setText(item.getItemreceived());

        return root;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }
}
