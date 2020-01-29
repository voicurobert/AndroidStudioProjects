package com.rwee.mobilegatewaywo.custom_view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.connectivity_activity.SetOnInitialisationCompleteListener;
import com.rwee.mobilegatewaywo.engines.SmallworldServicesDelegate;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDConnectionPipe;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDManhole;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDServiceConnection;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDSewerSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSHydrant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSMainSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSServicePoint;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSStation;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTank;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTee;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTreatmentPlant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSValve;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Robert on 03/02/16.
 */
public class CustomSpinner extends Spinner{

    private TextView textView;

    public CustomSpinner( Context context, int mode ){
        super( context, mode );
    }

    public CustomSpinner( Context context, AttributeSet attrs ){
        super( context, attrs );
    }

    public CustomSpinner( Context context, AttributeSet attrs, int defStyleAttr ){
        super( context, attrs, defStyleAttr );
    }

    public void setEnumeratedValues( final Activity activity, String datasetName, String collectionName, final String fieldName, final String fieldValue ){
        final CustomSpinner self = this;
        SmallworldServicesDelegate.instance.callGetEnumeratedValues( activity, datasetName, collectionName, fieldName, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){
                final String[] values = new String[ result.size( ) ];
                for( int i = 0; i < result.size( ); i++ ){
                    HashMap< String, String > pair = result.get( i );
                    values[ i ] = pair.get( "value" );
                }
                ArrayAdapter< String > adapter = new ArrayAdapter<>( activity, R.layout.adapter_simple_text_view_layout, R.id.spinner_item_text_view, values );
                self.setAdapter( adapter );
                int pos = adapter.getPosition( fieldValue );
                self.setSelection( pos );
            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }

    public void setSpecifications( final Activity activity, String datasetName, String collectionName, final String fieldValue ){
        final CustomSpinner self = this;
        SmallworldServicesDelegate.instance.callGetSpecifications( activity, datasetName, collectionName, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){
                final String[] values = new String[ result.size( ) ];
                for( int i = 0; i < result.size( ); i++ ){
                    HashMap< String, String > pair = result.get( i );
                    values[ i ] = pair.get( "spec_id" );
                }
                ArrayAdapter< String > a = new ArrayAdapter<>( activity, R.layout.adapter_simple_text_view_layout, R.id.spinner_item_text_view, values );
                self.setAdapter( a );
                int pos = a.getPosition( fieldValue );
                self.setSelection( pos );
            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }

    public void setWOLocationTypeObjects( Activity activity ){
        String[] values = {
                WDManhole.EXTERNAL_NAME,
                WDServiceConnection.EXTERNAL_NAME,
                WSHydrant.EXTERNAL_NAME,
                WSServicePoint.EXTERNAL_NAME,
                WSTee.EXTERNAL_NAME,
                WSValve.EXTERNAL_NAME };
        ArrayAdapter< String > a = new ArrayAdapter<>( activity, R.layout.adapter_simple_text_view_layout, R.id.spinner_item_text_view, values );
        setAdapter( a );
    }

    public void setWOExtentTypeObjects( Activity activity ){
        String[] values = {
                WSStation.EXTERNAL_NAME,
                WSTank.EXTERNAL_NAME,
                WSTreatmentPlant.EXTERNAL_NAME};
        ArrayAdapter< String > a = new ArrayAdapter<>( activity, R.layout.adapter_simple_text_view_layout, R.id.spinner_item_text_view, values );
        setAdapter( a );
    }

    public void setWORouteTypeObjects( Activity activity ){
        String[] values = {
                WDConnectionPipe.EXTERNAL_NAME,
                WDSewerSection.EXTERNAL_NAME,
                WSMainSection.EXTERNAL_NAME, };
        ArrayAdapter< String > a = new ArrayAdapter<>( activity, R.layout.adapter_simple_text_view_layout, R.id.spinner_item_text_view, values );
        setAdapter( a );
    }

    public void setTextView( TextView tv ){
        this.textView = tv;
    }

    public TextView getTextView( ){
        return textView;
    }
}
