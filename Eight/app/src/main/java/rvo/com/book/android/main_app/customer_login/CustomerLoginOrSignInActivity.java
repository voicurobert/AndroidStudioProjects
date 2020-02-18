package rvo.com.book.android.main_app.customer_login;

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
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.common.Validator;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.android.notification.FirebaseProperties;
import rvo.com.book.android.main_app.EightMainAppActivity;
import rvo.com.book.android.main_app.reset_password.ForgotPassword;
import rvo.com.book.datamodel.repositories.FirestoreManager;

public class CustomerLoginOrSignInActivity extends FragmentActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox stayLoggedIn;
    private CustomerLoginOrSignInActivity activity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login_or_signin_activity);

        FirebaseInstanceId.getInstance()
                          .getInstanceId()
                          .addOnSuccessListener(instanceIdResult -> FirebaseProperties.getInstance().setCurrentToken(instanceIdResult.getToken()));
        activity = this;
        progressBar = findViewById(R.id.customerLoginOrSignInProgressBarId);
        logInCustomerIfAuthenticated();

        Button loginButton = findViewById(R.id.customerLoginButton);
        Button signInButton = findViewById(R.id.customerSignInButtonId);
        Button signInButtonLater = findViewById(R.id.customerSignInLaterButtonId);
        Button resetPasswordButton = findViewById(R.id.customerForgotPasswordButtonId);
        loginButton.setOnClickListener(v -> loginButtonClicked());
        signInButton.setOnClickListener(v -> activateSignUpCustomerActivity());
        emailEditText = findViewById(R.id.customerEmailLoginEditTextId);
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // focus lost
                if (!emailEditText.getText().toString().equals("")) {
                    String email = Validator.getEmailFromEditText(emailEditText);
                    if (email != null && email.equals("")) {
                        EightAlertDialog.showAlertWithMessage("Email is incorrect!", activity);
                    }
                }
            }
        });
        passwordEditText = findViewById(R.id.customerPasswordLoginEditTextId);
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Validator.getEmailFromEditText(emailEditText);
                Validator.getPasswordFromEditText(passwordEditText);
            }

        });
        stayLoggedIn = findViewById(R.id.customerStayLoggedIdCheckBoxId);

        resetPasswordButton.setOnClickListener(v -> ForgotPassword.handleForgotPasswordButtonClick(activity, ForgotPassword.CUSTOMER));

        signInButtonLater.setOnClickListener(v -> signInLaterButtonClicked());
    }

    public void signInLaterButtonClicked() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isComplete()) {
                progressBar.setVisibility(View.GONE);
                activateEightMainAppActivity();
            }
        });
    }

    private void loginButtonClicked() {
        String email = Validator.getEmailFromEditText(emailEditText);
        String password = Validator.getPasswordFromEditText(passwordEditText);
        if (email == null) {
            EightAlertDialog.showAlertWithMessage("Email not set!", activity);
            return;
        } else if (email.equals("")) {
            EightAlertDialog.showAlertWithMessage("Email is incorrect!", activity);
            return;
        }
        if (password == null) {
            EightAlertDialog.showAlertWithMessage("Password not set", activity);
            return;
        }
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentFirebaseUser = firebaseAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);
        if (currentFirebaseUser != null) {
            if (currentFirebaseUser.isAnonymous() && !currentFirebaseUser.isEmailVerified()) {
                AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
                currentFirebaseUser.linkWithCredential(authCredential).addOnCompleteListener(this, task -> {
                    if (task.isComplete()) {
                        checkForEmailAndPasswordAndSetCustomer(email, password);
                    }
                });
            } else if (currentFirebaseUser.isAnonymous() || !currentFirebaseUser.isEmailVerified()) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> checkForEmailAndPasswordAndSetCustomer(email, password));

            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> checkForEmailAndPasswordAndSetCustomer(email, password));
            }
        } else {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
                if (task.isComplete()) {
                    checkForEmailAndPasswordAndSetCustomer(email, password);
                }
            });
        }
    }

    public void logInCustomerIfAuthenticated() {
        String email = getEmailFromSharedPrefs();
        String password = getPasswordFromSharedPrefs();
        if (email == null && password == null) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        checkForEmailAndPasswordAndSetCustomer(email, password);
    }

    private void checkForEmailAndPasswordAndSetCustomer(String email, String password) {
        FirestoreManager.getInstance().customerWithEmail(email, response1 -> {
            if (response1 == null) {
                EightAlertDialog.showAlertWithMessage("Email incorrect!", activity);
                emailEditText.requestFocus();
                progressBar.setVisibility(View.GONE);
            }else{
                Eight.firestoreManager.customerWithEmailAndPassword(email, password, object -> {
                    if (object == null) {
                        EightAlertDialog.showAlertWithMessage("Password incorrect!", activity);
                        passwordEditText.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        if (stayLoggedIn.isChecked()) {
                            // save to shared prefs
                            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.CUSTOMER_EMAIL_KEY, email, activity);
                            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.CUSTOMER_PASSWORD_KEY, password, activity);
                        }
                        Eight.dataModel.setCustomer((Customer) object);
                        progressBar.setVisibility(View.GONE);
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> activateEightMainAppActivity());
                    }
                });
            }
        });
    }

    private void activateEightMainAppActivity() {
        Customer customer = Eight.dataModel.getCustomer();
        if ( customer != null) {
            customer.setFirebaseToken(FirebaseProperties.getInstance().getCurrentToken());
            Eight.firestoreManager.updateCustomerFirebaseToken(customer);
        }
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getApplicationContext(), EightMainAppActivity.class);
        Eight.dataModel.initialiseActiveFirms(() -> {
            progressBar.setVisibility(View.GONE);
            startActivity(intent);
            finish();
        });
    }

    private void activateSignUpCustomerActivity() {
        Intent intent = new Intent(getApplicationContext(), CustomerSignInActivity.class);
        startActivity(intent);
    }


    private String getEmailFromSharedPrefs() {
        return EightSharedPreferences.getInstance().getString(EightSharedPreferences.CUSTOMER_EMAIL_KEY);
    }

    private String getPasswordFromSharedPrefs() {
        return EightSharedPreferences.getInstance().getString(EightSharedPreferences.CUSTOMER_PASSWORD_KEY);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
