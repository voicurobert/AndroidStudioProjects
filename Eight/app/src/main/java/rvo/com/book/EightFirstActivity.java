package rvo.com.book;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.billing.BillingActivity;
import rvo.com.book.billing.InAppBilling;
import rvo.com.book.common.Eight;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.eight_db.Customer;
import rvo.com.book.eight_db.Firm;
import rvo.com.book.firebase.FirebaseAuthManager;
import rvo.com.book.firebase.FirebaseProperties;
import rvo.com.book.main_app.EightMainAppActivity;
import rvo.com.book.main_app.customer_login.CustomerLoginOrSignInActivity;
import rvo.com.book.main_app.firm_login.FirmLoginOrSignInActivity;


public class EightFirstActivity extends FragmentActivity {

    private EightFirstActivity activity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eight_first_activity);
        activity = this;
        progressBar = findViewById(R.id.bookWelcomeProgressBar);
        InAppBilling.initInstance(activity);
        FirebaseDatabase.getInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> FirebaseProperties.getInstance().setCurrentToken(instanceIdResult.getToken()));
        Eight.isGooglePlayServicesAvailable(this);
        if (!Eight.isConnectedToNetwork(getApplicationContext())) {
            EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
        }
        EightSharedPreferences.getInstance().createSharedPreferencesForActivity(this);

        if (EightSharedPreferences.getInstance().modeEnabled()) {
            if (EightSharedPreferences.getInstance().isFirmMode()) {
                handleFirmModeEnabled();
            } else if (EightSharedPreferences.getInstance().isCustomerMode()) {
                handleCustomerModeEnabled();
            }
        }

        Button continueAsFirmButton = findViewById(R.id.continueAsFirmButtonId);
        continueAsFirmButton.setOnClickListener(view -> {
            if (!Eight.isConnectedToNetwork(getApplicationContext())) {
                EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
                return;
            }
            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.MODE, EightSharedPreferences.FIRM_MODE, this);
            activateBillingOrFirmSignInOrLoginApp();
        });

        Button continueAsCustomerButton = findViewById(R.id.continueAsCustomerButtonId);
        continueAsCustomerButton.setOnClickListener(view -> {
            if (!Eight.isConnectedToNetwork(getApplicationContext())) {
                EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
                return;
            }
            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.MODE, EightSharedPreferences.CUSTOMER_MODE, this);
            activateCustomerSignInOrLogin();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!Eight.isConnectedToNetwork(getApplicationContext())) {
            EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    private void activateBillingOrFirmSignInOrLoginApp() {
        InAppBilling billing = InAppBilling.getInstance();
        billing.isSubscribed(subscribed -> {
            if (!subscribed) {
                Intent intent = new Intent(activity.getApplicationContext(), BillingActivity.class);
                startActivity(intent);
            } else {
                progressBar.setVisibility(View.GONE);
                activateFirmSignInOrLoginActivity();
            }
        });
    }

    private void activateBillingOrFirmMainApp() {
        InAppBilling.getInstance().isSubscribed(subscribed -> {
            if (!subscribed) {
                activateBillingActivity();
            } else {
                progressBar.setVisibility(View.GONE);
                activateEightMainApp();
            }
        });
    }

    private void activateBillingActivity() {
        Intent intent = new Intent(getApplicationContext(), BillingActivity.class);
        startActivity(intent);
    }

    private void activateFirmSignInOrLoginActivity() {
        Intent activityIntent = new Intent(getApplicationContext(), FirmLoginOrSignInActivity.class);
        startActivity(activityIntent);
    }


    private void activateCustomerSignInOrLogin() {
        Intent activityIntent = new Intent(getApplicationContext(), CustomerLoginOrSignInActivity.class);
        startActivity(activityIntent);
    }

    private void activateEightMainApp() {
        Intent activityIntent = new Intent(getApplicationContext(), EightMainAppActivity.class);
        startActivity(activityIntent);
    }

    private void handleFirmModeEnabled() {
        String firmEmail = EightSharedPreferences.getInstance().getString(EightSharedPreferences.FIRM_EMAIL_KEY);
        String firmPassword = EightSharedPreferences.getInstance().getString(
                EightSharedPreferences.FIRM_PASSWORD_KEY);
        if (firmEmail != null && firmPassword != null) {
            if (FirebaseAuthManager.getInstance().isUserAuthenticated()) {
                progressBar.setVisibility(View.VISIBLE);
                Eight.firestoreManager.firmOwnerFromEmailAndPassword(firmEmail, firmPassword, object -> {
                    Firm firm = (Firm) object;
                    Eight.dataModel.setFirm(firm);
                    activateBillingOrFirmMainApp();
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                FirebaseAuthManager.getInstance().getFirebaseAuth().signInWithEmailAndPassword(firmEmail, firmPassword).addOnCompleteListener(activity, task -> {
                    if (task.isComplete()) {
                        Eight.firestoreManager.firmOwnerFromEmailAndPassword(firmEmail, firmPassword, object -> {
                            Firm firm = (Firm) object;
                            Eight.dataModel
                                    .setFirm(
                                            firm);
                            activateBillingOrFirmMainApp();
                            progressBar
                                    .setVisibility(
                                            View.GONE);
                        });
                    }
                });
            }
        }
    }

    private void handleCustomerModeEnabled() {
        String email = EightSharedPreferences.getInstance().getString(EightSharedPreferences.CUSTOMER_EMAIL_KEY);
        String password = EightSharedPreferences.getInstance().getString(EightSharedPreferences.CUSTOMER_PASSWORD_KEY);
        if (email != null && password != null) {
            progressBar.setVisibility(View.VISIBLE);
            if (FirebaseAuthManager.getInstance().isUserAuthenticated()) {
                Eight.firestoreManager.customerWithEmailAndPassword(email, password, object -> {
                    Customer customer = (Customer) object;
                    Eight.dataModel.setCustomer(customer);
                    activateEightMainApp();
                    //activateCustomerModeMainActivity();
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                FirebaseAuthManager.getInstance().getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
                    if (task.isComplete()) {
                        Eight.firestoreManager.customerWithEmailAndPassword(email, password, object -> {
                            Customer customer = (Customer) object;
                            Eight.dataModel.setCustomer(customer);
                            //activateCustomerModeMainActivity();
                            activateEightMainApp();
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                });
            }
        }
    }
}
