package rvo.com.book;


/**
 * Created by Robert on 29/03/2018.
 */

public class Log {

    private static final String TAG = "EIGHT";

    public static void log(String message) {
        android.util.Log.d(TAG, message);
    }
}
