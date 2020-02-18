package rvo.com.book.android.main_app;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.android.main_app.alerts.AddAlertDialog;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.android.main_app.billing.BillingActivity;
import rvo.com.book.android.main_app.billing.InAppBilling;
import rvo.com.book.common.Eight;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.datamodel.entities.Category;
import rvo.com.book.datamodel.entities.Employee;
import rvo.com.book.datamodel.entities.Schedule;

public class EmployeesFragment extends Fragment {

    private EmployeesFragment fragment;
    private List<Category> selectedCategories = new ArrayList<>();
    private EmployeeAdapter adapter;
    private boolean drawLayout = false;

    public EmployeesFragment() {
    }

    public void setSelectedCategories(List<Category> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void setDrawLayout(boolean drawLayout) {
        this.drawLayout = drawLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragment = this;
        View thisView = inflater.inflate(R.layout.employees_fragment, container, false);
        GridView gridView = thisView.findViewById(R.id.employeesGridLayout);
        List<Employee> employees = Eight.dataModel.getEmployees();
        if (employees.size() >= 2) {
            gridView.setNumColumns(2);
        }
        adapter = new EmployeeAdapter(getContext(), employees, this);
        gridView.setAdapter(adapter);
        FloatingActionButton addEmployeeButton = thisView.findViewById(R.id.addEmployeeFloatingButtonId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            addEmployeeButton.show();
        }
        addEmployeeButton.setOnClickListener(view -> addEmployeeButtonClicked());
        return thisView;
    }


    private void addEmployeeButtonClicked() {
        selectedCategories.clear();
        if (Eight.dataModel.getCategories().isEmpty()) {
            EightAlertDialog.showAlertWithMessage("Create at least one category before you add employees!", fragment.getActivity());
            return;
        }
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_employee_layout, EmployeesFragment.this, getLayoutInflater(), "Add employee");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        LinearLayout categoriesLayout = alertView.findViewById(R.id.categoriesLayout);
        drawLayout = true;
        for (CheckBox checkBox : getCategoriesAsListOfCheckBoxes()) {
            categoriesLayout.addView(checkBox);
        }
        drawLayout = false;
        Button addButton = alertView.findViewById(R.id.insertOrEditEmployeeButtonId);
        addButton.setText(R.string.add);
        Button closeButton = alertView.findViewById(R.id.employeeCloseButtonId);
        EditText employeeNameEditText = alertView.findViewById(R.id.employeeNameEditTextId);
        addButton.setOnClickListener(v -> {
            String employeeName = employeeNameEditText.getText().toString();
            if (employeeName.equals(" ")) {
                EightAlertDialog
                        .showAlertWithMessage("Set employee name...", fragment.getActivity());
                return;
            }
            List<String> categoryIds = getSelectedCategoryIds();
            if (categoryIds == null) {
                EightAlertDialog.showAlertWithMessage("Please select at least one category!", fragment.getActivity());
                return;
            }

            Employee employee = new Employee();
            employee.setCategories(categoryIds);
            employee.setName(employeeName);
            employee.setFirmId(Eight.dataModel.getFirm().getId());
            Eight.firestoreManager.insertEmployee(employee);
            Eight.dataModel.addEmployee(employee);
            // insert schedule based on firm schedule
            Schedule schedule = Eight.firestoreManager.insertScheduleBasedOnSchedule(Eight.dataModel.getFirm().getSchedule());
            employee.setScheduleId(schedule.getId());
            Eight.firestoreManager.updateEmployeeWithScheduleId(employee.getId(), schedule.getId());
            employee.setSchedule(schedule);
            adapter.setEmployees(Eight.dataModel.getEmployees());
            addAlertDialog.close();
        });
        addAlertDialog.show();
        closeButton.setOnClickListener(v -> addAlertDialog.close());
    }


    public void addCategory(Category category) {
        selectedCategories.add(category);
    }

    public void removeCategory(Category category) {
        selectedCategories.remove(category);
    }

    public List<CheckBox> getCategoriesAsListOfCheckBoxes() {
        List<CheckBox> checkBoxes = new ArrayList<>();
        List<Category> categories = Eight.dataModel.getCategories();
        for (Category category : categories) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(category.getName());
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setTypeface(Typeface.create("open_sans_semibold", Typeface.BOLD), Typeface.BOLD);
            checkBox.setTextColor(Color.WHITE);
            checkBox.setTextSize(12);
            checkBox.setOnCheckedChangeListener((compoundButton, b) -> {
                Category c = Eight.dataModel.getCategoryFromCategoryName(compoundButton.getText().toString());
                if (drawLayout) {
                    return;
                }
                if (b) {
                    addCategory(c);
                } else {
                    removeCategory(c);
                }
            });
            if (!selectedCategories.isEmpty()) {
                for (Category c : selectedCategories) {
                    if (c.getName().equals(category.getName())) {
                        checkBox.setChecked(true);
                    }
                }
            }
            checkBoxes.add(checkBox);
        }
        return checkBoxes;
    }

    public List<String> getSelectedCategoryIds() {
        List<String> categories = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        if (selectedCategories.isEmpty()) {
            return null;
        }
        for (int i = 0; i < selectedCategories.size(); i++) {
            categories.add(selectedCategories.get(i).getId());
            if (i == selectedCategories.size() - 1) {
                stringBuilder.append(selectedCategories.get(i).getId());
            } else {
                stringBuilder.append(selectedCategories.get(i).getId());
                stringBuilder.append(",");
            }
        }
        return categories;
        //return stringBuilder.toString();
    }

    protected List<Category> getSelectedCategories() {
        return selectedCategories;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (EightSharedPreferences.getInstance().isCustomerMode()) {
            return;
        }
        if (InAppBilling.getInstance().getBillingClient().isReady()) {
            InAppBilling.getInstance().isSubscribed(subscribed -> {
                if (!subscribed) {
                    Intent intent = new Intent(this.getContext(), BillingActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
