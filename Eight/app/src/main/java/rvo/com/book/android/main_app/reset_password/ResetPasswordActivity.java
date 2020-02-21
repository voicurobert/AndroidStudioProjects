package rvo.com.book.android.main_app.reset_password;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentActivity;

import rvo.com.book.R;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.common.Tools;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.CustomerRepository;
import rvo.com.book.datamodel.repositories.FirebaseRepository;
import rvo.com.book.datamodel.repositories.FirmRepository;

public class ResetPasswordActivity extends FragmentActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;
    private ResetPasswordActivity activity;
    private String context;
    private String password;
    private String email;
    private ProgressBar progressBar;


    @Override protected void onCreate(Bundle savedInstanceState) {
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
                updatePasswordIfConnected(activity, password, email, context);
            }
        });
    }

    private void updatePasswordIfConnected(Activity activity, String password, String email, String context) {
        FirebaseRepository repository;
        if (context.equals(ForgotPassword.CUSTOMER)) {
            repository = CustomerRepository.getInstance();
        } else {
            repository = FirmRepository.getInstance();
        }
        repository.objectFromEmail(email, object -> {
            if (object == null) {
                EightAlertDialog.showAlertWithMessage(activity.getString(R.string.email_not_exists), activity);
                return;
            }
            if (object instanceof Customer) {
                Customer customer = (Customer)object;
                customer.setPassword(password);
                CustomerRepository.getInstance().updateRecord(customer, Customer.PASSWORD, customer.getPassword()).addOnCompleteListener(command -> {
                    EightAlertDialog.showAlertWithMessage(getString(R.string.password_change), activity);
                    goneProgress();
                    activity.finish();
                });
            } else if (object instanceof Firm) {
                Firm firm = (Firm)object;
                firm.setPassword(password);
                FirmRepository.getInstance().updateRecord(firm, Firm.PASSWORD, firm.getPassword()).addOnCompleteListener(command -> {
                    EightAlertDialog.showAlertWithMessage(getString(R.string.password_change), activity);
                    goneProgress();
                    activity.finish();
                });
            }
        });
    }

    public void handleFocusForEditText(EditText editText, String key) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                return;
            }
            switch (key) {
                case "email":
                    String email = Tools.getEmailFromEditText(emailEditText);
                    if (email == null) {
                        EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_set), activity);
                    } else if (email.equals("")) {
                        EightAlertDialog.showAlertWithMessage(getString(R.string.email_empty), activity);
                    } else {
                        showProgress();
                        if (context.equals("customer")) {
                            CustomerRepository.getInstance().objectFromEmail(email, object -> {
                                if (object == null) {
                                    EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_exists), activity);
                                }
                                goneProgress();
                            });
                        } else {
                            FirmRepository.getInstance().objectFromEmail(email, object -> {
                                if (object == null) {
                                    EightAlertDialog.showAlertWithMessage(getString(R.string.email_not_exists), activity);
                                }
                                goneProgress();
                            });
                        }
                    }
                    break;
                case "retypePassword":
                    String p1 = Tools.getPasswordFromEditText(passwordEditText);
                    String p2 = Tools.getPasswordFromEditText(retypePasswordEditText);
                    if (p1 != null && p2 != null && !p2.equals(p1)) {
                        EightAlertDialog.showAlertWithMessage(getString(R.string.passwords_not_match), activity);
                    }
                    break;
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
            email = Tools.getEmailFromEditText(emailEditText);
        }
        if (passwordEditText.getText().toString().equals("")) {
            message = getString(R.string.enter_password);
            passwordEditText.requestFocus();
            return message;
        } else {
            // it is encrypted
            password = Tools.getPasswordFromEditText(passwordEditText);
        }
        if (retypePasswordEditText.getText().toString().equals("")) {
            message = getString(R.string.enter_password);
            retypePasswordEditText.requestFocus();
            return message;
        } else {
            // it is encrypted
            retypedPassword = Tools.getPasswordFromEditText(retypePasswordEditText);
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
