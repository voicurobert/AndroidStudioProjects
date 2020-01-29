package rvo.mobilegateway.physical_datamodel;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;

import rvo.mobilegateway.R;

/**
 * Created by Robert on 18/01/16.
 */
public class Route {
    public Polyline route;
    public PolylineOptions polylineOptions = new PolylineOptions();
    public int defaultColor = Color.rgb( 40, 100, 40 );
    public int selectColor = Color.RED;


    public LinearLayout getLayoutForEditor( Activity activity ){
        LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );

        // add type
        LinearLayout childLinearLayout = ( LinearLayout ) activity.getLayoutInflater().inflate( R.layout.field_value_edit_layout, null );
        TextView fieldTV = ( TextView ) childLinearLayout.findViewById( R.id.fieldTV );
        fieldTV.setText( "Route Type" );
        EditText valueTV = ( EditText ) childLinearLayout.findViewById( R.id.valueTV );
        valueTV.setText( "" );
        linearLayout.addView( childLinearLayout );

        return linearLayout;
    }


    public Map< String, String > fieldsAndValues( ){
        return null;
    }
}
