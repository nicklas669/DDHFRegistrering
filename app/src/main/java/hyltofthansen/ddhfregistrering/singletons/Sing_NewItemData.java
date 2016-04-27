package hyltofthansen.ddhfregistrering.singletons;

import android.app.Application;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Identity;
import java.util.ArrayList;

/**
 * Sing_AsyncTasks responsible for sharing data between Fragments in Swipeviews.
 */
public class Sing_NewItemData extends Application {

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
           JSONitem = new JSONObject()
                   .put("headline", titelTxt.getText().toString())
                    .put("description", beskrivelseTxt.getText().toString())
                    //.put("received_at", modtagelsesDatoTxt.getText().toString())
                    //.put("dating_from",dateringFraTxt.getText().toString())
                    //.put("dating_to", dateringTilTxt.getText().toString())
                    .put("donator", refDonatorTxt.getText().toString())
                    .put("producer", refProducentTxt.getText().toString())
                    .put("zipcode", postNrTxt.getText().toString());
        } catch (JSONException e) {
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
