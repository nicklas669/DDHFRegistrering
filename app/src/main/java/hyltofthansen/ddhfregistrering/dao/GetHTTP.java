package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.adapters.CustomArrayAdapter;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.fragments.SearchItemFragment;

/**
 * Class responsible for GET HTTP functionality to API
 * Reads JSON objects from the /items/ URL, which shows an overview of all the items in the database
 */

public class GetHTTP extends AsyncTask {

    private Context context;
    private CustomArrayAdapter listAdapter;
    private ArrayList<ItemDTO> items;
    private SearchItemFragment searchItemFragment;
    private static final String TAG = "GetHTTP";


    public GetHTTP(Context context, ArrayList<ItemDTO> items, CustomArrayAdapter listAdapter) {
        this.context = context;
        this.items = items;
        this.listAdapter = listAdapter;
    }

    public GetHTTP(Context context, ArrayList<ItemDTO> items, CustomArrayAdapter listAdapter, SearchItemFragment searchItemFragment) {
        this.context = context;
        this.items = items;
        this.listAdapter = listAdapter;
        this.searchItemFragment = searchItemFragment;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        String url = context.getString(R.string.API_URL_MATHIAS)+"?userID=56837dedd2d76438906140";
        String USER_AGENT = "Mozilla/5.0";
        //Log.d(TAG, "doInBackground køres!");

        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional, default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONArray itemsfromDB = new JSONArray(response.toString());

            for (int x = 0; x < itemsfromDB.length(); x++) {
//                Log.d(TAG, "Kører loop " + x);
                if(isCancelled()) {
                    Log.d(TAG, "Task cancelled");
                    break;
                }
                JSONObject item = itemsfromDB.getJSONObject(x);
                items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.getString("defaultimage"), listAdapter));
            }
//            Log.d(TAG, "Færdig med task");

        } catch (Exception e) {
            e.printStackTrace();
            response.append("Der opstod en fejl! "+e.toString());
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        listAdapter.updateItemsList(items);
        if (searchItemFragment != null) searchItemFragment.stopRefreshingAnimation();
    }
}
