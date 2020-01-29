package rvo.holidayplanner.core;

import android.location.Address;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import rvo.holidayplanner.R;

/**
 * Created by Robert on 7/7/2015.
 */
public class RoutePlanManager {
    public static final RoutePlanManager instance = new RoutePlanManager();
    private List< RoutePlanner > routePlannerList = new ArrayList<>();
    private RoutePlanner currentRoutePlanner = null;
    private RouteEngine routeEngine = new RouteEngine();
    private GoogleMap mainGoogleMap = null;

    private RoutePlanManager(){

    }

    public RoutePlanner getRoutePlanner(){
        return currentRoutePlanner;
    }

    public void setRoutePlanner(RoutePlanner routePlanner) {
        this.currentRoutePlanner = routePlanner;
    }

    public RouteEngine getRouteEngine() {
        return routeEngine;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.mainGoogleMap = googleMap;
    }

    public GoogleMap getGoogleMap() {
        return mainGoogleMap;
    }

    public boolean currentRoutePlannerHasStartingLocation(){

        if( currentRoutePlanner == null || currentRoutePlanner.getStartRouteLocation() == null ){
            return false;
        }else{
            return true;
        }
    }

    public boolean currentRoutePlannerHasEndingLocation(){
        if( currentRoutePlanner == null || currentRoutePlanner.getEndRouteLocation() == null ){
            return false;
        }else{
            return true;
        }
    }

    public void initializeRoutePlanner(){
        this.currentRoutePlanner = new RoutePlanner();
        this.currentRoutePlanner.setIsRouteActive( true );
        this.routePlannerList.add( this.currentRoutePlanner );
    }

    public void setStartingRouteLocation( LatLng coord, Address address ){
        // set starting location of currentRoutePlanner
        RouteLocation loc = new RouteLocation( coord, address );
        this.currentRoutePlanner.setStartRouteLocation(loc);
    }

    public void setEndingRouteLocation( LatLng coord, Address address ){
        RouteLocation loc = new RouteLocation( coord, address );
        this.currentRoutePlanner.setEndRouteLocation(loc);
    }

    public void addMarkerToMap( RouteLocation loc ){
        LatLng position = loc.getCoordinate();
        loc.setMarker(this.mainGoogleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.start_location))));
    }

    public void addMarkerToMap( String type ){
        LatLng position = null;
        RouteLocation loc = null;
        int id = 0;
        if( type.equals( "start" )){
            loc = this.currentRoutePlanner.getStartRouteLocation();
            position = loc.getCoordinate();
            id = R.mipmap.start_location;
        }else if( type.equals( "end" ) ){
            loc = this.currentRoutePlanner.getEndRouteLocation();
            position = loc.getCoordinate();
            id = R.mipmap.end_location;
        }
        loc.setMarker(this.mainGoogleMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource( id ) ) ) );
    }

    public RouteSection addSectionToRoute( RouteLocation startRouteLocation, RouteLocation endRouteLocation){
        RouteSection section = new RouteSection(startRouteLocation, endRouteLocation);
        this.currentRoutePlanner.getSections().add( section );
        return section;
    }

}
