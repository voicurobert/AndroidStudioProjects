package rvo.mobilegateway.main_activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.text.DecimalFormat;
import rvo.mobilegateway.Constants;
import rvo.mobilegateway.SystemVerificationManager;
import rvo.mobilegateway.L;
import rvo.mobilegateway.design_activity.DesignBrowserActivity;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.R;
import rvo.mobilegateway.SettingsActivity;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.find_activity.FinderActivity;
import rvo.mobilegateway.insert_activity.InsertActivity;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.Route;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;
import rvo.mobilegateway.tasks_activity.TasksActivity;


/**
 *  MobileGatewayMapActivity class represents the main activity of this application. <br>
 *      <br>
 *  This class is responsible for: <br>
 *  -> viewing smallworld data/objects, the objects are drawn through MapEngine delegate <br>
 *  -> handling marker, hence smallworld objects, selection <br>
 *  -> retrieves current location of the user <br>
 *  -> updates location of an object <br>
 */
public class MobileGatewayMapActivity extends AppCompatActivity implements
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        View.OnTouchListener{

    public static MobileGatewayMapActivity masterActivity;
    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager = null;
   // private BookmarkEngine bookmarkEngine;
    private LatLng currentLocation;
    private View drawingView;
    private EditorFragment editorFragment;
    private RelativeLayout mapLayout;
    private LinearLayout editorLayout;
    private ImageButton drawLinesButton;

    /**
     * Sets the view of this activity, initialises a Google Map object, sets a custom info window adapter for markers and sets mandatory listeners( marker selection, marker drag, etc) for Google Map
     * @param savedInstanceState
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.map_activity );
        mapLayout = (RelativeLayout) getWindow().getDecorView().findViewById( R.id.mapLayout );
        editorLayout = (LinearLayout) getWindow().getDecorView().findViewById( R.id.editorFragmentLayout );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        masterActivity = this;
        SystemVerificationManager.verify( this );
        setUpMapIfNeeded( );

        // set editor fragment
        editorFragment = ( EditorFragment ) getSupportFragmentManager().findFragmentById( R.id.editorFragmentId );

        if( editorFragment.getView() != null ){
            setMapLayoutFullscreen();
        }
        drawingView = findViewById( R.id.drawer_view );
        drawingView.setOnTouchListener( this );
       // bookmarkEngine = BookmarkEngine.instance;
       // bookmarkEngine.setContext( getApplicationContext( ) );
       // bookmarkEngine.resetBookmarkList( );
       // bookmarkEngine.map = map;


        drawLinesButton = ( ImageButton ) findViewById( R.id.drawLinesButtonId );
        drawLinesButton.setBackgroundResource( R.drawable.draw_line );
        drawLinesButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ){
                handleDrawLinesClickListener();
            }
        } );

        /*
        if( map != null ) {
            SharedPreferences sharedPrefs = getSharedPreferences( "CURRENT_BOOKMARK", 0 );
            String bookmarkName = sharedPrefs.getString( "bookmark_name", null );
            if( bookmarkName != null ) {
                Bookmark bookmark = bookmarkEngine.getBookmarkByName( bookmarkName );
                MapEngine.setBookmark( map, bookmark );
            } else {
                MapEngine.setDefaultLocation( map );
            }
        }
        */

        // ImageButton bookmark = ( ImageButton ) findViewById( R.id.save_bookmark );
        /*
        bookmark.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick( View v ){

                AlertDialog.Builder builder = new AlertDialog.Builder( MobileGatewayMapActivity.this );
                builder.setMessage( "Bookmark name" );
                LayoutInflater inflater = MobileGatewayMapActivity.this.getLayoutInflater( );
                View view = inflater.inflate( R.layout.bookmark_dialog, null );
                final EditText bookmarkNameEditText = ( EditText ) view.findViewById( R.id.bookmarkNameEditText );

                builder.setView( view );

                builder.setPositiveButton( "OK", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick( DialogInterface dialog, int which ){
                        CameraPosition cameraPosition = map.getCameraPosition( );
                        float longitude = ( float ) cameraPosition.target.longitude;
                        float latitude = ( float ) cameraPosition.target.latitude;
                        float zoom = cameraPosition.zoom;
                        float bearing = cameraPosition.bearing;
                        String bookmarkName = bookmarkNameEditText.getText( ).toString( );

                        // save current bookmark in BOOKMARKS shared preferences
                        // do a check for bookmark name
                        if( bookmarkEngine.bookmarkExist( bookmarkName ) ){
                            L.t( getApplicationContext( ), "Bookmark already exists, check Bookmarks..." );
                        } else{
                            bookmarkEngine.addBookmarkToSharedPrefs( bookmarkName, latitude, longitude, zoom, bearing );
                            bookmarkEngine.resetBookmarkList( );
                        }
                    }
                } );

                builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick( DialogInterface dialog, int which ){
                        dialog.dismiss( );
                    }
                } );
                AlertDialog alertDialog = builder.create( );
                alertDialog.show( );
            }
        } );
        */


        setUpMap( );
    }


    @Override
    protected void onResume( ) {
        L.m( "onResume" );
        super.onResume( );
      //  SystemVerificationManager.verify( this );
    //    setUpMapIfNeeded( );
  //      MapEngine.refreshMap( map, map.getCameraPosition(), getApplicationContext() );
        if( getApplicationContext() == null ){
            L.m( "main app context null" );
        }else{
            L.m( "main app context not null" );
        }
    }

    @Override
    protected void onRestart( ) {
        L.m( "onRestart" );
        super.onRestart( );
    }

    @Override
    protected void onPause( ) {
        L.m( "onPause" );
        super.onPause( );
    }

    @Override
    protected void onDestroy( ) {
        L.m( "onDestroy" );
        super.onDestroy( );
    }

    @Override
    protected void onStop( ) {
        L.m( "onStop" );
        super.onStop( );
    }

    @Override
    protected void onStart( ) {
        L.m( "onStart" );
        super.onStart( );
    }


    /**
     * Initialises map object
     */
    private void setUpMapIfNeeded( ) {
        // Do a null check to confirm that we have not already instantiated the map.
        if( map == null ) {
            // Try to obtain the map from the SupportMapFragment.
            map = ( ( SupportMapFragment ) getSupportFragmentManager( ).findFragmentById( R.id.map ) ).getMap( );
            // Check if we were successful in obtaining the map.
            if( map != null ) {
                setUpMap( );
            }
        }
    }

    /**
     * Sets listeners and enables some options for the map
     */
    private void setUpMap( ) {
        map.getUiSettings( ).setZoomControlsEnabled( true );
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            //map.setMyLocationEnabled( true );
        }
        map.setMyLocationEnabled( true );
        map.setOnMyLocationButtonClickListener( this );
        map.setOnMarkerClickListener( this );
        map.setOnCameraChangeListener( this );
        map.setOnMarkerDragListener( this );
        map.setOnMapClickListener( this );
        map.setOnMapLongClickListener( this );
        map.setOnPolylineClickListener( this );
        MapEngine.goTo( this, map, new LatLng( Constants.instance.defaultLatitude, Constants.instance.defaultLongitute ), Constants.instance.defaultZoom );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater( ).inflate( R.menu.mobile_gateway_menu, menu );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId( );

        //noinspection SimplifiableIfStatement
        if( id == R.id.action_settings ) {
            Intent settingsIntent = new Intent( getApplicationContext( ), SettingsActivity.class );
            startActivity( settingsIntent );
            return true;
            //  } else if( id == R.id.action_bookmarks ){
            //      Intent bookmarksIntent = new Intent( getApplicationContext( ), BookmarkActivity.class );
            //      startActivity( bookmarksIntent );
            //      return true;
        } else if( id == R.id.action_finder ) {
            Intent finderIntent = new Intent( getApplicationContext( ), FinderActivity.class );
            startActivity( finderIntent );
            return true;
        } else if( id == R.id.action_design_browser ) {
            if( MapEngine.designIsActivated( ) ) {
                L.t( getApplicationContext( ), "Close current design in order to search and activate another design..." );
            } else {
                Intent designBrowserIntent = new Intent( getApplicationContext( ), DesignBrowserActivity.class );
                startActivity( designBrowserIntent );
            }
            return true;
        } else if( id == R.id.tasks_list ) {
            if( MapEngine.designIsActivated( ) ) {
                Intent taskIntent = new Intent( getApplicationContext( ), TasksActivity.class );
                startActivity( taskIntent );
            } else {
                L.t( getApplicationContext( ), "Activate a design..." );
            }
            return true;
        } else if( id == R.id.action_close_design_browser ) {
            MapEngine.setCurrentDesign( null );
            MapEngine.refreshMap( map, map.getCameraPosition( ), getApplicationContext( ) );
            PniDataStoreSingleton.instance.networkConnections.clear( );
            PniDataStoreSingleton.instance.designSet.clear();
            this.setTitle( R.string.title_activity_mobile_gateway_map );
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onCameraChange( CameraPosition cameraPosition ) {
       // SystemVerificationManager.verify( this );
        L.m( "zoom: " + cameraPosition.zoom );
        MapEngine.deselectObject( );
        SmallworldServicesDelegate.instance.callPrepareMapService( MobileGatewayMapActivity.masterActivity, map.getProjection().getVisibleRegion().latLngBounds.getCenter(), cameraPosition.zoom );
        //setMapLayoutFullscreen();
        //MapEngine.refreshMap( map, cameraPosition, getApplicationContext( ) );
        if( MapEngine.routeToInsert != null ){
            MapEngine.routeToInsert.route = map.addPolyline( MapEngine.routeToInsert.polylineOptions.geodesic( true ).width( 2 ).color( Color.BLACK ) );
        }
    }

    @Override
    public boolean onMarkerClick( Marker marker ) {
        MapEngine.deselectObject( );
        if( editorFragment.getView() != null ){
            setEditorLayoutView( );
            editorFragment.setMode( 2 );
            editorFragment.setEditorObject( marker );
            MapEngine.highlightSmallworldObject( editorFragment.getCurrentObject() );
        }
        return true;
    }

    @Override
    public void onPolylineClick( Polyline polyline ){
        MapEngine.deselectObject();
        if( editorFragment.getView() != null ){
            setEditorLayoutView( );
            editorFragment.setMode( 2 );
            editorFragment.setEditorObject( polyline );
            MapEngine.highlightSmallworldObject( editorFragment.getCurrentObject( ) );
        }
    }


    /**
     * If the GPS service is active then move the map camera position to the users location
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick( ){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  false;
        }
        locationManager = ( LocationManager ) getSystemService( Context.LOCATION_SERVICE );
        Location location = locationManager.getLastKnownLocation( LocationManager.PASSIVE_PROVIDER );
        if( location != null ){
            LatLng latLng = new LatLng( location.getLatitude( ), location.getLongitude( ) );
            currentLocation = latLng;
            CameraPosition cp = new CameraPosition.Builder( ).target( latLng ).zoom( 18 ).build( );
            map.animateCamera( CameraUpdateFactory.newCameraPosition( cp ) );
        }


        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 5000, 20, new LocationListener( ){
            @Override
            public void onLocationChanged( Location location ){

                LatLng latLng = new LatLng( location.getLatitude( ), location.getLongitude( ) );
                DecimalFormat format = new DecimalFormat( "##.00" );

                if( currentLocation == null ){
                    currentLocation = latLng;
                }else if( !format.format( currentLocation.latitude ).matches( format.format( location.getLatitude( ) ) ) &&
                        !format.format( currentLocation.longitude ).matches( format.format( location.getLongitude( ) ) ) ){
                    MapEngine.goTo( null, map, latLng, 18 );

                }
            }

            @Override
            public void onStatusChanged( String provider, int status, Bundle extras ){

            }

            @Override
            public void onProviderEnabled( String provider ){

            }

            @Override
            public void onProviderDisabled( String provider ){

            }
        } );

        return true;

    }


    public GoogleMap getMap( ) {
        return map;
    }


    @Override
    public void onMarkerDragStart( Marker marker ) {
    }

    @Override
    public void onMarkerDrag( Marker marker ) {
    }

    /**
     * Smallworld object location update. Gets the updated position of the marker and updates location in smallworld through UpdateSmallworldObjects delegate engine.
     * @param marker
     */
    @Override
    public void onMarkerDragEnd( Marker marker ) {
        // sync new location with smallworld
        String title = marker.getTitle( );
        LatLng newPosition = marker.getPosition();
        switch( title ){
            case UndergroundUtilityBox.EXTERNAL_NAME:
                UndergroundUtilityBox uub = PniDataStoreSingleton.instance.getUubByMarkerId( marker.getId() );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), UndergroundUtilityBox.COLLECTION_NAME, uub.id, newPosition );
                break;
            case Hub.EXTERNAL_NAME:
                Hub hub = PniDataStoreSingleton.instance.getHubByMarkerId( marker.getId( ) );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), Hub.COLLECTION_NAME, hub.id, newPosition );
                break;
            case Pole.EXTERNAL_NAME:
                Pole pole = PniDataStoreSingleton.instance.getPoleByMarkerId( marker.getId( ) );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), Pole.COLLECTION_NAME, pole.id, newPosition );
                break;
            case AccessPoint.EXTERNAL_NAME:
                AccessPoint ap = PniDataStoreSingleton.instance.getAccessPointByMarkerId( marker.getId( ) );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), AccessPoint.COLLECTION_NAME, ap.id, newPosition );
                break;
            case Building.EXTERNAL_NAME:
                Building b = PniDataStoreSingleton.instance.getBuildingByMarkerId( marker.getId( ) );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), Building.COLLECTION_NAME, b.id, newPosition );
                break;
            case TerminalEnclosure.EXTERNAL_NAME:
                TerminalEnclosure te = PniDataStoreSingleton.instance.getTerminalEnclosureByMarkerId( marker.getId( ) );
                SmallworldServicesDelegate.instance.callUpdateLocation( masterActivity, getApplicationContext( ), TerminalEnclosure.COLLECTION_NAME, te.id, newPosition );
                break;
        }
    }

    @Override
    public void onMapLongClick( LatLng latLng ) {
        // insert records...
        // first check that latLng is in our country range, country range calculated from current location
        // TODO: add two methods on MapEngine: getCountry and coordinateIsInCountry()
        Intent insertIntent = new Intent( getApplicationContext(), InsertActivity.class );
        insertIntent.putExtra( "latitude", latLng.latitude );
        insertIntent.putExtra( "longitude", latLng.longitude );
        startActivity( insertIntent );
      //  GlobalHandler.markerToInsert = MobileGatewayMapActivity.masterActivity.getMap().addMarker( new MarkerOptions().position( latLng ) );
    }

    @Override
    public void onMapClick( LatLng latLng ) {
        MapEngine.deselectObject();
        if( editorFragment.getView() != null ){
            setMapLayoutFullscreen();
            if( MapEngine.routeToInsert != null ){
                MapEngine.routeToInsert.route.remove();
            }
            MapEngine.routeToInsert = null;
        }
        /*
        if( GlobalHandler.markerToInsert != null ){
            GlobalHandler.markerToInsert.remove( );
            GlobalHandler.markerToInsert = null;
        }
        */
    }


    public void setMapLayoutFullscreen(){
        /*
        LinearLayout.LayoutParams newMapLayoutParams = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT );
        newMapLayoutParams.weight = 2;
        mapLayout.setLayoutParams( newMapLayoutParams );
        LinearLayout.LayoutParams editorLayoutParams = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT );
        editorLayoutParams.weight = 0;
        editorLayout.setLayoutParams( editorLayoutParams );
        */
        //editorFragment.getView().setVisibility( View.INVISIBLE );
        editorLayout.setVisibility( View.INVISIBLE );
    }

    public void setEditorLayoutView(){
        /*
        LinearLayout.LayoutParams newMapLayoutParams = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT );
        newMapLayoutParams.weight = 1.5f;
        mapLayout.setLayoutParams( newMapLayoutParams );
        LinearLayout.LayoutParams editorLayoutParams = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT );
        editorLayoutParams.weight = 0.5f;
        editorLayout.setLayoutParams( editorLayoutParams );
        */
        editorLayout.setVisibility( View.VISIBLE );
        editorLayout.bringToFront( );
    }

    public EditorFragment getEditorFragment( ){
        return editorFragment;
    }

    @Override
    public boolean onTouch( View v, MotionEvent event ){
        Polyline currentLine = null;
        switch( event.getAction() ){
            case MotionEvent.ACTION_MOVE :
                // save each point
                if( MapEngine.routeToInsert.route != null ){
                    MapEngine.routeToInsert.route.remove();
                }
                int X1 = (int) event.getX();
                int Y1 = (int) event.getY();
                Point point = new Point();
                point.x = X1;
                point.y = Y1;
                LatLng firstGeoPoint = map.getProjection().fromScreenLocation( point );

                // add this point to a pre atribute, so we can save all these points
                MapEngine.routeToInsert.polylineOptions.add( firstGeoPoint );
                MapEngine.routeToInsert.route = map.addPolyline( MapEngine.routeToInsert.polylineOptions.geodesic( true ).width( 2 ).color( Color.BLACK ) );
                break;

            case MotionEvent.ACTION_UP :
                // draw current points to map
                drawLinesButton.setBackgroundResource( R.drawable.draw_line );
                MapEngine.drawLines = false;
                break;
        }

        if ( MapEngine.drawLines ) {
            return true;
        }else{

            return false;
        }
    }

    private void handleDrawLinesClickListener(){
        if( MapEngine.drawLines ){
            drawLinesButton.setBackgroundResource( R.drawable.draw_line );
            MapEngine.drawLines = false;
        }
        setEditorLayoutView( );
        if( MapEngine.routeToInsert == null ){
            MapEngine.routeToInsert = new Route();
        }
        editorFragment.setEditorObject( MapEngine.routeToInsert );
        editorFragment.setMode( 1 );
        drawLinesButton.setBackgroundResource( R.drawable.selected_draw_line );
        MapEngine.drawLines = true;
    }


}
