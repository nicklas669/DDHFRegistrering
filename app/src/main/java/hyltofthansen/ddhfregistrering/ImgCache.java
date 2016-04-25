package hyltofthansen.ddhfregistrering;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;

/**
 * Created by hylle on 21-01-2016.
 */
public class ImgCache {
    private static String TAG = "ImgCache";
    private static Bitmap bitmap;

    public static Bitmap getExistingImage(int itemid, int imageNumber, int dstWidth, int dstHeight, String imgSize) {
        File storagePath = Environment.getExternalStorageDirectory();
        //Log.d(TAG, "storagePath: "+storagePath.toString());

        File file = new File(storagePath + "/DDHF_" + itemid + "_" + imageNumber + "_" + imgSize + ".jpg");
        //Log.d(TAG, "file: "+file.getAbsolutePath().toString());
        if (file.exists()) {
            Log.d(TAG, file.getAbsolutePath().toString() + " findes!");
            int orient = ImgRotationDetection.getOrientationFromFile(file);
            bitmap = ImgScaling.decodeSampledBitmapFromFile(file, dstWidth, dstHeight);
            bitmap = ImgRotationDetection.rotateImageFromOrientation(bitmap, orient);
        } else {
            return null;
        }
        return bitmap;
    }

    public static File saveFileFromURL(String imageURL, int imageNumber, int itemid, String imgSize) {
        URL url = null;
        File file = null;
        try {
            url = new URL(imageURL);
            InputStream input = url.openStream();
            File storagePath = Environment.getExternalStorageDirectory();
            file = new File(storagePath,"DDHF_" + itemid + "_" + imageNumber + "_" + imgSize + ".jpg");
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                    output.write(buffer, 0, bytesRead);
                }
                return file;
            } finally {
                output.close();
                input.close();
                return file;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
