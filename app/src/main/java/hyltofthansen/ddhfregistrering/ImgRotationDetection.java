package hyltofthansen.ddhfregistrering;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Class detecting an bitmap's rotation based on EXIF data and rotates the bitmap.
 */
public class ImgRotationDetection {

    private static final String TAG = "ImgRotationDetection";

    public static Bitmap getCorrectRotatedBitmap(Bitmap scaledBitmap, String path) {
        try {
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            Log.d(TAG, orientation + " orientation");

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
}
