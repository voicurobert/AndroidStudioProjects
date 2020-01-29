package rvo.com.book;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import rvo.com.book.common.Eight;
import rvo.com.book.common.EightSharedPreferences;
import rvo.com.book.eight_db.Booking;
import rvo.com.book.eight_db.Firm;

public class NotifyFirmWorker extends Worker {

    public NotifyFirmWorker(@NotNull Context context, @NotNull WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        return sendNotifications();
    }

    private Result sendNotifications() {
        EightSharedPreferences sharedPreferences = EightSharedPreferences.getInstance();

        if (sharedPreferences.isCustomerMode()) {
            return Result.failure();
        }
        Firm firm = Eight.dataModel.getFirm();
        if (firm == null) {
            Eight.firestoreManager.firmOwnerFromEmail(sharedPreferences.getString(EightSharedPreferences.FIRM_EMAIL_KEY), object -> processBookingsForFirm((Firm) object));
        } else {
            return processBookingsForFirm(firm);
        }
        return Result.success();
    }

    private Result processBookingsForFirm(Firm firm) {
        Eight.firestoreManager.getTodaysBookingsForFirm(firm, object -> {
            ArrayList<Booking> bookings = Eight.bookingsFromObject(object);
        });
        return Result.success();
    }
}
