package rvo.com.book.reset_password;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import rvo.com.book.R;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.common.Validator;

public class ResetPasswordActivity extends FragmentActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;
    private ResetPasswordActivity activity;
    private String context;
    private String password;
    private String email;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);

        context = (String) getIntent().getSerializableExtra("context");
        activity = this;
        progressBar = findViewById(R.id.resetPasswordProgressBar);
        emailEditText = findViewById(R.id.resetEmailText);
        handleFocusForEditText(emailEditText, "email");
        passwordEditText = findViewById(R.id.resetPasswordEditTextId);

        retypePasswordEditText = findViewById(R.id.resetRetypePasswordEditTextId);
        handleFocusForEditText(retypePasswordEditText, "retypePassword");
        Button resetButton = findViewById(R.id.resetButtonId);

        resetButton.setOnClickListener(v -> {
            String message = validateEditTexts();
            if (message != null) {
                EightAlertDialog.showAlertWithMessage(message, activity);
            } else {
                showProgress();
                ForgotPassword.updatePasswordIfConnected(activity, password, context, email, () -> {
                    EightAlertDialog.showAlertWithMessage(getString(R.string.password_change), activity);
                    goneProgress();
                    activity.finish();
                });
            }
        });
    }

    public void handleFocusForEditText(EditText editText, final String key) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                switch (key) {
                    case "email":
                        final String email = Validator.getEmailFromEditText(emailEditText);
                        if (email == null) {
                            EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_set), activity);
                        } else if (email.equals("")) {
                            EightAlertDialog.showAlertWithMessage(getString(R.string.email_empty), activity);
                        } else {
                            if (context.equals("customer")) {
                                showProgress();
                                Eight.firestoreManager.customerWithEmail(email, object -> {
                                    if (object == null) {
                                        EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_exists), activity);
                                    }
                                    goneProgress();
                                });
                            } else {
                                showProgress();
                                Eight.firestoreManager.firmOwnerFromEmail(email, object -> {
                                    if (object == null) {
                                        EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_exists), activity);
                                    }
                                    goneProgress();
                                });
                            }
                        }
                        break;
                    case "retypePassword":
                        String p1 = Validator.getPasswordFromEditText(passwordEditText);
                        String p2 = Validator.getPasswordFromEditText(retypePasswordEditText);
                        if (p1 != null && p2 != null && !p2.equals(p1)) {
                            EightAlertDialog.showAlertWithMessage(getString(R.string.passwords_not_match), activity);
                        }
                        break;
                }
            }
        });
    }

    private String validateEditTexts() {
        String retypedPassword;
        String message = null;
        if (emailEditText.getText().toString().equals("")) {
            message = getString(R.string.enter_email);
            emailEditText.requestFocus();
            return message;
        } else {
            email = Validator.getEmailFromEditText(emailEditText);
        }
        if (passwordEditText.getText().toString().equals("")) {
            message = getString(R.string.enter_password);
            passwordEditText.requestFocus();
            return message;
        } else {
            // it is encrypted
            password = Validator.getPasswordFromEditText(passwordEditText);
        }
        if (retypePasswordEditText.getText().toString().equals("")) {
            message = getString(R.string.enter_password);
            retypePasswordEditText.requestFocus();
            return message;
        } else {
            // it is encrypted
            retypedPassword = Validator.getPasswordFromEditText(retypePasswordEditText);
        }
        if (password != null && retypedPassword != null) {
            if (!password.equals(retypedPassword)) {
                message = getString(R.string.passwords_not_match);
            }
        }
        return message;
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void goneProgress() {
        progressBar.setVisibility(View.GONE);
    }
}
