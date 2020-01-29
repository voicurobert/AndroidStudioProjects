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

public class Pole extends AbstractTelcoObject implements IMapListener{
    public static final String COLLECTION_NAME = "pole";
    public static final String EXTERNAL_NAME = "Pole";
    public String oroAlias = "";
    public String owner = "";
    public String ownerAlias = "";
    public String name = "";
    public String constructionStatus = "";
    public Marker marker = null;
    public String usage = "";
    public String materialType = "";
    public LatLng location = null;
    public MarkerOptions markerOptions = null;
    public BitmapDescriptor icon = null;


	public Pole(){
		this.hasSpec = false;
	}

    @Override
    public String toString() {
        return "Pole - " + name;
    }

    @Override
    public boolean equals(Object o) {
        if( o instanceof Pole && ((Pole)o).id == this.id ){
            return true;
        }else{
            return false;
        }
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
        if( usage != null ){
            LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_view_layout, null );
            TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
            fieldTV.setText( SmallworldFieldMetadata.USAGE_FIELD_NAME );
            TextView valueTV = ( TextView ) childLinearLayout.findViewById( R.id.valueTV );
            valueTV.setText( usage );
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
        // usage
        mapping.put( SmallworldFieldMetadata.USAGE_FIELD_NAME, usage );
        // material type
        mapping.put( SmallworldFieldMetadata.MATERIAL_TYPE_FIELD_NAME, materialType );


        return mapping;
    }

    @Override
    public String getSpecificationName( ){
        return null;
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
    public void prepareTasks( Context context, String collectionName, String id ){
        ASynchronousThreadExecutor aSynchronousThreadExecutor = new ASynchronousThreadExecutor( 4 );
        aSynchronousThreadExecutor.addTaskToRun( setInternalEquipments( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setPassingThroughCables( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setConnectedCables( context, collectionName, id ) );
        aSynchronousThreadExecutor.addTaskToRun( setLogicalObjects( context, collectionName, id) );
        setaSynchronousThreadExecutor( aSynchronousThreadExecutor );
    }

    @Override
    public BitmapDescriptor getIco( ){
        // first check that this object is selected
        if( MapEngine.isObjectSelected( this ) || ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( id ) ){
            return BitmapDescriptorFactory.fromResource( R.drawable.selected_pole );
        }
        return BitmapDescriptorFactory.fromResource( R.drawable.pole );
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
