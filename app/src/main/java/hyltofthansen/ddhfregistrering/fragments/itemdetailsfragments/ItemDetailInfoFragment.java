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
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.FragmentDataSingleton;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.dao.DeleteHTTP;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dao.PostHTTPEdit;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.DatePickerFragment;

/**
 * ItemDetailInfoFragment is showing detailed information about a specific item which the user has clicked on
 */
public class ItemDetailInfoFragment extends Fragment {

    private static final String TAG = "ItemDetailInfoFragment";
    ItemDTO itemObject;
    View root;
    private EditText et_headline, et_descript, et_receiveDate, et_datingFrom, et_datingTo,
            et_donator, et_producer, et_zip;
    private TextView tv_itemid;
    private boolean editing = false;
    private JSONObject JSONitem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fr_itemdetails_info, container, false);
        itemObject = Singleton.getInstance().getClickedItem();
        getActivity().setTitle(itemObject.getItemheadline().toString());

        Singleton.getInstance().getItemDetails(getActivity(), itemObject.getItemid(), this);

        tv_itemid = (TextView) root.findViewById(R.id.itemdetails_tv_id);
        et_headline = (EditText) root.findViewById(R.id.itemdetails_TitleEdit);
        et_descript = (EditText) root.findViewById(R.id.itemdetails_DescripEdit);
        et_receiveDate = (EditText) root.findViewById(R.id.itemdetails_ReceiveEdit);
        et_datingFrom = (EditText) root.findViewById(R.id.itemdetails_DatingFromEdit);
        et_datingTo = (EditText) root.findViewById(R.id.itemdetails_DatingToEdit);
        et_donator = (EditText) root.findViewById(R.id.itemdetails_DonatorEdit);
        et_producer = (EditText) root.findViewById(R.id.itemdetails_ProdEdit);
        et_zip = (EditText) root.findViewById(R.id.itemdetails_ZipEdit);

        disableEditTexts();

        tv_itemid.setText("Genstand id: " + itemObject.getItemid());

        et_headline.setText(itemObject.getItemheadline());

        et_descript.setText(itemObject.getItemdescription());
        Log.d(TAG, itemObject.getItemdescription());

        if(!itemObject.getItemreceived().equals("null"))
            et_receiveDate.setText(itemObject.getItemreceived());

        if(!itemObject.getItemdatingfrom().equals("null") ||itemObject.getItemdatingfrom().equals("0000-00-00") )
            et_datingFrom.setText(itemObject.getItemdatingfrom());

        if(!itemObject.getItemdatingto().equals("null") ||itemObject.getItemdatingto().equals("0000-00-00"))
            et_datingTo.setText(itemObject.getItemdatingto());

        if(!itemObject.getDonator().equals("null"))
            et_donator.setText(itemObject.getDonator());

        if(!itemObject.getProducer().equals("null"))
            et_producer.setText(itemObject.getProducer());

        if(!itemObject.getpostnummer().equals("0"))
            et_zip.setText(itemObject.getpostnummer());

        setEditTextDatePicker(et_receiveDate);
        setEditTextDatePicker(et_datingFrom);
        setEditTextDatePicker(et_datingTo);

        return root;
    }

    private void setEditTextDatePicker(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment(editText);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void updateEditViews() {
        itemObject = Singleton.getInstance().getClickedItem();
        Log.d(TAG, "updateEditView");

        disableEditTexts();

        et_headline.setText(itemObject.getItemheadline().toString());

        et_descript.setText(itemObject.getItemdescription().toString());
        Log.d(TAG, itemObject.getItemdescription().toString());

        if(!itemObject.getItemreceived().equals("null"))
            et_receiveDate.setText(itemObject.getItemreceived().toString());

        if(!itemObject.getItemdatingfrom().equals("null") ||itemObject.getItemdatingfrom().equals("0000-00-00") )
            et_datingFrom.setText(itemObject.getItemdatingfrom().toString());

        if(!itemObject.getItemdatingto().equals("null") ||itemObject.getItemdatingto().equals("0000-00-00"))
            et_datingTo.setText(itemObject.getItemdatingto().toString());

        if(!itemObject.getDonator().equals("null"))
            et_donator.setText(itemObject.getDonator().toString());

        if(!itemObject.getProducer().equals("null"))
            et_producer.setText(itemObject.getProducer().toString());

        if(!itemObject.getpostnummer().equals("0"))
            et_zip.setText(itemObject.getpostnummer());
    }

    public void setItem(ItemDTO itemObject) {
        this.itemObject = itemObject;
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
                // TODO: Skift "blyant" ud med "done"-tegn i toolbar/actionbar og fjern skraldespand
                if (!editing) {
                    Log.d(TAG, "enabling edit texts!");
                    enableEditTexts();
                    editing = true;
                } else {
                    Log.d(TAG, "Opdaterer genstand!");
                    if (et_headline.getText().toString().trim().equals("")) {
                        et_headline.setError("Indtast en titel!");
                        et_headline.requestFocus();
                        return false;
                    }
                    Log.d(TAG, "createJSONItem() start");
                    createJSONItem();
                    Log.d(TAG, "createJSONItem() slut");
                    PostHTTPEdit postHTTPEdit = new PostHTTPEdit(getActivity(), itemObject.getItemid(), JSONitem);
                    postHTTPEdit.execute();
                    disableEditTexts();
                    editing = false;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createJSONItem() {
        FragmentDataSingleton.getInstance().setTitelTxt(et_headline);
        FragmentDataSingleton.getInstance().setBeskrivelseTxt(et_descript);
        FragmentDataSingleton.getInstance().setModtagelsesDatoTxt(et_receiveDate);
        FragmentDataSingleton.getInstance().setDateringFraTxt(et_datingFrom);
        FragmentDataSingleton.getInstance().setDateringTilTxt(et_datingTo);
        FragmentDataSingleton.getInstance().setRefDonatorTxt(et_donator);
        FragmentDataSingleton.getInstance().setRefProducentTxt(et_producer);
        FragmentDataSingleton.getInstance().setPostNrTxt(et_zip);
        JSONitem = FragmentDataSingleton.getInstance().getJSONitem();
    }

    /**
     * Disables all the edittexts, making them editable.
     */
    private void disableEditTexts() {
        et_headline.setEnabled(false);
        et_descript.setEnabled(false);
        et_receiveDate.setEnabled(false);
        et_datingFrom.setEnabled(false);
        et_datingTo.setEnabled(false);
        et_donator.setEnabled(false);
        et_producer.setEnabled(false);
        et_zip.setEnabled(false);
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
                    Singleton.getInstance().deleteHTTP(getActivity());
//                    DeleteHTTP deleteHTTP = new DeleteHTTP(getActivity(), Singleton.getInstance().getClickedItem().getItemid());
//                    deleteHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Log.d(TAG, "Der blev trykket nej!");
                    break;
            }
        }
    };
}
