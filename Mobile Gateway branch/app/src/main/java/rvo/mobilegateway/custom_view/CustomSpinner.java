package rvo.mobilegateway.custom_view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;

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

    public void setEnumeratedValues( final Activity activity, String collectionName, final String fieldName, final String fieldValue ){
        final CustomSpinner self = this;
        SmallworldServicesDelegate.instance.callGetEnumeratedValues( activity, collectionName, fieldName, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ){
                final String[] values = new String[ result.size() ];
                for(  int i = 0; i < result.size(); i ++ ){
                    HashMap< String, String > pair = result.get( i );
                    values[ i ] = pair.get( "spec_id" );
                }
                ArrayAdapter< String > a = new ArrayAdapter<  >( activity, android.R.layout.simple_spinner_item, values );
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

    public void setTextView( TextView tv ){
        this.textView = tv;
    }

    public TextView getTextView( ){
        return textView;
    }
}
