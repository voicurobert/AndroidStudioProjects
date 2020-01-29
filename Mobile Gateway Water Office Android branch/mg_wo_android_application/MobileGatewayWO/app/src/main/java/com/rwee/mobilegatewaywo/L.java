package com.rwee.mobilegatewaywo;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;


/**
 * Created by Robert on 8/12/2015.
 */
public class L {
    private static final String TAG = MobileGatewayWOMapActivity.class.getName();
    public static void m( String message ){
        Log.d(TAG, message);
    }

    public static void t( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
    }
}
