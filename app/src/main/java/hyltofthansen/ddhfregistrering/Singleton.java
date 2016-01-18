package hyltofthansen.ddhfregistrering;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import hyltofthansen.ddhfregistrering.adapters.CustomArrayAdapter;
import hyltofthansen.ddhfregistrering.dao.DownloadImageTask;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.SearchItemFragment;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailInfoFragment;

/**
 * Singleton responsible for keeping track of the App's AsyncTask
 */
public class Singleton extends Application {

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
        getHTTP = new GetHTTP(ctx, items, listAdapter, searchItemFragment);
        allTasks.add(getHTTP);
        getHTTP.execute();
    }


    private void cancelAllTask() {
        for ( AsyncTask task : allTasks) {
            if((task != null) && (task.getStatus() == AsyncTask.Status.RUNNING)) {
                task.cancel(true);
                allTasks.remove(task);
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
        getHTTPDetailsTask.execute();
    }

    public ItemDTO getDetailedItem() {
        return items.get(itemdetailsID);
    }

    public void setClickedItem(ItemDTO clickedItem) {
        this.clickedItem = clickedItem;
    }

    public ItemDTO getClickedItem() {
        return clickedItem;
    }

    public void fetchDefaultImage(String defaultImageUrl, ArrayList<Bitmap> images, BaseAdapter listAdapter) {
//        cancelAllTask();
        DownloadImageTask dloadImageTask = new DownloadImageTask(images, listAdapter);
        allTasks.add(dloadImageTask);
        dloadImageTask.fetchImages(defaultImageUrl);
    }
}
