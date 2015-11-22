package hyltofthansen.ddhfregistrering;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class responsible for POST HTTP functionality to API 0.1 on CreateItem
 */
public class PostAPI extends AsyncTask<Map<String, Object>, String, Integer> {

    private Map<String, Object> postParams = new LinkedHashMap<>();
    private int responseCode;
    URL url;
    StringBuffer response;

    @Override
    protected Integer doInBackground(Map<String, Object>... params) {
        /**
         *  Opret nyt item:
         http POST "http://78.46.187.172:4019/items?itemheadline=test&itemdescription=blahblahblah"

         Venligst udlånt fra http://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
         */
//        StringBuffer response = new StringBuffer();
        try {
            StringBuilder postData = new StringBuilder();
            for (Map<String, Object> param : params) {
                for (Map.Entry<String, Object> parameters : param.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(parameters.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(String.valueOf(parameters.getValue()), "UTF-8"));
                }
            }
            //Opretter POST URL
            try {
                //url = new URL(getString(R.string.URL) +"/items?"+postData);
                url = new URL("http://78.46.187.172:4019/items?"+postData);
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
            String responseMsg = "PostAPI.java - Response Code: " + responseCode;
            System.out.println(responseMsg);

            conn.disconnect();

            // Evt. læse svaret men ved ikke om vi har brug for andet end response code?

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
    protected void onPostExecute(Integer result) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        int responseCode = 201;
        if(responseCode == 201) {
            // 2. Chain together various setter methods to set the dialog characteristics
            //builder.setMessage("Genstand oprettet successfuldt. Responskode " + result)
            //        .setTitle("Success");
            //Gå tilbage til hovedmenu her
        } else {
            //builder.setMessage("Fejl ved oprettelse. Responskode" + result)
            //        .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        //AlertDialog dialog = builder.create();
        //dialog.show();
    }

//    public PostAPI(Map<String, Object> postParams) {
//        this.postParams = postParams;
//    }



}

