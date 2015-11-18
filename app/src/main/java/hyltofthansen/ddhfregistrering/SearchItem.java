package hyltofthansen.ddhfregistrering;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchItem extends Fragment {

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         getActivity().setTitle("Søg efter genstand");
         ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

         View root = inflater.inflate(R.layout.searchitem, container, false);

         final EditText edit_id = (EditText) root.findViewById(R.id.edit_searchID);
         final TextView ev_httpresponse = (TextView) root.findViewById(R.id.et_httpget);
         Button b_search = (Button) root.findViewById(R.id.b_searchforID);

         b_search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                if (edit_id.getText().length() > 0) { // hvis der står noget i ID-feltet
                    final String itemID = edit_id.getText().toString();
                    new AsyncTask() {

                        @Override
                        protected Object doInBackground(Object[] params) {
                            // FYR HTTP GET AF HER
                            // http GET "http://78.46.187.172:4019/items/3"

                            String url = "http://78.46.187.172:4019/items/"+itemID;
                            String USER_AGENT = "Mozilla/5.0";
                            StringBuffer response = new StringBuffer();
                            try {
                                URL obj = new URL(url);
                                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                                // optional default is GET
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

                            } catch (Exception e) {
                                e.printStackTrace();
                                response.append("Der opstod en fejl! "+e.toString());
                            }
                            return response;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            // OPDATER HTTP TEXTVIEW
                            ev_httpresponse.setText(o.toString());
                        }
                    }.execute();

                 }
             }
         });

         return root;
     }
}
