package hyltofthansen.ddhfregistrering.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import java.io.File;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.ImgCache;
import hyltofthansen.ddhfregistrering.ImgScaling;
import hyltofthansen.ddhfregistrering.adapters.Adapter_SearchList;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;

/**
 * Created by Nicklas on 07-01-2016.
 * Class responsible for starting a background thread that downloads an image from an url
 * The image's dimensions is first read and then the dimensions are scaled down so that a small thumbnail version is downloaded (to preserve memory)
 * http://stackoverflow.com/a/10868126
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private static final String TAG = "DownloadImageTask";
    ArrayList<Bitmap> imageList;
    BaseAdapter listAdapter;
    String urldisplay;
    private DTO_Item DTOItem;
    private Bitmap image;

    public DownloadImageTask(DTO_Item DTOItem, Adapter_SearchList customArrayAdapter) {
        this.DTOItem = DTOItem;
        this.listAdapter = customArrayAdapter;
        urldisplay = DTOItem.getDefaultImageURL();
    }

    @Override
    protected void onPreExecute() {
        image = ImgCache.getExistingImageSearchListSize(DTOItem.getItemid(), 0);
        DTOItem.setGettingPicture(true);
    }

    protected Bitmap doInBackground(String... urls) {

        Log.d(TAG, DTOItem + " er " + ImgCache.checkIfImageIsSaved(DTOItem.getItemid(), 0));
        if (image == null) {
            Log.d(TAG, "Image er null " + DTOItem.getItemid());
            File file = new File(ImgCache.saveFileFromURL(urldisplay, DTOItem.getItemid(), 0), "");
            image = ImgScaling.decodeSampledBitmapFromFile(file, 50, 50);
        }
        if (image != null) {
            Log.d(TAG, "Image er ikke null" + DTOItem.getItemid());
        }
        return image;
    }

    protected void onPostExecute(Bitmap result) {
        Log.d(TAG, "PostExecute defaultimage");
        Log.d(TAG, DTOItem.getItemheadline());
        DTOItem.setDefaultImage(result);
        DTOItem.defaultImageDownloaded(true);
        listAdapter.notifyDataSetChanged();
    }
}
