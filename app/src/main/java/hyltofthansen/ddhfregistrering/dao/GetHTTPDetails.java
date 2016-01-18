package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailInfoFragment;

/**
 * Class responsible for GET HTTP functionality to API
 * Reads JSON object details from a specific item id /items/"id" URL, which shows the details of a given item
 */

public class GetHTTPDetails extends AsyncTask {

    private Context context;
    private ArrayList<ItemDTO> items;
    private static final String TAG = "GetHTTPDetails";
    private int itemID;
    private ItemDetailInfoFragment detailsFragment;


    public GetHTTPDetails(Context context, int itemID, ArrayList<ItemDTO> items,
                          ItemDetailInfoFragment detailsFragment) {
        this.context = context;
        this.itemID = itemID;
        this.items = items;
        this.detailsFragment = detailsFragment;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.d(TAG, "Kører GetHTTPDetails");
        Log.d(TAG, "Item id : " + itemID);
        // URL til Mathias' API
        String url = context.getString(R.string.API_URL_MATHIAS)+itemID+
                "?userID=56837dedd2d76438906140";

        String USER_AGENT = "Mozilla/5.0";

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
                if (isCancelled()) {
                    break;
                }
            }
            in.close();

            JSONObject item = new JSONObject(response.toString());

//            items.add(new ItemDTO(item.getInt("itemid"),
//                    item.getString("itemheadline"),
//                    item.optString("itemdescription"),
//                    item.optString("itemreceived"),
//                    item.optString("itemdatingfrom"),
//                    item.optString("itemdatingto"),
//                    item.optString("donator"),
//                    item.optString("producer"),
//                    item.optInt("postnummer"),
//                    item.optJSONArray("images")));
            Singleton.getInstance().setClickedItem(new ItemDTO(item.getInt("itemid"),
                    item.getString("itemheadline"),
                    item.optString("itemdescription"),
                    item.optString("itemreceived"),
                    item.optString("itemdatingfrom"),
                    item.optString("itemdatingto"),
                    item.optString("donator"),
                    item.optString("producer"),
                    item.optInt("postnummer"),
                    item.optJSONArray("images")));
            Log.d(TAG, item.getString("itemheadline"));
            Log.d(TAG, item.getString("itemdescription"));

        } catch (JSONException e) {
            Log.d(TAG, e.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            response.append("Der opstod en fejl! "+e.toString());
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        detailsFragment.updateEditViews();
        Log.d(TAG, "Updated editviews()");
        super.onPostExecute(o);
    }
}
