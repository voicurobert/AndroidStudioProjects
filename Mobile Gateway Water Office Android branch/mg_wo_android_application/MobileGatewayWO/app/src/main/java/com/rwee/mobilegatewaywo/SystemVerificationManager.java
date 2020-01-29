package com.rwee.mobilegatewaywo;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.rwee.mobilegatewaywo.connectivity_activity.SetOnInitialisationCompleteListener;
import com.rwee.mobilegatewaywo.engines.SmallworldServicesDelegate;

import java.util.HashMap;
import java.util.List;



/**
 * Created by Robert on 7/3/2015.
 */
public class SystemVerificationManager{

    public static final SystemVerificationManager instance = new SystemVerificationManager( );
    private boolean networkStatus = false;
    private boolean gssServer = false;
    private boolean locationService = false;
    private boolean googlePlayService = false;


    private SystemVerificationManager( ){

    }

    public static void verify( final Context context ){

        instance.checkForInternetConnection( context );
        //instance.checkForGooglePlayServices( context );
        instance.checkForActiveGPS( context );

        // checking connection to GSS/JBOSS server
        SmallworldServicesDelegate.instance.callGSSDescribe( context, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ) {
                if( complete ) {
                    instance.gssServer = true;
                } else {
                    L.t( context, "No response from GSS server. Consult your system administrator!" );
                    Intent settingsIntent = new Intent( context, SettingsActivity.class );
                    context.startActivity( settingsIntent );
                }
            }
            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }

    private void checkForInternetConnection( Context context ) {

        ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService( context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo = cm.getActiveNetworkInfo( );
        if( ( networkInfo == null ) || ( !networkInfo.isConnectedOrConnecting() ) ){
            networkStatus = true;
            L.t( context, "No Internet connection..." );
        }

    }

    private void checkForGooglePlayServices( Context context ){
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable( context );
        if( resultCode != ConnectionResult.SUCCESS ){
            L.t( context, "No Google Play Services..." );
        }else{
            googlePlayService = true;
        }
    }


    private void checkForActiveGPS( Context context ){
        LocationManager locationManager = ( LocationManager ) context.getSystemService( Context.LOCATION_SERVICE );
        if( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ){
            L.t( context, "No GPS Service..." );
        }else{
            locationService = true;
        }
    }


    public boolean isNetworkStatus( ) {
        return networkStatus;
    }

    public boolean isGooglePlayService( ) {
        return googlePlayService;
    }

    public boolean isGssServer( ) {
        return gssServer;
    }

    public boolean isLocationService( ) {
        return locationService;
    }

    public void setGssServer( boolean gssServer ){
        this.gssServer = gssServer;
    }
}
