package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;

/**
 * Created by hylle on 14-01-2016.
 */
public class GetItemPicturesForGridViewTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadImageTask";
    ArrayList<Bitmap> imageList;
    BaseAdapter listAdapter;
    String urldisplay;
    private int itemID;
    private Context ctx;


    public GetItemPicturesForGridViewTask
            (Context ctx,
             int itemid,
             ArrayList<Bitmap> imageList,
             BaseAdapter listAdapter) {
        this.ctx = ctx;
        this.itemID = itemid;
        this.imageList = imageList;
        this.listAdapter = listAdapter;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = ctx.getString(R.string.API_URL_MATHIAS)+itemID+
                "?userID=56837dedd2d76438906140";

        //Get item image url's
        String USER_AGENT = "Mozilla/5.0";

        Bitmap image = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        urldisplay = "http://msondrup.dk/api/uploads/32_568c0bd3769f2.jpg"; //Wuhehe
        //Log.d("DownloadImageTask", urldisplay);
        InputStream in;
        try {
            in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 50, 50);

        // Decode bitmap with inSampleSize set
        try {
            in = new java.net.URL(urldisplay).openStream();
            options.inJustDecodeBounds = false;
            image = BitmapFactory.decodeStream(in, null, options);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        imageList.add(result);
        listAdapter.notifyDataSetChanged();
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
