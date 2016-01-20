package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hyltofthansen.ddhfregistrering.ImgRotationDetection;
import hyltofthansen.ddhfregistrering.R;

/**
 * Class getting images for gridview for a specific item
 */
public class GetFullScreenPicTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "GridViewImageTask";
    private String itemIDURL;
    private int itemID;
    private Context ctx;
    private Bitmap currentImage;
    private ProgressBar pb;
    private int clickedImage;
    private ImageView imageView;
    private int orientation;

    public GetFullScreenPicTask(Context ctx, int itemid, int clickedImage, ImageView imageView, ProgressBar pb) {
        this.ctx = ctx;
        this.itemID = itemid;
        this.clickedImage = clickedImage;
        this.imageView = imageView;
        this.pb = pb;
    }

    @Override
    protected void onPreExecute() {
        pb.setVisibility(View.VISIBLE);
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        // TODO: Slet debug logs
        Log.d(TAG, "Henter fullscreen pic");
        itemIDURL = ctx.getString(R.string.API_URL_MATHIAS)+itemID+
                "?userID=56837dedd2d76438906140";
        Log.d(TAG, itemID + " img " + clickedImage);
        //Get item image url's
        String USER_AGENT = "Mozilla/5.0";
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(itemIDURL);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();

            JSONObject item = new JSONObject(response.toString());
            //Get all image URL's from an item
            String imageURL = item.getJSONObject("images").getJSONObject("image_" + clickedImage).get("href").toString();

            ImgRotationDetection.saveFileToGetOrientation(imageURL);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream in;
            try {
                in = new URL(imageURL).openStream();
                BitmapFactory.decodeStream(in, null, options);
//                currentImage = BitmapFactory.decodeStream(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            options.inSampleSize = calculateInSampleSize(options, 1280, 720);

            try {
                in = new java.net.URL(imageURL).openStream();
                options.inJustDecodeBounds = false;
                currentImage = BitmapFactory.decodeStream(in, null, options);
                in.close();

                currentImage = ImgRotationDetection.getCorrectRotatedBitmap(currentImage);

            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Færdig med at hente fullscreen pic");
            publishProgress();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentImage;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        pb.setVisibility(View.INVISIBLE);
        imageView.setImageBitmap(currentImage);
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
