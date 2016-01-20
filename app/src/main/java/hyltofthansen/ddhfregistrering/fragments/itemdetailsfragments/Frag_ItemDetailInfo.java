package hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;
import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dao.PostHTTPEdit;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;
import hyltofthansen.ddhfregistrering.fragments.Frag_DatePicker;

/**
 * Frag_ItemDetailInfo is showing detailed information about a specific item which the user has clicked on
 */
public class Frag_ItemDetailInfo extends Fragment {

    private static final String TAG = "Frag_ItemDetailInfo";
    DTO_Item itemObject;
    View root;
    private EditText et_headline, et_descript, et_receiveDate, et_datingFrom, et_datingTo,
            et_donator, et_producer, et_zip;
    private TextView tv_itemid;
    private boolean editing = false;
    private JSONObject JSONitem;

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem checkMark = menu.findItem(R.id.action_checkmark);
        MenuItem editPencil = menu.findItem(R.id.action_edit_item);
        MenuItem garbageCan = menu.findItem(R.id.action_delete_item);
        MenuItem undo = menu.findItem(R.id.action_undo);

        if(checkMark != null && editPencil != null && garbageCan != null) {
            if (editing) {
                undo.setVisible(true);
                editPencil.setVisible(false);
                checkMark.setVisible(true);
                garbageCan.setVisible(false);
            } else {
                undo.setVisible(false);
                editPencil.setVisible(true);
                checkMark.setVisible(false);
                garbageCan.setVisible(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        root = inflater.inflate(R.layout.fr_itemdetails_info, container, false);
        itemObject = Sing_AsyncTasks.getInstance().getClickedItem();
        getActivity().setTitle(itemObject.getItemheadline().toString());

        Sing_AsyncTasks.getInstance().getItemDetails(getActivity(), itemObject.getItemid(), this);

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
        et_headline.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_descript.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_receiveDate.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_datingFrom.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_datingTo.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_donator.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_producer.setTextColor(getResources().getColor(R.color.editTextBlack));
        et_zip.setTextColor(getResources().getColor(R.color.editTextBlack));



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
                Frag_DatePicker newFragment = new Frag_DatePicker(editText);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
    }

    public void updateEditViews() {
        itemObject = Sing_AsyncTasks.getInstance().getClickedItem();
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

    public void setItem(DTO_Item itemObject) {
        this.itemObject = itemObject;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_item:
                // TODO: Skift "blyant" ud med "done"-tegn i toolbar/actionbar og fjern skraldespand
                if (!editing) {
                    Log.d(TAG, "enabling edit texts!");
                    enableEditTexts();
                    editing = true;
                    getActivity().invalidateOptionsMenu();
                } else {

                }
                return true;
            case R.id.action_checkmark:
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
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_undo:
                Log.d(TAG, "Undo");
                editing = false;
                updateEditViews();
                getActivity().invalidateOptionsMenu();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createJSONItem() {
        Sing_NewItemData.getInstance().setTitelTxt(et_headline);
        Sing_NewItemData.getInstance().setBeskrivelseTxt(et_descript);
        Sing_NewItemData.getInstance().setModtagelsesDatoTxt(et_receiveDate);
        Sing_NewItemData.getInstance().setDateringFraTxt(et_datingFrom);
        Sing_NewItemData.getInstance().setDateringTilTxt(et_datingTo);
        Sing_NewItemData.getInstance().setRefDonatorTxt(et_donator);
        Sing_NewItemData.getInstance().setRefProducentTxt(et_producer);
        Sing_NewItemData.getInstance().setPostNrTxt(et_zip);
        JSONitem = Sing_NewItemData.getInstance().getJSONitem();
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

//    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int id) {
//            switch (id){
//                case DialogInterface.BUTTON_POSITIVE:
//                    Sing_AsyncTasks.getInstance().deleteHTTP(getActivity());
//                    break;
//
//                case DialogInterface.BUTTON_NEGATIVE:
//                    //No button clicked
//                    Log.d(TAG, "Der blev trykket nej!");
//                    break;
//            }
//        }
//    };
}
