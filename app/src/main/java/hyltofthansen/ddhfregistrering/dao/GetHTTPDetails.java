package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.R;
import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.fragments.itemdetailsfragments.ItemDetailsFragment;

/**
 * Class responsible for GET HTTP functionality to API
 * Reads JSON object details from a specific item id /items/"id" URL, which shows the details of a given item
 */

public class GetHTTPDetails extends AsyncTask {

    private Context context;
    private ArrayList<ItemDTO> items;
    private static final String TAG = "GetHTTPDetails";
    private int itemID;
    private ItemDetailsFragment detailsFragment;


    public GetHTTPDetails(Context context, int itemID, ArrayList<ItemDTO> items, ItemDetailsFragment detailsFragment) {
        this.context = context;
        this.itemID = itemID;
        this.items = items;
        this.detailsFragment = detailsFragment;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        // URL til Claus' API
        //String url = context.getString(R.string.API_URL) + "/items/" + itemID;

        // URL til Mathias' API
        String url = context.getString(R.string.API_URL_MATHIAS)+itemID+"?userID=56837dedd2d76438906140";

        String USER_AGENT = "Mozilla/5.0";

        StringBuffer response = new StringBuffer();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            // optional, default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
            //Log.d(TAG, "\nSending 'GET' request to URL : " + url);
            Log.d(TAG, "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //Log.d(TAG, String.valueOf(in.readLine()));
                response.append(inputLine);
                //Log.d(TAG, String.valueOf(response));
            }
            in.close();

            JSONObject item = new JSONObject(response.toString());

            items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.optString("itemdescription"), item.optString("itemreceived"), item.optString("itemdatingfrom"),
                    item.optString("itemdatingto"), item.optString("donator"), item.optString("producer"), item.optInt("postnummer")));


        } catch (Exception e) {
            e.printStackTrace();
            response.append("Der opstod en fejl! "+e.toString());
        }
        return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        //Log.d(TAG, String.valueOf(items.size() + " Item size"));
        detailsFragment.updateTextViews();
        super.onPostExecute(o);
    }

    public void fetchItems() {
        this.execute();
    }

}
