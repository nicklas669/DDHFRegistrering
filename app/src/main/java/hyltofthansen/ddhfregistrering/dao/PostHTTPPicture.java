package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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

    private static final String TAG = "PostHTTPPicture";
    private JSONObject JSONitem;
    private int responseCode;
    URL url;
    StringBuffer response;
    Activity context;
    private JSONObject itemMedBilled;
    private SharedPreferences prefs;
    private int itemid;

    public PostHTTPPicture(Activity context, int itemid) {
        this.context = context;
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
                System.out.println("URL til at uploade bilede: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "image/jpg"); // content type til Mathias' API

//            Log.d(TAG, String.valueOf(prefs.getString("chosenImage", null)));
//            String filePath = String.valueOf(prefs.getString("chosenImage", null));
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String imgPath = prefs.getString("chosenImage", null);
            Log.d(TAG, "imgPath: "+imgPath);
            // Lav file path om
            if (imgPath.contains("file:")) {
                imgPath = imgPath.split(":/")[1];
            } else {
                imgPath = getFilePathFromContentUri(Uri.parse(imgPath), context.getContentResolver());
                //Log.d(TAG, "ny imgPath: "+imgPath);
            }
            Log.d(TAG, "ny imgPath: "+imgPath);

            OutputStream os = conn.getOutputStream();
//            File imgFile = new File(imgPath);
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
            String responseMsg = "PostHTTPController.java - Response Code: " + responseCode;
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
        catch (UnsupportedEncodingException e) {
            Log.d(TAG, e.toString());
        } catch (ProtocolException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return responseCode;
    }



    @Override
    protected void onPostExecute(Object o) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        responseCode = Integer.valueOf(o.toString());
        if (responseCode == 200) {
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
        } else {
            builder.setMessage("Genstand oprettet men fejl ved upload af billede. Responskode: " + responseCode)
                    .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        // Ryd gemt billede fra app's data
        prefs.edit().remove("chosenImage").commit();
    }

    /**
     * Gets the corresponding path to a file from the given content:// URI
     * @param selectedImageUri The content:// URI to find the file path from
     * @param contentResolver The content resolver to use to perform the query.
     * @return the file path as a string
     */
    private String getFilePathFromContentUri(Uri selectedImageUri,
                                             ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        //Log.d(TAG, "returnerer filePath: "+filePath);
        return filePath;
    }
}

