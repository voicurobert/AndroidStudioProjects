package rvo.holidayplanner.core;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Robert on 7/6/2015.
 */
public class RouteLocation {
    private LatLng coordinate = null;
    private Address address = null;
    private Marker marker = null;

    public RouteLocation(){

    }

    public RouteLocation(LatLng coord, Address address){
        this.address = address;
        this.coordinate = coord;
    }

    public Address getAddress() {
        return address;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isSameLocationAs( RouteLocation otherRouteLocation){
        if( this.coordinate.latitude == otherRouteLocation.getCoordinate().latitude ||
                this.coordinate.longitude ==  otherRouteLocation.getCoordinate().longitude ){
            return true;
        }
        return false;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
