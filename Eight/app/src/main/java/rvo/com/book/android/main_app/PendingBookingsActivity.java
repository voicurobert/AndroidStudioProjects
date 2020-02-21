package rvo.com.book.android.main_app;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.repositories.BookingRepository;

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
        BookingRepository.getInstance().getPendingBookingsForFirmOwnerId(DataModel.getInstance().getFirm(), objects -> {
            List<Booking> bookings = new ArrayList<>();
            for (FirebaseRecord record : objects){
                bookings.add((Booking)record);
            }
            PendingBookingAdapter adapter = new PendingBookingAdapter(getApplicationContext(), bookings, this::refreshBookings);
            listView.setAdapter(adapter);
            listView.invalidateViews();
        });
    }
}
