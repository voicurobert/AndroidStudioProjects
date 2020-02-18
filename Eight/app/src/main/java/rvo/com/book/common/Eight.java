package rvo.com.book.common;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import rvo.com.book.datamodel.entities.Booking;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.repositories.FirestoreManager;


public class Eight {
    public static DataModel dataModel = DataModel.getInstance();
    public static FirestoreManager firestoreManager = FirestoreManager.getInstance();

    public static Integer calculateDurationAsMinutes(String duration) {
        String hours;
        int minutes = 0;
        if (duration.contains("-")) {
            hours = duration.split("-")[0];
            minutes += Integer.valueOf(duration.split("-")[1]);
        } else {
            hours = duration;
        }
        int h = Integer.valueOf(hours);
        if (h > 1) {
            for (int i = 1; i <= h; i++) {
                minutes += 60;
            }
        } else if (h == 1) {
            minutes += 60;
        }
        return minutes;
    }

    public static void isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
        }
    }

    public static ArrayList<Booking> bookingsFromObject(Object object) {
        ArrayList<Booking> bookings = new ArrayList<>();
        for (Object o : getArrayOfTObjects(object)) {
            if (o instanceof Booking) {
                bookings.add((Booking) o);
            }
        }
        return bookings;
    }

    private static List<Object> getArrayOfTObjects(Object object) {
        List<Object> list = new ArrayList<>();
        if (object.getClass().isArray()) {
            list = Arrays.asList((Object[]) object);
        } else if (object instanceof Collection) {
            list = new ArrayList<>((Collection<?>) object);
        }
        return list;
    }


    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isConnected = false;
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                Network activeNetwork = connectivityManager.getActiveNetwork();
                if (activeNetwork != null) {
                    NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork);
                    if (networkCapabilities != null) {
                        isConnected = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                      networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                    }
                }
            } else {
                for (Network network : connectivityManager.getAllNetworks()) {
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        isConnected |= networkInfo.isConnected();
                    }
                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        isConnected |= networkInfo.isConnected();
                    }
                }
            }
        }

        return isConnected;
    }
}
