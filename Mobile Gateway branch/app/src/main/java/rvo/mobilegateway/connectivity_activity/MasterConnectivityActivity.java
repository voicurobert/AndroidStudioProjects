package rvo.mobilegateway.connectivity_activity;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.AsynchronousThreadRunner;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.engines.SetOnTaskCompleteListener;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.IMapListener;


public class MasterConnectivityActivity extends AppCompatActivity implements
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        OnStreetViewPanoramaReadyCallback{

    private GoogleMap googleMap = null;
    private TextView title = null;
    public static MasterConnectivityActivity connectivityActivity;
    private StreetViewPanorama streetViewPanorama;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        Bundle extraInfo = getIntent( ).getExtras( );
        setContentView( R.layout.master_connectivity_activity );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager( ).beginTransaction();
        // Left Layout
        TabHost tabHost = ( TabHost ) findViewById( R.id.tabHost );
        tabHost.setup( );

        tabHost.addTab( tabHost.newTabSpec( "map_tab" ).setIndicator( "Map", null ).setContent( R.id.tab1 ) );
        tabHost.addTab( tabHost.newTabSpec( "street_view_tab" ).setIndicator( "StreetView", null ).setContent( R.id.tab2 ) );

        googleMap = ( ( SupportMapFragment ) getSupportFragmentManager( ).findFragmentById( R.id.mapFragment ) ).getMap( );
        googleMap.setOnCameraChangeListener( this );
        googleMap.setOnInfoWindowClickListener( this );
        googleMap.getUiSettings( ).setZoomControlsEnabled( true );
        googleMap.setOnMarkerClickListener( this );
        googleMap.setInfoWindowAdapter( new CustomInfoWindowAdapter( this ) );
        SupportStreetViewPanoramaFragment streetViewFragment = (SupportStreetViewPanoramaFragment)getSupportFragmentManager().findFragmentById( R.id.streetViewFragment );
        streetViewFragment.getStreetViewPanoramaAsync( this );
        title = ( TextView ) findViewById( R.id.titleTextViewMaster );
        title.setTextColor( Color.BLACK );
        title.setTypeface( null, Typeface.BOLD );
        title.setTextSize( 22 );
        LatLng location = new LatLng( extraInfo.getFloat( "latitude" ), extraInfo.getFloat( "longitude" ) );
        goToPreviousCameraPosition( extraInfo, location );
       // streetViewPanorama = streetViewFragment.getStreetViewPanorama( );
        /*
        streetViewPanorama.setPosition( location, 300 );

        streetViewPanorama.setUserNavigationEnabled( true );
        streetViewPanorama.setStreetNamesEnabled( true );
        streetViewPanorama.setZoomGesturesEnabled( true );
        streetViewPanorama.setOnStreetViewPanoramaClickListener( new StreetViewPanorama.OnStreetViewPanoramaClickListener( ){
            @Override
            public void onStreetViewPanoramaClick( StreetViewPanoramaOrientation streetViewPanoramaOrientation ) {
                L.m( "bla: " + streetViewPanoramaOrientation.toString());
            }
        } );
        */
        // Right Layout
        if( MapEngine.isSimpleStructureSelected() ){
            StructureFragment structureFragment = new StructureFragment();
            fragmentTransaction.add( R.id.fragmentContainer, structureFragment );
        }else{
            RmeFragmentContainer rmeFragmentContainer = new RmeFragmentContainer();
            fragmentTransaction.add( R.id.fragmentContainer, rmeFragmentContainer );
        }
        fragmentTransaction.commit( );
        connectivityActivity = this;
    }

    private void goToPreviousCameraPosition( Bundle bundle, LatLng location ){
        Float zoom = bundle.getFloat( "zoom" );
        //CameraPosition cp = new CameraPosition.Builder( ).target( location ).zoom( zoom ).build( );
        googleMap.animateCamera( CameraUpdateFactory.newCameraPosition( MobileGatewayMapActivity.masterActivity.getMap().getCameraPosition() ) );
    }


    @Override
    public void onCameraChange( CameraPosition cameraPosition ){
        MapEngine.refreshMap( googleMap, cameraPosition, getApplicationContext( ) );
    }

    @Override
    protected void onResume( ){
        connectivityActivity = this;
        super.onResume( );

    }

    @Override
    protected void onPause( ){
        super.onPause( );
        connectivityActivity = null;
    }

    @Override
    public void onInfoWindowClick( Marker marker ){
        if( MapEngine.isObjectSelected() ){
            // the object from global handler is a record from previous map
            AbstractSmallWorldObject previousMapObject = MapEngine.getSelectedObject();
            LatLng previuosLoc = ( (IMapListener)previousMapObject).getLocation();
            MapEngine.setSelectedObject( PniDataStoreSingleton.instance.getObjectFromMarkerPosition( previuosLoc ) );
            MapEngine.deselectObject();
        }
        PniDataStoreSingleton.instance.clearPassingThroughCables( );
        AbstractTelcoObject selection = PniDataStoreSingleton.instance.getObjectFromMarkerPosition( marker.getPosition( ) );
        MapEngine.highlightSmallworldObject( selection );
        marker.hideInfoWindow();
        title.setText( selection.getExternalName() );

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager( ).beginTransaction();
        if( MapEngine.isSimpleStructureSelected() ){
            StructureFragment structureFragment = new StructureFragment( );
            fragmentTransaction.replace( R.id.fragmentContainer, structureFragment );
        }else{
            RmeFragmentContainer rmeFragmentContainer = new RmeFragmentContainer( );
            fragmentTransaction.replace( R.id.fragmentContainer, rmeFragmentContainer );
        }
        selection.prepareTasks( this, MapEngine.getSelectedObject().getCollectionName( ), String.valueOf( MapEngine.getSelectedObject().id ) );
        AsynchronousThreadRunner task = new AsynchronousThreadRunner( this, selection.getaSynchronousThreadExecutor(), new SetOnTaskCompleteListener< Boolean >( ){
            @Override
            public void onTaskComplete( Boolean result ){
                fragmentTransaction.commit( );
            }
        } );
        task.execute( );
    }

    @Override
    public boolean onMarkerClick( Marker marker ){
        marker.showInfoWindow( );
        return true;
    }

    @Override
    public void onBackPressed( ){
        MapEngine.deselectObject();
        MobileGatewayMapActivity.masterActivity.setMapLayoutFullscreen( );
        MapEngine.setSelectedObject( null );
        super.onBackPressed( );
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState ){
        super.onPostCreate( savedInstanceState );
        title.setText( MapEngine.getSelectedObject( ).getExternalName( ) );
    }

    @Override
    public void onStreetViewPanoramaReady( StreetViewPanorama streetViewPanorama ) {
        this.streetViewPanorama = streetViewPanorama;
        LatLng location = new LatLng( getIntent( ).getExtras( ).getFloat( "latitude" ), getIntent( ).getExtras( ).getFloat( "longitude" ) );
        streetViewPanorama.setPosition( location, 300 );
        streetViewPanorama.setUserNavigationEnabled( true );
        streetViewPanorama.setStreetNamesEnabled( true );
        streetViewPanorama.setZoomGesturesEnabled( true );

    }

    @Override
    public void onMapClick( LatLng latLng ){

    }
}
