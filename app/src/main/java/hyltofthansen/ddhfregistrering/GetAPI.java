package hyltofthansen.ddhfregistrering;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class responsible for GET HTTP functionality to API 0.1 on searchItem
 */

public class GetAPI extends AsyncTask {

    Context context;
    BaseAdapter listAdapter;
    ArrayList<ItemDTO> items;

    public GetAPI(Context context, ArrayList<ItemDTO> items, BaseAdapter listAdapter) {
        this.context = context;
        this.items = items;
        this.listAdapter = listAdapter;
    }

    @Override
    protected Object doInBackground(Object[] params) {
            String url = context.getString(R.string.URL)+"/items/";
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
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray itemsfromDB = new JSONArray(response.toString());
                for (int x = 0; x < itemsfromDB.length(); x++) {
                    if (x < 20) { // maks. 20 items til test
                        JSONObject item = itemsfromDB.getJSONObject(x);
                        items.add(new ItemDTO(item.getInt("itemid"), item.getString("itemheadline"), item.optString("itemdescription"), item.optString("itemreceived"), item.optString("itemdatingfrom"),
                                item.optString("itemdatingto"), item.optString("donator"), item.optString("producer"), item.optInt("postnummer")));
                    }
                }
                //for (ItemDTO item : items) System.out.println(item);

            } catch (Exception e) {
                e.printStackTrace();
                response.append("Der opstod en fejl! "+e.toString());
            }
            return response;
    }

    @Override
    protected void onPostExecute(Object o) {
        listAdapter.notifyDataSetChanged();
        super.onPostExecute(o);
    }

    public void fetchItems() {
        this.execute();
    }
}
