package rvo.com.book.main_app;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rvo.com.book.R;
import rvo.com.book.alerts.AddAlertDialog;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.billing.BillingActivity;
import rvo.com.book.billing.InAppBilling;
import rvo.com.book.common.CustomSpinnerAdapter;
import rvo.com.book.common.Eight;
import rvo.com.book.common.EightDate;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.eight_db.Booking;
import rvo.com.book.eight_db.Category;
import rvo.com.book.eight_db.Customer;
import rvo.com.book.eight_db.Employee;
import rvo.com.book.eight_db.Firm;
import rvo.com.book.eight_db.Product;
import rvo.com.book.eight_db.Schedule;
import rvo.com.book.firebase.NotificationManager;
import rvo.com.book.main_app.customer_login.CustomerLoginOrSignInActivity;

public class ScheduleFragment extends Fragment {

    private Firm firm;
    private ListView bookingsListView;
    private EightDate date;
    private Employee selectedEmployee;
    private Schedule employeeSchedule;
    private ProgressBar progressBar;
    private List<String> employeeNames = new ArrayList<>();
    private TextView currentDayTextView;

    public ScheduleFragment() {
        date = new EightDate();
    }

    public void setFirm(Firm firm) {
        this.firm = firm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.schedule_fragment, container, false);
        progressBar = thisView.findViewById(R.id.scheduleProgressBarId);
        currentDayTextView = thisView.findViewById(R.id.currentDayTextView);
        currentDayTextView.setText(date.toString());
        firm = Eight.dataModel.getFirm();

        Button tomorrowButton = thisView.findViewById(R.id.tomorrowButtonId);
        Button yesterdayButton = thisView.findViewById(R.id.yesterdayButtonId);
        tomorrowButton.setOnClickListener(view -> tomorrowButtonClicked());
        yesterdayButton.setOnClickListener(view -> yesterdayButtonClicked());

        bookingsListView = thisView.findViewById(R.id.bookingsCardListView);
        Spinner employeesSpinner = thisView.findViewById(R.id.employeesSpinnerId);
        employeeNames = Eight.dataModel.getEmployeeNames();
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext(), employeeNames);
        employeesSpinner.setAdapter(customSpinnerAdapter);
        employeesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final Employee employee = Eight.dataModel.getEmployeeFromName(employeeNames.get(i));
                if (employee.getSchedule() == null) {
                    Eight.dataModel.initialiseSchedulesForEmployee(employee, () -> computeScheduleProgramViewForEmployee(employee));
                } else {
                    computeScheduleProgramViewForEmployee(employee);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        FloatingActionButton insertBookingButton = thisView.findViewById(R.id.insertBookingButtonId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            insertBookingButton.show();
        }
        insertBookingButton.setOnClickListener(view -> insertBookingButtonClicked());
        return thisView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EightSharedPreferences.getInstance().isCustomerMode()) {
            return;
        }
        InAppBilling.getInstance().isSubscribed(subscribed -> {
            if (!subscribed) {
                Intent intent = new Intent(this.getContext(), BillingActivity.class);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }

    private void computeScheduleProgramViewForEmployee(Employee employee) {
        if (employee == null) {
            return;
        }
        selectedEmployee = employee;
        employeeSchedule = employee.getSchedule();
        if (employeeSchedule == null) {
            List<Map<String, Booking>> list = new ArrayList<>();
            Map<String, Booking> noSchedule = new HashMap<>();
            noSchedule.put(employee.getName() + " has no schedule today.", null);
            list.add(noSchedule);
            BookingAdapter adapter = new BookingAdapter(this, list, employee);
            bookingsListView.setAdapter(adapter);
            return;
        }
        setProgressVisible();
        Eight.dataModel.initialiseBookingsForEmployeeOnDate(employee, date, () -> {
            List<Map<String, Booking>> bookingsOnTime = employee.getBookingsOnHourAsList();

            BookingAdapter adapter = new BookingAdapter(this, bookingsOnTime, employee);
            bookingsListView.setAdapter(adapter);
            setProgressInvisible();
        });
    }

    public void rejectFirmBooking(Booking booking) {
        String alertTitle = "Reject booking" + booking.dateDescription() + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(alertTitle);
        builder.setPositiveButton("OK", (dialog, which) -> {
            Eight.firestoreManager.setBookingActivationStatus(booking.getId(), Booking.DECLINE);
            booking.setStatus(Booking.DECLINE);
            computeScheduleProgramViewForEmployee(selectedEmployee);
        });
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void insertBookingButtonClicked() {
        if (!EightSharedPreferences.getInstance().isFirmMode() &&
            Eight.dataModel.getCustomer() == null) {
            preSignInOrLogin();
            return;
        }
        if (employeeSchedule == null) {
            return;
        } else {
            if (!selectedEmployee.isWorkingToday(date)) {
                EightAlertDialog.showAlertWithMessage("Sorry :( " + selectedEmployee.getName() + " is not working today.", getActivity());
                return;
            }
        }
        if (date.getMonthAsInteger() < currentMonth()) {
            EightAlertDialog.showAlertWithMessage("You cannot make a booking in the past!", getActivity());
            return;
        } else if (date.getMonthAsInteger() <= currentMonth() &&
                   date.getDayAsInteger() < currentDay()) {
            EightAlertDialog.showAlertWithMessage("You cannot make a booking in the past!", getActivity());
            return;
        }
        AddAlertDialog insertBookingDialog = new AddAlertDialog(R.layout.insert_booking_layout,
                                                                this,
                                                                getLayoutInflater(),
                                                                "Add booking at " + selectedEmployee.getName());
        insertBookingDialog.createAlertDialog();
        View alertView = insertBookingDialog.getView();
        Spinner categoriesSpinner = alertView.findViewById(R.id.categoriesSpinnerId);
        Spinner productsSpinner = alertView.findViewById(R.id.productsSpinnerId);
        CustomSpinnerAdapter arrayAdapter = new CustomSpinnerAdapter(getContext(), Eight.dataModel.getCategoriesNamesFromCategoryIds(selectedEmployee.getCategories()));
        categoriesSpinner.setAdapter(arrayAdapter);
        String categoryName = arrayAdapter.getItem(0);
        Category category = Eight.dataModel.getCategoryFromCategoryName(categoryName);
        CustomSpinnerAdapter productsAdapter = new CustomSpinnerAdapter(getContext(), Eight.dataModel.getProductNamesFromCategory(category));
        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryName = arrayAdapter.getItem(i);
                Category category = Eight.dataModel.getCategoryFromCategoryName(categoryName);
                CustomSpinnerAdapter productsAdapter = new CustomSpinnerAdapter(getContext(), Eight.dataModel.getProductNamesFromCategory(category));
                productsSpinner.setAdapter(productsAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        productsSpinner.setAdapter(productsAdapter);
        TextView dateTextView = alertView.findViewById(R.id.dateTextViewId);
        dateTextView.setText(date.toString());
        Spinner hoursSpinner = alertView.findViewById(R.id.hoursSpinnerId);
        Spinner minutesSpinner = alertView.findViewById(R.id.minutesSpinnerId);
        List<String> hours = employeeSchedule.getWorkingHoursForDay(date.getDayAsString());
        CustomSpinnerAdapter hoursAdapter = new CustomSpinnerAdapter(getContext(), hours);
        hoursSpinner.setAdapter(hoursAdapter);
        CustomSpinnerAdapter minutesAdapter = new CustomSpinnerAdapter(getContext(), Schedule.getMinutes());
        minutesSpinner.setAdapter(minutesAdapter);
        Button addButton = alertView.findViewById(R.id.addButtonId);

        LinearLayout customerLinearLayout = alertView.findViewById(R.id.bookCustomerLayout);
        EditText customerEditText = alertView.findViewById(R.id.bookCustomerEditTextId);
        if (EightSharedPreferences.getInstance().isCustomerMode()) {
            customerLinearLayout.setVisibility(View.GONE);
        }

        addButton.setOnClickListener(view -> {
            String bookHours = (String) hoursSpinner.getSelectedItem();
            String bookMinutes = (String) minutesSpinner.getSelectedItem();
            String productName = (String) productsSpinner.getSelectedItem();
            Product product = Eight.dataModel.getProductFromProductName(productName);
            if (EightSharedPreferences.getInstance().isFirmMode() &&
                customerEditText.getText().toString().equals("")) {
                EightAlertDialog.showAlertWithMessage("Customer name must be set!", this.getActivity());
                return;
            }
            String customerName = EightSharedPreferences.getInstance().isCustomerMode() ? null : customerEditText.getText().toString();
            if (addBookingButtonClicked(bookHours, bookMinutes, product, customerName)) {
                insertBookingDialog.close();
            }
        });
        Button closeButton = alertView.findViewById(R.id.closeButtonId);
        insertBookingDialog.show();
        closeButton.setOnClickListener(view -> insertBookingDialog.close());
    }

    private boolean addBookingButtonClicked(String hours, String minutes, Product product, String customerName) {
        boolean isBookFromFirm = customerName != null;
        if (product == null) {
            EightAlertDialog.showAlertWithMessage(getString(R.string.no_product_selected), this.getActivity());
            return false;
        }
        String message = selectedEmployee.bookingCanBeAddedOnHoursAndMinutes(hours, minutes, product);
        if (message != null) {
            EightAlertDialog.showAlertWithMessage(message, this.getActivity());
            return false;
        }
        date.setHoursAndMinutes(hours, minutes);

        Booking booking = new Booking();
        booking.setDate(date.getDateInRestFormat());
        if (customerName == null) {
            Customer customer = Eight.dataModel.getCustomer();
            booking.setCustomer(customer);
            booking.setCustomerId(customer.getId());
            booking.setCustomerName(customer.getName());
            customerName = customer.getName();
            booking.setStatus(-1);
        } else {
            booking.setCustomerName(customerName);
            booking.setStatus(1);
        }
        booking.setEmployeeId(selectedEmployee.getId());
        booking.setEmployee(selectedEmployee);
        booking.setEmployeeName(selectedEmployee.getName());
        booking.setProductId(product.getId());
        booking.setProductName(product.getName());
        booking.setProduct(product);
        booking.setFirmId(Eight.dataModel.getFirm().getId());
        booking.setFirm(Eight.dataModel.getFirm());

        Eight.firestoreManager.insertBooking(booking);
        if (!isBookFromFirm) {
            NotificationManager.getInstance().sendNotification("New booking",
                                                               customerName + " has requested a booking for " + product.getName() + " at " + date.toString(),
                                                               firm.getFirebaseToken());
        }

        selectedEmployee.addBooking(booking);
        selectedEmployee.computeBookings();
        List<Map<String, Booking>> bookingsOnTime = selectedEmployee.getBookingsOnHourAsList();
        ((BookingAdapter) bookingsListView.getAdapter()).setBookingMap(bookingsOnTime);
        bookingsListView.invalidateViews();
        return true;
    }

    private void tomorrowButtonClicked() {
        Integer todayNumber = date.getDayAsInteger();
        Integer month = date.getMonthAsInteger();
        Integer year = date.getYearAsInteger();
        date = new EightDate(todayNumber + 1, month, year);
        currentDayTextView.setText(date.toString());
        computeScheduleProgramViewForEmployee(selectedEmployee);
    }

    private void yesterdayButtonClicked() {
        Integer todayNumber = date.getDayAsInteger();
        Integer month = date.getMonthAsInteger();
        Integer year = date.getYearAsInteger();
        date = new EightDate(todayNumber - 1, month, year);
        currentDayTextView.setText(date.toString());
        computeScheduleProgramViewForEmployee(selectedEmployee);
    }

    private Integer currentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    private Integer currentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    private void setProgressVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setProgressInvisible() {
        progressBar.setVisibility(View.GONE);
    }

    private void preSignInOrLogin() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this.getActivity());
        builder.setTitle(getString(R.string.logged_in_to_book));
        builder.setPositiveButton(getString(R.string.signin_or_login), (dialog, which) -> signInOrLogin());
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void signInOrLogin() {
        EightSharedPreferences.getInstance().saveString(EightSharedPreferences.MODE, EightSharedPreferences.CUSTOMER_MODE, getActivity());
        EightSharedPreferences.getInstance().saveString(EightSharedPreferences.CUSTOMER_EMAIL_KEY, null, getActivity());
        EightSharedPreferences.getInstance().saveString(EightSharedPreferences.CUSTOMER_PASSWORD_KEY, null, getActivity());
        Intent intent = new Intent(this.getContext(), CustomerLoginOrSignInActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

}
