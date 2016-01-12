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
 * Class responsible for GET HTTP functionality to API
 * Reads JSON objects from the /items/ URL, which shows an overview of all the items in the database
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
        // URL til Mathias' API
        String url = context.getString(R.string.API_URL_MATHIAS)+"?userID=56837dedd2d76438906140";
        String USER_AGENT = "Mozilla/5.0";
        Log.d(TAG, "doInBackground køres!");

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
            //Log.d(TAG, "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //Log.d(TAG, String.valueOf(in.readLine()));
                response.append(inputLine);
                //Log.d(TAG, String.valueOf(response));
            }
            in.close();
            con.disconnect();

            JSONArray itemsfromDB = new JSONArray(response.toString());
            //Log.d(TAG, String.valueOf(itemsfromDB.length() + " Itemfromdb size"));

            for (int x = 0; x < itemsfromDB.length(); x++) {
                JSONObject item = itemsfromDB.getJSONObject(x);
                //Log.d(TAG, item.getString("itemid")+" defaultimage: "+item.getString("defaultimage"));
                //items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.getString("defaultimage")));
                items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.getString("defaultimage"), listAdapter));
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
        //Log.d(TAG, String.valueOf(items.size() + " Item size"));
        super.onPostExecute(o);
    }

    public void fetchItems() {
        this.execute();
    }

    public BaseAdapter getlistAdapter() {
        return listAdapter;
    }
}
