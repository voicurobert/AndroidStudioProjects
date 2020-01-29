package rvo.mobilegateway;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 8/12/2015.
 */


public class L {
    private static final String TAG = MobileGatewayMapActivity.class.getName();
    public static void m( String message ){
        Log.d(TAG, message);
    }

    public static void t( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();

    }
}
