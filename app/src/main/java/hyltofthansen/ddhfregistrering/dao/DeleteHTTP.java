package hyltofthansen.ddhfregistrering.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * Class responsible for GET HTTP functionality to API
 * Deletes a JSON object from the /items/ URL
 */

public class DeleteHTTP extends AsyncTask {

    private Context context;
    private int itemid;
    private static final String TAG = "DeleteHTTP";


    public DeleteHTTP(Context context, int itemid) {
        this.context = context;
        this.itemid = itemid;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        int response = -1;
        // URL til Mathias' API
        URL url = null;
        try {
            url = new URL(context.getString(R.string.API_URL_MATHIAS)+itemid+"?userID=56837dedd2d76438906140");
            Log.d(TAG, "Delete url: "+url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setRequestMethod("DELETE");

            response = httpURLConnection.getResponseCode();
            Log.d(TAG, String.valueOf(response));
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        int response = (int) o;
        //Log.d(TAG, String.valueOf(items.size() + " Item size"));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (response == 200) {
            builder.setMessage("Genstand slettet. Responskode: " + response)
                    .setTitle("Success");
        } else {
            builder.setMessage("Der skete en fejl. Responskode: " + response)
                    .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
        //super.onPostExecute(o);
    }

    public void fetchItems() {
        this.execute();
    }
}
