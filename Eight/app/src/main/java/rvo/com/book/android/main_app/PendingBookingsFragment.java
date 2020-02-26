package rvo.com.book.android.main_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.repositories.BookingRepository;

public class PendingBookingsFragment extends Fragment {

    private ListView listView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.pending_bookings_activity_layout, container, false);
        listView = myView.findViewById(R.id.pendingBookingsListViewId);
        refreshBookings();
        return myView;
    }


    protected void refreshBookings() {
        BookingRepository.getInstance().getPendingBookingsForFirmOwnerId(DataModel.getInstance().getFirm(), objects -> {
            List<Booking> bookings = new ArrayList<>();
            for (FirebaseRecord record : objects) {
                bookings.add((Booking) record);
            }
            PendingBookingAdapter adapter = new PendingBookingAdapter(getContext(), bookings, this::refreshBookings, "firm");
            listView.setAdapter(adapter);
            listView.invalidateViews();
        });
    }
}
