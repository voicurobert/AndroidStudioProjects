package rvo.com.book.android.main_app;

import android.app.AlertDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.repositories.FirestoreManager;
import rvo.com.book.android.notification.NotificationManager;

public class PendingBookingAdapter implements ListAdapter {

    private List<Booking> bookings;
    private Context context;
    private ISetBookingActivation bookingActivation;

    public PendingBookingAdapter(Context context, List<Booking> bookings, ISetBookingActivation bookingActivation) {
        this.context = context;
        this.bookings = bookings;
        this.bookingActivation = bookingActivation;
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
        return bookings.size();
    }

    @Override
    public Booking getItem(int i) {
        return bookings.get(i);
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
            view = layoutInflater.inflate(R.layout.pending_booking_item, null);
        }
        Booking booking = getItem(i);
        TextView bookingTextView = view.findViewById(R.id.pendingBookingTextViewId);
        ImageView activateBooking = view.findViewById(R.id.acceptBookingId);
        ImageView declineBooking = view.findViewById(R.id.declineBookingId);
        TextView statusTextView = view.findViewById(R.id.bookingStatusTextViewId);
        if (booking.getCustomerId() != null) {
            Eight.firestoreManager.getObjectFromId(FirestoreManager.CUSTOMERS, booking.getCustomerId(), Customer.class, object -> {
                if (EightSharedPreferences.getInstance().isFirmMode()) {
                    bookingTextView.setText(
                            booking.getPendingBookingDescriptionForFirmMode());
                    activateBooking.setVisibility(View.VISIBLE);
                    declineBooking.setVisibility(View.VISIBLE);
                } else {
                    bookingTextView.setText(
                            booking.getPendingBookingDescriptionForCustomerMode());
                    statusTextView.setVisibility(View.VISIBLE);
                    declineBooking.setVisibility(View.VISIBLE);
                }
                if (booking.isDeclined()) {
                    declineBooking.setEnabled(false);
                }
            });
        }

        statusTextView.setText(booking.activeString());
        activateBooking.setOnClickListener(v -> activateOrDeactivateBooking(booking, Booking.ACTIVE));
        declineBooking.setOnClickListener(v -> activateOrDeactivateBooking(booking, Booking.DECLINE));
        return view;
    }

    private void activateOrDeactivateBooking(Booking booking, Integer status) {
        if (EightSharedPreferences.getInstance().isFirmMode()) {
            String notifTitle;
            String notifBody;
            String alertTitle;
            if (status.equals(Booking.ACTIVE)) {
                alertTitle = "Accept booking on " + booking.dateDescription() + "?";
                notifTitle = "Accept booking";
                notifBody = Eight.dataModel.getFirm().getName() + " has accepted your booking!";
            } else {
                alertTitle = "Reject booking?";
                notifTitle = "Reject booking";
                notifBody = Eight.dataModel.getFirm().getName() + " has rejected your booking!";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(alertTitle);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // activation
                Eight.firestoreManager.setBookingActivationStatus(booking.getId(), status);
                booking.setStatus(status);
                Eight.firestoreManager.getObjectFromId(FirestoreManager.CUSTOMERS, booking.getCustomerId(), Customer.class, object -> {
                    Customer customer = (Customer) object;
                    if (customer != null) {
                        NotificationManager.getInstance().sendNotification(notifTitle, notifBody, customer.getFirebaseToken());
                        if (bookingActivation != null) {
                            bookingActivation.activatedBooking();
                        }
                    }
                });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // customer mode, delete booking
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.delete_booking);
            builder.setPositiveButton("OK", (dialog, which) -> {
                Eight.firestoreManager.deleteBooking(booking.getId());
                NotificationManager.getInstance().sendNotification("Deleted booking.",
                                                                   Eight.dataModel.getCustomer().getName() +
                                                                   " has deleted a booking!",
                                                                   Eight.dataModel.getFirm().getFirebaseToken());
                if (bookingActivation != null) {
                    bookingActivation.activatedBooking();
                }

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
