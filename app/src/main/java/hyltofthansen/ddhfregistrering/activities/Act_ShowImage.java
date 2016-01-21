package hyltofthansen.ddhfregistrering.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import hyltofthansen.ddhfregistrering.ImgCache;
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
        Bitmap bitmap = ImgCache.getExistingImageFullScreenSize(itemid, clickedImage);
        if(bitmap != null) {
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
