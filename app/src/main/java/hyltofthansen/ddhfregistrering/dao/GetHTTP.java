package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.adapters.Adapter_SearchList;
import hyltofthansen.ddhfregistrering.dto.DTO_Item;
import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.fragments.Frag_SearchItem;

/**
 * Class responsible for GET HTTP functionality to API
 * Reads JSON objects from the /items/ URL, which shows an overview of all the items in the database
 */

public class GetHTTP extends AsyncTask {

    private Context context;
    private Adapter_SearchList listAdapter;
    private ArrayList<DTO_Item> items;
    private Frag_SearchItem searchItemFragment;
    private static final String TAG = "GetHTTP";

    public GetHTTP(Context context, ArrayList<DTO_Item> items, Adapter_SearchList listAdapter, Frag_SearchItem searchItemFragment) {
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
                items.add(new DTO_Item(item.getInt("itemid"),
                        item.getString("itemheadline"),
                        item.getString("defaultimage")));
//                        item.optJSONArray("images"), //Denne skal fjernes eller også skal der itereres detaljer for hver item allerede her!
//                        listAdapter));
            }
//            Log.d(TAG, "Færdig med task");

        } catch (ConnectException e) {
            response.append("Kunne ikke forbinde: "+  e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            response.append("Der opstod en fejl! "+e.toString());
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        Log.d(TAG, "onPostExecute");
        listAdapter.updateItemsList(items);
        if (searchItemFragment != null) searchItemFragment.stopRefreshingAnimation();
    }
}
