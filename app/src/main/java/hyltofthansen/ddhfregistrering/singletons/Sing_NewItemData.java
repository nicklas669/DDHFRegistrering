package hyltofthansen.ddhfregistrering.singletons;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Sing_AsyncTasks responsible for sharing data between Fragments in Swipeviews.
 */
public class Sing_NewItemData extends Application {
    private static final String TAG = "Sing_NewItemData";
    private JSONObject JSONitem;

    private EditText titelTxt;
    private EditText beskrivelseTxt;
    private EditText modtagelsesDatoTxt;
    private EditText dateringFraTxt;
    private EditText dateringTilTxt;
    private EditText refDonatorTxt;
    private EditText refProducentTxt;
    private EditText postNrTxt;
    private ArrayList<String> photoFilePathList;

    private static Sing_NewItemData firstInstance = null;

    private Sing_NewItemData() {
        photoFilePathList = new ArrayList<>();
    }

    public void addPhotoFilePath(String photoFilePath) {
        photoFilePathList.add(photoFilePath);
    }

    public static Sing_NewItemData getInstance() {
        if (firstInstance == null) {
            firstInstance = new Sing_NewItemData();
        }
        return firstInstance;
    }

    public void setPostNrTxt(EditText postNrTxt) {
        this.postNrTxt = postNrTxt;
    }

    public void setTitelTxt(EditText titelTxt) {
        this.titelTxt = titelTxt;
    }

    public void setBeskrivelseTxt(EditText beskrivelseTxt) {
        this.beskrivelseTxt = beskrivelseTxt;
    }

    public void setModtagelsesDatoTxt(EditText modtagelsesDatoTxt) {
        this.modtagelsesDatoTxt = modtagelsesDatoTxt;
    }

    public void setDateringFraTxt(EditText dateringFraTxt) {
        this.dateringFraTxt = dateringFraTxt;
    }

    public void setDateringTilTxt(EditText dateringTilTxt) {
        this.dateringTilTxt = dateringTilTxt;
    }

    public void setRefDonatorTxt(EditText refDonatorTxt) {
        this.refDonatorTxt = refDonatorTxt;
    }

    public void setRefProducentTxt(EditText refProducentTxt) {
        this.refProducentTxt = refProducentTxt;
    }

    public void deletePhotoFilePathItem(int position) {
        photoFilePathList.remove(position);
    }

    public JSONObject getJSONitem() {
        try {
            boolean receiveDateSet = false,
                    dateFromSet = false,
                    dateToSet = false;
            long longReceived = 0, longFrom = 0, longTo = 0;
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            if (!modtagelsesDatoTxt.getText().toString().isEmpty() && modtagelsesDatoTxt.getText() != null) {
                Log.d(TAG, "modtagelsesDato: "+modtagelsesDatoTxt.getText().toString().split(" ")[0]);
                Date dateReceived = (Date) formatter.parse(modtagelsesDatoTxt.getText().toString().split(" ")[0]);
                longReceived = dateReceived.getTime()/1000;
                Log.d(TAG, "modtagelsesDato: "+longReceived);
                receiveDateSet = true;
            }

            if (!dateringFraTxt.getText().toString().isEmpty() && dateringFraTxt.getText() != null) {
                Log.d(TAG, "dateringFraDato: " + dateringFraTxt.getText().toString().split(" ")[0]);
                Date dateFrom = (Date) formatter.parse(dateringFraTxt.getText().toString().split(" ")[0]);
                longFrom = dateFrom.getTime() / 1000;
                Log.d(TAG, "dateringFra: " + longFrom);
                dateFromSet = true;
            }

            if (!dateringTilTxt.getText().toString().isEmpty() && dateringTilTxt.getText() != null) {
                Log.d(TAG, "dateringTil: " + dateringTilTxt.getText().toString().split(" ")[0]);
                Date dateTo = (Date) formatter.parse(dateringTilTxt.getText().toString().split(" ")[0]);
                longTo = dateTo.getTime() / 1000;
                Log.d(TAG, "dateringTilTxt: " + longTo);
                dateToSet = true;
            }

           JSONitem = new JSONObject()
                   .put("headline", titelTxt.getText().toString())
                   .put("description", beskrivelseTxt.getText().toString())
                   .put("donator", refDonatorTxt.getText().toString())
                   .put("producer", refProducentTxt.getText().toString())
                   .put("zipcode", postNrTxt.getText().toString());

            if (receiveDateSet) JSONitem.put("received_at", longReceived);
            if (dateFromSet) JSONitem.put("dating_from", longFrom);
            if (dateToSet) JSONitem.put("dating_to", longTo);


            Log.d(TAG, "getJSONitem: "+JSONitem);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return JSONitem;
    }

    public EditText getTitelTxt() {
        return titelTxt;
    }

    public ArrayList getPhotoFileList() {
        return photoFilePathList;
    }
}
