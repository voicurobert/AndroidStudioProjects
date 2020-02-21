package rvo.com.book.android.main_app.reset_password;

import android.app.Activity;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

import rvo.com.book.R;

public class ForgotPassword {

    public static final String CUSTOMER = "customer";
    public static final String FIRM = "firm";

    public static void handleForgotPasswordButtonClick(Activity activity, String context) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.reset_password));
        builder.setPositiveButton(activity.getString(R.string.yes), (dialog, which) -> activateResetPasswordActivity(activity, context));
        builder.setNegativeButton(activity.getString(R.string.no), (dialog, which) -> dialog.cancel());
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void activateResetPasswordActivity(Activity activity, String context) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (task.isComplete()) {
                Intent intent = new Intent(activity, ResetPasswordActivity.class);
                intent.putExtra("context", context);
                activity.startActivity(intent);
            }
        });
    }


}
