package rvo.lipe.facebook_login;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import rvo.lipe.LipeLog;

/**
 * Created by Robert on 20/01/2018.
 */

public class CurrentLocation{
    
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Activity activity;
    private static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private Location currentLocation;
    
    
    public CurrentLocation( Activity activity ){
        this.activity = activity;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( activity );
        checkPermission();
        createLocationRequest();
        
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult( LocationResult locationResult ){
                
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                   // LipeLog.log( location.toString() );
                }
            }
        };
        setCurrentLocation();
    }
    
    public void checkPermission(){
        if( ContextCompat.checkSelfPermission( activity, ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( activity, ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( activity,
                    new String[]{ ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION },
                    123 );
        }
    }
    
    public void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval( 10000 );
        locationRequest.setFastestInterval( 5000 );
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest( locationRequest );
        SettingsClient client = LocationServices.getSettingsClient( activity );
        Task< LocationSettingsResponse > task = client.checkLocationSettings( builder.build() );
        
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                setCurrentLocation();
            }
        });
        /*
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException ) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult( activity, REQUEST_CHECK_SETTINGS );
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
        */
    }
    
    public void setCurrentLocation(){
        if( ContextCompat.checkSelfPermission( activity, ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission( activity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            fusedLocationProviderClient.requestLocationUpdates( locationRequest, locationCallback, null );
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener( activity, new OnSuccessListener< Location >(){
                        @Override
                        public void onSuccess( Location location ){
                            if( location != null ){
                                // Logic to handle location object
                              //  LipeLog.log( String.valueOf( location.getLatitude() ) );
                              //  LipeLog.log( String.valueOf( location.getLongitude() ) );
                                currentLocation = location;
                            }
                        }
            } );
        }
    }
    
    public Location getCurrentLocation(){
        return currentLocation;
    }
}
