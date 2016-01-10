package hyltofthansen.ddhfregistrering.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by hylle on 23-12-2015.
 * Inspiration fra http://developer.android.com/guide/topics/ui/controls/pickers.html
 */
@SuppressLint("ValidFragment")
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    EditText editText;
    Boolean today;

    /**
     * Sæt hvilken editText der skal sættes dato på, og om datoen pr. default skal være dags dato.
     * @param editText Hvilken edittext skal der indsættes dato på?
     */
    public DatePickerFragment (EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //editText.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Bruger dags dato som default dato her
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

}
