package hyltofthansen.ddhfregistrering;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import static android.app.ProgressDialog.show;

public class NewItem extends Fragment {

    String[] fields = {"Genstand nr.", "Betegnelse", "Modtagelsesdato", "Datering fra", "Datering til",
            "Beskrivelse", "Ref. til donator", "Ref. til producent", "Geografisk område", "Ref. til emnegruppe(r)"};

    String[] descriptions = {"Skriv genstand nummer her", "Skriv betegnelse her", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
            "Indtast beskrivelse", "Referencenummer til donator", "Referencenummer til producent", "Vælg kommune her", "Referencenummer til emnegruppe(r)"};

    ListView lv;
    BaseAdapter listAdapter;
    ActionBar actionBar;


    //Inflate ActionBar for Opret Genstand fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_createitem, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Der blev trykket på "Opret" knappen i Opret Genstand actionbaren
            case R.id.action_create:
                System.out.println("Opret knap trykket");

                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        /**
                         *  Opret nyt item:
                            http POST "http://78.46.187.172:4019/items?itemheadline=test&itemdescription=blahblahblah"

                         Venligst udlånt fra http://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
                         */
                        URL url = null;
                        StringBuffer response = new StringBuffer();

                        try {
                            url = new URL("http://78.46.187.172:4019/items");       //Denne URL bør hentes ét sted fra, hvis Server ip skal skiftes
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        Map<String,Object> postParams = new LinkedHashMap<>();
                        postParams.put("itemheadline", "Hest");
                        postParams.put("itemdescription", "Dette er en hest");
                        System.out.println("Async oprettet");

                        //Tilføj selv flere
                        try {

                            //Opretter POST URL
                            StringBuilder postData = new StringBuilder();
                            for (Map.Entry<String, Object> param : postParams.entrySet()) {
                                if (postData.length() != 0) postData.append('&');

                                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));

                                postData.append('=');
                                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                            }
                            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                            System.out.println("PostData incoming");
                            System.out.println(postData);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                            conn.setDoOutput(true);
                            conn.getOutputStream().write(postDataBytes);

                            int responseCode = conn.getResponseCode();
                            System.out.println("\nSending 'GET' request to URL : " + url);
                            System.out.println("Response Code : " + responseCode);

                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            String inputLine;

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            in.close();
                        }
                        catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (ProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                };
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Registrer ny genstand");

        //Aktiver ActionBar menu med Opret knap
        setHasOptionsMenu(true);

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
