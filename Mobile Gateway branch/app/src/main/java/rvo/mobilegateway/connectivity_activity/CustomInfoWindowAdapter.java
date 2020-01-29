package rvo.mobilegateway.connectivity_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;

/**
 * Created by Robert on 10/19/2015.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private AppCompatActivity activity;

    public CustomInfoWindowAdapter( AppCompatActivity activity ){
        this.activity = activity;
    }

    @Override
    public View getInfoWindow( Marker marker ) {
        return null;
    }


    @Override
    public View getInfoContents( Marker marker ) {
        LinearLayout mainView = ( LinearLayout) activity.getLayoutInflater().inflate( R.layout.custom_info_window_layout, null );
        TextView title = ( TextView ) mainView.findViewById( R.id.titleInfoWindowTextView );
        String markerTitle = marker.getTitle( );
        title.setText( markerTitle );
        title.setTypeface( null, Typeface.BOLD );
        title.setTextColor( Color.RED );
        title.setTextSize( 20 );
        switch( markerTitle ){
            case UndergroundUtilityBox.EXTERNAL_NAME:
                UndergroundUtilityBox uub = PniDataStoreSingleton.instance.getUubByLocation( marker.getPosition( ) );
                mainView.addView( uub.getViewForInfoWindow( activity ) );
                break;
            case Hub.EXTERNAL_NAME:
                Hub hub = PniDataStoreSingleton.instance.getHubByLocation( marker.getPosition( ) );
                mainView.addView( hub.getViewForInfoWindow( activity ) );
                break;
            case Pole.EXTERNAL_NAME:
                Pole pole = PniDataStoreSingleton.instance.getPoleByLocation( marker.getPosition( ) );
                mainView.addView( pole.getViewForInfoWindow( activity ) );
                break;
            case AccessPoint.EXTERNAL_NAME:
                AccessPoint ap = PniDataStoreSingleton.instance.getAccessPointByLocation( marker.getPosition( ) );
                mainView.addView( ap.getViewForInfoWindow( activity ) );
                break;
            case Building.EXTERNAL_NAME:
                Building b = PniDataStoreSingleton.instance.getBuildingByLocation( marker.getPosition( ) );
                mainView.addView( b.getViewForInfoWindow( activity ) );
                break;
            case TerminalEnclosure.EXTERNAL_NAME:
                TerminalEnclosure te = PniDataStoreSingleton.instance.getTerminalEnclosureByLocation( marker.getPosition( ) );
                mainView.addView( te.getViewForInfoWindow( activity ) );
                break;
        }
        return mainView;
    }
}
