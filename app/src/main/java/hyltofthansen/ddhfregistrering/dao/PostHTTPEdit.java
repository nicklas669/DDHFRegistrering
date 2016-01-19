package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hyltofthansen.ddhfregistrering.R;

/**
 * Class responsible for POST HTTP editing an item's details
 */
public class PostHTTPEdit extends AsyncTask {

    private static final String TAG = "PostHTTPEdit";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    private JSONObject itemMedBilled;
    private SharedPreferences prefs;
    private int itemid;

    public PostHTTPEdit(Activity context, int itemid, JSONObject JSONitem) {
        this.context = context;
        this.itemid = itemid;
        //Log.d(TAG, "itemid er: "+itemid);
        this.JSONitem = JSONitem;
    }


    @Override
    protected Integer doInBackground(Object[] params) {
        try {
            //Opretter POST URL
            try {
                String urlAPI = null;
                urlAPI = context.getString(R.string.API_URL_MATHIAS) + itemid + "?userID=56837dedd2d76438906140";
                url = new URL(urlAPI);
                Log.d(TAG, "URL til at ændre item: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = conn.getOutputStream();
            os.write(JSONitem.toString().getBytes());
            os.flush();
            os.close();

            responseCode = conn.getResponseCode();
            String responseMsg = "PostHTTPEdit - Response Code: " + responseCode;
            Log.d(TAG, responseMsg);


            StringBuffer response = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            Log.d(TAG, response.toString());

            in.close();
            conn.disconnect();

        }
        catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return responseCode;
    }

    @Override
    protected void onPostExecute(Object o) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        responseCode = Integer.valueOf(o.toString());
        if (responseCode == 200) {
                builder.setMessage("Genstand ændret. Responskode: " + responseCode).setTitle("Success")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Tilbage til hovedmenu
                                context.onBackPressed();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
        } else {
            builder.setMessage("Der skete en fejl ved ændring af genstand. Responskode: " + responseCode)
                    .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

