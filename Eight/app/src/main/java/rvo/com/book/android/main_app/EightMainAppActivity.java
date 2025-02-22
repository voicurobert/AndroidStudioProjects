package rvo.com.book.android.main_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;

import rvo.com.book.Log;
import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.first_activity.EightFirstActivity;
import rvo.com.book.android.main_app.alerts.AddAlertDialog;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.android.main_app.billing.BillingActivity;
import rvo.com.book.android.main_app.billing.InAppBilling;
import rvo.com.book.android.main_app.firm_login.FirmLocationMapActivity;
import rvo.com.book.android.main_app.schedule.SetScheduleActivity;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.entities.Schedule;
import rvo.com.book.datamodel.repositories.FirmRepository;
import rvo.com.book.datamodel.repositories.ScheduleRepository;

public class EightMainAppActivity extends FragmentActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Firm firm;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app_activity);
        firm = DataModel.getInstance().getFirm();
        navigationView = findViewById(R.id.navigationViewId);
        loadOrUnloadMenuItems(navigationView.getMenu());
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.firmDetailsId:
                    editFirmDetailsMenuClicked();
                    break;
                case R.id.editFirmMenuId:
                    editFirmMenuClicked();
                    break;
                case R.id.editScheduleFirmMenuId:
                    editScheduleFirmMenuClicked();
                    break;
                case R.id.activateOrDeactivateFirmMenuId:
                    activateOrDeactivateFirmMenuClicked(item);
                    break;
                case R.id.signOutMenuId:
                    signOutMenuClicked();
                    break;
                case R.id.privacyPolicyMenuId:
                    privacyPolicyMenuClicked();
                    break;
                case R.id.activeFirmsId:
                    activeFirmsMenuClicked();
                    break;
                case R.id.yourBookingsId:
                    yourBookingsMenuClicked();
                    break;
                case R.id.pendingBookingsId:
                    pendingBookingsMenuClicked();
                    break;
            }
            closeDrawer();
            return true;
        });
        drawerLayout = findViewById(R.id.drawerLayoutId);
        ImageButton drawNavigationButton = findViewById(R.id.openNavigationButtonId);
        drawNavigationButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        activateDefaultFragment();
    }

    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
    }

    private void activateDefaultFragment() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            YourBookingsFragment yourBookingsFragment = new YourBookingsFragment();
            activateFragment(yourBookingsFragment);
        } else {
            activeFirmsMenuClicked();
        }
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void editFirmDetailsMenuClicked() {
        Intent firmIntent = new Intent(getApplicationContext(), FirmDetailsMasterActivity.class);
        startActivity(firmIntent);
    }

    private void editFirmMenuClicked() {
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.edit_firm_layout, this, getLayoutInflater(), getString(R.string.editFirm_string));
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        EditText firmNameEditText = alertView.findViewById(R.id.firmNameEditTextId_edit_firm_layout);
        EditText firmEmailEditText = alertView.findViewById(R.id.firmEmailEditTextId_edit_firm_layout);
        EditText firmPhoneNumberEditText = alertView.findViewById(R.id.phoneNumberEditTextId_edit_firm_layout);
        EditText firmAddressEditText = alertView.findViewById(R.id.firmAddressEditTextId_edit_firm_layout);

        firmAddressEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                activateMapsForUpdatingFirmAddress();
                addAlertDialog.close();
            }
        });

        firmNameEditText.setText(firm.getName());
        firmEmailEditText.setText(firm.getEmail());
        firmPhoneNumberEditText.setText(firm.getPhoneNumber());
        firmAddressEditText.setText(firm.getAddress());
        Button editFirm = alertView.findViewById(R.id.editFirmButtonId_edit_firm_layout);
        editFirm.setText(R.string.edit);
        editFirm.setOnClickListener(v -> {
            String firmName, firmEmail, phoneNumber;
            firmName = firmNameEditText.getText().toString();
            if (firmName.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Firm name not set!", getParent());
            }
            firmEmail = firmEmailEditText.getText().toString();
            if (firmEmail.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Firm email not set!", getParent());
            }
            phoneNumber = firmPhoneNumberEditText.getText().toString();
            if (phoneNumber.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Firm phone number not set!", getParent());
            }
            firm.setName(firmName);
            firm.setPhoneNumber(phoneNumber);
            FirmRepository.getInstance().updateRecord(firm, Firm.NAME, firm.getName(), Firm.PHONE_NUMBER, firm.getPhoneNumber());
            addAlertDialog.close();
        });
        Button closeButton = alertView.findViewById(R.id.editFirmCloseButtonId_edit_firm_layout);
        addAlertDialog.show();
        closeButton.setOnClickListener(v -> addAlertDialog.close());
    }

    private void activateMapsForUpdatingFirmAddress() {
        Intent map = new Intent(this.getApplicationContext(), FirmLocationMapActivity.class);
        map.putExtra("context", "updateAddress");
        startActivity(map);
    }

    private void editScheduleFirmMenuClicked() {
        Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
        intent.putExtra("context", "firm");
        if (firm.getSchedule() == null) {
            ScheduleRepository.getInstance().objectFromId(firm.getScheduleId(), object -> {
                if (object != null) {
                    Schedule schedule = (Schedule) object;
                    firm.setSchedule(schedule);
                    startActivity(intent);
                }
            });
        } else {
            startActivity(intent);
        }
    }

    private void activateOrDeactivateFirmMenuClicked(MenuItem item) {
        navigationView.getMenu().findItem(R.id.activateOrDeactivateFirmMenuId);
        if (firm.firmIsActive()) {
            // deactivate firm
            activateOrDeactivateFirmButtonDialogClicked(0);
            item.setTitle(getString(R.string.deactivate_firm_string));
        } else {
            // activate firm
            activateOrDeactivateFirmButtonDialogClicked(1);
            item.setTitle(getString(R.string.deactivate_firm_string));
        }
    }

    private void activateOrDeactivateFirmButtonDialogClicked(Integer status) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        String title;
        if (status.equals(0)) {
            title = getResources().getString(R.string.deactivate_firm);
        } else {
            title = getResources().getString(R.string.activate_firm);
        }
        builder.setTitle(title);
        builder.setPositiveButton(getString(R.string.yes), (dialog1, which) -> {
            firm.setStatus(status);
            FirmRepository.getInstance().updateRecord(firm, Firm.STATUS, firm.getStatus());
        });
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel());
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signOutMenuClicked() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.sign_out));
        builder.setPositiveButton(getString(R.string.yes), (dialog, which) -> signOut());
        builder.setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel());
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signOut() {
        EightSharedPreferences.getInstance().signOut(this);
        Intent intent = new Intent(getApplicationContext(), EightFirstActivity.class);
        startActivity(intent);
    }

    private void privacyPolicyMenuClicked() {
        activateActivity(PrivacyPolicyActivity.class);
    }

    private void activeFirmsMenuClicked() {
        ActiveFirmsFragment activeFirmsFragment = new ActiveFirmsFragment();
        DataModel.getInstance().initialiseActiveFirms(() -> activateFragment(activeFirmsFragment));
    }

    private void loadOrUnloadMenuItems(Menu menu) {
        if (!EightSharedPreferences.getInstance().isFirmMode()) {
            menu.findItem(R.id.firmDetailsId).setVisible(false);
            menu.findItem(R.id.editFirmMenuId).setVisible(false);
            menu.findItem(R.id.editScheduleFirmMenuId).setVisible(false);
            menu.findItem(R.id.activateOrDeactivateFirmMenuId).setVisible(false);
            menu.findItem(R.id.pendingBookingsId).setVisible(false);
        } else {
            menu.findItem(R.id.activeFirmsId).setVisible(false);
            MenuItem menuItem = menu.findItem(R.id.activateOrDeactivateFirmMenuId);
            if (menuItem != null) {
                if (firm.firmIsActive()) {
                    menuItem.setTitle(getResources().getString(R.string.deactivate_firm_string));
                } else {
                    menuItem.setTitle(getResources().getString(R.string.activate_firm_string));
                }
            }
        }
    }

    protected void activateFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayoutId, fragment)
                                   .addToBackStack(fragment.getClass().getSimpleName()).commit();
    }

    private void activateActivity(Class c) {
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    private void yourBookingsMenuClicked() {
        activateFragment(new YourBookingsFragment());
    }

    private void pendingBookingsMenuClicked() {
        activateFragment(new PendingBookingsFragment());
    }

    @Override public void onBackPressed() {
        finish();
    }

    @Override protected void onPostResume() {
        super.onPostResume();
        if (EightSharedPreferences.getInstance().isCustomerMode()) {
            return;
        }
        if (InAppBilling.getInstance().getBillingClient().isReady()) {
            InAppBilling.getInstance().isSubscribed(subscribed -> {
                if (!subscribed) {
                    Intent intent = new Intent(this, BillingActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
