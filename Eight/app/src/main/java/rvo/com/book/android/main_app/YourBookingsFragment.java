package rvo.com.book.android.main_app;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.BookingRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class YourBookingsFragment extends Fragment {

    private Object owner;
    private YourBookingsFragment fragment;
    private ListView bookingsListView;
    private ProgressBar progressBar;

    public YourBookingsFragment() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            owner = DataModel.getInstance().getFirm();
        } else {
            owner = DataModel.getInstance().getCustomer();
        }
        this.fragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.your_bookings_fragment, container, false);
        progressBar = myView.findViewById(R.id.ownerProfileProgressBarId);
        bookingsListView = myView.findViewById(R.id.profileBookingsListViewId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            setTodaysBookings();
        } else {
            setCustomerBookings();
        }
        return myView;
    }

    private void setTodaysBookings() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            showProgressBar();
            BookingRepository.getInstance().getTodaysBookingsForFirm((Firm) owner, objects -> {
                List<Booking> bookings = new ArrayList<>();
                for (FirebaseRecord record : objects) {
                    bookings.add((Booking) record);
                }
                PendingBookingAdapter adapter = new PendingBookingAdapter(getContext(), bookings, this::setTodaysBookings, "today's firm bookings");
                goneProgressBar();
                bookingsListView.setAdapter(adapter);
                bookingsListView.invalidateViews();
            });
        }
    }

    private void setCustomerBookings() {
        Customer customer = (Customer) owner;
        if (customer != null) {
            showProgressBar();
            BookingRepository.getInstance().getAllBookingsFromCustomerId(customer, objects -> {
                if (!objects.isEmpty()) {
                    List<Booking> bookings = new ArrayList<>();
                    for (FirebaseRecord record : objects) {
                        bookings.add((Booking) record);
                    }
                    PendingBookingAdapter adapter = new PendingBookingAdapter(fragment.getContext(), bookings, this::setCustomerBookings, "customer");
                    bookingsListView.setAdapter(adapter);
                    bookingsListView.invalidate();
                }
                goneProgressBar();
            });
        }
    }


    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void goneProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
