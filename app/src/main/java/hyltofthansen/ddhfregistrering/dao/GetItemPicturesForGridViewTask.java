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

import hyltofthansen.ddhfregistrering.ImgRotationDetection;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.adapters.Adapter_ItemDetailsImage;

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
    private final int MAX_PICS = 99;
    private ProgressBar pb;


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
    }

    public GetItemPicturesForGridViewTask(Context mContext, ArrayList<Bitmap> pictures, Adapter_ItemDetailsImage adapter_itemDetailsImage) {
        this.ctx = mContext;
        this.imageList = pictures;
        this.listAdapter = adapter_itemDetailsImage;
    }

    @Override
    protected void onPreExecute() {
//        pb.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        Log.d(TAG, "Henter gridview pic URLs");
        itemIDURL = ctx.getString(R.string.API_URL_MATHIAS)+itemID+
                "?userID=56837dedd2d76438906140";

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
                //Log.d(TAG, String.valueOf(in.readLine()));
                response.append(inputLine);
                //Log.d(TAG, String.valueOf(response));
            }
            bufferedReader.close();

            Log.d(TAG, String.valueOf(imageList.size()));

            JSONObject item = new JSONObject(response.toString());
            //Get all image URL's from an item
            for (int x = imageList.size(); x < MAX_PICS; x++) {
                String imageURL = item.getJSONObject("images").getJSONObject("image_" + x).get("href").toString();
                Log.d(TAG, "Henter billed " + x + " :" + imageURL);

                    // First decode with inJustDecodeBounds=true to check dimensions
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;

                    //Get pictures
                    InputStream in;
                    try {
                        in = new URL(imageURL).openStream();
                        BitmapFactory.decodeStream(in, null, options);
                        currentImage = BitmapFactory.decodeStream(in);
//                        currentImage = Bitmap.createScaledBitmap(currentImage,300,300,false);
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Calculate inSampleSize
                    options.inSampleSize = calculateInSampleSize(options, 300, 300);

                    // Decode bitmap with inSampleSize set
                    try {
                        in = new URL(imageURL).openStream();
                        options.inJustDecodeBounds = false;
                        currentImage = BitmapFactory.decodeStream(in, null, options);
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                    Log.d(TAG, "Færdig med at hente gridview pic");
                    publishProgress();
            }
            Log.d(TAG, item.getJSONObject("images").getJSONObject("image_0").get("href").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentImage;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        imageList.add(currentImage);
        listAdapter.notifyDataSetChanged();
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
//        pb.setVisibility(View.INVISIBLE);
        super.onPostExecute(bitmap);
    }

    /**
     * Takes an BitmapFactory.Options object, reads the image dimensions saved in it and tries to scale it as close as possible to reqWidth x reqHeight
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
