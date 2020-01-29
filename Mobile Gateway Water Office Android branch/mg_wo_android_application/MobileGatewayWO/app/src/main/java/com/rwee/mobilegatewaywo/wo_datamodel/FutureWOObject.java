package com.rwee.mobilegatewaywo.wo_datamodel;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.custom_view.CustomSpinner;
import com.rwee.mobilegatewaywo.engines.FutureTransactionDelegate;
import com.rwee.mobilegatewaywo.main_activity.EditorFragment;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 19/02/16.
 */
public class FutureWOObject{

    public WOLocation woLocation;
    public WORoute woRoute;
    public WOExtent woExtent;
    public List< Marker > markers = new ArrayList<>(  );
    public Polyline prePolygonRoute;

    public FutureWOObject(){

    }

    public boolean isLocation(){
        if( woLocation != null ){
            return true;
        }
        return false;
    }

    public boolean isRoute(){
        if( woRoute != null ){
            return true;
        }
        return false;
    }

    public boolean isExtent(){
        if( woExtent != null ){
            return true;
        }
        return false;
    }



    public static LinearLayout getLocationFutureWOObjectLayout( Activity activity ){
        final LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        LinearLayout childLinearLayout = ( LinearLayout) activity.getLayoutInflater().inflate( R.layout.insert_wo_location_future_object_layout, null );
        TextView locationWOObjectTypeTextView = ( TextView ) childLinearLayout.findViewById( R.id.woObjectTypeId );
        locationWOObjectTypeTextView.setText( "WO Object type" );
        final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );
        valueSpinner.setWOLocationTypeObjects( activity );
        valueSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ){
            @Override
            public void onItemSelected( AdapterView< ? > parent, View view, int position, long id ){
                String objectType = parent.getSelectedItem( ).toString( );
                if( linearLayout.getChildCount() == 2 && linearLayout.getChildAt( 1 ) != null ){
                    linearLayout.removeViewAt( 1 );
                }
                linearLayout.addView( setLayoutForSelectedWOObjectType( objectType ) );
                    //linearLayout.invalidate();
                linearLayout.requestLayout();
            }
            @Override
            public void onNothingSelected( AdapterView< ? > parent ){}
        } );
      //  TextView imageTextView = ( TextView ) childLinearLayout.findViewById( R.id.imageViewTextViewId );
      //  imageTextView.setText( "Image" );
      //  ImageView imageView = ( ImageView ) childLinearLayout.findViewById( R.id.imageView );
        linearLayout.addView( childLinearLayout );
        return linearLayout;
    }

    public static LinearLayout getRouteFutureWOObjectLayout( Activity activity ){
        final LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        LinearLayout childLinearLayout = ( LinearLayout) activity.getLayoutInflater().inflate( R.layout.insert_wo_location_future_object_layout, null );
        TextView locationWOObjectTypeTextView = ( TextView ) childLinearLayout.findViewById( R.id.woObjectTypeId );
        locationWOObjectTypeTextView.setText( "WO Object type" );
        final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );
        valueSpinner.setWORouteTypeObjects( activity );
        valueSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ){
            @Override
            public void onItemSelected( AdapterView< ? > parent, View view, int position, long id ){
                String objectType = parent.getSelectedItem( ).toString( );
                if( linearLayout.getChildCount() == 2 && linearLayout.getChildAt( 1 ) != null ){
                    linearLayout.removeViewAt( 1 );
                }
                linearLayout.addView( setLayoutForSelectedWOObjectType( objectType ) );
                //linearLayout.invalidate();
                linearLayout.requestLayout();
            }
            @Override
            public void onNothingSelected( AdapterView< ? > parent ){}
        } );
        //  TextView imageTextView = ( TextView ) childLinearLayout.findViewById( R.id.imageViewTextViewId );
        //  imageTextView.setText( "Image" );
        //  ImageView imageView = ( ImageView ) childLinearLayout.findViewById( R.id.imageView );
        linearLayout.addView( childLinearLayout );
        return linearLayout;
    }

    public static LinearLayout getExtentFutureWOObjectLayout( Activity activity ){
        final LinearLayout linearLayout = new LinearLayout( activity.getApplicationContext() );
        linearLayout.setGravity( Gravity.CENTER );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        LinearLayout childLinearLayout = ( LinearLayout) activity.getLayoutInflater().inflate( R.layout.insert_wo_location_future_object_layout, null );
        TextView locationWOObjectTypeTextView = ( TextView ) childLinearLayout.findViewById( R.id.woObjectTypeId );
        locationWOObjectTypeTextView.setText( "WO Object type" );
        final CustomSpinner valueSpinner = ( CustomSpinner ) childLinearLayout.findViewById( R.id.valueSpinner );
        valueSpinner.setWOExtentTypeObjects( activity );
        valueSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ){
            @Override
            public void onItemSelected( AdapterView< ? > parent, View view, int position, long id ){
                String objectType = parent.getSelectedItem( ).toString( );
                if( linearLayout.getChildCount() == 2 && linearLayout.getChildAt( 1 ) != null ){
                    linearLayout.removeViewAt( 1 );
                }
                linearLayout.addView( setLayoutForSelectedWOObjectType( objectType ) );
                //linearLayout.invalidate();
                linearLayout.requestLayout();
            }
            @Override
            public void onNothingSelected( AdapterView< ? > parent ){}
        } );
        //  TextView imageTextView = ( TextView ) childLinearLayout.findViewById( R.id.imageViewTextViewId );
        //  imageTextView.setText( "Image" );
        //  ImageView imageView = ( ImageView ) childLinearLayout.findViewById( R.id.imageView );
        linearLayout.addView( childLinearLayout );
        return linearLayout;
    }


    public String getGeometryString(){
        String geometryString = "";
        if( woLocation != null ){
            LatLng coord = woLocation.coordinate;
            geometryString = geometryString.concat( String.valueOf( coord.longitude ) + "-" + String.valueOf( coord.latitude ) );
        }else if( woRoute != null ){
            List< LatLng > points = woRoute.polylineOptions.getPoints();
            for( LatLng point : points ){
                geometryString = geometryString.concat( String.valueOf( point.longitude ) + "-" + String.valueOf( point.latitude ) ) + ";";
            }
        }else if( woExtent != null ){
            List< LatLng > points = woExtent.polygonOptions.getPoints();
            for( LatLng point : points ){
                geometryString = geometryString.concat( String.valueOf( point.longitude ) + "-" + String.valueOf( point.latitude ) ) + ";";
            }
        }

        return geometryString;
    }

    public String getRouteLength(){
        double routeLength = SphericalUtil.computeLength( this.woRoute.polylineOptions.getPoints() );

        return String.valueOf( routeLength ).substring( 0, 6 );
    }

    public static LinearLayout setLayoutForSelectedWOObjectType( String objectType ){
        EditorFragment editorFragment = MobileGatewayWOMapActivity.masterActivity.getEditorFragment( );
        Activity activity = editorFragment.getActivity( );
        editorFragment.getProposedFieldsAndValues().clear();
        switch( objectType ){
            case WDManhole.EXTERNAL_NAME :
                WDManhole manhole = new WDManhole();
                editorFragment.setCurrentObject( manhole );
                return manhole.getLayoutForEditor( activity );
            case WDServiceConnection.EXTERNAL_NAME :
                WDServiceConnection serviceConnection = new WDServiceConnection();
                editorFragment.setCurrentObject( serviceConnection );
                return serviceConnection.getLayoutForEditor( activity );
            case WSHydrant.EXTERNAL_NAME :
                WSHydrant hydrant = new WSHydrant();
                editorFragment.setCurrentObject( hydrant );
                return hydrant.getLayoutForEditor( activity );
            case WSServicePoint.EXTERNAL_NAME :
                WSServicePoint servicePoint = new WSServicePoint();
                editorFragment.setCurrentObject( servicePoint );
                return servicePoint.getLayoutForEditor( activity );
            case WSTee.EXTERNAL_NAME :
                WSTee tee = new WSTee();
                editorFragment.setCurrentObject( tee );
                return tee.getLayoutForEditor( activity );
            case WSValve.EXTERNAL_NAME :
                WSValve valve = new WSValve();
                editorFragment.setCurrentObject( valve );
                return valve.getLayoutForEditor( activity );
            case WDConnectionPipe.EXTERNAL_NAME :
                WDConnectionPipe connectionPipe = new WDConnectionPipe();
                connectionPipe.calculatedPipeLength = FutureTransactionDelegate.getInstance( ).getCurrentFutureObject().getRouteLength();
                editorFragment.setCurrentObject( connectionPipe );
                return connectionPipe.getLayoutForEditor( activity );
            case WDSewerSection.EXTERNAL_NAME :
                WDSewerSection sewerSection = new WDSewerSection();
                sewerSection.sewerSectionLength = FutureTransactionDelegate.getInstance( ).getCurrentFutureObject().getRouteLength();
                editorFragment.setCurrentObject( sewerSection );
                return sewerSection.getLayoutForEditor( activity );
            case WSMainSection.EXTERNAL_NAME :
                WSMainSection mainSection = new WSMainSection();
                mainSection.length = FutureTransactionDelegate.getInstance( ).getCurrentFutureObject().getRouteLength();
                editorFragment.setCurrentObject( mainSection );
                return mainSection.getLayoutForEditor( activity );
            case WSStation.EXTERNAL_NAME :
                WSStation station = new WSStation();
                editorFragment.setCurrentObject( station );
                return station.getLayoutForEditor( activity );
            case WSTank.EXTERNAL_NAME :
                WSTank tank = new WSTank();
                editorFragment.setCurrentObject( tank );
                return tank.getLayoutForEditor( activity );
            case WSTreatmentPlant.EXTERNAL_NAME :
                WSTreatmentPlant treatmentPlant = new WSTreatmentPlant();
                editorFragment.setCurrentObject( treatmentPlant );
                return treatmentPlant.getLayoutForEditor( activity );
        }
        return null;
    }

}
