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
import rvo.com.book.datamodel.entities.Category;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Product;

public class ProductsAdapter implements ListAdapter {

    private ProductsFragment productsFragment;
    private List<Product> products;

    public ProductsAdapter(ProductsFragment productsFragment, List<Product> products) {
        this.productsFragment = productsFragment;
        this.products = products;
    }

    public void setProductsForCategory(Category category) {
        this.products = DataModel.getInstance().getProductsFromCategoryId(category);
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
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
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
        Product product = getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) productsFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.product_group, null);
        }
        CardView productCard = convertView.findViewById(R.id.productsCardView);
        productCard.setOnLongClickListener(v -> {
            productsFragment.editProductButtonClicked(product);
            return true;
        });
        TextView textView = convertView.findViewById(R.id.productDescriptionTextViewId);
        textView.setText(product.getDescription());
        return convertView;
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
