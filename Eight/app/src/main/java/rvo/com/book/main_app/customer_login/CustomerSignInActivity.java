package rvo.com.book.main_app.customer_login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import rvo.com.book.R;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.common.Validator;
import rvo.com.book.firebase.FirebaseAuthManager;
import rvo.com.book.firebase.FirebaseProperties;


public class CustomerSignInActivity extends FragmentActivity {
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText retypePasswordEditText;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private CustomerSignInActivity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.customer_signin_activity);
        nameEditText = findViewById(R.id.customerNameEditTextId);
        handleFocusForEditText(nameEditText, "name");
        passwordEditText = findViewById(R.id.customerPasswordEditTextId);
        handleFocusForEditText(passwordEditText, "password");
        retypePasswordEditText = findViewById(R.id.customerRetypePasswordEditTextId);
        handleFocusForEditText(retypePasswordEditText, "retypePassword");
        emailEditText = findViewById(R.id.customerEmailEditTextId);
        handleFocusForEditText(emailEditText, "email");
        phoneNumberEditText = findViewById(R.id.customerPhoneNumberEditTextId);
        handleFocusForEditText(phoneNumberEditText, "phoneNumber");
        Button registerCustomerButton = findViewById(R.id.registerCustomerButtonId);
        registerCustomerButton.setOnClickListener(v -> registerCustomerButtonClicked());
    }

    public void handleFocusForEditText(EditText editText, String key) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                switch (key) {
                    case "email":
                        String email = Validator.getEmailFromEditText(emailEditText);
                        if (email == null) {
                            EightAlertDialog.showAlertWithMessage("Email is not set!", activity);
                        } else if (email.equals("")) {
                            EightAlertDialog.showAlertWithMessage("Email is incorrect", activity);
                        }
                        break;
                    case "retypePassword":
                        String p1 = Validator.getPasswordFromEditText(passwordEditText);
                        String p2 = Validator.getPasswordFromEditText(retypePasswordEditText);
                        if (p1 != null && p2 != null && !p2.equals(p1)) {
                            EightAlertDialog.showAlertWithMessage("Passwords does not match!", activity);
                        }
                        break;
                    case "phoneNumber":
                        String phoneNumber = Validator.getPhoneNumberFromEditText(phoneNumberEditText);
                        if (phoneNumber != null && phoneNumber.equals("")) {
                            EightAlertDialog.showAlertWithMessage("Phone number is incorrect", activity);
                            break;
                        }
                }
            }
        });
    }

    private void registerCustomerButtonClicked() {
        final String name = Validator.getNameFromEditText(nameEditText);
        if (name == null) {
            EightAlertDialog.showAlertWithMessage("Name is not set!", this);
            return;
        }
        final String password = Validator.getPasswordFromEditText(passwordEditText);
        if (password == null) {
            EightAlertDialog.showAlertWithMessage("Password is not set!", this);
            return;
        }
        final String retypedPassword = Validator.getPasswordFromEditText(retypePasswordEditText);
        if (retypedPassword == null) {
            EightAlertDialog.showAlertWithMessage("Password is not set!", this);
            return;
        }
        if (!password.equals(retypedPassword)) {
            EightAlertDialog.showAlertWithMessage("Passwords does not match!", this);
            return;
        }
        final String email = Validator.getEmailFromEditText(emailEditText);
        if (email == null || email.equals("")) {
            EightAlertDialog.showAlertWithMessage("Email is not set!", this);
            return;
        }
        final String phoneNumber = Validator.getPhoneNumberFromEditText(phoneNumberEditText);
        if (phoneNumber == null) {
            EightAlertDialog.showAlertWithMessage("Phone number is not set!", this);
            return;
        } else {
            if (phoneNumber.equals("")) {
                EightAlertDialog.showAlertWithMessage("Phone number is incorrect!", this);
                return;
            }
        }
        FirebaseAuthManager.getInstance().getFirebaseAuth().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (task.isComplete()) {
                Eight.firestoreManager.customerWithEmail(email, object -> {
                    if (object != null) {
                        EightAlertDialog.showAlertWithMessage("Email already exists! Please enter another email!", activity);
                    } else {
                        Eight.firestoreManager.writeCustomer(email, name, password, phoneNumber, FirebaseProperties.getInstance().getCurrentToken());
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }
}
