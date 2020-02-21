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
import rvo.com.book.common.Tools;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.FirmRepository;


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
            if (canSignInButtonBePressed()) {
                progressBar.setVisibility(View.VISIBLE);
                String email = Tools.getEmailFromEditText(emailEditText);
                FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        FirmRepository.getInstance().objectFromEmail(email, object -> {
                            if (object != null) {
                                EightAlertDialog.showAlertWithMessage("Email exists, please set another email!", activity);
                            } else {
                                insertFirmOwner();
                            }
                        });
                    }
                });
            }
        });
    }

    private void insertFirmOwner() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(activity, task -> {
            if (task.isComplete()) {
                Firm firm = new Firm();
                firm.setName(Tools.getNameFromEditText(firmNameEditText));
                firm.setEmail(Tools.getEmailFromEditText(emailEditText));
                firm.setPassword(Tools.getPasswordFromEditText(passwordEditText));
                firm.setPhoneNumber(Tools.getPhoneNumberFromEditText(phoneNumberEditText));
                firm.setStatus(0);
                DataModel.getInstance().setFirm(firm);
                activateSetFirmAddressActivity();
                progressBar.setVisibility(View.GONE);
                finishAndRemoveTask();
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
                        String email = Tools.getEmailFromEditText(emailEditText);
                        if (email == null) {
                            EightAlertDialog.showAlertWithMessage("Email is not set!", activity);
                        } else if (email.equals("")) {
                            EightAlertDialog.showAlertWithMessage("Email is incorrect", activity);
                        }
                        break;
                    case "password":
                    case "retypedPassword":
                        String p1 = Tools.getPasswordFromEditText(passwordEditText);
                        String p2 = Tools.getPasswordFromEditText(reTypePasswordEditText);
                        if (p1 != null && p2 != null && !p1.equals("") && !p2.equals("") &&
                            !p1.equals(p2)) {
                            EightAlertDialog.showAlertWithMessage("Passwords does not match", activity);
                            break;
                        }
                    case "phoneNumber":
                        String phoneNumber = Tools
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
        String email = Tools.getEmailFromEditText(emailEditText);
        if (email == null) {
            EightAlertDialog.showAlertWithMessage("Email is not set!", this);
            return false;
        } else if (email.equals("")) {
            EightAlertDialog.showAlertWithMessage("Email is not correct!", this);
            return false;
        }
        if (Tools.getNameFromEditText(firmNameEditText) == null) {
            EightAlertDialog.showAlertWithMessage("Firm name is not set!", activity);
            return false;
        }
        String p1 = Tools.getPasswordFromEditText(passwordEditText);
        if (p1 == null) {
            EightAlertDialog.showAlertWithMessage("Password is not set!", activity);
            return false;
        }
        String p2 = Tools.getPasswordFromEditText(reTypePasswordEditText);
        if (p2 == null) {
            EightAlertDialog.showAlertWithMessage("Retyped Password is not set!", activity);
            return false;
        }
        if (!p1.equals(p2)) {
            EightAlertDialog.showAlertWithMessage("Passwords does not match", activity);
            return false;
        }
        String phoneNumber = Tools.getPhoneNumberFromEditText(phoneNumberEditText);
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
