package rvo.com.book.main_app;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.eight_db.Firm;

public class FirmsAdapter implements ListAdapter {
    private ActiveFirmsFragment context;
    private List<Firm> firms;


    public FirmsAdapter(ActiveFirmsFragment context, List<Firm> firms) {
        this.firms = firms;
        this.context = context;
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
        return firms.size();
    }

    @Override
    public Firm getItem(int i) {
        return firms.get(i);
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
            LayoutInflater layoutInflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.firm_item, null);
        }
        final Firm firm = getItem(i);
        if (firm == null) {
            return view;
        }
        ImageView callButton = view.findViewById(R.id.callFirm);
        TextView firmNameTextView = view.findViewById(R.id.firmNameTextViewId);
        TextView firmAddressTextView = view.findViewById(R.id.firmAddressTextView);
        TextView firmScheduleTextView = view.findViewById(R.id.firmScheduleTextView);
        firmNameTextView.setText(firm.getName());
        String address = firm.getAddress();
        String[] split = address.split(",");
        String newAddress = split.length >= 1 ? split[0] : address;
        firmAddressTextView.setText(newAddress);
        if (firm.getSchedule() != null) {
            firmScheduleTextView.setText(firm.getSchedule().toString());
        } else {
            Eight.dataModel.initialiseScheduleForFirm(firm, () -> firmScheduleTextView.setText(firm.getSchedule().toString()));
        }
        CardView cardView = view.findViewById(R.id.firmCardViewId);
        cardView.setOnClickListener(view1 -> activateFirmDetailsActivityForFirm(firm));
        callButton.setOnClickListener(view1 -> context.callFirm(firm));
        return view;
    }

    private void activateFirmDetailsActivityForFirm(Firm firm) {
        Eight.dataModel.setFirm(firm);
        context.activateProgress();
        Eight.dataModel.initialiseDataStoreForSelectedFirm(firm, () -> {
            context.deactivateProgress();
            context.activateFirmDetailsActivity();
        });
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

}
