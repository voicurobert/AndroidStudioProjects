package rvo.com.book.common;


import android.text.Editable;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import rvo.com.book.android.main_app.alerts.EightAlertDialog;

public class Validator {

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

    public static String createDurationStringFromHoursAndMinutesEditText(EditText hoursEditText, EditText minutesEditText, Fragment fragment) {
        Editable hoursEditable = hoursEditText.getText();
        if (hoursEditable == null) {
            return null;
        }
        Editable minutesEditable = minutesEditText.getText();
        if (minutesEditable == null) {
            return null;
        }
        String hours = hoursEditable.toString();
        String minutes = minutesEditable.toString();
        if (hours.equals("")) {
            hours = "0";
        }
        if (minutes.equals("")) {
            minutes = "0";
        }
        if (hours.contains(".") || minutes.contains(".")) {
            EightAlertDialog
                    .showAlertWithMessage("Value cannot contain '.'!", fragment.getActivity());
            return null;
        }
        if (Integer.valueOf(hours) > 8) {
            EightAlertDialog.showAlertWithMessage("Value " + hours + " is too big!",
                                                  fragment.getActivity());
        }
        if (Integer.valueOf(minutes) > 60) {
            EightAlertDialog.showAlertWithMessage("Value " + minutes + " is too big!",
                                                  fragment.getActivity());
        }
        return hours + "-" + minutes;
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
                if (Validator.isEmailCorrect(email)) {
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
        return Encrypt.encryptString(editable.toString());
    }

    public static String getPhoneNumberFromEditText(EditText editText) {
        Editable editable = editText.getText();
        if (editable == null || editable.toString().equals("")) {
            return null;
        }
        String phoneNumber = editable.toString();
        if (Validator.isPhoneNumberCorrect(phoneNumber)) {
            return phoneNumber;
        } else {
            return "";
        }
    }

    public static boolean isEmailCorrect(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneNumberCorrect(String phoneNumber) {
        return phoneNumber.matches(
                "^(?:(?:(?:00\\s?|\\+)40\\s?|0)(?:7\\d{2}\\s?\\d{3}\\s?\\d{3}|(21|31)\\d{1}\\s?\\d{3}\\s?\\d{3}|((2|3)[3-7]\\d{1})\\s?\\d{3}\\s?\\d{3}|(8|9)0\\d{1}\\s?\\d{3}\\s?\\d{3}))$");
    }
}
