package hyltofthansen.ddhfregistrering.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.singletons.Sing_NewItemData;

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
    private ArrayList<String> imageFilePathList;
    private ProgressDialog progressDialog;
    private int uploaded;

    public PostHTTPPicture(Activity context, int itemid) {
        this.context = context;
        this.itemid = itemid;
        this.imageFilePathList = Sing_NewItemData.getInstance().getPhotoFileList();
        progressDialog = new ProgressDialog(context);
        uploaded = 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Uploader", "Vent venligst");

        progressDialog.setCancelable(false);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        progressDialog.setMessage("Uploader billed " + uploaded + " ud af " + imageFilePathList.size());
    }

    @Override
    protected Integer doInBackground(Object[] params) {
        for (int x = 0; imageFilePathList.size() > x; x++ ) {
            try {
                //Opretter POST URL
                try {
                    String urlAPI = null;
                    urlAPI = context.getString(R.string.API_URL) + itemid;
                    url = new URL(urlAPI);
                    System.out.println("URL til at uploade bilede: " + url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "image/jpg"); // content type til Mathias' API

                prefs = PreferenceManager.getDefaultSharedPreferences(context);

                OutputStream os = conn.getOutputStream();
                FileInputStream inputStream = new FileInputStream(imageFilePathList.get(x));

                byte[] data = new byte[1024];
                int read;

                while ((read = inputStream.read(data)) != -1) {
                    os.write(data, 0, read);
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

            } catch (UnsupportedEncodingException e) {
                Log.d(TAG, e.toString());
            } catch (ProtocolException e) {
                Log.d(TAG, e.toString());
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            uploaded++;
           publishProgress();
        }
        return responseCode;
    }



    @Override
    protected void onPostExecute(Object o) {
        try {
            progressDialog.hide();
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
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

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

