package hyltofthansen.ddhfregistrering;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Class responsible for creating various alert dialogs
 */
public class DialogCreater {

    private static Activity activity;
    private static Bundle savedInstanceState;
    private static String message;

    public DialogCreater(Activity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
    }

    public void showOKDialog(String message) {
        this.message = message;
        OKDialog okDialog = new OKDialog();
    }

    public static class OKDialog extends DialogFragment {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
