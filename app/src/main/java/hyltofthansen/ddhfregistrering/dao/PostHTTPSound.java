package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;

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
public class PostHTTPSound extends AsyncTask {

    private static final String TAG = "PostHTTPSound";
    private int responseCode;
    URL url;
    Activity context;
    private SharedPreferences prefs;
    private int itemid;

    public PostHTTPSound(Activity context, int itemid) {
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
                System.out.println("URL til at uploade lyd: " + url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "audio/3gp"); // content type til Mathias' API


//            String filePath = String.valueOf(prefs.getString("chosenImage", null));
            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String recordingPath = prefs.getString("recording", null);
            Log.d(TAG, "recordingPath: "+recordingPath);

            OutputStream os = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(recordingPath);

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
            builder.setMessage("Genstand oprettet men fejl ved upload af lydfil. Responskode: " + responseCode)
                    .setTitle("Fejl");
        }
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

        // Ryd gemt lydfil fra app's data
        prefs.edit().remove("recording").commit();
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
        return filePath;
    }
}

