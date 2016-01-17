package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.opengl.GLES10;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

            JSONObject item = new JSONObject(response.toString());
            //Get all image URL's from an item
            String imageURL = item.getJSONObject("images").getJSONObject("image_" + clickedImage).get("href").toString();

            //Get pictures
            InputStream in;
            try {
                in = new URL(imageURL).openStream();
                currentImage = BitmapFactory.decodeStream(in);
                in.close();
                currentImage = Bitmap.createScaledBitmap(currentImage, 2048, 2048, false);
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
        Log.d(TAG, "Sat imageView bitmap");
        super.onPostExecute(bitmap);
    }
}
