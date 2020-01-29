package com.rwee.mobilegatewaywo.wo_datamodel;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Robert on 10/02/16.
 */
public class WOLocation{
    public LatLng coordinate;
    public Marker marker;
    public MarkerOptions markerOptions = new MarkerOptions();
    public BitmapDescriptor icon = null;
}
