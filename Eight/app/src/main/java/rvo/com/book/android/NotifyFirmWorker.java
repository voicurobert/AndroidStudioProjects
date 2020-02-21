package rvo.com.book.android;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.FirebaseRecord;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.datamodel.repositories.BookingRepository;
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
        Firm firm = DataModel.getInstance().getFirm();
        if (firm == null) {
            firmRepository.objectFromEmail(sharedPreferences.getString(EightSharedPreferences.FIRM_EMAIL_KEY), object -> processBookingsForFirm((Firm) object));
        } else {
            return processBookingsForFirm(firm);
        }
        return Result.success();
    }

    private Result processBookingsForFirm(Firm firm) {
        BookingRepository.getInstance().getTodaysBookingsForFirm(firm, objects -> {
            List<Booking> bookings = new ArrayList<>();
            for(FirebaseRecord record : objects){
                bookings.add((Booking)record);
            }
        });
        return Result.success();
    }
}
