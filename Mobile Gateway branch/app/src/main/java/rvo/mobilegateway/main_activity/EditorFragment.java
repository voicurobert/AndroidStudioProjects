package rvo.mobilegateway.main_activity;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import java.util.HashMap;
import java.util.Map;
import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.SmallworldFieldMetadata;
import rvo.mobilegateway.custom_view.CustomEditText;
import rvo.mobilegateway.custom_view.CustomSpinner;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.AerialRoute;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.Route;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundRoute;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;

/**
 * Created by Robert on 24.11.2015.
 */
public class EditorFragment extends Fragment{

    private LinearLayout view;
    private int mode = 0;
    private TextView titleTextView = null;
    private LinearLayout buttonsLayout = null;
    private Map< String, String > proposedFieldsAndValues = new HashMap<>( );
    private AbstractTelcoObject currentObject;


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

    public void setEditorObject( Object object ){
        if( object instanceof Marker ){
            setSelectedMarker( ( Marker ) object );
        }else if ( object instanceof Polyline){
            setSelectedPolyline( (Polyline) object );
        }else{
            setStructureToInsert( object );
        }
    }

    public AbstractTelcoObject getCurrentObject( ){
        return currentObject;
    }

    private void setSelectedMarker( Marker selectedMarker ) {
        String title = selectedMarker.getTitle( );
        titleTextView.setText( title );
        if( view.getChildAt( 1 ) != null ){
            view.removeView( view.getChildAt( 1 ) );
        }
        currentObject = PniDataStoreSingleton.instance.getObjectFromMarkerPosition( selectedMarker.getPosition( ) );
        view.addView( currentObject.getLayoutForEditor( this.getActivity( ) ), 1 );
        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }

        view.requestLayout( );
    }

    private void setSelectedPolyline( Polyline line ){
       // this.selectedMarker = selectedMarker;
        currentObject = PniDataStoreSingleton.instance.getRouteObjectByPolyline( line );
        titleTextView.setText( currentObject.getExternalName() );
        if( view.getChildAt( 1 ) != null ){
            view.removeView( view.getChildAt( 1 ) );
        }
        view.addView( currentObject.getLayoutForEditor( this.getActivity() ), 1 );

        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }

        view.requestLayout( );
    }

    private void setStructureToInsert( Object  structureToInsert ){
        if( view.getChildAt( 1 ) != null ){
            view.removeViewAt( 1 );
        }
        if( structureToInsert instanceof AbstractSmallWorldObject ){
            titleTextView.setText( ( ( AbstractSmallWorldObject ) structureToInsert ).getExternalName( ) );
            view.addView( ( ( AbstractSmallWorldObject ) structureToInsert ).getLayoutForEditor( this.getActivity( ) ), 1 );
        }else if( structureToInsert instanceof Route){
            titleTextView.setText( "Route" );
            view.addView( ( ( Route ) structureToInsert ).getLayoutForEditor( this.getActivity( ) ), 1 );
        }
        if( view.getChildAt( 2 ) == null ){
            view.addView( buttonsLayout );
        }
        view.requestLayout( );
    }

    public void setMode( int mode ) {
        this.mode = mode;
        if( mode == 1 ) {
            // insert mode
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
            buttonsLayout.removeAllViews();
            view.removeView( buttonsLayout );
            buttonsLayout = getButtonsView();
            ImageButton connectivityButton = new ImageButton( getContext() );
            connectivityButton.setBackgroundResource( R.drawable.connectivity );
            buttonsLayout.addView( connectivityButton );
            connectivityButton.setOnClickListener( new View.OnClickListener( ){
                @Override
                public void onClick( View v ){
                    if( !currentObject.isRoute( ) ){
                        MapEngine.goToConnectivity( MobileGatewayMapActivity.masterActivity.getMap( ), MapEngine.getSelectedObject() );
                    }

                }
            } );
            ImageButton updateButton = new ImageButton( getContext( ) );
            updateButton.setBackgroundResource( R.drawable.update );
            updateButton.setOnClickListener( new View.OnClickListener( ){
                @Override
                public void onClick( View v ){
                    doUpdate( );
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
        setProposedFieldsAndValues( ( LinearLayout ) view.getChildAt( 1 ) );
        L.m( proposedFieldsAndValues.toString( ) );
        if( !proposedFieldsAndValues.isEmpty() ){
            SmallworldServicesDelegate.instance.callUpdateRecord(
                    this.getActivity( ),
                    getContext( ),
                    currentObject.getCollectionName( ),
                    String.valueOf( currentObject.id ),
                    proposedFieldsAndValues );
        }else{
            L.t( getContext(), "No update required, change some values!" );
        }
    }

    private void doInsert(){
        L.m( proposedFieldsAndValues.toString() );

        SmallworldServicesDelegate.instance.callInsertRecord(
                this.getActivity(),
                getContext(),
                currentObject.getCollectionName(),
                proposedFieldsAndValues );
    }


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
