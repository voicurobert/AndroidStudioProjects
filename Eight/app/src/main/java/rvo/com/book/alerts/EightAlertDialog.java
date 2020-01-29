package rvo.com.book.alerts;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;


public class EightAlertDialog {
    private static final String DEFAULT_DIALOG_TITLE = "Alert!";

    private EightAlertDialog() {

    }

    public static void showAlertWithMessage(String message, Activity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(DEFAULT_DIALOG_TITLE);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
