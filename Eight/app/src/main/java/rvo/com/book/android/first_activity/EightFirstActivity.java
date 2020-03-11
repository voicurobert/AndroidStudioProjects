package rvo.com.book.android.first_activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.main_app.EightMainAppActivity;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.android.main_app.billing.BillingActivity;
import rvo.com.book.android.main_app.billing.InAppBilling;
import rvo.com.book.android.main_app.customer_login.CustomerLoginOrSignInActivity;
import rvo.com.book.android.main_app.firm_login.FirmLoginOrSignInActivity;
import rvo.com.book.android.notification.FirebaseProperties;
import rvo.com.book.common.Tools;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.CustomerRepository;
import rvo.com.book.datamodel.repositories.FirmRepository;


public class EightFirstActivity extends FragmentActivity {

    private EightFirstActivity activity;
    private ProgressBar progressBar;
    private InAppBilling inAppBilling;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eight_first_activity);
        activity = this;
        progressBar = findViewById(R.id.bookWelcomeProgressBar);
        inAppBilling = InAppBilling.getInstance();
        inAppBilling.start(this);
        FirebaseDatabase.getInstance();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> FirebaseProperties.getInstance().setCurrentToken(instanceIdResult.getToken()));
        Tools.isGooglePlayServicesAvailable(this);
        if (!Tools.isConnectedToNetwork(getApplicationContext())) {
            EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
        }
        EightSharedPreferences.getInstance().createSharedPreferencesForActivity(this);

        loginIfAuthenticated();

        Button continueAsFirmButton = findViewById(R.id.continueAsFirmButtonId);
        continueAsFirmButton.setOnClickListener(view -> {
            if (!Tools.isConnectedToNetwork(getApplicationContext())) {
                EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
                return;
            }
            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.MODE, EightSharedPreferences.FIRM_MODE, this);
            activateBillingOrFirmSignInOrLoginApp();
        });

        Button continueAsCustomerButton = findViewById(R.id.continueAsCustomerButtonId);
        continueAsCustomerButton.setOnClickListener(view -> {
            if (!Tools.isConnectedToNetwork(getApplicationContext())) {
                EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
                return;
            }
            EightSharedPreferences.getInstance().saveString(EightSharedPreferences.MODE, EightSharedPreferences.CUSTOMER_MODE, this);
            activateCustomerSignInOrLogin();
        });
    }

    private void loginIfAuthenticated(){
        if (EightSharedPreferences.getInstance().modeEnabled()) {
            if (EightSharedPreferences.getInstance().isFirmMode()) {
                handleFirmModeEnabled();
            } else if (EightSharedPreferences.getInstance().isCustomerMode()) {
                handleCustomerModeEnabled();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Tools.isConnectedToNetwork(getApplicationContext())) {
            EightAlertDialog.showAlertWithMessage(getString(R.string.no_internet_access), this);
        }
        loginIfAuthenticated();
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
        inAppBilling.isSubscribed(subscribed -> {
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
        inAppBilling.isSubscribed(subscribed -> {
            if (!subscribed) {
                activateBillingActivity();
            } else {
                DataModel.getInstance().initialiseDataStore(null, () -> {
                    activateEightMainApp();
                    finishAndRemoveTask();
                });
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
        progressBar.setVisibility(View.GONE);
        Intent activityIntent = new Intent(getApplicationContext(), EightMainAppActivity.class);
        startActivity(activityIntent);
        finishAndRemoveTask();
    }

    private void handleFirmModeEnabled() {
        String firmEmail = EightSharedPreferences.getInstance().getString(EightSharedPreferences.FIRM_EMAIL_KEY);
        String firmPassword = EightSharedPreferences.getInstance().getString(EightSharedPreferences.FIRM_PASSWORD_KEY);
        if (firmEmail != null && firmPassword != null) {
            if (isUserAuthenticated()) {
                progressBar.setVisibility(View.VISIBLE);
                FirmRepository.getInstance().objectFromEmailAndPassword(firmEmail, firmPassword, object -> {
                    Firm firm = (Firm) object;
                    DataModel.getInstance().setFirm(firm);
                    activateBillingOrFirmMainApp();
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(firmEmail, firmPassword).addOnCompleteListener(activity, task -> {
                    if (task.isComplete()) {
                        FirmRepository.getInstance().objectFromEmailAndPassword(firmEmail, firmPassword, object -> {
                            Firm firm = (Firm) object;
                            DataModel.getInstance().setFirm(firm);
                            activateBillingOrFirmMainApp();
                            progressBar.setVisibility(View.GONE);
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
            if (isUserAuthenticated()) {
                CustomerRepository.getInstance().objectFromEmailAndPassword(email, password, object -> {
                    Customer customer = (Customer) object;
                    DataModel.getInstance().setCustomer(customer);
                    activateEightMainApp();
                    //activateCustomerModeMainActivity();
                    progressBar.setVisibility(View.GONE);
                });
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, task -> {
                    if (task.isComplete()) {
                        CustomerRepository.getInstance().objectFromEmailAndPassword(email, password, object -> {
                            Customer customer = (Customer) object;
                            DataModel.getInstance().setCustomer(customer);
                            //activateCustomerModeMainActivity();
                            activateEightMainApp();
                            progressBar.setVisibility(View.GONE);
                        });
                    }
                });
            }
        }
    }

    private boolean isUserAuthenticated() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }
}
