package rvo.mobilegateway.physical_datamodel;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.Map;
import rvo.mobilegateway.R;
import rvo.mobilegateway.SmallworldFieldMetadata;
import rvo.mobilegateway.engines.ASynchronousThreadExecutor;
import rvo.mobilegateway.engines.ConnectivityEngine;
import rvo.mobilegateway.engines.MapEngine;

/**
 * Created by Robert on 8/7/2015.
 */
public class AccessPoint extends AbstractTelcoObject implements IMapListener{
    public static final String EXTERNAL_NAME = "Access Point";
    public static final String COLLECTION_NAME = "access_point";
    public static final String SPECIFICATION_NAME = "access_point_spec";
    public String type = "";
    public MarkerOptions markerOptions = null;
    public LatLng location = null;
    public Marker marker = null;
    public BitmapDescriptor icon = null;

    public AccessPoint(){

    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof AccessPoint && ((AccessPoint)o).id == this.id ){
            return true;
        }else{
            return false;
        }
    }

    public LinearLayout getViewForInfoWindow( Activity activity ){
        LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        // add a linear layout for every "viewable" attribute that is not unset
        if( type != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.TYPE_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( type );
            linearLayout.addView( childLinearLayout );
        }
        if( specificationName != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.SPECIFICATION_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( specificationName );
            linearLayout.addView( childLinearLayout );
        }
        return linearLayout;
    }


    @Override
    public Map< String, String > fieldsAndValues( ){
        Map< String, String > mapping = new HashMap<>(  );
        // type
        mapping.put( SmallworldFieldMetadata.TYPE_FIELD_NAME, type );
        // specification
        if( hasSpec ){
            mapping.put( SmallworldFieldMetadata.SPECIFICATION_FIELD_NAME, specificationName );
        }

        return mapping;
    }

    @Override
    public String getSpecificationName( ){
        return SPECIFICATION_NAME;
    }

    @Override
    public LatLng getLocation( ){
        return markerOptions.getPosition();
    }

    @Override
    public String getCollectionName( ){
        return COLLECTION_NAME;
    }

    @Override
    public String getExternalName( ){
        return EXTERNAL_NAME;
    }

    @Override
    public void prepareTasks( Context context, String collectionName, String id ){
        ASynchronousThreadExecutor aSynchronousThreadExecutor = new ASynchronousThreadExecutor( 3 );
        aSynchronousThreadExecutor.addTaskToRun( setInternalEquipments( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setPassingThroughCables( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setConnectedCables( context, collectionName, id ) );
        setaSynchronousThreadExecutor( aSynchronousThreadExecutor );
    }

    @Override
    public BitmapDescriptor getIco( ){
        // first check that this object is selected
        if( MapEngine.isObjectSelected( this ) || ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( id ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.selected_access_point );
        }
        return BitmapDescriptorFactory.fromResource( R.drawable.access_point );
    }

    @Override
    public int getColorId( ){
        return 0;
    }

    @Override
    public int getRouteWidth( ){
        return 0;
    }
}
