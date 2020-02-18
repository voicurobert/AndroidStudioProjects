package rvo.com.book.android.main_app.firm_login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import rvo.com.book.R;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.common.Validator;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.interfaces.IObjectModified;


/**
 * Created by Robert on 28/03/2018.
 */

public class FirmSignInActivity extends FragmentActivity {

    private EditText passwordEditText = null;
    private EditText reTypePasswordEditText = null;
    private EditText emailEditText = null;
    private EditText firmNameEditText = null;
    private EditText phoneNumberEditText = null;
    private FirmSignInActivity activity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firm_signin_activity);
        passwordEditText = findViewById(R.id.passwordEditTextId1);
        handleOnFocusLostForEditText(passwordEditText, "password");
        reTypePasswordEditText = findViewById(R.id.passwordEditTextId2);
        handleOnFocusLostForEditText(reTypePasswordEditText, "retypePassword");
        emailEditText = findViewById(R.id.emailEditTextId);
        handleOnFocusLostForEditText(emailEditText, "email");
        firmNameEditText = findViewById(R.id.firmNameEditTextId);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditTextId);
        handleOnFocusLostForEditText(phoneNumberEditText, "phoneNumber");
        Button nextButton = findViewById(R.id.nextFirmButtonId);
        progressBar = findViewById(R.id.firmSignInProgressBarId);
        activity = this;
        nextButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            if (canSignInButtonBePressed()) {
                String email = Validator.getEmailFromEditText(emailEditText);
                FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Eight.firestoreManager.firmOwnerFromEmail(email, object -> {
                            if (object != null) {
                                EightAlertDialog.showAlertWithMessage("Email exists, please set another email!", activity);
                            } else {
                                insertFirmOwner(() -> {
                                    activateSetFirmAddressActivity();
                                    progressBar.setVisibility(View.GONE);
                                    finishAndRemoveTask();
                                });
                            }
                        });
                    }
                });
            }
        });
    }

    private void insertFirmOwner(IObjectModified objectModified) {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (task.isComplete()) {
                Firm firm = new Firm();
                firm.setName(Validator.getNameFromEditText(firmNameEditText));
                firm.setEmail(Validator.getEmailFromEditText(emailEditText));
                firm.setPassword(Validator.getPasswordFromEditText(passwordEditText));
                firm.setPhoneNumber(Validator.getPhoneNumberFromEditText(phoneNumberEditText));
                firm.setActive(0);
                Eight.dataModel.setFirm(firm);
                objectModified.objectModified();
            }
        });

    }

    private void activateSetFirmAddressActivity() {
        Intent intent = new Intent(getApplicationContext(), FirmLocationMapActivity.class);
        intent.putExtra("context", "setAddress");
        startActivity(intent);
    }

    private void handleOnFocusLostForEditText(EditText editText, final String type) {
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                switch (type) {
                    case "email":
                        String email = Validator.getEmailFromEditText(emailEditText);
                        if (email == null) {
                            EightAlertDialog.showAlertWithMessage("Email is not set!", activity);
                        } else if (email.equals("")) {
                            EightAlertDialog.showAlertWithMessage("Email is incorrect", activity);
                        }
                        break;
                    case "password":
                    case "retypedPassword":
                        String p1 = Validator.getPasswordFromEditText(passwordEditText);
                        String p2 = Validator.getPasswordFromEditText(reTypePasswordEditText);
                        if (p1 != null && p2 != null && !p1.equals("") && !p2.equals("") &&
                            !p1.equals(p2)) {
                            EightAlertDialog.showAlertWithMessage("Passwords does not match", activity);
                            break;
                        }
                    case "phoneNumber":
                        String phoneNumber = Validator
                                .getPhoneNumberFromEditText(phoneNumberEditText);
                        if (phoneNumber != null && phoneNumber.equals("")) {
                            EightAlertDialog.showAlertWithMessage("Phone number is incorrect", activity);
                            break;
                        }
                }
            }
        });
    }

    private boolean canSignInButtonBePressed() {
        String email = Validator.getEmailFromEditText(emailEditText);
        if (email == null) {
            EightAlertDialog.showAlertWithMessage("Email is not set!", this);
            return false;
        } else if (email.equals("")) {
            EightAlertDialog.showAlertWithMessage("Email is not correct!", this);
            return false;
        }
        if (Validator.getNameFromEditText(firmNameEditText) == null) {
            EightAlertDialog.showAlertWithMessage("Firm name is not set!", activity);
            return false;
        }
        String p1 = Validator.getPasswordFromEditText(passwordEditText);
        if (p1 == null) {
            EightAlertDialog.showAlertWithMessage("Password is not set!", activity);
            return false;
        }
        String p2 = Validator.getPasswordFromEditText(reTypePasswordEditText);
        if (p2 == null) {
            EightAlertDialog.showAlertWithMessage("Retyped Password is not set!", activity);
            return false;
        }
        if (!p1.equals(p2)) {
            EightAlertDialog.showAlertWithMessage("Passwords does not match", activity);
            return false;
        }
        String phoneNumber = Validator.getPhoneNumberFromEditText(phoneNumberEditText);
        if (phoneNumber == null) {
            EightAlertDialog.showAlertWithMessage("Phone number is not set!", activity);
            return false;
        } else if (phoneNumber.equals("")) {
            EightAlertDialog.showAlertWithMessage("Phone number is incorrect!", activity);
            return false;
        }
        return true;
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
