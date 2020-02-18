package rvo.com.book.android.main_app;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.datamodel.entities.Booking;

public class PendingBookingsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_bookings_activity_layout);

        listView = findViewById(R.id.pendingBookingsListViewId);
        refreshBookings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBookings();
    }

    protected void refreshBookings() {
        Eight.firestoreManager.getPendingBookingsForFirmOwnerId(Eight.dataModel.getFirm(), object -> {
            if (object instanceof ArrayList<?>) {
                List<Booking> bookings = (ArrayList) object;
                PendingBookingAdapter adapter = new PendingBookingAdapter(getApplicationContext(), bookings, () -> refreshBookings());
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }
        });

    }
}
