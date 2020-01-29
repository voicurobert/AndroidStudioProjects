package rvo.mobilegateway.physical_datamodel;


import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.SmallworldFieldMetadata;
import rvo.mobilegateway.custom_view.CustomEditText;
import rvo.mobilegateway.custom_view.CustomSpinner;
import rvo.mobilegateway.engines.ASynchronousThreadExecutor;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.logical_datamodel.LogicalObject;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;


/**
 * Created by Robert on 08/12/15. </br>
 * Structures, by definitions, can have: connected routes, connected cables, passing through cables and internal equipments.
 *
 *
 */


public abstract class AbstractSmallWorldObject implements Serializable{
    public int id;
    public List< LogicalObject > logicalObjects = null;
    public boolean hasSpec = true; // this defaults to true. if a specific instance of this object does not have specification then set it to false
    public String specificationName = "";
    private ASynchronousThreadExecutor aSynchronousThreadExecutor;

    //public List< UndergroundRoute > linkedRoutes = null;

    public abstract String getCollectionName();
    public abstract String getExternalName();
    public abstract Map< String, String > fieldsAndValues();
    public abstract String getSpecificationName();

    public boolean addLogicalObject( LogicalObject logicalObject ){
        if( logicalObjects == null ){
            logicalObjects = new ArrayList<>(  );
        }
        if( !logicalObjects.contains( logicalObject ) ){
            logicalObjects.add( logicalObject );
            return true;
        }else{
            return false;
        }
    }


    protected Runnable setInternalEquipments( final Context context, final String collectionName, final String id ){
        final AbstractSmallWorldObject currentObject = this;
        return new Runnable( ){
            @Override
            public void run( ){
                L.m( "running setInternalEquipments" );
                SmallworldServicesDelegate.instance.callGetEquipmentsService(
                        (AbstractTelcoObject)currentObject,
                        context,
                        collectionName,
                        String.valueOf( id ) );
            }
        };
    }

    protected Runnable setPassingThroughCables( final Context context, final String collectionName, final String id  ){
        final AbstractSmallWorldObject currentObject = this;
        return new Runnable( ){
            @Override
            public void run( ){
                L.m( "running setPassingThroughCables" );

                SmallworldServicesDelegate.instance.callGetCables(
                        ( AbstractTelcoObject )currentObject,
                        context,
                        collectionName,
                        String.valueOf( id ),
                        false );
            }
        };
    }

    protected Runnable setConnectedCables( final Context context, final String collectionName, final String id  ){
        final AbstractSmallWorldObject currentObject = this;
        return new Runnable( ){
            @Override
            public void run( ){
                SmallworldServicesDelegate.instance.callGetCables(
                        (AbstractTelcoObject)currentObject,
                        context,
                        collectionName,
                        String.valueOf( id ),
                        true );
            }
        };
    }

    protected Runnable setLogicalObjects( final Context context, final String collectionName, final String id  ){
        final AbstractSmallWorldObject currentObject = this;
        return new Runnable( ){
            @Override
            public void run( ){
                SmallworldServicesDelegate.instance.callGetAssociatedLniObjects( context, currentObject, collectionName, id );
            }
        };
    }

    public ASynchronousThreadExecutor getaSynchronousThreadExecutor( ){
        return aSynchronousThreadExecutor;
    }

    public void setaSynchronousThreadExecutor( ASynchronousThreadExecutor aSynchronousThreadExecutor ){
        this.aSynchronousThreadExecutor = aSynchronousThreadExecutor;
    }

    public abstract void prepareTasks( Context context, String collectionName, String id  );

    public LinearLayout getLayoutForEditor( final Activity activity ) {
        LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        final Map< String, String > fieldsAndValues = fieldsAndValues();
        Set< String > fields = fieldsAndValues.keySet();
        for( final String field : fields ){
            if( SmallworldFieldMetadata.instance.FIELD_ENUMERATION_MAPPING.get( field ) ){
                LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.enumerated_field_value_edit_layout, null );
                TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
                fieldTV.setText( field );
                final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );
                valueSpinner.setEnumeratedValues(
                        activity,
                        this.getCollectionName( ),
                        SmallworldFieldMetadata.instance.FIELD_MAPPING.get( field ),
                        fieldsAndValues.get( field ) );
                valueSpinner.setTextView( fieldTV );
                linearLayout.addView( childLinearLayout );
            }else{
                LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater( ).inflate( R.layout.field_value_edit_layout, null );
                TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
                fieldTV.setText( field );
                CustomEditText valueTV = ( CustomEditText ) childLinearLayout.findViewById( R.id.valueTV );
                valueTV.setText( fieldsAndValues.get( field ) );
                valueTV.setLabel( fieldTV );
                linearLayout.addView( childLinearLayout );
            }
        }
        return linearLayout;
    }

    public boolean isRoute(){
        if( this instanceof UndergroundRoute || this instanceof AerialRoute ){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean equals( Object o ){
        if( o instanceof AbstractSmallWorldObject ){
            if( this.id == ( ( AbstractSmallWorldObject ) o ).id ){
                return true;
            }
        }else{
            return false;
        }
        return super.equals( o );
    }
}
