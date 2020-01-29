package rvo.com.book.main_app;


import android.content.Intent;
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

import rvo.com.book.R;
import rvo.com.book.billing.BillingActivity;
import rvo.com.book.billing.InAppBilling;
import rvo.com.book.common.Eight;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.eight_db.Booking;
import rvo.com.book.eight_db.Customer;
import rvo.com.book.eight_db.Firm;


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
            owner = Eight.dataModel.getFirm();
        } else {
            owner = Eight.dataModel.getCustomer();
        }
        this.fragment = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.your_bookings_fragment, container, false);
        progressBar = myView.findViewById(R.id.ownerProfileProgressBarId);
        TextView titleTextView = myView.findViewById(R.id.ownerTitleTextViewId);
        bookingsListView = myView.findViewById(R.id.profileBookingsListViewId);
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            titleTextView.setText(R.string.owner_firm_title);
            setTodaysBookings();
        } else {
            titleTextView.setText(R.string.owner_customer_title);
            setCustomerBookings();
        }

        return myView;
    }

    private void setTodaysBookings() {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            showProgressBar();
            Eight.firestoreManager.getPendingBookingsForFirmOwnerId(((Firm) owner), object -> {
                        ArrayList<Booking> bookings = Eight.bookingsFromObject(object);
                        PendingBookingAdapter adapter = new PendingBookingAdapter(getContext(), bookings, () -> setTodaysBookings());
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
            Eight.firestoreManager.getAllBookingsFromCustomerId(customer, object -> {
                ArrayList<Booking> bookings = Eight.bookingsFromObject(object);
                PendingBookingAdapter adapter = new PendingBookingAdapter(fragment.getContext(), bookings, () -> setCustomerBookings());
                bookingsListView.setAdapter(adapter);
                bookingsListView.invalidate();
                goneProgressBar();
            });
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        if (EightSharedPreferences.getInstance().isCustomerMode()) {
            return;
        }
        InAppBilling.getInstance().isSubscribed(subscribed -> {
            if (!subscribed) {
                Intent intent = new Intent(this.getContext(), BillingActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }


    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void goneProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
