package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import hyltofthansen.ddhfregistrering.R;

/**
 * Class responsible for POST HTTP functionality to API 0.1 on CreateItem
 */
public class PostHTTPPicture extends AsyncTask {

    private static final String TAG = "PostHTTP";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    private JSONObject itemMedBilled;
    private SharedPreferences prefs;
    private int itemid;
    private android.support.v4.app.FragmentManager fm;


    public PostHTTPPicture(Activity context, android.support.v4.app.FragmentManager fm, int itemid) {
//        this.JSONitem = JSONitem;
        this.context = context;
        this.fm = fm;
        this.itemid = itemid;
    }


    @Override
    protected Integer doInBackground(Object[] params) {
        try {
            //Opretter POST URL
            try {
                String urlAPI = null;
                urlAPI = context.getString(R.string.API_URL_MATHIAS) + itemid + "?userID=56837dedd2d76438906140";
                url = new URL(urlAPI);
                System.out.println("URL: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpg"); // content type til Mathias' API

//            Log.d(TAG, String.valueOf(prefs.getString("chosenImage", null)));
//            String filePath = String.valueOf(prefs.getString("chosenImage", null));
            prefs = context.getPreferences(Context.MODE_PRIVATE);
            String imgPath = prefs.getString("chosenImage", null);
            Log.d(TAG, imgPath);

            OutputStream os = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(imgPath);

            byte[] data = new byte[1024];
            int read;

            while((read = inputStream.read(data)) != -1) {
                os.write(data,0,read);
            }
            inputStream.close();
            os.flush();
            os.close();

            responseCode = conn.getResponseCode();
            String responseMsg = "PostHTTP.java - Response Code: " + responseCode;
            Log.d(TAG, responseMsg);


            // Evt. læse svaret men ved ikke om vi har brug for andet end response code?
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
        catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString());
        } catch (ProtocolException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
        return responseCode;
    }



    @Override
    protected void onPostExecute(Object o) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        responseCode = Integer.valueOf(o.toString());
        //int responseCode = 201;
        if (responseCode == 201) {
            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Genstand oprettet successfuldt. Responskode: " + responseCode)
                    .setTitle("Success");
            //Gå tilbage til hovedmenu her
            fm.popBackStack();
        } else {
            builder.setMessage("Fejl ved oprettelse. Responskode: " + responseCode)
                    .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        // Ryd gemt billede fra app's data
        prefs.edit().remove("chosenImage").commit();
    }
}

