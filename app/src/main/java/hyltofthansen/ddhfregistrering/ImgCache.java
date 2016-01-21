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

    public static Bitmap getExistingImageFullScreenSize(int itemid, int imageNumber) {
        File storagePath = Environment.getExternalStorageDirectory();
        Log.d(TAG, storagePath.toString());
        File file = new File(storagePath + "/DDHF_" + itemid + "_" + imageNumber + ".jpg");
        if (file.exists()) {
            Log.d(TAG, file.getAbsolutePath().toString() + " findes!");
            int orient = ImgRotationDetection.getOrientationFromFile(file);
            Log.d(TAG, "Orientation i Act_ShowImage " + orient);
            bitmap = ImgScaling.decodeSampledBitmapFromFile(file, 1280, 720);
            bitmap = ImgRotationDetection.rotateImageFromOrientation(bitmap, orient);
        } else {
            return null;
        }
        return bitmap;
    }

    public static boolean checkIfImageIsSaved(int itemid, int imageNumber) {
        File storagePath = Environment.getExternalStorageDirectory();
        Log.d(TAG, storagePath.toString());
        File file = new File(storagePath + "/DDHF_" + itemid + "_" + imageNumber + ".jpg");
        return file.exists();
    }

    public static Bitmap getExistingImageSearchListSize(int itemid, int imageNumber) {
        File storagePath = Environment.getExternalStorageDirectory();
        Log.d(TAG, storagePath.toString());
        File file = new File(storagePath + "/DDHF_" + itemid + "_" + imageNumber + ".jpg");
        if (file.exists()) {
            Log.d(TAG, file.getAbsolutePath().toString() + " findes!");
            int orient = ImgRotationDetection.getOrientationFromFile(file);
            Log.d(TAG, "Orientation i Act_ShowImage " + orient);
            bitmap = ImgScaling.decodeSampledBitmapFromFile(file, 50, 50);
            bitmap = ImgRotationDetection.rotateImageFromOrientation(bitmap, orient);
        } else {
            return null;
        }
        return bitmap;
    }



    public static File saveFileFromURL(String imageURL, int imageNumber, int itemid) {
        URL url = null;
        File file = null;
        try {
            url = new URL(imageURL);
            InputStream input = url.openStream();
            File storagePath = Environment.getExternalStorageDirectory();
//            int itemid = Sing_AsyncTasks.getInstance().getClickedItem().getItemid();
            file = new File(storagePath,"DDHF_" + itemid + "_" + imageNumber + ".jpg");
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
