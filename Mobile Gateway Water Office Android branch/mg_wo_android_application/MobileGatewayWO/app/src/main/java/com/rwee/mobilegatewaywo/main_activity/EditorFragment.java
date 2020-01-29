package com.rwee.mobilegatewaywo.main_activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.custom_view.CustomEditText;
import com.rwee.mobilegatewaywo.custom_view.CustomSpinner;
import com.rwee.mobilegatewaywo.engines.FutureTransactionDelegate;
import com.rwee.mobilegatewaywo.engines.SmallworldServicesDelegate;
import com.rwee.mobilegatewaywo.engines.WODataStoreSingleton;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
import com.rwee.mobilegatewaywo.wo_datamodel.FutureWOObject;
import com.rwee.mobilegatewaywo.wo_datamodel.SmallworldFieldMetadata;
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
import java.util.Map;

/**
 * Created by Robert on 09/02/16.
 */
public class EditorFragment extends Fragment{
        //implements AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener, View.OnFocusChangeListener{

    private LinearLayout view;
    private TextView titleTextView = null;
    private LinearLayout buttonsLayout = null;
    private String collectionName = null;
    private int recordId;
    private Map< String, String > proposedFieldsAndValues = new HashMap<>( );
    private int mode = 0;
    private AbstractWaterOfficeObject currentObject;


    public EditorFragment(){

    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = (LinearLayout)inflater.inflate( R.layout.custom_info_window_layout, container, true );
        view.setBackgroundColor( Color.WHITE );
        titleTextView = ( TextView ) view.findViewById( R.id.titleInfoWindowTextView );
        titleTextView.setTextSize( 24 );
        titleTextView.setTypeface( null, Typeface.BOLD );
        buttonsLayout = getButtonsView();
        return view;
    }

    @Nullable
    @Override
    public View getView( ) {
        return view;
    }

    public AbstractWaterOfficeObject getCurrentObject( ){
        return currentObject;
    }

    public void setCurrentObject( AbstractWaterOfficeObject currentObject ){
        this.currentObject = currentObject;
    }

    public Map< String, String > getProposedFieldsAndValues( ){
        return proposedFieldsAndValues;
    }

    public void setEditorObject( Object object ){
        if( object instanceof Marker ){
            setSelectedMarker( ( Marker ) object );
        }else if ( object instanceof Polyline){
            setSelectedPolyline( ( Polyline )object );
        }else if( object instanceof Polygon ){
            setSelectedPolygon( ( Polygon ) object );
        }else if( object instanceof FutureWOObject ){
            //setInsertModeEditor( (FutureWOObject)object );
        }

    }

    private void setSelectedMarker( Marker selectedMarker ) {
        String title = selectedMarker.getTitle( );
        titleTextView.setText( title );
        if( view.getChildAt( 1 ) != null ){
            view.removeView( view.getChildAt( 1 ) );
        }
        LinearLayout childLayout;
        LatLng markerPosition = selectedMarker.getPosition( );
        switch( title ){
            case WDManhole.EXTERNAL_NAME :
                WDManhole manhole = WODataStoreSingleton.instance.getWdManholeByLocation( markerPosition );
                currentObject = manhole;
                collectionName = WDManhole.COLLECTION_NAME;
                recordId = manhole.id;
                childLayout = manhole.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
            case WDServiceConnection.EXTERNAL_NAME :
                WDServiceConnection serviceConnection = WODataStoreSingleton.instance.getWdServiceConnectionByLocation( markerPosition );
                currentObject = serviceConnection;
                collectionName = WDServiceConnection.COLLECTION_NAME;
                recordId = serviceConnection.id;
                childLayout = serviceConnection.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
            case WSHydrant.EXTERNAL_NAME :
                WSHydrant hydrant = WODataStoreSingleton.instance.getWsHydrantByLocation( markerPosition );
                currentObject = hydrant;
                collectionName = WSHydrant.COLLECTION_NAME;
                recordId = hydrant.id;
                childLayout = hydrant.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
            case WSServicePoint.EXTERNAL_NAME :
                WSServicePoint servicePoint = WODataStoreSingleton.instance.getWsServicePointByLocation( markerPosition );
                currentObject = servicePoint;
                collectionName = WSServicePoint.COLLECTION_NAME;
                recordId = servicePoint.id;
                childLayout = servicePoint.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
            case WSTee.EXTERNAL_NAME :
                WSTee tee = WODataStoreSingleton.instance.getWsTeeByLocation( markerPosition );
                currentObject = tee;
                collectionName = WSTee.COLLECTION_NAME;
                recordId = tee.id;
                childLayout = tee.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
            case WSValve.EXTERNAL_NAME :
                WSValve valve = WODataStoreSingleton.instance.getWsValveByLocation( markerPosition );
                currentObject = valve;
                collectionName = WSValve.COLLECTION_NAME;
                recordId = valve.id;
                childLayout = valve.getLayoutForEditor( this.getActivity( ) );
                view.addView( childLayout, 1 );
                break;
        }

        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }

        view.requestLayout( );

    }


    private void setSelectedPolyline( Polyline polyline ){

        AbstractWaterOfficeObject woRouteObject = WODataStoreSingleton.instance.getWoRouteObjectByPolyline( polyline );
        if( view.getChildAt( 1 ) != null ){
            view.removeView( view.getChildAt( 1 ) );
        }
        LinearLayout childLayout;
        if( woRouteObject instanceof WDConnectionPipe ){
            titleTextView.setText( WDConnectionPipe.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WDConnectionPipe.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }else if( woRouteObject instanceof WDSewerSection ){
            titleTextView.setText( WDSewerSection.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WDSewerSection.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }else if( woRouteObject instanceof WSMainSection ){
            titleTextView.setText( WSMainSection.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WSMainSection.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }
        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }

        view.requestLayout( );
    }

    public void setSelectedPolygon( Polygon polygon ){
        AbstractWaterOfficeObject woRouteObject = WODataStoreSingleton.instance.getWoExtentObjectByPolygon( polygon );
        if( view.getChildAt( 1 ) != null ){
            view.removeView( view.getChildAt( 1 ) );
        }
        LinearLayout childLayout;
        if( woRouteObject instanceof WSStation ){
            titleTextView.setText( WSStation.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WSStation.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }else if( woRouteObject instanceof WSTank ){
            titleTextView.setText( WSTank.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WSTank.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }else if( woRouteObject instanceof WSTreatmentPlant ){
            titleTextView.setText( WSTreatmentPlant.EXTERNAL_NAME );
            currentObject = woRouteObject;
            collectionName = WSTreatmentPlant.COLLECTION_NAME;
            recordId = woRouteObject.id;
            childLayout = woRouteObject.getLayoutForEditor( this.getActivity( ) );
            view.addView( childLayout, 1 );
        }
        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }

        view.requestLayout( );
    }

    public void setInsertModeEditor( ){
       // view.removeAllViews();
        view.removeView( view.getChildAt( 1 ) );
        if( FutureTransactionDelegate.getInstance( ).isPointsMode() ){
            titleTextView.setText( "Location" );
            view.addView( FutureWOObject.getLocationFutureWOObjectLayout( this.getActivity( ) ), 1 );
        }else if( FutureTransactionDelegate.getInstance( ).isRoutesMode() ){
            titleTextView.setText( "Route" );
            view.addView( FutureWOObject.getRouteFutureWOObjectLayout( this.getActivity( ) ), 1 );
        }else if( FutureTransactionDelegate.getInstance( ).isPolygonsMode() ){
            titleTextView.setText( "Polygon" );
            view.addView( FutureWOObject.getExtentFutureWOObjectLayout( this.getActivity( ) ), 1 );
        }
        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }
        view.invalidate();
        view.requestLayout( );
    }


    public void setMode( int mode ) {
        if( mode == 1 ) {
            // insert mode
            this.mode = mode;
            buttonsLayout.removeAllViews();
            buttonsLayout = getButtonsView();
            view.removeView( buttonsLayout );
            ImageButton insertButton = new ImageButton( getContext( ) );
            insertButton.setBackgroundResource( R.drawable.insert );
            insertButton.setScaleType( ImageView.ScaleType.CENTER_CROP );
            insertButton.setOnClickListener( new View.OnClickListener( ){
                @Override
                public void onClick( View v ) {
                    doInsert();
                }
            } );
            buttonsLayout.addView( insertButton );
        } else if( mode == 2 ) {
            // update mode
            this.mode = mode;
            buttonsLayout.removeAllViews( );
            view.removeView( buttonsLayout );
            buttonsLayout = getButtonsView();
            ImageButton updateButton = new ImageButton( getContext( ) );
            updateButton.setBackgroundResource( R.drawable.update );
            updateButton.setOnClickListener( new View.OnClickListener( ){
                @Override
                public void onClick( View v ) {
                    doUpdate();
                }
            } );
            buttonsLayout.addView( updateButton );
        }
        view.addView( buttonsLayout );
        view.invalidate( );
        view.requestLayout( );

    }

    private LinearLayout getButtonsView( ){
        LinearLayout buttonLayout = new LinearLayout( getContext() );
        buttonLayout.setOrientation( LinearLayout.HORIZONTAL );
        return buttonLayout;
    }

    private void doUpdate(){
        // compare current values from fields with original values, and get those modified values and do an update
        LinearLayout fieldsAndValuesLayout = null;
        if( mode == 1){
            fieldsAndValuesLayout = ( LinearLayout )( (LinearLayout) view.getChildAt( 1 ) ).getChildAt( 1 );
        }else if( mode == 2 ){
            fieldsAndValuesLayout = ( LinearLayout )( (LinearLayout) view.getChildAt( 1 ) );
        }

        setProposedFieldsAndValues( fieldsAndValuesLayout );
        SmallworldServicesDelegate.instance.callUpdateRecord(
                this.getActivity( ),
                getContext( ),
                currentObject.getDatasetName( ),
                collectionName,
                String.valueOf( recordId ),
                proposedFieldsAndValues );
        proposedFieldsAndValues.clear();
    }

    private void doInsert(  ){
        String value = ( (CustomSpinner) view.getChildAt( 1 ).findViewById( R.id.valueSpinner ) ).getSelectedItem( ).toString();
        LinearLayout fieldsAndValuesLayout = ( LinearLayout )( (LinearLayout) view.getChildAt( 1 ) ).getChildAt( 1 );
        setProposedFieldsAndValues( fieldsAndValuesLayout );
        String[] basicMetadata = FutureTransactionDelegate.getInstance( ).getMetadata( value );
        String collectionName = basicMetadata[ 0 ];
        String datasetName = basicMetadata[ 1 ];

        // add geometry field
        if( FutureTransactionDelegate.getInstance( ).isPointsMode( ) ){
            for( FutureWOObject futureWOObject : FutureTransactionDelegate.getInstance( ).getWoObjectsToInsert() ){
                // in case of manholes, the manhole number must be unique so if the size of the objects that we have to insert, set the manhole_number ++
                proposedFieldsAndValues.put( "geometry", futureWOObject.getGeometryString() );
                SmallworldServicesDelegate.instance.callInsertRecord(
                        this.getActivity( ),
                        getContext( ),
                        datasetName,
                        collectionName,
                        proposedFieldsAndValues );
            }

        }else if( FutureTransactionDelegate.getInstance( ).isRoutesMode() ){
            proposedFieldsAndValues.put( "geometry", FutureTransactionDelegate.getInstance( ).getCurrentFutureObject().getGeometryString() );
            SmallworldServicesDelegate.instance.callInsertRecord(
                    this.getActivity( ),
                    getContext( ),
                    datasetName,
                    collectionName,
                    proposedFieldsAndValues );
        }else if( FutureTransactionDelegate.getInstance( ).isPolygonsMode() ){
            proposedFieldsAndValues.put( "geometry", FutureTransactionDelegate.getInstance( ).getCurrentFutureObject().getGeometryString() );
            SmallworldServicesDelegate.instance.callInsertRecord(
                    this.getActivity( ),
                    getContext( ),
                    datasetName,
                    collectionName,
                    proposedFieldsAndValues );
        }
        MobileGatewayWOMapActivity.masterActivity.handleFinnishInsert( );
        proposedFieldsAndValues.clear( );
    }
    /*
    // INTERFACE: AdapterView.OnItemSelectedListener
    @Override
    public void onItemSelected( AdapterView< ? > parent, View view, int position, long id ){

        String label = ((CustomSpinner )parent).getTextView().getText().toString();
        if( parent.getSelectedItem( ) == null ){
            return;
        }
        String newValue = parent.getSelectedItem( ).toString( );
        String oldValue;
        String smallworldField;
        if( label.equals( "Specification" ) ){
            smallworldField = currentObject.getSpecificationName();
            oldValue = currentObject.specificationName;
        }else{
            smallworldField = SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label );
            oldValue = currentObject.fieldsAndValues( ).get( label );
        }
        if( FutureTransactionDelegate.getInstance().insertMode() ){
            L.m( "new value : " + newValue );
            L.m( "label: " + label );
        }
        if( oldValue != null ){
            if( !oldValue.equals( newValue ) ){
                proposedFieldsAndValues.put( smallworldField, newValue  );
            }
        }
    }

    @Override
    public void onNothingSelected( AdapterView< ? > parent ){

    }
    // END INTERFACE: AdapterView.OnItemSelectedListener

    // INTERFACE: TextView.OnEditorActionListener
    @Override
    public boolean onEditorAction( TextView v, int actionId, KeyEvent event ){
        String label = ((CustomEditText)v).getLabel().getText().toString();
        String oldValue = currentObject.fieldsAndValues( ).get( label );
        String newValue = v.getText( ).toString( );
        String smallworldField = SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label );
        if( !oldValue.equals( newValue  ) ){
            proposedFieldsAndValues.put( smallworldField, newValue  );
        }
        return false;
    }
    // END INTERFACE: TextView.OnEditorActionListener

    // INTERFACE : View.OnFocusChangeListener
    @Override
    public void onFocusChange( View v, boolean hasFocus ){
        String label = ((CustomEditText )v).getLabel().getText().toString();
        String smallworldField = SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label );
        String oldValue = currentObject.fieldsAndValues().get( label );
        String newValue = ((TextView)v).getText().toString();
        if( !hasFocus && !oldValue.equals( newValue ) ){
            proposedFieldsAndValues.put( smallworldField, newValue  );
        }
    }
    // END INTERFACE : View.OnFocusChangeListener
    */

    private void setProposedFieldsAndValues( LinearLayout layout ){
        int childSize = layout.getChildCount();
        for( int i = 0; i < childSize; i++ ){
            LinearLayout childLayout = ( LinearLayout )layout.getChildAt( i );
            View spinner = childLayout.findViewById( R.id.valueSpinner );
            View editText = childLayout.findViewById( R.id.valueTV );
            if( spinner != null ){
                String label = (( CustomSpinner )spinner).getTextView().getText().toString( );
                String value = ( ( CustomSpinner )spinner ).getSelectedItem().toString();
                if( mode == 1){
                    // insert
                    if( label.equals( "Specification" ) ){
                        proposedFieldsAndValues.put( currentObject.getSpecificationName(), value );
                    }else{
                        proposedFieldsAndValues.put( SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label ), value );
                    }
                }else if( mode == 2){
                    // update
                    String oldValue = "";
                    if( label.equals( "Specification" ) ){
                        oldValue = currentObject.specificationName;
                        label = currentObject.getSpecificationName();
                        if( !oldValue.equals( value ) ){
                            proposedFieldsAndValues.put( label, value );
                        }
                    }else{
                        oldValue = currentObject.fieldsAndValues( ).get( label );

                        if( !oldValue.equals( value ) ){
                            proposedFieldsAndValues.put( SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label ), value );
                        }
                    }
                }
            }
            if( editText != null ){
                String label = ( (CustomEditText)editText ).getLabel( ).getText().toString();
                String value = ( (CustomEditText)editText ).getText( ).toString( );
                if( value.isEmpty() ){
                    value = " ";
                }
                if( mode == 1){

                    proposedFieldsAndValues.put( SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label ), value );
                }else if( mode == 2){
                    String oldValue = currentObject.fieldsAndValues().get( label );
                    if( !oldValue.equals( value ) ){
                        proposedFieldsAndValues.put( SmallworldFieldMetadata.instance.FIELD_MAPPING.get( label ), value );
                    }
                }
            }
        }
    }


}
