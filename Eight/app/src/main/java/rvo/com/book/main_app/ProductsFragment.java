package rvo.com.book.main_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import rvo.com.book.R;
import rvo.com.book.alerts.AddAlertDialog;
import rvo.com.book.alerts.EightAlertDialog;
import rvo.com.book.common.CustomSpinnerAdapter;
import rvo.com.book.common.Eight;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.common.Validator;
import rvo.com.book.eight_db.Category;
import rvo.com.book.eight_db.Product;

public class ProductsFragment extends Fragment {

    private Category category;
    private CategoriesFragment categoriesFragment;
    private ProductsAdapter productsAdapter;

    public ProductsFragment(Category category, CategoriesFragment categoriesFragment) {
        this.categoriesFragment = categoriesFragment;
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View thisView = inflater.inflate(R.layout.products_fragment, container, false);
        GridView gridView = thisView.findViewById(R.id.productsGridView);

        List<Product> products = Eight.dataModel.getProductsFromCategoryId(category);
        if (products.size() == 1) {
            gridView.setNumColumns(1);
        } else {
            gridView.setNumColumns(2);
        }
        productsAdapter = new ProductsAdapter(this, products);
        gridView.setAdapter(productsAdapter);
        FloatingActionButton addProductButton = thisView.findViewById(R.id.addProductButtonId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            addProductButton.show();
        }
        addProductButton.setOnClickListener(view -> addProductButtonClicked());
        return thisView;
    }

    private void addProductButtonClicked() {
        List<String> categories = Eight.dataModel.getCategoryNamesAsList();
        if (categories.isEmpty()) {
            EightAlertDialog.showAlertWithMessage("Create at least one category before you create products!", getActivity());
            return;
        }
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_product_layout, ProductsFragment.this, getLayoutInflater(), "Add Product");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        Spinner categoriesSpinner = alertView.findViewById(R.id.categoriesSpinnerId);
        CustomSpinnerAdapter categoriesAdapter = new CustomSpinnerAdapter(getContext(), Eight.dataModel.getCategoryNamesAsList());
        categoriesSpinner.setAdapter(categoriesAdapter);
        EditText productNameEditText = alertView.findViewById(R.id.productNameEditTextId);
        EditText priceEditText = alertView.findViewById(R.id.priceEditTextId);
        Spinner hoursSpinner = alertView.findViewById(R.id.productHoursSpinnerId);
        CustomSpinnerAdapter hoursAdapter = new CustomSpinnerAdapter(getContext(), categoriesFragment.getHoursList());
        hoursSpinner.setAdapter(hoursAdapter);
        Spinner minutesSpinner = alertView.findViewById(R.id.productMinutesSpinnerId);
        CustomSpinnerAdapter minutesAdapter = new CustomSpinnerAdapter(getContext(), categoriesFragment.getMinutesList());
        minutesSpinner.setAdapter(minutesAdapter);
        hoursSpinner.setSelection(0);
        minutesSpinner.setSelection(1);
        Button addButton = alertView.findViewById(R.id.insertOrEditProductButtonId);
        Button closeButton = alertView.findViewById(R.id.productCloseButtonId);
        addButton.setOnClickListener(view -> {
            String productName = Validator.getProductNameFromEditText(productNameEditText, this);
            String price = priceEditText.getText().toString();
            if (price.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set product name...", getActivity());
            }
            String duration = Validator.createDurationStringFromHoursAndMinutesSpinner(hoursSpinner, minutesSpinner);
            String selectedCategory = (String) categoriesSpinner.getSelectedItem();
            Category category = Eight.dataModel.getCategoryIdFromCategoryName(selectedCategory);
            Product product = new Product();
            product.setName(productName);
            product.setDuration(duration);
            product.setPrice(Integer.valueOf(price));
            product.setFirmCategoryId(category.getId());
            product.setCategory(category);
            product.setFirmId(Eight.dataModel.getFirm().getId());
            product.setFirm(Eight.dataModel.getFirm());
            Eight.firestoreManager.insertProduct(product, () -> {
                Eight.dataModel.addProduct(product);
                productsAdapter.setProductsForCategory(category);
                addAlertDialog.close();
            });
        });
        addAlertDialog.show();
        closeButton.setOnClickListener(view -> addAlertDialog.close());
    }

    protected void editProductButtonClicked(Product product) {
        AddAlertDialog addAlertDialog = new AddAlertDialog(R.layout.add_or_edit_product_layout, this, getLayoutInflater(), "Edit product");
        addAlertDialog.createAlertDialog();
        View alertView = addAlertDialog.getView();
        Spinner categoriesSpinner = alertView.findViewById(R.id.categoriesSpinnerId);
        CustomSpinnerAdapter categoriesAdapter = new CustomSpinnerAdapter(getContext(), Eight.dataModel.getCategoryNamesAsList());
        categoriesSpinner.setAdapter(categoriesAdapter);
        categoriesSpinner.setSelection(Eight.dataModel.getCategories().indexOf(Eight.dataModel.getCategoryFromCategoryId(product.getCategory().getId())));
        EditText productNameEditText = alertView.findViewById(R.id.productNameEditTextId);
        productNameEditText.setText(product.getName());
        EditText priceEditText = alertView.findViewById(R.id.priceEditTextId);
        Spinner hoursSpinner = alertView.findViewById(R.id.productHoursSpinnerId);
        CustomSpinnerAdapter hoursAdapter = new CustomSpinnerAdapter(getContext(), categoriesFragment.getHoursList());
        hoursSpinner.setAdapter(hoursAdapter);
        Spinner minutesSpinner = alertView.findViewById(R.id.productMinutesSpinnerId);
        CustomSpinnerAdapter minutesAdapter = new CustomSpinnerAdapter(getContext(), categoriesFragment.getMinutesList());
        minutesSpinner.setAdapter(minutesAdapter);
        priceEditText.setText(String.valueOf(product.getPrice()));

        hoursSpinner.setSelection(categoriesFragment.getHoursList().indexOf(product.getDurationHours()));
        minutesSpinner.setSelection(categoriesFragment.getMinutesList().indexOf(product.getDurationMinutes()));

        Button editButton = alertView.findViewById(R.id.insertOrEditProductButtonId);
        editButton.setText(R.string.edit);
        Button deleteButton = alertView.findViewById(R.id.deleteProductButtonId);
        deleteButton.setVisibility(View.VISIBLE);
        Button closeButton = alertView.findViewById(R.id.productCloseButtonId);
        editButton.setOnClickListener(view -> {
            String productName = productNameEditText.getText().toString();
            if (productName.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set product name...", getActivity());
            }
            String price = priceEditText.getText().toString();
            if (price.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set price ...", getActivity());
            }
            Integer priceInteger = Integer.valueOf(price);
            String duration = Validator.createDurationStringFromHoursAndMinutesSpinner(hoursSpinner, minutesSpinner);
            if (duration != null && duration.equals(" ")) {
                EightAlertDialog.showAlertWithMessage("Set duration ...", getActivity());
            }
            String selectedCategory = (String) categoriesSpinner.getSelectedItem();
            Category category = Eight.dataModel.getCategoryFromCategoryName(selectedCategory);
            //final int position = Eight.dataModel.getCategories().indexOf( category );

            Eight.firestoreManager.updateProduct(product, productName, priceInteger, duration, category.getId(), () -> {
                productsAdapter.setProductsForCategory(category);
                addAlertDialog.close();
            });

        });
        addAlertDialog.show();
        deleteButton.setOnClickListener(view -> {
            Category category = Eight.dataModel.getCategoryFromCategoryId(product.getCategory().getId());
            Eight.firestoreManager.deleteProduct(product, () -> {
                Eight.dataModel.removeProduct(product.getId());
                productsAdapter.setProductsForCategory(category);
                addAlertDialog.close();
            });
        });
        closeButton.setOnClickListener(view -> addAlertDialog.close());
    }

}
