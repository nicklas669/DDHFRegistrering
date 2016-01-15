package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
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
public class PostHTTPController extends AsyncTask {

    private static final String TAG = "PostHTTPController";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    private JSONObject item;
    private SharedPreferences prefs;
    private android.support.v4.app.FragmentManager fm;


    public PostHTTPController(JSONObject JSONitem, Activity context, FragmentManager fm) {
        this.JSONitem = JSONitem;
        this.context = context;
        this.fm = fm;
    }


    @Override
    protected Integer doInBackground(Object[] params) {

        try {

            //Opretter POST URL
            try {
                String urlAPI = context.getString(R.string.API_URL_MATHIAS)+"?userID=56837dedd2d76438906140";
                url = new URL(urlAPI);
                System.out.println("URL: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json"); // content type til Mathias' API

            OutputStream os = conn.getOutputStream();
            os.write(JSONitem.toString().getBytes());
            os.flush();
            os.close();

            responseCode = conn.getResponseCode();
            String responseMsg = "PostHTTPController.java - Response Code: " + responseCode;
            Log.d(TAG, responseMsg);


            // Evt. læse svaret men ved ikke om vi har brug for andet end response code?
           response = new StringBuffer();
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
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseCode;
    }


    @Override
    protected void onPostExecute(Object o) {
        boolean mediaAttached = false;
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        responseCode = Integer.valueOf(o.toString());
        //int responseCode = 201;
        if (responseCode == 201) {
            //Check om der er valgt et billede
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs.contains("chosenImage")) {
                Log.d(TAG, "Der hører et billede til genstanden!!");
                //Der er et billed, så det skal uploades efter genstanden er oprettet
                try {
                    mediaAttached = true;
                    // Et item er oprettet og det gemmes som JSONObject ud fra response
                    Log.d(TAG, "response 2: " + response.toString());
                    item = new JSONObject(response.toString());
                    PostHTTPPicture postHTTPPicture = new PostHTTPPicture(context, fm, item.getInt("itemid"));
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
                    PostHTTPSound postHTTPSound = new PostHTTPSound(context, fm, item.getInt("itemid"));
                    postHTTPSound.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (mediaAttached == false) {
                Log.d(TAG, "Der hører IKKE et billede eller lyd? til genstanden!!");
                builder.setMessage("Genstand oprettet. Responskode: " + responseCode)
                        .setTitle("Success");
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }

//            }
            //Gå tilbage til hovedmenu her
            //fm.popBackStack();
        } else {
            builder.setMessage("Fejl v. oprettelse af genstand. Responskode: " + responseCode)
                    .setTitle("Fejl");
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }
    }
}

