package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dao.DeleteHTTP;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * ItemDetailInfoFragment is showing detailed information about a specific item which the user has clicked on
 */
public class ItemDetailInfoFragment extends Fragment {

    private static final String TAG = "ItemDetailInfoFragment";
    ItemDTO item;
    View root;
    ArrayList<ItemDTO> items;
    private ItemDTO itemFromExtra;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.itemdetailsinfolayout, container, false);
        item = getItemFromExtra();
        getActivity().setTitle(item.getItemheadline().toString());
        items = new ArrayList<ItemDTO>();
        GetHTTPDetails getHTTPDetails = new GetHTTPDetails(getActivity(), item.getItemid(), items, this);
        getHTTPDetails.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return root;
    }

    public void updateTextViews() {
        TextView tv_headline = (TextView) root.findViewById(R.id.tv_itemheadline);
        tv_headline.setText(item.getItemheadline().toString());

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

    public ItemDTO getItemFromExtra() {
        Bundle extras = getActivity().getIntent().getExtras();
                item = new ItemDTO(extras.getInt("itemid"),extras.getString("itemheadline"),extras.getString("itemdescription"),extras.getString("itemreceived")
                ,extras.getString("itemdatingfrom"),extras.getString("itemdatingto"),extras.getString("donator")
                ,extras.getString("producer"), extras.getInt("postnummer"));
        return item;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_item:   //Hvis man klikker på skraldespanden
                //Log.d(TAG, "Skraldespand kaldt fra " + TAG);
                Bundle extras = getActivity().getIntent().getExtras();
                //Log.d(TAG, "itemid: "+extras.getInt("itemid"));
                // TODO: Spørg her om man er sikker på at man vil slette!!
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setMessage("Er du sikker på at du vil slette genstanden?").setTitle("Verifikation");
                dialogBuilder.setPositiveButton("Ja", dialogClickListener);
                dialogBuilder.setNegativeButton("Nej", dialogClickListener);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            switch (id){
                case DialogInterface.BUTTON_POSITIVE:
                    Bundle extras = getActivity().getIntent().getExtras();
                    DeleteHTTP deleteHTTP = new DeleteHTTP(getActivity(), extras.getInt("itemid"));
                    deleteHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Log.d(TAG, "Der blev trykket nej!");
                    break;
            }
        }
    };
}
