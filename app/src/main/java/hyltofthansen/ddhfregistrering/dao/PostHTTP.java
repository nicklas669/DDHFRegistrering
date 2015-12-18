package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import hyltofthansen.ddhfregistrering.R;

/**
 * Class responsible for POST HTTP functionality to API 0.1 on CreateItem
 */
public class PostHTTP extends AsyncTask {

    private Map<String, Object> postParams;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    FragmentManager fm;

    public PostHTTP(Map<String, Object> postParams, Activity context, FragmentManager fm) {
        this.postParams = postParams;
        this.context = context;
        this.fm = fm;
    }


    @Override
    protected Integer doInBackground(Object[] params) {

        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : postParams.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            //Opretter POST URL
            try {
                url = new URL(context.getString(R.string.API_URL)+"/items?"+postData);
                //url = new URL("http://78.46.187.172:4019/items?"+postData);
                System.out.println("URL: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(postData.toString());
            wr.flush();

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

