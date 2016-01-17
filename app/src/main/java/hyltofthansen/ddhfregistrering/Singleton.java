package hyltofthansen.ddhfregistrering;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListAdapter;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.adapters.CustomArrayAdapter;
import hyltofthansen.ddhfregistrering.dao.GetHTTP;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.SearchItemFragment;

/**
 * Singleton responsible for keeping track of the App's AsyncTask
 */
public class Singleton extends Application {

    private static Singleton firstInstance = null;
    private GetHTTP getHTTP;
    private ArrayList<ItemDTO> items;


    private Singleton() {
        items = new ArrayList<ItemDTO>();
    }

    public static Singleton getInstance() {
        if (firstInstance == null) {
            firstInstance = new Singleton();
        }
        return firstInstance;
    }

    public void fetchItemsFromAPI(Context ctx, CustomArrayAdapter listAdapter, SearchItemFragment searchItemFragment) {
        getHTTP = new GetHTTP(ctx, items, listAdapter, searchItemFragment);
        getHTTP.execute();
    }

    public ArrayList<ItemDTO> getItems() {
        return items;
    }
}
