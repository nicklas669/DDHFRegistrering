package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import hyltofthansen.ddhfregistrering.R;

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
        Log.d(TAG, "Trying to delete item: "+itemid);
        int response = -1;
        URL url = null;
        try {
            url = new URL(context.getString(R.string.API_URL)+itemid+"?token=test");
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

            StringBuffer responseBuffer = new StringBuffer();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
            Log.d(TAG, responseBuffer.toString());
            in.close();
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
        int responseCode = (int) o;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (responseCode == 200) {
            builder.setMessage("Genstand slettet. Responskode: " + responseCode).setTitle("Success")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Tilbage til hovedmenu
                            Activity activity = (Activity) context;
                            activity.onBackPressed();
                        }
                    });
        } else {
            builder.setMessage("Fejl ved sletning. Responskode: " + responseCode).setTitle("Fejl")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
