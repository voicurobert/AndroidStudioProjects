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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.ViewFlipper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.Cable;
import rvo.mobilegateway.physical_datamodel.EquipmentObject;
import rvo.mobilegateway.physical_datamodel.OpticalBundle;
import rvo.mobilegateway.physical_datamodel.Port;

/**
 * Created by Robert on 10/13/2015.
 */
public class StructureFragment extends Fragment{

    private View view;
    private ViewFlipper viewFlipper;
    private TextView structureHierarchyTextView;
    private Cable selectedCable;
    private EquipmentObject selectedEquipment;
    private Context context;
    private GestureDetector gesture;
    private Map< EquipmentObject, ScrollView > equipmentLayoutMap = new HashMap<>( );
    private Map< Cable, ExpandableListView > connectionsViewMap = new HashMap<>( );

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate( R.layout.structure_fragment_layout, container, false );

        TabHost tabHost = ( TabHost ) view.findViewById( R.id.structureTabHost );
        tabHost.setup( );
        tabHost.addTab( tabHost.newTabSpec( "structure_related_items_tab" )
                .setIndicator( "Connected items", null )
                .setContent( R.id.structureConnectedItems ) );

        viewFlipper = ( ViewFlipper ) view.findViewById( R.id.structureViewFlipper );
        structureHierarchyTextView = ( TextView ) view.findViewById( R.id.structureHierarchyTextView );
        structureHierarchyTextView.setTextColor( Color.BLACK );
        structureHierarchyTextView.setTextSize( 16 );
        structureHierarchyTextView.setTypeface( null, Typeface.BOLD );
        structureHierarchyTextView.setText( "" );
        context = view.getContext( );
        viewFlipper.addView( computeRootedObjectView( ) );

        gesture = new GestureDetector( getActivity( ), new GestureDetector.SimpleOnGestureListener( ){
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
                                addMappingToEquipmentMap( );
                                break;
                            case 1:
                                if( selectedCable != null ) {
                                    if( !connectionsViewMap.containsKey( selectedCable ) ) {
                                        connectionsViewMap.put( selectedCable, computeCablesView( selectedCable ) );
                                    }
                                    viewFlipper.addView( connectionsViewMap.get( selectedCable ) );
                                    viewFlipper.showNext( );
                                } else {
                                    // we have selected an equipment
                                    addMappingToEquipmentMap( );
                                }
                                break;
                            case 2 : addMappingToEquipmentMap( );
                                break;
                        }
                    } else if( e2.getX( ) - e1.getX( ) > SWIPE_MIN_DISTANCE
                            && Math.abs( velocityX ) > SWIPE_THRESHOLD_VELOCITY ) {
                        if( viewFlipper.getDisplayedChild( ) > 0 ) {
                            viewFlipper.removeView( viewFlipper.getCurrentView( ) );
                            // deselect cable path
                            if( selectedCable != null ) {
                                MapEngine.highlightRouteForCable( selectedCable, false );
                                MapEngine.setHighlighedCable( null );
                                selectedCable = null;
                            }
                        }
                    }
                } catch( Exception e ) {
                    e.printStackTrace( );
                    L.m( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + e.getMessage( ) );
                }
                return super.onFling( e1, e2, velocityX, velocityY );
            }
        } );
        return view;
    }

    private ScrollView computeRootedObjectView( ) {
        final ScrollView scrollView = new ScrollView( context );
        scrollView.setFillViewport( true );
        final LinearLayout linearLayout = new LinearLayout( context );
        final RadioGroup radioGroup = new RadioGroup( context );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        List< EquipmentObject > equipments = MapEngine.getSelectedObject().internalEquipments;
        if( equipments != null ){
            Arrays.sort( equipments.toArray() );
            for( EquipmentObject equipment : equipments ) {
                RadioButton rb = equipment.asRadioButton( context );
                radioGroup.addView( rb );
            }
            linearLayout.addView( radioGroup );
        }

        List< Cable > passingThroughCables =  MapEngine.getSelectedObject().passingThroughCables;
        if( passingThroughCables != null ){
            ListView listView = new ListView( context );
            final PassingThroughListAdapter listAdapter = new PassingThroughListAdapter( context, passingThroughCables );
            listView.setAdapter( listAdapter );
            listView.setOnItemClickListener( new AdapterView.OnItemClickListener( ){
                @Override
                public void onItemClick( AdapterView< ? > parent, View view, int position, long id ) {
                    if( selectedCable != null ) {
                        MapEngine.highlightRouteForCable( selectedCable, false );
                    }
                    selectedCable = listAdapter.passingThroughCables.get( position );
                    MapEngine.setHighlighedCable( selectedCable );
                    MapEngine.highlightRouteForCable( selectedCable, true );
                }
            } );
            linearLayout.addView( listView );
        }

        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ){
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId ) {
                if( selectedCable != null ){
                    MapEngine.highlightRouteForCable( selectedCable, false );
                    selectedCable = null;
                }
                selectedEquipment = MapEngine.getSelectedObject().getInternalEquipmentById( checkedId, false );
                structureHierarchyTextView.setText( selectedEquipment.longDescription );
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
                if( selectedCable != null ){
                    MapEngine.highlightRouteForCable( selectedCable, false );
                    selectedCable = null;
                }
                selectedEquipment = MapEngine.getSelectedObject().getInternalEquipmentById( radioGroup.getCheckedRadioButtonId( ), false );
                if( selectedEquipment != null ) {
                    structureHierarchyTextView.setText( selectedEquipment.longDescription );
                }

            }
        } );

        scrollView.addView( linearLayout );
        return scrollView;
    }

    private void addMappingToEquipmentMap( ) {
        if( selectedEquipment != null ) {
            if( !equipmentLayoutMap.containsKey( selectedEquipment ) ) {
                /*
                String collName = null;
                switch( selectedEquipment.equipmentType ) {
                    case "splice_closure":
                        collName = EquipmentObject.SPLICE_CLOSURE_COLLECTION_NAME;
                        break;
                    case "optical_splitter":
                        collName = EquipmentObject.OPTICAL_SPLITTER_COLLECTION_NAME;
                        break;
                }
                */
                equipmentLayoutMap.put( selectedEquipment, computeEquipmentView( selectedEquipment ) );
            }
            viewFlipper.addView( equipmentLayoutMap.get( selectedEquipment ) );
            viewFlipper.showNext( );
        }
    }

    private ScrollView computeEquipmentView( final EquipmentObject equipment ) {
        final ScrollView scrollView = new ScrollView( context );
        scrollView.setFillViewport( true );
        final LinearLayout linearLayout = new LinearLayout( context );
        linearLayout.setOrientation( LinearLayout.VERTICAL );
        final RadioGroup radioGroup = new RadioGroup( context );
        List< EquipmentObject > equipments = equipment.internalEquipments;

        if( equipments != null ) {
            Collections.sort( equipments );
            for( EquipmentObject e : equipments ) {
                RadioButton rb = e.asRadioButton( context );
                radioGroup.addView( rb );
            }
        }

        List< Port > ports = equipment.ports;
        if( ports != null ){
            Collections.sort( ports );
            ExpandableListView expandableListView = new ExpandableListView( context );
            PortsExpandableListAdapter portsExpandableListAdapter = new PortsExpandableListAdapter( context, ports, expandableListView );
            expandableListView.setAdapter( portsExpandableListAdapter );
            expandableListView.setOnTouchListener( new View.OnTouchListener( ){
                @Override
                public boolean onTouch( View v, MotionEvent event ) {
                    return gesture.onTouchEvent( event );
                }
            } );

            linearLayout.addView( expandableListView );
        }

        List< Cable > inCables = equipment.inCables;
        List< Cable > outCables = equipment.outCables;
        if( inCables != null ){
            Collections.sort( inCables );
            for( Object o : inCables ) {
                final Cable inCable = ( Cable ) o;
                final RadioButton rb = new RadioButton( context );
                rb.setText( inCable.computeName( ) );
                rb.setId( inCable.id );
                rb.setTextColor( Color.BLACK );
                radioGroup.addView( rb );
            }
        }
        if( outCables != null ){
            Collections.sort( outCables );

            for( Object o : outCables ) {
                final Cable outCable = ( Cable ) o;
                final RadioButton rb = new RadioButton( context );
                rb.setText( outCable.computeName( ) );
                rb.setId( outCable.id );
                rb.setTextColor( Color.BLACK );
                radioGroup.addView( rb );
            }
        }


        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener( ){
            @Override
            public void onCheckedChanged( RadioGroup group, int checkedId ) {

                if( selectedCable != null ) {
                    MapEngine.highlightRouteForCable( selectedCable, false );
                }
                Cable currentCable = MapEngine.getSelectedObject().getCableById( checkedId );
                if( currentCable != null ) {
                    // we selected a cable
                    selectedCable = currentCable;
                    MapEngine.setHighlighedCable( selectedCable );
                    MapEngine.highlightRouteForCable( selectedCable, true );
                } else {
                    selectedEquipment = equipment.getInternalEquipmentById( checkedId, false );
                    if( selectedCable != null ) {
                        selectedCable = null;
                    }
                    if( selectedEquipment != null ) {
                        structureHierarchyTextView.setText( selectedEquipment.longDescription );
                    }
                }
            }
        } );

        linearLayout.addView( radioGroup );
        scrollView.addView( linearLayout );
        scrollView.setOnTouchListener( new View.OnTouchListener( ){
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                return gesture.onTouchEvent( event );
            }
        } );
        scrollView.addOnLayoutChangeListener( new View.OnLayoutChangeListener( ){
            @Override
            public void onLayoutChange( View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom ) {
                selectedEquipment = equipment.getInternalEquipmentById( radioGroup.getCheckedRadioButtonId( ), false );
                if( selectedEquipment != null ) {
                    structureHierarchyTextView.setText( selectedEquipment.longDescription );
                }
                if( selectedCable != null ) {
                    MapEngine.setHighlighedCable( selectedCable );
                    MapEngine.highlightRouteForCable( selectedCable, true );
                }

            }
        } );
        return scrollView;
    }

    private ExpandableListView computeCablesView( final Cable cable ) {
        final ExpandableListView expandableListView = new ExpandableListView( context );
        List< OpticalBundle > bundles = cable.bundles;
        FirstLevelListAdapter firstLevelListAdapter = new FirstLevelListAdapter( context, bundles );
        expandableListView.setAdapter( firstLevelListAdapter );
        expandableListView.setOnTouchListener( new View.OnTouchListener( ){
            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                return gesture.onTouchEvent( event );
            }
        } );
        return expandableListView;
    }

    @Override
    public void onStop( ) {
        super.onStop( );
        L.m( "StructureFragment - onStop" );
        if( selectedCable != null ) {
            MapEngine.highlightRouteForCable( selectedCable, false );
            MapEngine.setHighlighedCable( null );
        }
        //GlobalHandler.selectedObject = null;
    }

    @Override
    public void onDestroy( ) {
        super.onDestroy( );
        L.m( "StructureFragment - onDestroy" );
        if( selectedCable != null ) {
            MapEngine.highlightRouteForCable( selectedCable, false );
            MapEngine.setHighlighedCable( null );
        }
    }
}
