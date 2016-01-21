package hyltofthansen.ddhfregistrering.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

import hyltofthansen.ddhfregistrering.ImgRotationDetection;
import hyltofthansen.ddhfregistrering.ImgScaling;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.singletons.Sing_AsyncTasks;

/**
 * Activity showing fullscreen image of a clicked gridview image
 */
public class Act_ShowImage extends Activity {

    private static final String TAG = "Act_ShowImage";
    int itemid, clickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_showimage);
        getDataFromExtra();
        ImageView imageView = (ImageView) findViewById(R.id.fullimageView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarFullScreen);
        progressBar.setVisibility(View.INVISIBLE);

        File storagePath = Environment.getExternalStorageDirectory();
        Log.d(TAG, storagePath.toString());
        File file = new File(storagePath + "/DDHF_" + itemid + "_" + clickedImage + ".jpg");
        if(file.exists()) {
            Log.d(TAG, file.getAbsolutePath().toString() + " findes!");
            int orient = ImgRotationDetection.getOrientationFromFile(file);
            Log.d(TAG, "Orientation i Act_ShowImage " + orient);
            Bitmap bitmap = ImgScaling.decodeSampledBitmapFromFile(file, 1280, 720);
            bitmap = ImgRotationDetection.rotateImageFromOrientation(bitmap, orient);
            imageView.setImageBitmap(bitmap);
        } else {
            Sing_AsyncTasks.getInstance().getFullScreenPic(this, itemid, clickedImage, imageView, progressBar);
        }

        super.onCreate(savedInstanceState);
    }

    public void getDataFromExtra() {
            Bundle bundle = getIntent().getExtras();
            itemid = bundle.getInt("itemid");
            clickedImage = bundle.getInt("clickedimage");
        }
}
