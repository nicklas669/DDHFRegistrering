package hyltofthansen.ddhfregistrering;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class detecting an bitmap's rotation based on EXIF data and rotates the bitmap.
 */
public class ImgRotationDetection {

    private static final String TAG = "ImgRotationDetection";
    private static int orientation;

    /**
     * Used by DownloadTask's
     * @param scaledBitmap
     * @return
     */
    public static Bitmap getCorrectRotatedBitmap(Bitmap scaledBitmap) {
            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    scaledBitmap = rotateImage(scaledBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    scaledBitmap = rotateImage(scaledBitmap, 180);
                    break;
                // etc.
            }

        return scaledBitmap;
    }

    /**
     * Used by camera intent
     * @param scaledBitmap
     * @param path
     * @return
     */
    public static Bitmap getCorrectRotatedBitmap(Bitmap scaledBitmap, String path) {
        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.d(TAG, String.valueOf(scaledBitmap.getHeight() + " bitmap height"));

            Log.d(TAG, orientation + " orientation");

            ei = new ExifInterface(path);
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    scaledBitmap = rotateImage(scaledBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    scaledBitmap = rotateImage(scaledBitmap, 180);
                    break;
                // etc.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private static Bitmap rotateImage(Bitmap scaledBitmap, float angle) {
            Bitmap retVal;

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            retVal = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            return retVal;
    }

    public static void saveFileToGetOrientation(String imageURL) {
        URL url = null;
        try {
            url = new URL(imageURL);
        InputStream input = url.openStream();
            File storagePath = Environment.getExternalStorageDirectory();
            File file = new File(storagePath,"DDHF_" + System.currentTimeMillis() + ".jpg");
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
            } finally {
                output.close();
                input.close();
            }
            Log.d(TAG, file.getAbsolutePath());
            ExifInterface ei = new ExifInterface(file.toString());
            orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);


            Log.d(TAG, orientation + " orientation");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
