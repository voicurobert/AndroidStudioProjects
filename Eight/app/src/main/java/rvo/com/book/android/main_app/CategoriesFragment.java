package rvo.com.book.android.main_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.main_app.alerts.AddAlertDialog;
import rvo.com.book.android.main_app.alerts.EightAlertDialog;
import rvo.com.book.datamodel.entities.Category;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.repositories.CategoryRepository;

public class CategoriesFragment extends Fragment {

    private ProgressBar progressBar;
    private CategoriesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.categories_fragment, container, false);
        GridView gridView = thisView.findViewById(R.id.categoriesGridView);
        List<Category> categories = DataModel.getInstance().getCategories();
        if (categories.size() == 1) {
            gridView.setNumColumns(1);
        } else if (categories.size() == 2) {
            gridView.setNumColumns(2);
        } else {
            gridView.setNumColumns(3);
        }
        adapter = new CategoriesAdapter(this, categories);
        gridView.setAdapter(adapter);

        FloatingActionButton addCategoryButton = thisView.findViewById(R.id.addCategoryButtonId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            addCategoryButton.show();
        }
        addCategoryButton.setOnClickListener(view -> addCategoryButtonClicked());
        progressBar = thisView.findViewById(R.id.categoriesAndProductsProgressBarId);
        return thisView;
    }

    private void addCategoryButtonClicked() {
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_category_layout, CategoriesFragment.this, getLayoutInflater(), "Add category");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        Button addButton = alertView.findViewById(R.id.insertOrEditCategoryButtonId);
        Button closeButton = alertView.findViewById(R.id.categoryCloseButtonId);
        EditText categoryNameEditText = alertView.findViewById(R.id.categoryNameEditTextId);
        addButton.setOnClickListener(view -> {
            String categoryName = categoryNameEditText.getText().toString();
            if (categoryName.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set category...", getActivity());
                return;
            }
            Category category = new Category();
            category.setName(categoryName);
            category.setFirm(DataModel.getInstance().getFirm());
            category.setFirmId(DataModel.getInstance().getFirm().getId());
            progressBar.setVisibility(View.VISIBLE);
            CategoryRepository.getInstance().insertRecord(category).addOnCompleteListener(command -> {
                addAlertDialog.close();
                DataModel.getInstance().addCategory(category);
                progressBar.setVisibility(View.GONE);
                adapter.setCategories();
            });
        });
        addAlertDialog.show();
        closeButton.setOnClickListener(view -> addAlertDialog.close());
    }

    protected void editCategoryButtonClicked(Category category) {
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_category_layout, this, getLayoutInflater(), "Edit category");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        EditText categoryNameEditText = alertView.findViewById(R.id.categoryNameEditTextId);
        categoryNameEditText.setText(category.getName());
        Button editButton = alertView.findViewById(R.id.insertOrEditCategoryButtonId);
        editButton.setText(R.string.edit);
        Button deleteButton = alertView.findViewById(R.id.deleteCategoryButtonId);
        deleteButton.setVisibility(View.VISIBLE);
        Button closeButton = alertView.findViewById(R.id.categoryCloseButtonId);

        editButton.setOnClickListener(view -> {
            String categoryName = categoryNameEditText.getText().toString();
            makeProgressBarVisible();
            category.setName(categoryName);
            CategoryRepository.getInstance().updateRecord(category, Category.NAME, category.getName()).addOnCompleteListener(command -> {
                addAlertDialog.close();
                makeProgressBarGone();
            });
        });
        addAlertDialog.show();
        deleteButton.setOnClickListener(view -> {
            if (!DataModel.getInstance().categoryHasProducts(category)) {
                makeProgressBarVisible();
                CategoryRepository.getInstance().deleteRecord(category).addOnCompleteListener(command -> {
                    DataModel.getInstance().removeCategory(category.getId());
                    adapter.setCategories();
                    makeProgressBarGone();
                });
            } else {
                EightAlertDialog.showAlertWithMessage("The category " + category.getName() + " has products. Delete the products first!", getActivity());
            }
            addAlertDialog.close();
        });

        closeButton.setOnClickListener(view -> addAlertDialog.close());
    }

    protected List<String> getHoursList() {
        List<String> hoursList = new ArrayList<>();
        hoursList.add("0");
        hoursList.add("1");
        hoursList.add("2");
        hoursList.add("3");
        hoursList.add("4");
        return hoursList;
    }

    protected List<String> getMinutesList() {
        List<String> minutesList = new ArrayList<>();
        minutesList.add("0");
        minutesList.add("15");
        minutesList.add("30");
        minutesList.add("45");
        return minutesList;
    }

    private void makeProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void makeProgressBarGone() {
        progressBar.setVisibility(View.GONE);
    }

}
