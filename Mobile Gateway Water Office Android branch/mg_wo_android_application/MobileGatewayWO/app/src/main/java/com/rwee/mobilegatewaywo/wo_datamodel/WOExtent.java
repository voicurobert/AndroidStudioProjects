package com.rwee.mobilegatewaywo.wo_datamodel;

import android.graphics.Color;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

/**
 * Created by Robert on 10/02/16.
 */
public class WOExtent{

    public Polygon polygon;
    public Circle circle;
    public PolygonOptions polygonOptions = new PolygonOptions();
    public int defaultColor = Color.rgb( 40, 100, 40 );
    public int selectColor = Color.RED;

}
