package hyltofthansen.ddhfregistrering.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nicklas on 07-01-2016.
 * http://stackoverflow.com/a/10868126
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap image = null;

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        String urldisplay = urls[0];
        //Log.d("DownloadImageTask", urldisplay);
        InputStream in = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            BitmapFactory.decodeStream(in, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 50, 50);
        //Log.d("DownloadImageTask", "inSampleSize bliver: "+options.inSampleSize);

        // Decode bitmap with inSampleSize set
        try {
            in = new java.net.URL(urldisplay).openStream();
            options.inJustDecodeBounds = false;
            image = BitmapFactory.decodeStream(in, null, options);
            //Log.d("DownloadImageTask", "image dimensioner bliver: "+image.getWidth()+", "+image.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        //Log.d("DownloadImageTask", "image width: "+width+", height: "+height);
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
