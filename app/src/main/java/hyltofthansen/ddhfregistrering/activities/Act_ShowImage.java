package hyltofthansen.ddhfregistrering.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.dao.GetFullScreenPicTask;

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
        Log.d(TAG, String.valueOf(itemid) + " img " + String.valueOf(clickedImage));
        Singleton.getInstance().getFullScreenPic(this,itemid, clickedImage, imageView, progressBar);

        super.onCreate(savedInstanceState);
    }

    public void getDataFromExtra() {
            Bundle bundle = getIntent().getExtras();
            itemid = bundle.getInt("itemid");
            clickedImage = bundle.getInt("clickedimage");
        }
}
