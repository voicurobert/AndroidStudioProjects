package rvo.mobilegateway.connectivity_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.EquipmentObject;
import rvo.mobilegateway.physical_datamodel.Port;

/**
 * Created by Robert on 10/13/2015.
 */
public class RmeFragment extends Fragment{

    public static View view;
    private ViewFlipper viewFlipper;
    private TextView hierarchyTextView;
    private Context context;
    private EquipmentObject selectedEquipment;
    public static GestureDetector gesture;
    private Map< EquipmentObject, ScrollView > equipmentLayoutMap = new HashMap<>( );
    public static RmeFragment instance;


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate( R.layout.rme_fragment_layout, container, true );
        viewFlipper = ( ViewFlipper ) view.findViewById( R.id.rmeViewFlipper );
        hierarchyTextView = ( TextView ) view.findViewById( R.id.rmeHierarchyTextView );
        hierarchyTextView.setTextColor( Color.BLACK );
        hierarchyTextView.setTextSize( 16 );
        hierarchyTextView.setTypeface( null, Typeface.BOLD );
        hierarchyTextView.setText( "" );
        context = view.getContext( );
        equipmentLayoutMap.clear( );

        viewFlipper.addView( computeRootedObjectView( ) );

        gesture = new GestureDetector( view.getContext( ), new GestureDetector.SimpleOnGestureListener( ){
            @Override
            public boolean onDown( MotionEvent e ) {
                return true;
            }

            @Override
            public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY ) {
                final int SWIPE_MIN_DISTANCE = 70;
                final int SWIPE_MAX_OFF_PATH = 250;
                final int SWIPE_THRESHOLD_VELOCITY = 200;
                try {
                    if( Math.abs( e1.getY( ) - e2.getY( ) ) > SWIPE_MAX_OFF_PATH )
                        return false;
                    if( e1.getX( ) - e2.getX( ) > SWIPE_MIN_DISTANCE && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY ) {

                        int childId = viewFlipper.getDisplayedChild( );
                        switch( childId ) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                                addMappingToEquipmentMap( );
                                break;
                            default:break;
                        }
                    } else if( e2.getX( ) - e1.getX( ) > SWIPE_MIN_DISTANCE
                            && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY ) {
                        if( viewFlipper.getDisplayedChild( ) > 0 ) {
                            viewFlipper.removeView( viewFlipper.getCurrentView( ) );
                        }
                    }
                } catch( Exception e ) {
                    e.printStackTrace( );
                    L.m( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.getMessage( ) );
                }
                return super.onFling( e1, e2, velocityX, velocityY );
            }
        } );

        instance = this;
        return view;

    }


    private ScrollView computeRootedObjectView( ) {
        final ScrollView scrollView = new ScrollView( context );
        final LinearLayout linearLayout = new LinearLayout( context );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        final RadioGroup radioGroup = new RadioGroup( context );
        List< EquipmentObject > equipments = MapEngine.getSelectedObject().internalEquipments;
        if( equipments != null ){
            Collections.sort( equipments );
            for( EquipmentObject equipment : equipments ) {
                RadioButton rb = equipment.asRadioButton( context );
                rb.setId( equipment.id );
                radioGroup.addView( rb );
            }
            linearLayout.addView( radioGroup );
        }
        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ){
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId ) {
                selectedEquipment = MapEngine.getSelectedObject().getInternalEquipmentById( checkedId, false );
                if( selectedEquipment != null ){
                    hierarchyTextView.setText( selectedEquipment.longDescription );
                }
            }
        } );
        scrollView.setOnTouchListener( new View.OnTouchListener( ){
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                return gesture.onTouchEvent( event );
            }
        } );
        scrollView.addOnLayoutChangeListener( new View.OnLayoutChangeListener( ){
            @Override
            public void onLayoutChange( View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom ) {
                selectedEquipment = null;
                radioGroup.clearCheck( );
                hierarchyTextView.setText( " " );
            }
        } );
        scrollView.addView( linearLayout );
        return scrollView;
    }

    private void addMappingToEquipmentMap( ) {
        if( selectedEquipment != null ) {
            if( !equipmentLayoutMap.containsKey( selectedEquipment ) ) {
                equipmentLayoutMap.put( selectedEquipment, computeEquipmentView( selectedEquipment ) );
            }
            viewFlipper.addView( equipmentLayoutMap.get( selectedEquipment ) );
            viewFlipper.showNext( );
        }
    }

    private ScrollView computeEquipmentView( final EquipmentObject equipment ) {

        final ScrollView scrollView = new ScrollView( context );
        final LinearLayout linearLayout = new LinearLayout( context );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        final RadioGroup radioGroup = new RadioGroup( context );
        scrollView.setFillViewport( true );
        List< EquipmentObject > equipments = equipment.internalEquipments;

        if( equipments != null ) {
            Collections.sort( equipments );
            for( EquipmentObject e : equipments ) {
                RadioButton rb = e.asRadioButton( context );
                radioGroup.addView( rb );
            }
            linearLayout.addView( radioGroup );
        }

        List< Port > ports = equipment.ports;
        if( ports != null ){
            Collections.sort( ports );
            ExpandableListView expandableListView = new ExpandableListView( context );
            PortsExpandableListAdapter portsExpandableListAdapter = new PortsExpandableListAdapter( context, ports, expandableListView );
            expandableListView.setAdapter( portsExpandableListAdapter );
            expandableListView.setOnTouchListener( new View.OnTouchListener( ){
                @Override
                public boolean onTouch( View v, MotionEvent event ){
                    return gesture.onTouchEvent( event );
                }
            } );

            linearLayout.addView( expandableListView );

        }

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ){
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId ) {
                if( checkedId != -1 ){
                    selectedEquipment = equipment.getInternalEquipmentById( checkedId, false );
                    hierarchyTextView.setText( selectedEquipment.longDescription );
                }
            }
        } );

        scrollView.setOnTouchListener( new View.OnTouchListener( ){
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                return gesture.onTouchEvent( event );
            }
        } );
        scrollView.addOnLayoutChangeListener( new View.OnLayoutChangeListener( ){
            @Override
            public void onLayoutChange( View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom ) {
                radioGroup.clearCheck();
                selectedEquipment = null;

                /*
                if( selectedEquipment != null ){
                    selectedEquipment = selectedEquipment.getInternalEquipmentById( radioGroup.getCheckedRadioButtonId(), false );
                    hierarchyTextView.setText( selectedEquipment.longDescription );
                    //  LogicalConsumersFragment.instance.resetListView( );
                }
                */
            }
        } );
        scrollView.addView( linearLayout );
        return scrollView;
    }


    public EquipmentObject getSelectedEquipment( ) {
        return selectedEquipment;
    }
}
