package rvo.holidayplanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Robert on 7/3/2015.
 */
public class ShowProgressDialog extends DialogFragment {
    private String message = "";

    public ShowProgressDialog( ){

    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = View.inflate(getActivity(), R.layout.progress_dialog_view, null);
        builder.setView(v);
        builder.setMessage(message);

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
