package hyltofthansen.ddhfregistrering;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

public class landingScreen extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.landingpage, container, false);

        Button b_newItem = (Button) root.findViewById(R.id.b_newItem);
        b_newItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new NewItem())
                        .addToBackStack(null).commit();
            }
        });


        return root;
    }
}