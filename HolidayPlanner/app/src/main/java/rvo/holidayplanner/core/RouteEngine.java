package rvo.holidayplanner.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by Robert on 7/7/2015.
 */
public class RouteEngine {

    public RouteEngine(){

    }

    public boolean iscurrentLocationAddressCorrect( Context context, LatLng coord ){
        boolean isAddressCorrect = false;
        Geocoder gc = new Geocoder( context);
        try {
            
            List<Address> addressList = gc.getFromLocation( coord.latitude, coord.longitude, 1 );
            for (int i = 0; i < addressList.size(); i++) {
                Address currentAddress = addressList.get(i);
                if( currentAddress != null ){
                    isAddressCorrect = true;
                }else{
                    return iscurrentLocationAddressCorrect( context, coord );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isAddressCorrect;
    }

    public RouteLocation getLocationFromCoordindateAndAddress( LatLng coord, Address address ){
        return new RouteLocation( coord, address );
    }

    public String createURLForDirectionAPI( LatLng startCoord, LatLng endCoord ){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "http://maps.googleapis.com/maps/api/directions/json" );
        stringBuilder.append( "?origin=" );
        stringBuilder.append( String.valueOf( startCoord.latitude ) );
        stringBuilder.append( "," );
        stringBuilder.append( String.valueOf( startCoord.longitude ) );
        stringBuilder.append( "&destination=" );
        stringBuilder.append( String.valueOf( endCoord.latitude ) );
        stringBuilder.append( "," );
        stringBuilder.append( String.valueOf( endCoord.longitude ) );
        stringBuilder.append( "&sensor=false&mode=driving&alternatives=true" );
        return stringBuilder.toString();
    }


    public void saveScreenPositionOnMap( GoogleMap googleMap, Context context ){
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        float longitude = ( float ) cameraPosition.target.longitude;
        float latitude = ( float ) cameraPosition.target.latitude;
        float zoom = cameraPosition.zoom;
        float bearing = cameraPosition.bearing;
        float tilt = cameraPosition.tilt;

        SharedPreferences sharedPrefs = context.getSharedPreferences( "MAP_SETTINGS", 0 );
        SharedPreferences.Editor ed = sharedPrefs.edit();
        ed.putFloat("longitude", longitude);
        ed.putFloat("latitude", latitude);
        ed.putFloat("zoom", zoom);
        ed.putFloat("bearing", bearing);
        ed.putFloat("tilt", tilt);
        ed.commit();
    }

    public void setSavedScreenPositionOnMap( GoogleMap googleMap, Context context ){
        SharedPreferences sharedPrefs = context.getSharedPreferences("MAP_SETTINGS", 0);
        LatLng position = new LatLng( sharedPrefs.getFloat("latitude", 0 ), sharedPrefs.getFloat("longitude", 0 ));
        CameraPosition cp = new CameraPosition.Builder().target(position).
                zoom(sharedPrefs.getFloat("zoom", 13.0f)).
                bearing(sharedPrefs.getFloat("bearing", 30f)).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
    }

    public RouteSection getSectionFromLocations( RoutePlanner routePlanner, LatLng start, LatLng end ){
        List<RouteSection> routeSections = routePlanner.getSections();
        for( int i = 0; i < routeSections.size(); i++ ){
            RouteSection currentSection = routeSections.get( i );
            if( currentSection.getStartRouteLocation().getCoordinate() == start &&
                    currentSection.getEndRouteLocation().getCoordinate() == end){
                return currentSection;
            }
        }
        return null;
    }
}
