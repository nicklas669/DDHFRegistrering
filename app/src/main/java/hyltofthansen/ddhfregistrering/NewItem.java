package hyltofthansen.ddhfregistrering;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
        getActivity().setTitle("Registrer ny genstand");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top

        View root = inflater.inflate(R.layout.new_item, container, false);
        lv = (ListView) root.findViewById(R.id.newItem_listview);

        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises
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
                                          // get prompts.xml view
                                          LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

                                          final View promptView = layoutInflater.inflate(R.layout.promptinput, null);
                                          final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                                          // set prompts.xml to be the layout file of the alertdialog builder
                                          alertDialogBuilder.setView(promptView);
                                          final TextView inputHeader = (TextView) promptView.findViewById(R.id.text_inputPrompt);
                                          final EditText input = (EditText) promptView.findViewById(R.id.userInput);
                                          switch (position) {
                                              case 0: // Genstand nr.
                                                  inputHeader.setText(((TextView) lv.getChildAt(0).findViewById(R.id.listHeader)).getText()); // sætter overskrift på inputDialog til overskrift fra den klikkede række
                                                  //TextView inputText = (TextView) lv.getChildAt(0).findViewById(R.id.listDescription); // fanger textView med beskrivelse til den valgte række
                                                  input.setHint(((TextView) lv.getChildAt(0).findViewById(R.id.listDescription)).getText());

                                                  // setup a dialog window
                                                  alertDialogBuilder
                                                          .setCancelable(false)
                                                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                              public void onClick(DialogInterface dialog, int id) {
                                                                  // get user input and set it to result
                                                                  ((TextView) lv.getChildAt(0).findViewById(R.id.listDescription)).setText(input.getText());
                                                              }
                                                          })
                                                          .setNegativeButton("Fortryd",
                                                                  new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int id) {
                                                                          dialog.cancel();
                                                                      }
                                                                  });

                                                  AlertDialog promptDialog_0 = alertDialogBuilder.create();
                                                  promptDialog_0.show();
                                                  promptDialog_0.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                                  promptDialog_0.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                                  break;

                                              case 1: // Betegnelse
                                                  inputHeader.setText(((TextView) lv.getChildAt(1).findViewById(R.id.listHeader)).getText()); // sætter overskrift på inputDialog til overskrift fra den klikkede række
                                                  //TextView inputText = (TextView) lv.getChildAt(0).findViewById(R.id.listDescription); // fanger textView med beskrivelse til den valgte række
                                                  input.setHint(((TextView) lv.getChildAt(1).findViewById(R.id.listDescription)).getText());

                                                  // setup a dialog window
                                                  alertDialogBuilder
                                                          .setCancelable(false)
                                                          .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                              public void onClick(DialogInterface dialog, int id) {
                                                                  // get user input and set it to result
                                                                  ((TextView) lv.getChildAt(1).findViewById(R.id.listDescription)).setText(input.getText());
                                                              }
                                                          })
                                                          .setNegativeButton("Fortryd",
                                                                  new DialogInterface.OnClickListener() {
                                                                      public void onClick(DialogInterface dialog, int id) {
                                                                          dialog.cancel();
                                                                      }
                                                                  });

                                                  AlertDialog promptDialog_1 = alertDialogBuilder.create();
                                                  promptDialog_1.show();
                                                  promptDialog_1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                                                  promptDialog_1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                                                  break;

                                              case 2: // Modtagelsesdato
                                                  DialogFragment receivingDate = new DatePickerFragment() {
                                                      @Override
                                                      public void onDateSet(DatePicker view, int year, int month, int day) {
                                                          descriptions[2] = "" + day + "-" + (month + 1) + "-" + year;
                                                          listAdapter.notifyDataSetChanged();
                                                      }
                                                  };
                                                  receivingDate.show(getFragmentManager(), "datePicker");
                                                  break;

                                              case 3: // Datering fra
                                                  DialogFragment datingFrom = new DatePickerFragment() {
                                                      @Override
                                                      public void onDateSet(DatePicker view, int year, int month, int day) {
                                                          descriptions[3] = "" + day + "-" + (month + 1) + "-" + year;
                                                          listAdapter.notifyDataSetChanged();
                                                      }
                                                  };
                                                  datingFrom.show(getFragmentManager(), "datePicker");
                                                  break;

                                              case 4: // Datering til
                                                  DialogFragment datingTo = new DatePickerFragment() {
                                                      @Override
                                                      public void onDateSet(DatePicker view, int year, int month, int day) {
                                                          descriptions[4] = "" + day + "-" + (month + 1) + "-" + year;
                                                          listAdapter.notifyDataSetChanged();
                                                      }
                                                  };
                                                  datingTo.show(getFragmentManager(), "datePicker");
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
