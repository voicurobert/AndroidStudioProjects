package rvo.mobilegateway.insert_activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.google.android.gms.maps.model.LatLng;

import rvo.mobilegateway.R;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;

/**
 * Created by Robert on 25.11.2015.
 */
public class InsertActivity extends Activity{

    private LinearLayout baseLayout;
    private AbstractSmallWorldObject objectToInsert;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.insert_activity_layout );
        baseLayout = ( LinearLayout ) findViewById( R.id.baseLayout );
        setViewComponentsForObjectSelection();
    }

    private void setViewComponentsForObjectSelection( ){
        final Spinner spinner = new Spinner( getApplicationContext() );
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource( this, R.array.insert_objects_array, android.R.layout.simple_spinner_item );
        spinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( spinnerAdapter );
        baseLayout.addView( spinner );

        Button nextButton = new Button( getApplicationContext() );
        nextButton.setText( "Next" );
        nextButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ) {

                Bundle bundle = getIntent().getExtras();
                float latitude = (float)bundle.getDouble( "latitude" );
                float longitude = (float)bundle.getDouble( "longitude"  );
                LatLng location = new LatLng( latitude, longitude );
                switch( spinner.getSelectedItem().toString() ){
                    case UndergroundUtilityBox.EXTERNAL_NAME :
                        objectToInsert = new UndergroundUtilityBox(  );
                        ( ( UndergroundUtilityBox ) objectToInsert ).location = location;
                        break;
                    case Pole.EXTERNAL_NAME :
                        objectToInsert = new Pole();
                        ( (Pole) objectToInsert ).location = location;
                        break;
                    case Hub.EXTERNAL_NAME :
                        objectToInsert = new Hub();
                        ((Hub) objectToInsert).location = location;
                        break;
                    case Building.EXTERNAL_NAME :
                        objectToInsert = new Building();
                        ((Building)objectToInsert).location = location;
                        break;
                    case TerminalEnclosure.EXTERNAL_NAME :
                        objectToInsert = new TerminalEnclosure();
                        ((TerminalEnclosure)objectToInsert).location = location;
                        break;
                    case AccessPoint.EXTERNAL_NAME :
                        objectToInsert = new AccessPoint( );
                        ((AccessPoint)objectToInsert).location = location;
                        break;
                }
                baseLayout.removeAllViews( );
                setViewComponentsForObjectInsertion( );
                finish();
            }
        } );
        baseLayout.addView( nextButton );
    }


    private void setViewComponentsForObjectInsertion(){
        MobileGatewayMapActivity.masterActivity.setEditorLayoutView( );
        MobileGatewayMapActivity.masterActivity.getEditorFragment( ).setEditorObject( objectToInsert );
        MobileGatewayMapActivity.masterActivity.getEditorFragment().setMode( 1 );
    }
}
