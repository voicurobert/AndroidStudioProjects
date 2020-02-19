package rvo.com.book.android;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

import rvo.com.book.common.Eight;
import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.FirmRepository;

public class NotifyFirmWorker extends Worker {

    private FirmRepository firmRepository;

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
            firmRepository.objectFromEmail(sharedPreferences.getString(EightSharedPreferences.FIRM_EMAIL_KEY), object -> processBookingsForFirm((Firm) object));
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
