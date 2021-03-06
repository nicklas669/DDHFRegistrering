package hyltofthansen.ddhfregistrering.singletons;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.activities.Act_ShowImage;
import hyltofthansen.ddhfregistrering.adapters.Adapter_ItemDetailsImage;
import hyltofthansen.ddhfregistrering.adapters.Adapter_SearchList;
import hyltofthansen.ddhfregistrering.dao.DeleteHTTP;
import hyltofthansen.ddhfregistrering.dao.DownloadImageTask;
import hyltofthansen.ddhfregistrering.dao.GetFullScreenPicTask;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dao.GetHTTPDetails;
import hyltofthansen.ddhfregistrering.dao.GetItemPicturesForGridViewTask;
import hyltofthansen.ddhfregistrering.dao.PostHTTPController;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;
import hyltofthansen.ddhfregistrering.fragments.Frag_SearchItem;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.Frag_ItemDetailInfo;

/**
 * Sing_AsyncTasks responsible for keeping track of the App's AsyncTask
 */
public class Sing_AsyncTasks extends Application {

    private static final String TAG = "Sing_AsyncTasks";
    private static Sing_AsyncTasks firstInstance = null;
    private GetHTTP getHTTP;
    private ArrayList<DTO_Item> items;
    private GetHTTPDetails getHTTPDetailsTask;
    private int itemdetailsID;
    private DTO_Item clickedItem;
    private ArrayList<AsyncTask> allTasks;

    public Adapter_SearchList getSearchListAdapter() {
        return searchListAdapter;
    }

    public Frag_SearchItem getSearchFragment() {
        return searchFragment;
    }

    private Frag_SearchItem searchFragment;
    private Adapter_SearchList searchListAdapter;
    private GetItemPicturesForGridViewTask getItemGridPics;

    private Sing_AsyncTasks() {
        items = new ArrayList<DTO_Item>();
        allTasks = new ArrayList<>();
    }

    public static Sing_AsyncTasks getInstance() {
        if (firstInstance == null) {
            firstInstance = new Sing_AsyncTasks();
        }
        return firstInstance;
    }

    public void fetchItemsFromAPI() {
        cancelAllTask();
        items = new ArrayList<DTO_Item>();
        getHTTP = new GetHTTP(searchFragment.getContext(), items, searchListAdapter, searchFragment);
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

    public ArrayList<DTO_Item> getItems() {
        return items;
    }

    public int getItemDetailsID() {
        return itemdetailsID;
    }

    public void getItemDetails(FragmentActivity activity, int itemid, Frag_ItemDetailInfo itemDetailInfoFragment) {
        itemdetailsID = itemid;
        cancelAllTask();
        getHTTPDetailsTask = new GetHTTPDetails(activity, itemid, items, itemDetailInfoFragment);
        allTasks.add(getHTTPDetailsTask);
        getHTTPDetailsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setClickedItem(DTO_Item clickedItem) {
        this.clickedItem = clickedItem;
    }

    public DTO_Item getClickedItem() {
        return clickedItem;
    }

    public void fetchItemGridPictures(Context context, int itemid, ArrayList<Bitmap> pictures,
                                      Adapter_ItemDetailsImage itemDetailsImageAdapter,
                                      ProgressBar pb) {
        if(getItemGridPics == null) {
            getItemGridPics = new GetItemPicturesForGridViewTask(context,
                    itemid, pictures, itemDetailsImageAdapter, pb);
            cancelAllTask();
            allTasks.add(getItemGridPics);
            getItemGridPics.execute();
        }
        if (getItemGridPics.getStatus() != AsyncTask.Status.RUNNING) {
            getItemGridPics = new GetItemPicturesForGridViewTask(context,
                    itemid, pictures, itemDetailsImageAdapter, pb);
            cancelAllTask();
            allTasks.add(getItemGridPics);
            getItemGridPics.execute();
        }
    }

    public void getFullScreenPic(Act_ShowImage actShowImage, int itemid, int clickedImage, ImageView imageView, ProgressBar progressBar) {
        GetFullScreenPicTask dwTask = new GetFullScreenPicTask(actShowImage, itemid, clickedImage, imageView, progressBar);
        cancelAllTask();
        allTasks.add(dwTask);
        dwTask.execute();
    }

    public void fetchDefaultImage(DTO_Item DTOItem, Adapter_SearchList customArrayAdapter) {
//        cancelAllTask();
        DownloadImageTask dloadImageTask = new DownloadImageTask(DTOItem, customArrayAdapter);
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

    public void setSearchFragment(Frag_SearchItem searchFragment) {
        this.searchFragment = searchFragment;
    }

    public void setSearchListAdapter(Adapter_SearchList searchListAdapter) {
        this.searchListAdapter = searchListAdapter;
    }
}
