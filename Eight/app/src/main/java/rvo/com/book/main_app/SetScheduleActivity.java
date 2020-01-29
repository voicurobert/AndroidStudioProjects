package rvo.com.book.main_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import rvo.com.book.R;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.eight_db.Employee;
import rvo.com.book.eight_db.Firm;
import rvo.com.book.eight_db.Schedule;
import rvo.com.book.firebase.FirebaseAuthManager;
import rvo.com.book.main_app.firm_login.FirmLoginOrSignInActivity;

public class SetScheduleActivity extends FragmentActivity {

    private SetScheduleActivity activity;
    private CheckBox mondayCheckBox;
    private CheckBox tuesdayCheckBox;
    private CheckBox thursdayCheckBox;
    private CheckBox wednesdayCheckBox;
    private CheckBox fridayCheckBox;
    private CheckBox saturdayCheckBox;
    private CheckBox sundayCheckBox;
    private EditText mondayStartsEditTextTime;
    private EditText tuesdayStartsEditTextTime;
    private EditText thursdayStartsEditTextTime;
    private EditText wednesdayStartsEditTextTime;
    private EditText fridayStartsEditTextTime;
    private EditText saturdayStartsEditTextTime;
    private EditText sundayStartsEditTextTime;
    private EditText mondayEndsEditTextTime;
    private EditText tuesdayEndsEditTextTime;
    private EditText thursdayEndsEditTextTime;
    private EditText wednesdayEndsEditTextTime;
    private EditText fridayEndsEditTextTime;
    private EditText saturdayEndsEditTextTime;
    private EditText sundayEndsEditTextTime;
    private Schedule schedule;
    private String context;
    private Employee employee;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_schedule_for_activity);
        activity = this;
        context = (String) getIntent().getSerializableExtra("context");
        if (context != null) {
            if (context.equals("employee")) {
                String id = getIntent().getStringExtra("employeeId");
                employee = Eight.dataModel.getEmployeeFromId(id);
                schedule = employee.getSchedule();
            } else {
                schedule = Eight.dataModel.getFirm().getSchedule();
            }
        }

        TextView titleTextViewId = findViewById(R.id.setScheduleTitleTextViewId);
        String title;
        if (employee == null) {
            title = Eight.dataModel.getFirm().getName() + "'s schedule";
        } else {
            title = employee.getName() + "'s schedule";
        }
        titleTextViewId.setText(title);
        mondayCheckBox = findViewById(R.id.mondayCheckBoxId);
        tuesdayCheckBox = findViewById(R.id.tuesdayCheckBoxId);
        wednesdayCheckBox = findViewById(R.id.wednesdayCheckBoxId);
        thursdayCheckBox = findViewById(R.id.thursdayCheckBoxId);
        fridayCheckBox = findViewById(R.id.fridayCheckBoxId);
        saturdayCheckBox = findViewById(R.id.saturdayCheckBoxId);
        sundayCheckBox = findViewById(R.id.sundayCheckBoxId);

        mondayStartsEditTextTime = findViewById(R.id.mondayStartsTimeEditText);
        tuesdayStartsEditTextTime = findViewById(R.id.tuesdayStartsTimeEditText);
        wednesdayStartsEditTextTime = findViewById(R.id.wednesdayStartsTimeEditText);
        thursdayStartsEditTextTime = findViewById(R.id.thursdayStartsTimeEditText);
        fridayStartsEditTextTime = findViewById(R.id.fridayStartsTimeEditText);
        saturdayStartsEditTextTime = findViewById(R.id.saturdayStartsTimeEditText);
        sundayStartsEditTextTime = findViewById(R.id.sundayStartsTimeEditText);

        mondayEndsEditTextTime = findViewById(R.id.mondayEndsTimeEditText);
        tuesdayEndsEditTextTime = findViewById(R.id.tuesdayEndsTimeEditText);
        wednesdayEndsEditTextTime = findViewById(R.id.wednesdayEndsTimeEditText);
        thursdayEndsEditTextTime = findViewById(R.id.thursdayEndsTimeEditText);
        fridayEndsEditTextTime = findViewById(R.id.fridayEndsTimeEditText);
        saturdayEndsEditTextTime = findViewById(R.id.saturdayEndsTimeEditText);
        sundayEndsEditTextTime = findViewById(R.id.sundayEndsTimeEditText);

        Schedule firmSchedule = Eight.dataModel.getFirm().getSchedule();
        if (firmSchedule == null) {
            setWorkingHours(mondayStartsEditTextTime, mondayEndsEditTextTime, null, null);
            setWorkingHours(tuesdayStartsEditTextTime, tuesdayEndsEditTextTime, null, null);
            setWorkingHours(wednesdayStartsEditTextTime, wednesdayEndsEditTextTime, null, null);
            setWorkingHours(thursdayStartsEditTextTime, thursdayEndsEditTextTime, null, null);
            setWorkingHours(fridayStartsEditTextTime, fridayEndsEditTextTime, null, null);
            setWorkingHours(saturdayStartsEditTextTime, saturdayEndsEditTextTime, null, null);
            setWorkingHours(sundayStartsEditTextTime, sundayEndsEditTextTime, null, null);
        } else {
            setWorkingHours(mondayStartsEditTextTime, mondayEndsEditTextTime, null, firmSchedule.getMondayWorkingHours());
            setWorkingHours(tuesdayStartsEditTextTime, tuesdayEndsEditTextTime, null, firmSchedule.getTuesdayWorkingHours());
            setWorkingHours(wednesdayStartsEditTextTime, wednesdayEndsEditTextTime, null, firmSchedule.getWednesdayWorkingHours());
            setWorkingHours(thursdayStartsEditTextTime, thursdayEndsEditTextTime, null, firmSchedule.getThursdayWorkingHours());
            setWorkingHours(fridayStartsEditTextTime, fridayEndsEditTextTime, null, firmSchedule.getFridayWorkingHours());
            setWorkingHours(saturdayStartsEditTextTime, saturdayEndsEditTextTime, null, firmSchedule.getSaturdayWorkingHours());
            setWorkingHours(sundayStartsEditTextTime, sundayEndsEditTextTime, null, firmSchedule.getSundayWorkingHours());
        }


        setLayoutValues();
        Button apply = findViewById(R.id.applyScheduleButtonId);
        apply.setOnClickListener(v -> {
            // if firmOwner has scheduleId set, do update else insert schedule and set firmOwner.scheduleId
            String monday = activity.getBooleanValue(mondayCheckBox);
            String tuesday = activity.getBooleanValue(tuesdayCheckBox);
            String wednesday = activity.getBooleanValue(wednesdayCheckBox);
            String thursday = activity.getBooleanValue(thursdayCheckBox);
            String friday = activity.getBooleanValue(fridayCheckBox);
            String saturday = activity.getBooleanValue(saturdayCheckBox);
            String sunday = activity.getBooleanValue(sundayCheckBox);

            String message = getString(R.string.enter_value);
            if (editTextIsNull(mondayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(tuesdayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(wednesdayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(thursdayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(fridayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(saturdayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(sundayStartsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(mondayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(tuesdayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(wednesdayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(thursdayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(fridayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(saturdayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            if (editTextIsNull(sundayEndsEditTextTime)) {
                EightAlertDialog.showAlertWithMessage(message, activity);
                mondayStartsEditTextTime.requestFocus();
                return;
            }
            String mondayWh = activity.getWorkingHoursString(mondayStartsEditTextTime, mondayEndsEditTextTime);
            String tuesdayWh = activity.getWorkingHoursString(tuesdayStartsEditTextTime, tuesdayEndsEditTextTime);
            String wednesdayWh = activity.getWorkingHoursString(wednesdayStartsEditTextTime, wednesdayEndsEditTextTime);
            String thursdayWh = activity.getWorkingHoursString(thursdayStartsEditTextTime, thursdayEndsEditTextTime);
            String fridayWh = activity.getWorkingHoursString(fridayStartsEditTextTime, fridayEndsEditTextTime);
            String saturdayWh = activity.getWorkingHoursString(saturdayStartsEditTextTime, saturdayEndsEditTextTime);
            String sundayWh = activity.getWorkingHoursString(sundayStartsEditTextTime, sundayEndsEditTextTime);
            if (schedule == null) {
                insertSchedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                               mondayWh, tuesdayWh, wednesdayWh, thursdayWh, fridayWh, saturdayWh,
                               sundayWh);
            } else {
                updateSchedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                               mondayWh, tuesdayWh, wednesdayWh, thursdayWh, fridayWh, saturdayWh,
                               sundayWh);
            }
        });
    }

    private void insertSchedule(String monday, String tuesday, String wednesday, String thursday,
                                String friday, String saturday, String sunday, String mondayWh,
                                String tuesdayWh, String wednesdayWh, String thursdayWh,
                                String fridayWh, String saturdayWh, String sundayWh) {
        FirebaseAuthManager.getInstance().getFirebaseAuth().signInAnonymously();

        Eight.firestoreManager.writeSchedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday,
                                             mondayWh, tuesdayWh, wednesdayWh, thursdayWh, fridayWh, saturdayWh,
                                             sundayWh, object -> {
                    Schedule schedule = (Schedule) object;
                    if (context.equals("firm")) {
                        Firm firm = Eight.dataModel.getFirm();
                        firm.setScheduleId(schedule.getId());
                        firm.setSchedule(schedule);
                        Eight.firestoreManager.insertFirm(firm);
                    } else {
                        employee.setSchedule(schedule);
                        Eight.firestoreManager.updateEmployeeWithScheduleId(employee.getId(), schedule.getId());
                    }
                    activateFirmLoginActivity();
                    finish();
                });
    }

    private void updateSchedule(String monday, String tuesday, String wednesday, String thursday,
                                String friday, String saturday, String sunday, String mondayWh,
                                String tuesdayWh, String wednesdayWh, String thursdayWh,
                                String fridayWh, String saturdayWh, String sundayWh) {
        Eight.firestoreManager.updateSchedule(schedule, monday, tuesday, wednesday, thursday, friday, saturday,
                                sunday, mondayWh, tuesdayWh, wednesdayWh, thursdayWh, fridayWh,
                                saturdayWh, sundayWh);
        if (context.equals("employee")) {
            employee.setSchedule(schedule);
        } else {
            Eight.dataModel.getFirm().setSchedule(schedule);
        }
        finish();
    }

    private void activateFirmLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), FirmLoginOrSignInActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLayoutValues() {
        if (schedule == null) {
            return;
        }
        setCheckBoxValue(mondayCheckBox, schedule.isMonday());
        setCheckBoxValue(tuesdayCheckBox, schedule.isTuesday());
        setCheckBoxValue(wednesdayCheckBox, schedule.isWednesday());
        setCheckBoxValue(thursdayCheckBox, schedule.isThursday());
        setCheckBoxValue(fridayCheckBox, schedule.isFriday());
        setCheckBoxValue(saturdayCheckBox, schedule.isSaturday());
        setCheckBoxValue(sundayCheckBox, schedule.isSunday());
        if (context.equals("employee")) {
            Schedule firmSchedule = Eight.dataModel.getFirm().getSchedule();
            setWorkingHours(mondayStartsEditTextTime, mondayEndsEditTextTime, schedule.getMondayWorkingHours(), firmSchedule.getMondayWorkingHours());
            setWorkingHours(tuesdayStartsEditTextTime, tuesdayEndsEditTextTime,
                            schedule.getTuesdayWorkingHours(),
                            firmSchedule.getTuesdayWorkingHours());
            setWorkingHours(wednesdayStartsEditTextTime, wednesdayEndsEditTextTime,
                            schedule.getWednesdayWorkingHours(),
                            firmSchedule.getWednesdayWorkingHours());
            setWorkingHours(thursdayStartsEditTextTime, thursdayEndsEditTextTime,
                            schedule.getThursdayWorkingHours(),
                            firmSchedule.getThursdayWorkingHours());
            setWorkingHours(fridayStartsEditTextTime, fridayEndsEditTextTime,
                            schedule.getFridayWorkingHours(), firmSchedule.getFridayWorkingHours());
            setWorkingHours(saturdayStartsEditTextTime, saturdayEndsEditTextTime,
                            schedule.getSaturdayWorkingHours(),
                            firmSchedule.getSaturdayWorkingHours());
            setWorkingHours(sundayStartsEditTextTime, sundayEndsEditTextTime,
                            schedule.getSundayWorkingHours(), firmSchedule.getSundayWorkingHours());
        } else {
            setWorkingHours(mondayStartsEditTextTime, mondayEndsEditTextTime, null,
                            schedule.getMondayWorkingHours());
            setWorkingHours(tuesdayStartsEditTextTime, tuesdayEndsEditTextTime, null,
                            schedule.getTuesdayWorkingHours());
            setWorkingHours(wednesdayStartsEditTextTime, wednesdayEndsEditTextTime, null,
                            schedule.getWednesdayWorkingHours());
            setWorkingHours(thursdayStartsEditTextTime, thursdayEndsEditTextTime, null,
                            schedule.getThursdayWorkingHours());
            setWorkingHours(fridayStartsEditTextTime, fridayEndsEditTextTime, null,
                            schedule.getFridayWorkingHours());
            setWorkingHours(saturdayStartsEditTextTime, saturdayEndsEditTextTime, null,
                            schedule.getSaturdayWorkingHours());
            setWorkingHours(sundayStartsEditTextTime, sundayEndsEditTextTime, null,
                            schedule.getSundayWorkingHours());
        }
    }

    private void setCheckBoxValue(CheckBox checkBox, boolean value) {
        checkBox.setChecked(value);
    }

    private void setWorkingHours(EditText startEditText, EditText endEditText, String startEndWhForEmployee, String startEndWhForFirm) {
        String[] valuesFirm;
        if (startEndWhForFirm == null) {
            valuesFirm = new String[]{"8", "20"};
        } else {
            valuesFirm = startEndWhForFirm.split("-");
        }
        String low = valuesFirm[0];
        String high = valuesFirm[1];
        String lowEmployee = low;
        String highEmployee = high;
        if (startEndWhForEmployee != null) {
            String[] valuesEmployee = startEndWhForEmployee.split("-");
            lowEmployee = valuesEmployee[0];
            highEmployee = valuesEmployee[1];
        }
        startEditText.setText(lowEmployee);
        endEditText.setText(highEmployee);
    }

    private String getBooleanValue(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            return "1";
        }
        return "0";
    }

    private String getWorkingHoursString(EditText start, EditText end) {
        String startTime = start.getText().toString();
        String endTime = end.getText().toString();
        return startTime + "-" + endTime;
    }

    private boolean editTextIsNull(EditText editText) {
        if (editText.getText().toString().equals("")) {
            if (editText.getId() == mondayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == tuesdayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == wednesdayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == thursdayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == fridayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == saturdayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == sundayStartsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == mondayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == tuesdayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == wednesdayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == thursdayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == fridayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == saturdayEndsEditTextTime.getId()) {
                return true;
            } else if (editText.getId() == sundayEndsEditTextTime.getId()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
