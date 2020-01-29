package rvo.holidayplanner;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Robert on 7/3/2015.
 */
public class InterfaceManager {
    public static final InterfaceManager instance = new InterfaceManager();

    private InterfaceManager(){

    }

    public boolean checkForInternetConnection( Context context ){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService( context.CONNECTIVITY_SERVICE );
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void sendMessageToLogcat( String message ){
        Log.d("TAG", message);
    }

    public void showToast( Context context, String message ){
        Toast.makeText( context, message, Toast.LENGTH_SHORT ).show();
    }
}
