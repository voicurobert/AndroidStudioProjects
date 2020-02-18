package rvo.com.book.android.main_app;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import rvo.com.book.R;

public class CustomSpinnerAdapter implements SpinnerAdapter {

    private List<String> items;
    private Context context;

    public CustomSpinnerAdapter(Context context, List<String> items) {
        this.items = items;
        this.context = context;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int i) {
        return items.get(i);
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
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.custom_spinner_item, null);
            }
        }
        if (view != null) {
            String name = getItem(i);
            TextView employeeName = view.findViewById(R.id.chooseEmployeeTextViewId);
            employeeName.setText(name);
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

    @Override
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null) {
                view = layoutInflater.inflate(R.layout.custom_spinner_item, null);
            }
        }
        if (view != null) {
            String name = getItem(i);
            TextView employeeName = view.findViewById(R.id.chooseEmployeeTextViewId);
            employeeName.setText(name);
        }

        return view;
    }
}
