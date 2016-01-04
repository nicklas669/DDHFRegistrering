//package hyltofthansen.ddhfregistrering.deprecated;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DialogFragment;
//import android.app.Fragment;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.DatePicker;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import hyltofthansen.ddhfregistrering.R;
//import hyltofthansen.ddhfregistrering.dao.PostHTTP;
//import hyltofthansen.ddhfregistrering.fragments.ImageBrowseFragment;
//
//@Deprecated
//public class NewItemFragmentDeprecated extends Fragment {
//
//    String[] fields = {"Betegnelse", "Beskrivelse", "Modtagelsesdato", "Datering fra", "Datering til",
//            "Ref. til donator", "Ref. til producent", "Postnummer"};
//
//    String[] descriptions = {"Skriv betegnelse her", "Indtast beskrivelse", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
//            "Referencenummer til donator", "Referencenummer til producent", "Indtast postnummer her"};
//
//    ListView lv;
//    BaseAdapter listAdapter;
//    ImageView imageView;
//
//    //Inflate ActionBar for Opret Genstand fragment
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_createitem, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_create_main: //Der blev trykket på "Opret" knappen i Opret Genstand actionbaren
//                // evt. dialog her der spørger om man er sikker på at man vil oprette?
//                //TODO Tjek at i det mindste Betegnelse er indskrevet, og om man evt. har oprettet flere gange i træk?
//                Map<String,Object> postParams = new LinkedHashMap<>();
//                postParams.put("itemheadline", descriptions[0]);
//                postParams.put("itemdescription", descriptions[1]);
//                postParams.put("itemreceived", descriptions[2]);
//                postParams.put("itemdatingfrom", descriptions[3]);
//                postParams.put("itemdatingto", descriptions[4]);
//                postParams.put("donator", descriptions[5]);
//                postParams.put("producer", descriptions[6]);
//                postParams.put("postnummer", descriptions[7]);
//
//                PostHTTP postHTTP = new PostHTTP(postParams, getActivity(), getFragmentManager());
//                postHTTP.execute();
//                break;
//            case R.id.action_photo: //Der blev trykket på kamera ikonet
//                getFragmentManager().beginTransaction().addToBackStack(null).
//                        replace(R.id.fragmentContainer, new ImageBrowseFragment())
//                        .commit();
//                break;
//        }
//        return true;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getActivity().setTitle("Registrer genstand");
//
//        //Aktiver ActionBar menu med Opret knap
//        setHasOptionsMenu(true);
//
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); // aktivér "tilbage"-pil i venstre top
//
//        View root = inflater.inflate(R.layout.new_item_listviewdeprecated, container, false);
//
//        lv = (ListView) root.findViewById(R.id.newItem_listview);
//
//        // Opsætning af ArrayAdapter der bruges til at bestemme hvordan listview skal vises
//        listAdapter = new ArrayAdapter(getActivity(), R.layout.newitemlayoutdeprecated, R.id.listHeader, fields) {
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//
//                TextView beskrivelse = (TextView) view.findViewById(R.id.listDescription);
//                beskrivelse.setText(descriptions[position]);
//                return view;
//            }
//        };
//
//        lv.setAdapter(listAdapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                      @Override
//                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                          switch (position) {
//                                              case 0: // Betegnelse
//                                                  showInputPrompt(position);
//                                                  break;
//
//                                              case 1: // Beskrivelse
//                                                  showInputPrompt(position);
//                                                  break;
//
//                                              case 2: // Modtagelsesdato
//                                                  showDatePickerFragment(position);
//                                                  break;
//
//                                              case 3: // Datering fra
//                                                  showDatePickerFragment(position);
//                                                  break;
//
//                                              case 4: // Datering til
//                                                  showDatePickerFragment(position);
//                                                  break;
//
//                                              case 5: // Ref. til donator
//                                                  showInputPrompt(position);
//                                                  break;
//
//                                              case 6: // Ref. til producent
//                                                  showInputPrompt(position);
//                                                  break;
//
//                                              case 7: // Postnummer
//                                                  showInputPrompt(position);
//                                                  break;
//                                          }
//                                      }
//                                  }
//        );
//        return root;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) { // fanger billede resultat fra camera intent
//        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
//        }
//    }
//
//    public void showInputPrompt(int position) {
//        // Nødvendig kode for at få korrekt index når der er scrolled i listen
//        int wantedPosition = position; // Whatever position you're looking for
//        int firstPosition = lv.getFirstVisiblePosition() - lv.getHeaderViewsCount(); // This is the same as child #0
//        final int index = wantedPosition - firstPosition;
//
//        // get prompts.xml view
//        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
//
//        final View promptView = layoutInflater.inflate(R.layout.promptinput, null);
//        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//
//        // set prompts.xml to be the layout file of the alertdialog builder
//        alertDialogBuilder.setView(promptView);
//        final TextView inputHeader = (TextView) promptView.findViewById(R.id.text_inputPrompt);
//        final EditText input = (EditText) promptView.findViewById(R.id.userInput);
//
//        inputHeader.setText(((TextView) lv.getChildAt(index).findViewById(R.id.listHeader)).getText()); // sætter overskrift på inputDialog til overskrift fra den klikkede række
//        input.setText(((TextView) lv.getChildAt(index).findViewById(R.id.listDescription)).getText());
//
//        // setup a dialog window
//        alertDialogBuilder
//                .setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // get user input and set it to result
//                        descriptions[index] = input.getText().toString();
//                        listAdapter.notifyDataSetChanged();
//                    }
//                })
//                .setNegativeButton("Fortryd",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//        AlertDialog promptDialog_1 = alertDialogBuilder.create();
//        promptDialog_1.show();
//        // Viser tastatur
//        promptDialog_1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        promptDialog_1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//    }
//
//    public void showDatePickerFragment(final int position) {
//        DialogFragment date = new DatePickerDialogFragment() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                descriptions[position] = "" + day + "-" + (month + 1) + "-" + year;
//                listAdapter.notifyDataSetChanged();
//            }
//        };
//        date.show(getFragmentManager(), "datePicker");
//    }
//}