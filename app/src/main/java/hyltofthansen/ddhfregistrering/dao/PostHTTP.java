package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;
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

    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    FragmentManager fm;

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
                //String urlAPI = context.getString(R.string.API_URL);
                String urlAPI = context.getString(R.string.API_URL_MATHIAS)+"?userID=56837dedd2d76438906140";
                url = new URL(urlAPI);
                System.out.println("URL: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Type", "application/json"); // content type til Mathias' API
            //conn.setRequestProperty("Content-Length", String.valueOf(JSONitem.length()));


            OutputStream os = conn.getOutputStream();
            os.write(JSONitem.toString().getBytes());
            os.flush();
            os.close();
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//            wr.write(JSONitem.toString());
//            wr.flush();
//            wr.close();

            responseCode = conn.getResponseCode();
            String responseMsg = "PostHTTP.java - Response Code: " + responseCode;
            System.out.println(responseMsg);

            conn.disconnect();

            // Evt. læse svaret men ved ikke om vi har brug for andet end response code?
            //StringBuffer response = new StringBuffer();
            //BufferedReader in = new BufferedReader(
            //        new InputStreamReader(conn.getInputStream()));
            //String inputLine;

            //while ((inputLine = in.readLine()) != null) {
            //    response.append(inputLine);
            //}
            //in.close();
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
        SharedPreferences prefs = context.getPreferences(Context.MODE_PRIVATE);
        prefs.edit().remove("chosenImage").commit();
    }
}

