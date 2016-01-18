package hyltofthansen.ddhfregistrering;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.activities.ShowImageActivity;
import hyltofthansen.ddhfregistrering.adapters.CustomArrayAdapter;
import hyltofthansen.ddhfregistrering.adapters.ItemDetailsImageAdapter;
import hyltofthansen.ddhfregistrering.dao.DeleteHTTP;
import hyltofthansen.ddhfregistrering.dao.DownloadImageTask;
import hyltofthansen.ddhfregistrering.dao.GetFullScreenPicTask;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dao.GetItemPicturesForGridViewTask;
import hyltofthansen.ddhfregistrering.dao.PostHTTPController;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.SearchItemFragment;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailInfoFragment;

/**
 * Singleton responsible for keeping track of the App's AsyncTask
 */
public class Singleton extends Application {

    private static final String TAG = "Singleton";
    private static Singleton firstInstance = null;
    private GetHTTP getHTTP;
    private ArrayList<ItemDTO> items;
    private GetHTTPDetails getHTTPDetailsTask;
    private int itemdetailsID;
    private ItemDTO clickedItem;
    private ArrayList<AsyncTask> allTasks;

    private Singleton() {
        items = new ArrayList<ItemDTO>();
        allTasks = new ArrayList<>();
    }

    public static Singleton getInstance() {
        if (firstInstance == null) {
            firstInstance = new Singleton();
        }
        return firstInstance;
    }

    public void fetchItemsFromAPI(Context ctx, CustomArrayAdapter listAdapter, SearchItemFragment searchItemFragment) {
        cancelAllTask();
        items = new ArrayList<ItemDTO>();
        getHTTP = new GetHTTP(ctx, items, listAdapter, searchItemFragment);
        allTasks.add(getHTTP);
        getHTTP.execute();
    }


    private void cancelAllTask() {
        for ( AsyncTask task : allTasks) {
            if(!(task instanceof GetHTTPDetails)) {
                if ((task != null) && (task.getStatus() == AsyncTask.Status.RUNNING)) {
                    task.cancel(true);
                }
            }
        }
    }

    public ArrayList<ItemDTO> getItems() {
        return items;
    }

    public int getItemDetailsID() {
        return itemdetailsID;
    }

    public void getItemDetails(FragmentActivity activity, int itemid, ItemDetailInfoFragment itemDetailInfoFragment) {
        itemdetailsID = itemid;
        cancelAllTask();
        getHTTPDetailsTask = new GetHTTPDetails(activity, itemid, items, itemDetailInfoFragment);
        allTasks.add(getHTTPDetailsTask);
        getHTTPDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setClickedItem(ItemDTO clickedItem) {
        this.clickedItem = clickedItem;
    }

    public ItemDTO getClickedItem() {
        Log.d(TAG, clickedItem.toString());
        return clickedItem;
    }

    public void fetchItemGridPictures(Context context, int itemid, ArrayList<Bitmap> pictures, ItemDetailsImageAdapter itemDetailsImageAdapter, ProgressBar pb) {
                GetItemPicturesForGridViewTask getItemGridPics = new GetItemPicturesForGridViewTask(context,
                        itemid,pictures, itemDetailsImageAdapter, pb);
        cancelAllTask();
        allTasks.add(getItemGridPics);
//        getItemGridPics.execute();
        getItemGridPics.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getFullScreenPic(ShowImageActivity showImageActivity, int itemid, int clickedImage, ImageView imageView, ProgressBar progressBar) {
        GetFullScreenPicTask dwTask = new GetFullScreenPicTask(showImageActivity, itemid, clickedImage, imageView, progressBar);
        cancelAllTask();
        allTasks.add(dwTask);
        dwTask.execute();
    }

    public void fetchDefaultImage(ItemDTO itemDTO, CustomArrayAdapter customArrayAdapter) {
//        cancelAllTask();
        DownloadImageTask dloadImageTask = new DownloadImageTask(itemDTO, customArrayAdapter);
        allTasks.add(dloadImageTask);
        dloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        dloadImageTask.execute();
    }

    public void callPostHTTPController(JSONObject jsoNitem, FragmentActivity activity) {
        PostHTTPController postHTTPController = new PostHTTPController(jsoNitem, activity);
        cancelAllTask();
        postHTTPController.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void deleteHTTP(FragmentActivity activity) {
        DeleteHTTP delHTTP = new DeleteHTTP(activity, clickedItem.getItemid());
        cancelAllTask();
        delHTTP.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}