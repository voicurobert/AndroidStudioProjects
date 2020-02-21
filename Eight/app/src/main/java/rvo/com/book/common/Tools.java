package rvo.com.book.common;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.Editable;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import rvo.com.book.android.main_app.alerts.EightAlertDialog;

public class Tools {

    public static Integer calculateDurationAsMinutes(String duration) {
        String hours;
        int minutes = 0;
        if (duration.contains("-")) {
            hours = duration.split("-")[0];
            minutes += Integer.valueOf(duration.split("-")[1]);
        } else {
            hours = duration;
        }
        int h = Integer.valueOf(hours);
        if (h > 1) {
            for (int i = 1; i <= h; i++) {
                minutes += 60;
            }
        } else if (h == 1) {
            minutes += 60;
        }
        return minutes;
    }

    public static void isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
        }
    }


    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                if (activeNetwork != null) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                    if (networkCapabilities != null) {
                        isConnected = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                      networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                    }
                }
            } else {
                for (Network network : connectivityManager.getAllNetworks()) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isConnected |= networkInfo.isConnected();
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isConnected |= networkInfo.isConnected();
                    }
                }
            }
        }

        return isConnected;
    }

    public static String getProductNameFromEditText(EditText editText, Fragment fragment) {
        Editable editable = editText.getText();
        if (editable == null) {
            EightAlertDialog.showAlertWithMessage("Something was wrong", fragment.getActivity());
        } else {
            String string = editable.toString();
            if (string.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Empty value", fragment.getActivity());
            }
            return string;
        }
        return null;
    }

    public static String createDurationStringFromHoursAndMinutesSpinner(Spinner hoursSpinner, Spinner minutesSpinner) {
        return hoursSpinner.getSelectedItem().toString() + "-" + minutesSpinner.getSelectedItem().toString();
    }

    public static String getEmailFromEditText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null) {
            return null;
        } else {
            String email = editable.toString();
            if (email.equals("")) {
                return "";
            } else {
                if (Tools.isEmailCorrect(email)) {
                    return email;
                } else {
                    return "";
                }
            }
        }

    }

    public static String getNameFromEditText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null || editable.toString().equals("")) {
            return null;
        }
        return editable.toString();
    }

    public static String getPasswordFromEditText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null || editable.toString().equals("")) {
            return null;
        }
        return encryptString(editable.toString());
    }

    public static String getPhoneNumberFromEditText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null || editable.toString().equals("")) {
            return null;
        }
        String phoneNumber = editable.toString();
        if (Tools.isPhoneNumberCorrect(phoneNumber)) {
            return phoneNumber;
        } else {
            return "";
        }
    }

    private static boolean isEmailCorrect(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneNumberCorrect(String phoneNumber) {
        return phoneNumber.matches("^(?:(?:(?:00\\s?|\\+)40\\s?|0)(?:7\\d{2}\\s?\\d{3}\\s?\\d{3}|(21|31)\\d{1}\\s?\\d{3}\\s?\\d{3}|((2|3)[3-7]\\d{1})\\s?\\d{3}\\s?\\d{3}|(8|9)0\\d{1}\\s?\\d{3}\\s?\\d{3}))$");
    }

    private static String encryptString(String string) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(string.getBytes(StandardCharsets.UTF_8));
            StringBuilder encryptedString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                encryptedString.append(Integer.toString((hash[i] & 0xff) + 0x100, 16).substring(1));
            }
            return encryptedString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
