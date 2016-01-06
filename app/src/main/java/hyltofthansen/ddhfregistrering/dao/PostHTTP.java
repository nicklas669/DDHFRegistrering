package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;

/**
 * Class responsible for POST HTTP functionality to API 0.1 on CreateItem
 */
public class PostHTTP extends AsyncTask {

    private static final String TAG = "PostHTTP";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    FragmentManager fm;
    private JSONObject itemMedBilled;
    private SharedPreferences prefs;

    public PostHTTP(JSONObject JSONitem, Activity context, FragmentManager fm) {
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

            //Check om der blev taget fotografier
            prefs = context.getPreferences(Context.MODE_PRIVATE);
            if(prefs.contains("chosenImage")) {
                Log.d(TAG, "Der er et billed");
                //Der er et billed, så det bliver uploaded
                try {
                    itemMedBilled = new JSONObject(response.toString());
                    pictureUpload();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "Der er IKKE et billed");
            }

            // Ryd gemt billede fra app's data
            prefs.edit().remove("chosenImage").commit();

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

    private void pictureUpload() {

        try {
            //Opretter POST URL
            try {
                String urlAPI = null;
                try {
                    urlAPI = context.getString(R.string.API_URL_MATHIAS) + itemMedBilled.get("itemid").toString() + "?userID=56837dedd2d76438906140";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                url = new URL(urlAPI);
                System.out.println("URL: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/jpg"); // content type til Mathias' API


            OutputStream os = conn.getOutputStream();
            Log.d(TAG, String.valueOf(prefs.getString("chosenImage", null)));
            String filePath = String.valueOf(prefs.getString("chosenImage", null));

            filePath = filePath.replace("/file:", "");

            FileInputStream inputStream = new FileInputStream("/storage/sdcard0/Pictures/picture.jpg");


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

            // Ryd gemt billede fra app's data
            prefs.edit().remove("chosenImage").commit();

            in.close();
            conn.disconnect();

        }
        catch (UnsupportedEncodingException e) {
            Log.d(TAG,e.toString());
        } catch (ProtocolException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
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

    }
}

