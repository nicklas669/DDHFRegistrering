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
import java.io.OutputStreamWriter;
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

    String[] fields = {"Betegnelse", "Beskrivelse", "Modtagelsesdato", "Datering fra", "Datering til",
            "Ref. til donator", "Ref. til producent", "Postnummer"};

    String[] descriptions = {"Skriv betegnelse her", "Indtast beskrivelse", "Indtast modtagelsesdato", "Indtast datering fra", "Indtast datering til",
            "Referencenummer til donator", "Referencenummer til producent", "Indtast postnummer her"};

    ListView lv;
    BaseAdapter listAdapter;

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
                        Map<String,Object> postParams = new LinkedHashMap<>();
                        postParams.put("itemheadline", "Hest");
                        postParams.put("itemdescription", "Dette er en hest");
                        //Tilføj selv flere

                        try {
                            StringBuilder postData = new StringBuilder();
                            for (Map.Entry<String, Object> param : postParams.entrySet()) {
                                if (postData.length() != 0) postData.append('&');
                                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                                postData.append('=');
                                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                            }

                            //System.out.println("PostData incoming");
                            //System.out.println(postData);

                            //Opretter POST URL
                            try {
                                url = new URL(getString(R.string.URL)+"/items?"+postData);
                                System.out.println("url: "+url);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));
                            conn.setDoOutput(true);
                            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                            wr.write(postData.toString());
                            wr.flush();

                            int responseCode = conn.getResponseCode();
                            System.out.println("Response Code: " + responseCode);

                            conn.disconnect();

                            // Evt. læse svaret men ved ikke om vi har brug for andet end response code?

                            //BufferedReader in = new BufferedReader(
                            //        new InputStreamReader(conn.getInputStream()));
                            //String inputLine;

                            //while ((inputLine = in.readLine()) != null) {
                            //    response.append(inputLine);
                            //}
                            //in.close();
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
                }.execute();
                break;
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
                                          switch (position) {
                                              case 0: // Betegnelse
                                                  showInputPrompt(position);
                                                  break;

                                              case 1: // Beskrivelse
                                                  showInputPrompt(position);
                                                  break;

                                              case 2: // Modtagelsesdato
                                                  showDatePickerFragment(position);
                                                  break;

                                              case 3: // Datering fra
                                                  showDatePickerFragment(position);
                                                  break;

                                              case 4: // Datering til
                                                  showDatePickerFragment(position);
                                                  break;

                                              case 5: // Ref. til donator
                                                  showInputPrompt(position);
                                                  break;

                                              case 6: // Ref. til producent
                                                  showInputPrompt(position);
                                                  break;

                                              case 7: // Postnummer
                                                  showInputPrompt(position);
                                                  break;
                                          }
                                      }
                                  }
        );
        return root;
    }

    public void showInputPrompt(int position) {
        System.out.println("KALDER showInputPrompt med POSITION: "+position);

        // Nødvendig kode for at få korrekt index når der er scrolled i listen
        int wantedPosition = position; // Whatever position you're looking for
        int firstPosition = lv.getFirstVisiblePosition() - lv.getHeaderViewsCount(); // This is the same as child #0
        final int index = wantedPosition - firstPosition;

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        final View promptView = layoutInflater.inflate(R.layout.promptinput, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to be the layout file of the alertdialog builder
        alertDialogBuilder.setView(promptView);
        final TextView inputHeader = (TextView) promptView.findViewById(R.id.text_inputPrompt);
        final EditText input = (EditText) promptView.findViewById(R.id.userInput);


        System.out.println("SÆTTER INPUTHEADER... index: "+index);
        inputHeader.setText(((TextView) lv.getChildAt(index).findViewById(R.id.listHeader)).getText()); // sætter overskrift på inputDialog til overskrift fra den klikkede række
        input.setHint(((TextView) lv.getChildAt(index).findViewById(R.id.listDescription)).getText());

        // setup a dialog window
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println("DER BLEV TRYKKET OK OG index ER NU: "+index);
                        // get user input and set it to result
                        descriptions[index] = input.getText().toString();
                        listAdapter.notifyDataSetChanged();
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
        // Viser tastatur
        promptDialog_1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        promptDialog_1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void showDatePickerFragment(final int position) {
        DialogFragment date = new DatePickerFragment() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                descriptions[position] = "" + day + "-" + (month + 1) + "-" + year;
                listAdapter.notifyDataSetChanged();
            }
        };
        date.show(getFragmentManager(), "datePicker");
    }
}
