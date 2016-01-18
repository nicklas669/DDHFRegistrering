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
import android.widget.EditText;

import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
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
    private EditText et_headline, et_descript, et_receiveDate, et_datingFrom, et_datingTo,
            et_donator, et_producer, et_zip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fr_itemdetails_info, container, false);
        item = Singleton.getInstance().getClickedItem();
        getActivity().setTitle(item.getItemheadline().toString());

        Singleton.getInstance().getItemDetails(getActivity(), item.getItemid(), this);

        et_headline = (EditText) root.findViewById(R.id.itemdetails_TitleEdit);
        et_descript = (EditText) root.findViewById(R.id.itemdetails_DescripEdit);
        et_receiveDate = (EditText) root.findViewById(R.id.itemdetails_ReceiveEdit);
        et_datingFrom = (EditText) root.findViewById(R.id.itemdetails_DatingFromEdit);
        et_datingTo = (EditText) root.findViewById(R.id.itemdetails_DatingToEdit);
        et_donator = (EditText) root.findViewById(R.id.itemdetails_DonatorEdit);
        et_producer = (EditText) root.findViewById(R.id.itemdetails_ProdEdit);
        et_zip = (EditText) root.findViewById(R.id.itemdetails_ZipEdit);

        return root;
    }

    public void updateEditViews() {
        item = Singleton.getInstance().getClickedItem();
        Log.d(TAG, "updateEditView");
        et_headline.setEnabled(false);
        et_headline.setText(item.getItemheadline().toString());

        et_descript.setEnabled(false);
        et_descript.setText(item.getItemdescription().toString());
        Log.d(TAG, item.getItemdescription().toString());

        et_receiveDate.setEnabled(false);
        if(!item.getItemreceived().equals("null"))
            et_receiveDate.setText(item.getItemreceived().toString());


        et_datingFrom.setEnabled(false);
        if(!item.getItemdatingfrom().equals("null") ||item.getItemdatingfrom().equals("0000-00-00") )
            et_datingFrom.setText(item.getItemdatingfrom().toString());


        et_datingTo.setEnabled(false);
        if(!item.getItemdatingto().equals("null") ||item.getItemdatingto().equals("0000-00-00"))
            et_datingTo.setText(item.getItemdatingto().toString());

        et_donator.setEnabled(false);
        if(!item.getDonator().equals("null"))
            et_donator.setText(item.getDonator().toString());

        et_producer.setEnabled(false);
        if(!item.getProducer().equals("null"))
            et_producer.setText(item.getProducer().toString());

        et_zip.setEnabled(false);
        if(!item.getpostnummer().equals("0"))
            et_zip.setText(item.getpostnummer());
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_item:   //Hvis man klikker på skraldespanden
                //Log.d(TAG, "Skraldespand kaldt fra " + TAG);
                Bundle extras = getActivity().getIntent().getExtras();
                //Log.d(TAG, "itemid: "+extras.getInt("itemid"));
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setMessage("Er du sikker på at du vil slette genstanden?").setTitle("Verifikation");
                dialogBuilder.setPositiveButton("Ja", dialogClickListener);
                dialogBuilder.setNegativeButton("Nej", dialogClickListener);
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return true;
            case R.id.action_edit_item:
                // TODO: Skift "blyant" ud med "done"-tegn i toolbar/actionbar
                enableEditTexts();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Enables all the edittexts, making them editable.
     */
    private void enableEditTexts() {
        et_headline.setEnabled(true);
        et_descript.setEnabled(true);
        et_receiveDate.setEnabled(true);
        et_datingFrom.setEnabled(true);
        et_datingTo.setEnabled(true);
        et_donator.setEnabled(true);
        et_producer.setEnabled(true);
        et_zip.setEnabled(true);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            switch (id){
                case DialogInterface.BUTTON_POSITIVE:
                    DeleteHTTP deleteHTTP = new DeleteHTTP(getActivity(), Singleton.getInstance().getClickedItem().getItemid());
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
