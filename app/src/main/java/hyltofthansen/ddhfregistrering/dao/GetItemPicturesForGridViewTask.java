package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.ImgCache;
import hyltofthansen.ddhfregistrering.ImgRotationDetection;
import hyltofthansen.ddhfregistrering.ImgScaling;
import hyltofthansen.ddhfregistrering.R;

/**
 * Class getting images for gridview for a specific item
 */
public class GetItemPicturesForGridViewTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "GridViewImageTask";
    private ArrayList<Bitmap> imageList;
    private BaseAdapter listAdapter;
    private String itemIDURL;
    private int itemID;
    private Context ctx;
    private Bitmap currentImage;
    private final int MAX_PICS = 6;
    private ProgressBar pb;
    private Bitmap image;
    private final int MAX_WIDTH = 300;
    private final int MAX_HEIGHT = 300;


    public GetItemPicturesForGridViewTask
            (Context ctx,
             int itemid,
             ArrayList<Bitmap> imageList,
             BaseAdapter listAdapter, ProgressBar pb) {
        this.ctx = ctx;
        this.itemID = itemid;
        this.imageList = imageList;
        this.listAdapter = listAdapter;
        this.pb = pb;
        //Log.d(TAG, "Tråd lavet");
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        itemIDURL = ctx.getString(R.string.API_URL)+itemID;

        //Get item image url's
        String USER_AGENT = "Mozilla/5.0";
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(itemIDURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional, default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();
            //Log.d(TAG, "response: "+response.toString());

            JSONObject object = new JSONObject(response.toString());
            JSONObject data = new JSONObject(object.getJSONObject(("data")).toString());
            JSONObject item = new JSONObject(data.getJSONObject("default").toString());
            //Log.d(TAG, "item: "+item);

            //Get all image URL's from an item
            //Log.d(TAG, "images: "+item.getJSONArray("images").optJSONObject(0).optString("thumb"));
            JSONArray images = item.getJSONArray("images");
            //Log.d(TAG, "images: "+item.getJSONArray("images"));
            //Log.d(TAG, "Genstand har "+ images.length() +" billeder!");

            // Debug: print billeder der tilhører genstanden (i medium størrelse)
            /*
            for (int x = 0; x < images.length(); x++)
                Log.d(TAG, "Image"+x+": "+images.getJSONObject(x).getString("medium"));
                */

            //Log.d(TAG, "Går ind i image loop!");
            if (images.length() > 0) {
                for (int x = 0; x < images.length(); x++) {
                    if (x < MAX_PICS) {
                        //Log.d(TAG, String.valueOf(x) + " x værdien");
                        currentImage = ImgCache.getExistingImage(itemID, x, MAX_WIDTH,MAX_HEIGHT, "medium");
                        if (currentImage == null) {
                            //Log.d(TAG, "Billedet er null " + x + " itemID " + itemID);
                            String imageURL = images.getJSONObject(x).getString("medium");
                            Log.d(TAG, x + " imageURL " + imageURL);
                            File file = new File(ImgCache.saveFileFromURL(imageURL, x, itemID, "medium"),"");
                            currentImage = ImgScaling.decodeSampledBitmapFromFile(file, MAX_WIDTH, MAX_HEIGHT);
                        }
                        imageList.add(x, currentImage);
                        publishProgress();
                    }
                }
            }

            /*
            Log.d(TAG, imageList.size() + " imagelist size");
            for (int x = imageList.size(); x < MAX_PICS; x++) {
                Log.d(TAG, String.valueOf(x) + " x værdien");
                currentImage = ImgCache.getExistingImage(itemID, x, MAX_WIDTH,MAX_HEIGHT);
                if (currentImage == null) {
                    Log.d(TAG, "Billedet er null " + x + " itemID " + itemID);
                    String imageURL = item.getJSONObject("images").getJSONObject("image_" + x).get("href").toString();
                    Log.d(TAG, "NU KOMMER DER PRINT!");
                    Log.d(TAG, x + " imageURL " + imageURL);
                    Log.d(TAG, "HVOR VAR DET?");
                    File file = new File(ImgCache.saveFileFromURL(imageURL, x, itemID),"");
                    currentImage = ImgScaling.decodeSampledBitmapFromFile(file, MAX_WIDTH, MAX_HEIGHT);
                }
                imageList.add(currentImage);
                publishProgress();
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentImage;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        Log.d(TAG, "onProgressUpdate kaldt");
        listAdapter.notifyDataSetChanged();
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onPostExecute");
        pb.setVisibility(View.INVISIBLE);
        super.onPostExecute(bitmap);
    }
}
