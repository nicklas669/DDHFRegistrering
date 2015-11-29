package hyltofthansen.ddhfregistrering;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchItem extends Fragment {

    ListView lv;
    BaseAdapter listAdapter;
    GetAPI getAPI;
    ArrayList<ItemDTO> items = new ArrayList<ItemDTO>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         getActivity().setTitle("Søg efter genstand");
         ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

         View root = inflater.inflate(R.layout.searchitem, container, false);
         lv = (ListView) root.findViewById(R.id.search_lv);

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises
        listAdapter = new ArrayAdapter(getActivity(), R.layout.searchlist, R.id.search_tvheadline, items) {
             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 View view = super.getView(position, convertView, parent);
                 TextView itemHeadline = (TextView) view.findViewById(R.id.search_tvheadline);
                 itemHeadline.setText(items.get(position).getItemheadline());
                 return view;
             }
        };
        lv.setAdapter(listAdapter);

        getAPI = new GetAPI(getActivity(), items, listAdapter);
        getAPI.fetchItems();

        return root;
     }
}
