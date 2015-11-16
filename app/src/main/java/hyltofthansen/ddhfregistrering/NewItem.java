package hyltofthansen.ddhfregistrering;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class NewItem extends Fragment {

    String[] fields = {"Genstand nr.", "Betegnelse", "Modtagelsesdato", "Datering fra", "Datering til",
            "Beskrivelse", "Ref. til donator", "Ref. til producent", "Geografisk område", "Ref. til emnegruppe(r)"};

    String[] descriptions = {"Skriv genstand nummer her", "Skriv betegnelse her", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
            "Indtast beskrivelse", "Referencenummer til donator", "Referencenummer til producent", "Vælg kommune her", "Referencenummer til emnegruppe(r)"};

    ListView lv;
    BaseAdapter listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Ny genstand");
        View root = inflater.inflate(R.layout.new_item, container, false);
        lv = (ListView) root.findViewById(R.id.newItem_listview);

        listAdapter = new ArrayAdapter(getActivity(), R.layout.listview_layout, R.id.listHeader, fields) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView beskrivelse = (TextView) view.findViewById(R.id.listDescription);
                beskrivelse.setText(descriptions[position]);
                return view;
            }
        };

        lv.setAdapter(listAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              switch (position) {
                  case 0: // Genstand nr.
                      // get prompts.xml view
                      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                      View promptView = layoutInflater.inflate(R.layout.promptinput, null);
                      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                      // set prompts.xml to be the layout file of the alertdialog builder
                      alertDialogBuilder.setView(promptView);

                      final TextView inputText = (TextView) lv.getChildAt(0).findViewById(R.id.listDescription); // fanger textView med beskrivelse til den valgte række

                      final EditText input = (EditText) promptView.findViewById(R.id.userInput);
                      input.setText(inputText.getText());

                      // setup a dialog window
                      alertDialogBuilder
                              .setCancelable(false)
                              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int id) {
                                      // get user input and set it to result

                                      inputText.setText(input.getText());
                                  }
                              })
                              .setNegativeButton("Cancel",
                                      new DialogInterface.OnClickListener() {
                                          public void onClick(DialogInterface dialog,	int id) {
                                              dialog.cancel();
                                          }
                                      });

                      // create an alert dialog
                      AlertDialog alertD = alertDialogBuilder.create();

                      alertD.show();
                      break;
                  case 1: // Betegnelse
                      break;
                  case 2: // Modtagelsesdato
                      DialogFragment dateFragment = new DatePickerFragment() {
                          @Override
                          public void onDateSet(DatePicker view, int year, int month, int day) {
                              descriptions[2] = "" + day + "-" + (month + 1) + "-" + year;
                              listAdapter.notifyDataSetChanged();
                          }
                      };
                      dateFragment.show(getFragmentManager(), "datePicker");
                      break;
                  case 3: // Datering fra
                      break;
                  case 4: // Datering til
                      break;
                  case 5: // Beskrivelse
                      break;
                  case 6: // Ref. til donator
                      break;
                  case 7: // Ref. til producent
                      break;
                  case 8: // Geografisk område
                      break;
                  case 9: // Ref. til emnegruppe(r)
                      break;
              }
          }
      }
        );
        return root;
    }
}
