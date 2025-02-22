package rvo.com.book.android.main_app.billing;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.android.main_app.firm_login.FirmLoginOrSignInActivity;

public class BillingActivity extends AppCompatActivity implements IBillingResponse {

    private InAppBilling billing = InAppBilling.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_activity_layout);
        Button subscribeButton = findViewById(R.id.subscribeButtonId);
        subscribeButton.setOnClickListener(v -> initiateSubscribe());
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Subscribe");
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.titanium)));
        }
        billing.setBillingResponse(this);
    }

    private void initiateSubscribe() {
        billing.subscribe(subscribed -> {
            if (!subscribed) {
                EightAlertDialog.showAlertWithMessage("Are you connected to google play? Please check that you are logged in.", this);
            }
        });
    }

    protected void activateFirmModeActivity() {
        Intent intent = new Intent(getApplicationContext(), FirmLoginOrSignInActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        billing.remakeConnection();
    }

    @Override
    public void subscribed(boolean subscribed) {
        if (subscribed) {
            activateFirmModeActivity();
        } else {
            EightAlertDialog.showAlertWithMessage("You are not subscribed!", this);
        }
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        EightSharedPreferences.getInstance().signOut(this);
    }
}
