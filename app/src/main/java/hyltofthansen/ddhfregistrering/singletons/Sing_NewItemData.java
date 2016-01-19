package hyltofthansen.ddhfregistrering.singletons;

import android.app.Application;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

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

    private static Sing_NewItemData firstInstance = null;

    private Sing_NewItemData() {

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

    public JSONObject getJSONitem() {
        try {
           JSONitem = new JSONObject().put("itemheadline",
                   titelTxt.getText().toString()).put("itemdescription",
                beskrivelseTxt.getText().toString()).put("itemreceived",
                   modtagelsesDatoTxt.getText().toString()).put("itemdatingfrom",
                   dateringFraTxt.getText().toString()).put("itemdatingto",
                dateringTilTxt.getText().toString()).put("donator",
                refDonatorTxt.getText().toString()).put("producer",
                refProducentTxt.getText().toString())
                .put("postnummer", postNrTxt.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JSONitem;
    }

    public EditText getTitelTxt() {
        return titelTxt;
    }
}
