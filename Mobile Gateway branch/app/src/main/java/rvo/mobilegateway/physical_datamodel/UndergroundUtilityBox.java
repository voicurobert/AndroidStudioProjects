package rvo.mobilegateway.physical_datamodel;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
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


public class UndergroundUtilityBox extends AbstractTelcoObject implements Comparable, IMapListener {

    public static final String COLLECTION_NAME = "uub";
    public static final String EXTERNAL_NAME = "Underground Utility Box";
    public static final String SPECIFICATION_NAME = "uub_spec";
    public LatLng location = null;
    public String constructionStatus = "";
    public String name = "";
    public String type = "";
    public String oroAlias = "";
    public String owner = "";
    public String ownerAlias = "";
    public String label = "";
    public MarkerOptions markerOptions = null;
    public Marker marker = null;
    public BitmapDescriptor icon = null;
	
	
	public UndergroundUtilityBox( ){

	}

	public UndergroundUtilityBox( int id, LatLng coordinate, String constructionStatus, String type, String name, MarkerOptions markerOptions ){
		this.id =  id ;
		this.location = coordinate;
        this.constructionStatus = constructionStatus;
		this.type = type;
        this.name = name;
		this.markerOptions = markerOptions;
	}

    @Override
    public boolean equals(Object o) {
        if (o instanceof UndergroundUtilityBox && ((UndergroundUtilityBox) o).id == this.id) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        String name = EXTERNAL_NAME;
        name = name + " - ";
        String recordName = this.name;
        if( recordName != null){
            name = name + recordName;
        }else{
            name = name + this.type;
        }
        return name;
    }

    @Override
    public int compareTo( Object another ) {
        return 1;
    }

    public LinearLayout getViewForInfoWindow( AppCompatActivity activity ){
        LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        // add a linear layout for every "viewable" attribute that is not unset
        if( name != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.NAME_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( name );
            linearLayout.addView( childLinearLayout );
        }
        if( constructionStatus != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.CONSTRUCTION_STATUS_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( constructionStatus );
            linearLayout.addView( childLinearLayout );
        }
        if( type != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.TYPE_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( type );
            linearLayout.addView( childLinearLayout );
        }
        return linearLayout;
    }

    @Override
    public Map< String, String > fieldsAndValues( ){
        Map< String, String > mapping = new HashMap<>(  );
        // name
        mapping.put( SmallworldFieldMetadata.NAME_FIELD_NAME, name );
        // oro alias
        mapping.put( SmallworldFieldMetadata.ORO_ALIAS_FIELD_NAME, oroAlias );
        // oro owner
        mapping.put( SmallworldFieldMetadata.ORO_OWNER_FIELD_NAME, owner );
        // oro owner alias
        mapping.put( SmallworldFieldMetadata.ORO_OWNER_ALIAS_FIELD_NAME, ownerAlias );
        // construction status
        mapping.put( SmallworldFieldMetadata.CONSTRUCTION_STATUS_FIELD_NAME, constructionStatus );
        // type
        mapping.put( SmallworldFieldMetadata.TYPE_FIELD_NAME, type );
        // specification
        mapping.put( SmallworldFieldMetadata.SPECIFICATION_FIELD_NAME, specificationName );
        return mapping;
    }

    @Override
    public String getSpecificationName( ){
        return SPECIFICATION_NAME;
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
    public LatLng getLocation( ){
        return markerOptions.getPosition();
    }

    @Override
    public BitmapDescriptor getIco( ){
        if( MapEngine.isObjectSelected( this ) || ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( id ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.selected_uub );
        }else{
            return BitmapDescriptorFactory.fromResource( R.drawable.uub );
        }
    }

    @Override
    public int getColorId( ){
        return 0;
    }

    @Override
    public int getRouteWidth( ){
        return 0;
    }

    @Override
    public void prepareTasks( Context context, String collectionName, String id ){
        ASynchronousThreadExecutor aSynchronousThreadExecutor = new ASynchronousThreadExecutor( 4 );
        aSynchronousThreadExecutor.addTaskToRun( setInternalEquipments( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setPassingThroughCables( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setConnectedCables( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setLogicalObjects( context, collectionName, id) );
        setaSynchronousThreadExecutor( aSynchronousThreadExecutor );
    }

}
