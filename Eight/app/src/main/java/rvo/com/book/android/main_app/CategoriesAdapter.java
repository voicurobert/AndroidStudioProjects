package rvo.com.book.android.main_app;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.datamodel.entities.Category;

public class CategoriesAdapter implements ListAdapter {

    private List<Category> categories;
    private CategoriesFragment categoriesFragment;

    public CategoriesAdapter(CategoriesFragment categoriesFragment, List<Category> categories) {
        this.categoriesFragment = categoriesFragment;
        this.categories = categories;
    }

    public void setCategories() {
        categories = Eight.dataModel.getCategories();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Category getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Category category = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) categoriesFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_group, null);
        }
        CardView categoryCard = convertView.findViewById(R.id.categoryCardView);
        categoryCard.setOnClickListener(v -> activateProductsFragmentForCategory(category));
        categoryCard.setOnLongClickListener(v -> {
            categoriesFragment.editCategoryButtonClicked(category);
            return true;
        });
        TextView textView = convertView.findViewById(R.id.categoryNameTextView);
        textView.setText(category.getName());
        return convertView;
    }

    private void activateProductsFragmentForCategory(Category category) {
        categoriesFragment.getActivity().getSupportFragmentManager()
                          .beginTransaction()
                          .add(R.id.firmDetailsFragmentLayoutId, new ProductsFragment(category, categoriesFragment))
                          .addToBackStack("fragment")
                          .commit();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
