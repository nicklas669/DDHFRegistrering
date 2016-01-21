package hyltofthansen.ddhfregistrering;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by hylle on 21-01-2016.
 */
public class ImgCache {
    private static String TAG = "ImgCache";
    private static Bitmap bitmap;

    public static Bitmap getExistingImage(int itemid, int imageNumber) {
        File storagePath = Environment.getExternalStorageDirectory();
        Log.d(TAG, storagePath.toString());
        File file = new File(storagePath + "/DDHF_" + itemid + "_" + imageNumber + ".jpg");
        if (file.exists()) {
            Log.d(TAG, file.getAbsolutePath().toString() + " findes!");
            int orient = ImgRotationDetection.getOrientationFromFile(file);
            Log.d(TAG, "Orientation i Act_ShowImage " + orient);
            bitmap = ImgScaling.decodeSampledBitmapFromFile(file, 1280, 720);
            bitmap = ImgRotationDetection.rotateImageFromOrientation(bitmap, orient);
        }
        return bitmap;

    }
}
