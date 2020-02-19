package rvo.com.book.android.main_app;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Map;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Employee;

public class BookingAdapter implements ListAdapter {

    private List<Map<String, Booking>> bookingMap;
    private Fragment fragment;
    private Employee employee;

    public BookingAdapter(Fragment fragment, List<Map<String, Booking>> bookingMap,
                          Employee employee) {
        this.bookingMap = bookingMap;
        this.fragment = fragment;
        this.employee = employee;
    }

    public void setBookingMap(List<Map<String, Booking>> bookingMap) {
        this.bookingMap = bookingMap;
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
        return bookingMap.size();
    }

    @Override
    public Map<String, Booking> getItem(int i) {
        return bookingMap.get(i);
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
        Map<String, Booking> bookingsOnTime = getItem(i);
        Activity activity = fragment.getActivity();
        if (activity == null) {
            return view;
        }
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.booking_card_layout, null);
            CardView cardView = view.findViewById(R.id.bookingCardViewId);

            TextView timeTextView = view.findViewById(R.id.bookingTimeTextViewId);
            TextView bookingProductTextView = view.findViewById(R.id.bookingProductTextViewId);
            TextView statusTextView = view.findViewById(R.id.statusTextView);
            String time = (String) bookingsOnTime.keySet().toArray()[0];
            Booking booking = bookingsOnTime.get(time);
            cardView.setOnLongClickListener(v -> {
                if (EightSharedPreferences.getInstance().isFirmMode() && booking != null &&
                    booking.getCustomerId() == null) {
                    ((ScheduleFragment) fragment).rejectFirmBooking(booking);
                }
                return true;
            });
            if (booking != null) {
                switch (booking.getStatus()) {
                    case 1:
                        statusTextView.setText(R.string.status);
                        statusTextView.setTextColor(activity.getResources().getColor(R.color.lime_green));
                        break;
                    case 2:
                        statusTextView.setText(R.string.finished);
                        statusTextView.setTextColor(activity.getResources().getColor(R.color.grey));
                        break;
                    case 0:
                        statusTextView.setText(R.string.rejected);
                        statusTextView.setTextColor(activity.getResources().getColor(R.color.dark_orange));
                        break;
                    case -1:
                        statusTextView.setText(R.string.pending);
                        statusTextView.setTextColor(Color.BLUE);
                        break;
                }
                if (employee.getSchedule() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(time);
                    stringBuilder.append(" - ");
                    stringBuilder.append(booking.getEmployee().calculateBookEndTime(time, booking.getProduct().calculateDurationAsMinutes()));
                    timeTextView.setText(stringBuilder.toString());
                    bookingProductTextView.setText(booking.description());
                } else {
                    bookingProductTextView.setText(time);
                }
            }
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
}
