package hyltofthansen.ddhfregistrering.dao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.dto.ItemDTO;
import hyltofthansen.ddhfregistrering.R;

/**
 * Class responsible for GET HTTP functionality to API 0.1 on searchItem
 */

public class GetHTTP extends AsyncTask {

    private Context context;
    private BaseAdapter listAdapter;
    private ArrayList<ItemDTO> items;
    private static final String TAG = "GetHTTP";


    public GetHTTP(Context context, ArrayList<ItemDTO> items, BaseAdapter listAdapter) {
        this.context = context;
        this.items = items;
        this.listAdapter = listAdapter;
    }

    @Override
    protected Object doInBackground(Object[] params) {
            String url = context.getString(R.string.API_URL)+"/items/";
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
                Log.d(TAG, "\nSending 'GET' request to URL : " + url);
                Log.d(TAG, "Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                int heste = 0;
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    heste++;
                    Log.d(TAG, String.valueOf(in.readLine()));
                    response.append(inputLine);
                    Log.d(TAG, String.valueOf(response));
                }
                in.close();

                JSONArray itemsfromDB = new JSONArray(response.toString());
                Log.d(TAG, String.valueOf(itemsfromDB.length() + " Itemfromdb size"));
                Log.d(TAG, heste + " heste size");

                for (int x = 0; x < itemsfromDB.length(); x++) {
                    if (x < 20) { // maks. 20 items til test      //TODO Fix så det virker uden limit uden der sker gentagelse
                        JSONObject item = itemsfromDB.getJSONObject(x);
                        items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.optString("itemdescription"), item.optString("itemreceived"), item.optString("itemdatingfrom"),
                                item.optString("itemdatingto"), item.optString("donator"), item.optString("producer"), item.optInt("postnummer")));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.append("Der opstod en fejl! "+e.toString());
            }
            return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        listAdapter.notifyDataSetChanged();
        Log.d(TAG, String.valueOf(items.size() + " Item size"));
        super.onPostExecute(o);
    }

    public void fetchItems() {
        this.execute();
    }

    public BaseAdapter getlistAdapter() {
        return listAdapter;
    }
}
