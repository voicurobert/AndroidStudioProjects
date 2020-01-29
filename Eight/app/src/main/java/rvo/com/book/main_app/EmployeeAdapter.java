package rvo.com.book.main_app;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

import rvo.com.book.R;
import rvo.com.book.alerts.AddAlertDialog;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.Eight;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.eight_db.Category;
import rvo.com.book.eight_db.Employee;

public class EmployeeAdapter implements ListAdapter {
    private Context context;
    private List<Employee> employees;
    private EmployeesFragment fragment;

    public EmployeeAdapter(Context context, List<Employee> employees, EmployeesFragment fragment) {
        this.employees = employees;
        this.context = context;
        this.fragment = fragment;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return employees.size();
    }

    @Override
    public Employee getItem(int i) {
        return employees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Employee employee = getItem(i);
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.employee_list_item, null);
        }
        CardView employeeCardView = view.findViewById(R.id.employeesCardViewId);
        employeeCardView.setOnLongClickListener(view1 -> {
            editEmployeeButtonClicked(employee);
            return true;
        });
        TextView employeeNameTextView = view
                .findViewById(R.id.employeeNameTextViewId_employee_list_item);
        TextView categoryNamesTextView = view
                .findViewById(R.id.categoryNamesTextViewId_employee_list_item);
        employeeNameTextView.setText(employee.getName());
        categoryNamesTextView.setText(getCategoryNames(employee));

        ImageView employeeSchedule = view.findViewById(R.id.employeeScheduleButtonId);

        if (EightSharedPreferences.getInstance().isFirmMode()) {
            employeeSchedule.setVisibility(View.VISIBLE);
            employeeSchedule.setOnClickListener(view1 -> employeeScheduleButtonClicked(employee));
        }

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private String getCategoryNames(Employee employee) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String categoryId : employee.getCategories()) {
            Category category = Eight.dataModel.getCategoryFromCategoryId(categoryId);
            stringBuilder.append(category.getName());
            stringBuilder.append(", ");
        }
        stringBuilder.setCharAt(stringBuilder.lastIndexOf(", "), ' ');
        return stringBuilder.toString();
    }

    private void editEmployeeButtonClicked(final Employee employee) {
        if (!EightSharedPreferences.getInstance().isFirmMode()) {
            return;
        }
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_employee_layout, fragment, fragment.getLayoutInflater(), "Edit employee");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        LinearLayout categoriesLayout = alertView.findViewById(R.id.categoriesLayout);
        fragment.setSelectedCategories(Eight.dataModel.getCategoriesFromCategoryIds(employee.getCategories()));
        fragment.setDrawLayout(true);
        List<CheckBox> checkBoxes = fragment.getCategoriesAsListOfCheckBoxes();
        for (CheckBox checkBox : checkBoxes) {
            categoriesLayout.addView(checkBox);
        }
        fragment.setDrawLayout(false);
        Button editButton = alertView.findViewById(R.id.insertOrEditEmployeeButtonId);
        editButton.setText(R.string.edit);
        Button deleteButton = alertView.findViewById(R.id.deleteEmployeeButtonId);
        deleteButton.setVisibility(View.VISIBLE);
        Button closeButton = alertView.findViewById(R.id.employeeCloseButtonId);
        EditText employeeNameEditText = alertView.findViewById(R.id.employeeNameEditTextId);
        employeeNameEditText.setText(employee.getName());
        editButton.setOnClickListener(v -> {
            String employeeName = employeeNameEditText.getText().toString();
            if (employeeName.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set employee name...", fragment.getActivity());
                return;
            }
            Eight.firestoreManager.updateEmployee(employee, employeeName, fragment.getSelectedCategoryIds());
            addAlertDialog.close();

        });
        addAlertDialog.show();
        deleteButton.setOnClickListener(v -> {
            Eight.firestoreManager.deleteEmployee(employee);
            Eight.dataModel.removeEmployee(employee);
            addAlertDialog.close();

        });
        closeButton.setOnClickListener(v -> addAlertDialog.close());
    }

    private void employeeScheduleButtonClicked(Employee employee) {
        final Intent intent = new Intent(context, SetScheduleActivity.class);
        intent.putExtra("context", "employee");
        intent.putExtra("employeeId", employee.getId());
        if (employee.getSchedule() == null) {
            Eight.dataModel.initialiseSchedulesForEmployee(employee, () -> fragment.startActivity(intent));
        } else {
            fragment.startActivity(intent);
        }
    }


}
