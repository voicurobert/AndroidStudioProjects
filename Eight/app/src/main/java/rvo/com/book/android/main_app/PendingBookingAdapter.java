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
import rvo.com.book.android.EightSharedPreferences;
import rvo.com.book.android.notification.NotificationManager;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Customer;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.BookingRepository;
import rvo.com.book.datamodel.repositories.CustomerRepository;

public class PendingBookingAdapter implements ListAdapter {

    private List<Booking> bookings;
    private Context context;
    private ISetBookingActivation bookingActivation;
    private String mode;

    public PendingBookingAdapter(Context context, List<Booking> bookings, ISetBookingActivation bookingActivation, String mode) {
        this.context = context;
        this.bookings = bookings;
        this.bookingActivation = bookingActivation;
        this.mode = mode;
    }

    @Override public boolean areAllItemsEnabled() {
        return false;
    }

    @Override public boolean isEnabled(int i) {
        return false;
    }

    @Override public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override public int getCount() {
        return bookings.size();
    }

    @Override public Booking getItem(int i) {
        return bookings.get(i);
    }

    @Override public long getItemId(int i) {
        return i;
    }

    @Override public boolean hasStableIds() {
        return false;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.pending_booking_item, null);
        }
        Booking booking = getItem(i);
        TextView bookingTextView = view.findViewById(R.id.pendingBookingTextViewId);
        ImageView activateBooking = view.findViewById(R.id.acceptBookingId);
        ImageView declineBooking = view.findViewById(R.id.declineBookingId);
        TextView statusTextView = view.findViewById(R.id.bookingStatusTextViewId);
        //        if (booking.getCustomerId() != null) {
        //            CustomerRepository.getInstance().objectFromId(booking.getCustomerId(), object -> {
        //                if (EightSharedPreferences.getInstance().isFirmMode()) {
        //                    bookingTextView.setText(booking.getPendingBookingDescriptionForFirmMode());
        //                    activateBooking.setVisibility(View.VISIBLE);
        //                    declineBooking.setVisibility(View.VISIBLE);
        //                } else {
        //                    bookingTextView.setText(booking.getPendingBookingDescriptionForCustomerMode());
        //                    statusTextView.setVisibility(View.VISIBLE);
        //                    declineBooking.setVisibility(View.VISIBLE);
        //                }
        //                if (booking.isDeclined()) {
        //                    declineBooking.setEnabled(false);
        //                }
        //            });
        //        }

        if (EightSharedPreferences.getInstance().isFirmMode()) {
            bookingTextView.setText(booking.getPendingBookingDescriptionForFirmMode());
            activateBooking.setVisibility(View.VISIBLE);
            declineBooking.setVisibility(View.VISIBLE);
        } else {
            bookingTextView.setText(booking.getPendingBookingDescriptionForCustomerMode());
            statusTextView.setVisibility(View.VISIBLE);
            declineBooking.setVisibility(View.VISIBLE);
        }
        if (booking.isDeclined()) {
            declineBooking.setEnabled(false);
        }
        if (mode.equals("today's firm bookings")) {
            bookingTextView.setText(booking.getBookingDescriptionForFirmMode());
            activateBooking.setVisibility(View.GONE);
            declineBooking.setVisibility(View.GONE);
            statusTextView.setVisibility(View.GONE);
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
            Firm firm = DataModel.getInstance().getFirm();
            if (status.equals(Booking.ACTIVE)) {
                alertTitle = "Accept booking on " + booking.dateDescription() + "?";
                notifTitle = "Accept booking";
                notifBody = firm.getName() + " has accepted your booking!";
            } else {
                alertTitle = "Reject booking?";
                notifTitle = "Reject booking";
                notifBody = firm.getName() + " has rejected your booking!";
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(alertTitle);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // activation
                booking.setStatus(status);
                BookingRepository.getInstance().updateRecord(booking, Booking.STATUS, status);
                CustomerRepository.getInstance().objectFromId(booking.getCustomerId(), object -> {
                    Customer customer = (Customer) object;
                    if (customer != null) {
                        NotificationManager.getInstance()
                                           .sendNotification(notifTitle, notifBody, customer
                                                   .getFirebaseToken());
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
                BookingRepository.getInstance().deleteRecord(booking);
                NotificationManager.getInstance().sendNotification("Deleted booking.",
                                                                   DataModel.getInstance()
                                                                            .getCustomer()
                                                                            .getName() +
                                                                   " has deleted a booking!", DataModel
                                                                           .getInstance().getFirm()
                                                                           .getFirebaseToken());
                if (bookingActivation != null) {
                    bookingActivation.activatedBooking();
                }

            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override public int getItemViewType(int i) {
        return i;
    }

    @Override public int getViewTypeCount() {
        return 1;
    }

    @Override public boolean isEmpty() {
        return false;
    }
}
