package rvo.com.book.android.main_app.firm_login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.main_app.EightMainAppActivity;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.android.main_app.reset_password.ForgotPassword;
import rvo.com.book.android.notification.FirebaseProperties;
import rvo.com.book.common.Eight;
import rvo.com.book.common.Validator;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.FirmRepository;


/**
 * Created by Robert on 28/03/2018.
 */

public class FirmLoginOrSignInActivity extends FragmentActivity {

    private Activity activity;
    private EditText emailEditText;
    private EditText passwordEditText;
    private boolean stayLoggedIn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firm_login_or_signin_activity);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> FirebaseProperties.getInstance().setCurrentToken(instanceIdResult.getToken()));

        activity = this;
        loginFirmIfAuthenticated();
        progressBar = findViewById(R.id.firmLoginProgressBarId);
        Button loginButton = findViewById(R.id.firmLoginButton);
        Button registerButton = findViewById(R.id.nextFirmButtonId);
        Button resetPasswordButton = findViewById(R.id.firmForgotPasswordButtonId);
        emailEditText = findViewById(R.id.firmEmailLoginEditTextId);
        emailEditText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                if (!emailEditText.getText().toString().equals("")) {
                    String email = Validator.getEmailFromEditText(emailEditText);
                    if (email != null && email.equals("")) {
                        EightAlertDialog.showAlertWithMessage("Email is incorrect", activity);
                        emailEditText.requestFocus();
                    }
                }
            }
        });
        passwordEditText = findViewById(R.id.firmPasswordLoginEditTextId);
        passwordEditText.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                String password = Validator.getPasswordFromEditText(passwordEditText);
                if (password != null && password.equals("")) {
                    EightAlertDialog.showAlertWithMessage("Password is incorrect", activity);
                    passwordEditText.requestFocus();
                }
            }

        });
        CheckBox stayLoggedIdRadioButton = findViewById(R.id.firmStayLoggedIdCheckBoxId);
        stayLoggedIdRadioButton.setOnCheckedChangeListener((compoundButton, b) -> stayLoggedIn = b);
        loginButton.setOnClickListener(view -> {
            String email = Validator.getEmailFromEditText(emailEditText);
            if (email == null) {
                EightAlertDialog.showAlertWithMessage("Set email", activity);
                emailEditText.requestFocus();
                return;
            }
            String password = Validator.getPasswordFromEditText(passwordEditText);
            if (password == null) {
                EightAlertDialog.showAlertWithMessage("Set password", activity);
                passwordEditText.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
                    if (task.isComplete()) {
                        checkForEmailAndPasswordAndSetFirm(email, password);
                    }
                });
            } else if (firebaseUser.isAnonymous()) {
                AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
                firebaseUser.linkWithCredential(authCredential).addOnCompleteListener(activity, task -> {

                    if (task.isComplete()) {
                        checkForEmailAndPasswordAndSetFirm(email, password);
                    }

                });
            } else if (!firebaseUser.isEmailVerified()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> checkForEmailAndPasswordAndSetFirm(email, password));
            }
        });
        registerButton.setOnClickListener(view -> activateAddFirmActivity());

        resetPasswordButton.setOnClickListener(view -> ForgotPassword.handleForgotPasswordButtonClick(activity, ForgotPassword.FIRM));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void activateAddFirmActivity() {
        Intent activityIntent = new Intent(this.getApplicationContext(), FirmSignInActivity.class);
        startActivity(activityIntent);
    }

    private void activateEightMainAppActivity() {
        Intent activityIntent = new Intent(getApplicationContext(), EightMainAppActivity.class);
        Eight.dataModel.initialiseDataStore(null, () -> {
            startActivity(activityIntent);
            finish();
        });
    }

    private void loginFirmIfAuthenticated() {
        if (!EightSharedPreferences.getInstance().isSharedPreferencesSet()) {
            EightSharedPreferences.getInstance().createSharedPreferencesForActivity(this);
        }
        String email = EightSharedPreferences.getInstance().getString(EightSharedPreferences.FIRM_EMAIL_KEY);
        String password = EightSharedPreferences.getInstance().getString(EightSharedPreferences.FIRM_PASSWORD_KEY);
        if (email == null && password == null) {
            return;
        }
        checkForEmailAndPasswordAndSetFirm(email, password);
    }

    private void checkForEmailAndPasswordAndSetFirm(String email, String password) {
        FirmRepository firmRepository = new FirmRepository();
        firmRepository.objectFromEmail(email, response -> {
            if (response == null) {
                EightAlertDialog.showAlertWithMessage("Email incorrect!", activity);
                progressBar.setVisibility(View.GONE);
                emailEditText.requestFocus();
            } else {
                firmRepository.objectFromEmailAndPassword(email, password, object -> {
                    if (object != null) {
                        if (stayLoggedIn) {
                            // save to shared prefs
                            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.FIRM_EMAIL_KEY, email, activity);
                            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.FIRM_PASSWORD_KEY, password, activity);
                        }
                        Firm firm = (Firm) object;
                        Eight.dataModel.setFirm(firm);
                        Eight.firestoreManager.setFirmFirebaseToken(FirebaseProperties.getInstance().getCurrentToken());
                        //activateBookForCompaniesActivity();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> activateEightMainAppActivity());

                        progressBar.setVisibility(View.GONE);
                    } else {
                        EightAlertDialog.showAlertWithMessage("Password incorrect!", activity);
                        passwordEditText.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
