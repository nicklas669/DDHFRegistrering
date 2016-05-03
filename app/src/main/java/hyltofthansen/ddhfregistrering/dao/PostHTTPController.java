package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import hyltofthansen.ddhfregistrering.R;


/**
 * Class responsible for POST HTTP functionality to API 0.1 on CreateItem
 */
public class PostHTTPController extends AsyncTask {

    private static final String TAG = "PostHTTPController";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    private JSONObject item;
    private SharedPreferences prefs;
    private Fragment fragment;

    public PostHTTPController(JSONObject JSONitem, Activity context) {
        this.JSONitem = JSONitem;
        this.context = context;
    }


    @Override
    protected Integer doInBackground(Object[] params) {

        //Log.d(TAG, "item: "+JSONitem.toString());
        try {

            //Opretter POST URL
            try {
                String urlAPI = context.getString(R.string.API_URL);
                url = new URL(urlAPI);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // content type til API

            Uri.Builder builder = null;
            try {
                //Log.d(TAG, "description: "+JSONitem.getString("description"));
                //Log.d(TAG, "producer: "+JSONitem.getString("producer"));
                builder = new Uri.Builder()
                        .appendQueryParameter("token", "test")
                        .appendQueryParameter("headline", JSONitem.getString("headline"))
                        .appendQueryParameter("description", JSONitem.getString("description"))
                        .appendQueryParameter("donator", JSONitem.getString("donator"))
                        .appendQueryParameter("producer", JSONitem.getString("producer"))
                        .appendQueryParameter("zipcode", JSONitem.getString("zipcode"))
                        .appendQueryParameter("dating_to", JSONitem.getString("dating_to"))
                        .appendQueryParameter("dating_from", JSONitem.getString("dating_from"))
                        .appendQueryParameter("received_at", JSONitem.getString("received_at"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String query = builder.build().getEncodedQuery();
            Log.d(TAG, "query: "+query);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();


            responseCode = conn.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);


            // Læs response da vi skal bruge itemID for det nyoprettede item
           response = new StringBuffer();
           BufferedReader in = new BufferedReader(
               new InputStreamReader(conn.getInputStream()));
           String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Log.d(TAG, response.toString());

            in.close();
            conn.disconnect();

        }
        catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        }
        return responseCode;
    }


    @Override
    protected void onPostExecute(Object o) {
        boolean mediaAttached = false;
        // 1. Instantiate an AlertDialog.Builder with its constructor

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            responseCode = Integer.valueOf(o.toString());
            if (responseCode == 200) {
                //Check om der er valgt et billede
                prefs = PreferenceManager.getDefaultSharedPreferences(context);
                if (prefs.contains("chosenImage")) {
                    Log.d(TAG, "Der hører et billede til genstanden!!");
                    //Der er et billed, så det skal uploades efter genstanden er oprettet
                    try {
                        mediaAttached = true;
                        // Et item er oprettet og det gemmes som JSONObject ud fra response
                        Log.d(TAG, "response 2: " + response.toString());
                        JSONObject object = new JSONObject(response.toString());
                        JSONObject data = new JSONObject(object.getJSONObject(("data")).toString());
                        //JSONArray itemsfromDB = new JSONArray(data.getJSONArray("default").toString());
                        item = new JSONObject(data.getJSONObject("default").toString());
                        //Log.d(TAG, "item: "+item);
                        //item = new JSONObject(response.toString());
                        PostHTTPPicture postHTTPPicture = new PostHTTPPicture(context, item.getInt("id"));
                        postHTTPPicture.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (prefs.contains("recording")) {
                    Log.d(TAG, "Der hører en lydfil til genstanden!!");
                    try {
                        mediaAttached = true;
                        // Et item er oprettet og det gemmes som JSONObject ud fra response
                        Log.d(TAG, "response 2: " + response.toString());
                        item = new JSONObject(response.toString());
                        PostHTTPSound postHTTPSound = new PostHTTPSound(context, item.getInt("itemid"));
                        postHTTPSound.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (mediaAttached == false) {
                    builder.setMessage("Genstand oprettet. Responskode: " + responseCode).setTitle("Success")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Tilbage til hovedmenu
                                    context.onBackPressed();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                }
            } else {
                builder.setMessage("Fejl v. oprettelse af genstand. Responskode: " + responseCode)
                        .setTitle("Fejl");
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

