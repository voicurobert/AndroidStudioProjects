package rvo.mobilegateway.physical_datamodel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Robert on 04/03/16.
 */
public interface IMapListener{
    LatLng getLocation();
    BitmapDescriptor getIco( );
    int getColorId();
    int getRouteWidth();
}
