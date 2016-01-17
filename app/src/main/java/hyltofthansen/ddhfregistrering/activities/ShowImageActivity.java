package hyltofthansen.ddhfregistrering.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dao.GetFullScreenPicTask;

/**
 * Created by hylle on 17-01-2016.
 */
public class ShowImageActivity extends Activity {

    private static final String TAG = "ShowImageActivity";
    int itemid, clickedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_showimage);
        getDataFromExtra();
        ImageView imageView = (ImageView) findViewById(R.id.fullimageView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarFullScreen);
        progressBar.setVisibility(View.INVISIBLE);
        Log.d(TAG,String.valueOf(itemid) + " img " + String.valueOf(clickedImage));
        GetFullScreenPicTask dwTask = new GetFullScreenPicTask(this, itemid, clickedImage, imageView, progressBar);
        dwTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);   //TODO Igen, sluk alle tråde i stedet for dette

        super.onCreate(savedInstanceState);
    }

    public void getDataFromExtra() {
            Bundle bundle = getIntent().getExtras();
            itemid = bundle.getInt("itemid");
            clickedImage = bundle.getInt("clickedimage");
        }
}
