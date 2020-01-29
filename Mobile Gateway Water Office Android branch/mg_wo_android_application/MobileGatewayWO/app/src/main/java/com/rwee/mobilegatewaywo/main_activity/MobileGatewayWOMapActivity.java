package com.rwee.mobilegatewaywo.main_activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.rwee.mobilegatewaywo.Constants;
import com.rwee.mobilegatewaywo.GlobalHandler;
import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.R;
import com.rwee.mobilegatewaywo.SettingsActivity;
import com.rwee.mobilegatewaywo.SystemVerificationManager;
import com.rwee.mobilegatewaywo.engines.FutureTransactionDelegate;
import com.rwee.mobilegatewaywo.engines.MapEngine;
import com.rwee.mobilegatewaywo.find_activity.FinderActivity;

import com.rwee.mobilegatewaywo.offline_mode.OfflineModeEngine;

import com.rwee.mobilegatewaywo.import_xml_data.ImportXMLDataActivity;


import static android.widget.Toast.LENGTH_SHORT;

public class MobileGatewayWOMapActivity extends AppCompatActivity implements
        GoogleMap.OnCameraChangeListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        SnapshotReadyCallback,
        View.OnTouchListener{


    private GoogleMap map;
    public static MobileGatewayWOMapActivity masterActivity;
    private View drawingView;
    private EditorFragment editorFragment;
    private RelativeLayout mapLayout;
    private LinearLayout editorLayout;
    private ImageButton drawPointButton;
    private ImageButton drawLineButton;
    private ImageButton drawPolygonButton;
    private LinearLayout contextualLayout;
    private ImageButton cancelButton;
    private ImageButton okButton;
    private ImageButton pictureButton;
    private ImageButton syncButton;
    private Dialog dialog;
    private TextView questionTextView;

    private static final String[] PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS};
    private static final int INITIAL_REQUEST = 1337;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_mobile_gateway_womap );
        mapLayout = ( RelativeLayout ) getWindow( ).getDecorView( ).findViewById( R.id.mapLayout );
        editorLayout = ( LinearLayout ) getWindow( ).getDecorView( ).findViewById( R.id.editorFragmentLayout );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );

        masterActivity = this;
        SystemVerificationManager.verify(this);
        setUpMapIfNeeded();

        // set editor fragment
        editorFragment = (EditorFragment) getSupportFragmentManager().findFragmentById(R.id.editorFragmentId);
        if (editorFragment.getView() != null) {
            setMapLayoutFullscreen();
        }

        contextualLayout = (LinearLayout) findViewById(R.id.contextualLayoutId);
        contextualLayout.setVisibility(View.INVISIBLE);
        drawingView = findViewById(R.id.drawer_view);
        drawingView.setOnTouchListener(this);
        // bookmarkEngine = BookmarkEngine.instance;
        // bookmarkEngine.setContext( getApplicationContext( ) );
        // bookmarkEngine.resetBookmarkList( );
        // bookmarkEngine.map = map;
        dialog = new Dialog( this );
        questionTextView = (TextView) dialog.findViewById(R.id.questionID);
        syncButton = (ImageButton) findViewById(R.id.syncButtonId);
        syncButton.setBackgroundResource(R.drawable.sync);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ask the user if the cameraPosition is ok
                //    AlertDialog.Builder builder = new AlertDialog.Builder( MobileGatewayWOMapActivity.this );
                //     builder.setMessage( "Camera Position" );
                //   LayoutInflater inflater = MobileGatewayWOMapActivity.this.getLayoutInflater( );
                //    View view = inflater.inflate(R.layout.bookmark_dialog, null);
                //  final EditText bookmarkNameEditText = ( EditText ) view.findViewById( R.id.bookmarkNameEditText );

                //  builder.setView( view );

                //   builder.setPositiveButton("OK");


                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Salvare date...");


                Button btnSave = (Button) dialog.findViewById(R.id.save);
                Button btnCancel = (Button) dialog.findViewById(R.id.cancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //     btnSave.setBackgroundResource( R.drawable.save );
                //    btnCancel.setBackgroundResource( R.drawable.cancel );

                dialog.show();


                OfflineModeEngine.instance.setOfflineMode(true);
                CameraPosition offlinePosition = map.getCameraPosition();
            }
        });


        initiateDrawingButtons();
        initiateContextualButtons();
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
        setUpMap();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        map.getUiSettings().setZoomControlsEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(PERMS, INITIAL_REQUEST);
                //map.setMyLocationEnabled(true);
            }
        } else {
            map.setMyLocationEnabled(true);
        }

        map.setOnMyLocationButtonClickListener( this );
        map.setOnMarkerClickListener( this );
        map.setOnCameraChangeListener( this );
        map.setOnMarkerDragListener( this );
        map.setOnMapClickListener( this );
        map.setOnMapLongClickListener( this );
        map.setOnPolygonClickListener( this );
        map.setOnPolylineClickListener( this );
        map.getUiSettings().setTiltGesturesEnabled( false );
       // map.snapshot( this );
        MapEngine.goTo( this, map, new LatLng( Constants.instance.defaultLatitude, Constants.instance.defaultLongitute ), Constants.instance.defaultZoom );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // map.setMyLocationEnabled( true );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mobile_gateway_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (id == R.id.action_finder){
            Intent finderIntent = new Intent( getApplicationContext( ), FinderActivity.class );
            startActivity( finderIntent );
            return true;
        }else if( id == R.id.action_import_xml_data ) {
            Intent importXmlIntent = new Intent( getApplicationContext( ), ImportXMLDataActivity.class );
            startActivity( importXmlIntent );
            return true;
        }
        return super.onOptionsItemSelected( item );

    }



    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //mImageView.setImageBitmap(imageBitmap);
        }
        super.onActivityResult( requestCode, resultCode, data );
    }

    @Override
    public void onCameraChange( CameraPosition cameraPosition ){
        MapEngine.refreshMap( map, cameraPosition, getApplicationContext( ) );
        FutureTransactionDelegate.getInstance( ).redrawMapObjects( );
        if( editorFragment.isVisible() ){
        //    setMapLayoutFullscreen();
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (editorFragment.getView() != null) {
            setMapLayoutFullscreen();
        }
        if (GlobalHandler.isObjectSelected) {
            GlobalHandler.deselectObject();
        }
        if( FutureTransactionDelegate.getInstance( ).insertMode( ) ){
            MapEngine.handleInsertWOFutureObject( latLng );

        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        // Intent insertIntent = new Intent( getApplicationContext(), InsertActivity.class );
        //  insertIntent.putExtra( "latitude", latLng.latitude );
        //  insertIntent.putExtra( "longitude", latLng.longitude );
        //  startActivity( insertIntent );
        //  GlobalHandler.markerToInsert = MobileGatewayMapActivity.masterActivity.getMap().addMarker( new MarkerOptions().position( latLng ) );
    }

    @Override
    public boolean onMarkerClick( Marker marker ){
        if( FutureTransactionDelegate.getInstance( ).insertMode() ){
            return true;

        }
        if (editorFragment.getView() != null) {
            setEditorLayoutView();
            editorFragment.setMode(2);
            editorFragment.setEditorObject(marker);
            GlobalHandler.setSelectedObject(editorFragment.getCurrentObject());
        }
        return true;
    }


    @Override
    public void onPolylineClick(Polyline polyline) {
        if (editorFragment.getView() != null) {
            setEditorLayoutView();
            editorFragment.setMode(2);
            editorFragment.setEditorObject(polyline);
            GlobalHandler.setSelectedObject(editorFragment.getCurrentObject());
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        if (editorFragment.getView() != null) {
            setEditorLayoutView();
            editorFragment.setMode(2);
            editorFragment.setEditorObject(polygon);
            GlobalHandler.setSelectedObject(editorFragment.getCurrentObject());
        }
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag( Marker marker ){

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    public void setMapLayoutFullscreen() {
        LinearLayout.LayoutParams newMapLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        newMapLayoutParams.weight = 2;
        mapLayout.setLayoutParams(newMapLayoutParams);
        LinearLayout.LayoutParams editorLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        editorLayoutParams.weight = 0;
        editorLayout.setLayoutParams( editorLayoutParams );
    }

    public void setEditorLayoutView() {
        LinearLayout.LayoutParams newMapLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        newMapLayoutParams.weight = 1.5f;
        mapLayout.setLayoutParams(newMapLayoutParams);
        LinearLayout.LayoutParams editorLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        editorLayoutParams.weight = 0.5f;
        editorLayout.setLayoutParams(editorLayoutParams);
    }

    public EditorFragment getEditorFragment() {
        return editorFragment;
    }

    public GoogleMap getMap() {
        return map;
    }


    private void initiateDrawingButtons() {
        drawPointButton = ( ImageButton ) findViewById( R.id.drawPointButtonId );
        drawPointButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ){
                if( !FutureTransactionDelegate.getInstance( ).insertMode() ){
                    setDrawingButtonsBackgroundResource( false );
                    handleDrawPointClickListener( );
                }

            }
        } );

        drawLineButton = ( ImageButton ) findViewById( R.id.drawLineButtonId );
        drawLineButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ){
                if( !FutureTransactionDelegate.getInstance( ).insertMode() ){
                    setDrawingButtonsBackgroundResource( false );
                    handleDrawLineClickListener( );
                }
            }
        } );
        drawPolygonButton = (ImageButton) findViewById(R.id.drawPolygonButtonId);
        drawPolygonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                if( !FutureTransactionDelegate.getInstance( ).insertMode( ) ){
                    setDrawingButtonsBackgroundResource( false );
                    handleDrawPolygonClickListener( );

                }
            }
        });

        setDrawingButtonsBackgroundResource(true);
    }

    private void setDrawingButtonsBackgroundResource(boolean enabled) {
        if (enabled) {
            drawPointButton.setBackgroundResource(R.drawable.draw_point);
            drawLineButton.setBackgroundResource(R.drawable.draw_line);
            drawPolygonButton.setBackgroundResource(R.drawable.draw_polygon);
        } else {
            drawPointButton.setBackgroundResource(R.drawable.draw_point_disabled);
            drawLineButton.setBackgroundResource(R.drawable.draw_line_disabled);
            drawPolygonButton.setBackgroundResource(R.drawable.draw_polygon_disabled);
        }

    }

    private void handleDrawPointClickListener() {
        setContextTitle(" - Adaugare locatii...");
        if (GlobalHandler.isObjectSelected) {
            GlobalHandler.deselectObject();
        }
        if (getEditorFragment().getView() != null) {
            setMapLayoutFullscreen();
        }
        contextualLayout.setVisibility( View.VISIBLE );
        FutureTransactionDelegate.getInstance( ).setPointsMode( true );

    }

    private void handleDrawLineClickListener() {
        setContextTitle(" - Adaugare rute...");
        if (GlobalHandler.isObjectSelected) {
            GlobalHandler.deselectObject();
        }
        if (getEditorFragment().getView() != null) {
            setMapLayoutFullscreen();
        }
        /*
        if( MapEngine.drawLines ){
            drawLinesButton.setBackgroundResource( R.mipmap.draw_line );
            MapEngine.drawLines = false;
        }
        setEditorLayoutView( );
        if( MapEngine.routeToInsert == null ){
            MapEngine.routeToInsert = new Route();
        }
        editorFragment.setEditorObject( MapEngine.routeToInsert );
        editorFragment.setMode( 1 );
        drawLinesButton.setBackgroundResource( R.mipmap.selected_draw_line );
        MapEngine.drawLines = true;
        */
        contextualLayout.setVisibility( View.VISIBLE );
        FutureTransactionDelegate.getInstance( ).setRoutesMode( true );

    }

    private void handleDrawPolygonClickListener() {
        setContextTitle(" - Adaugare poligon...");
        if (GlobalHandler.isObjectSelected) {
            GlobalHandler.deselectObject();
        }
        if (getEditorFragment().getView() != null) {
            setMapLayoutFullscreen();
        }
        contextualLayout.setVisibility( View.VISIBLE );
        FutureTransactionDelegate.getInstance( ).setPolygonsMode( true );

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                L.m("Action down");

                break;
            case MotionEvent.ACTION_MOVE:
                L.m("Action move");
                // save each point
                //  if( MapEngine.routeToInsert.route != null ){
                //      MapEngine.routeToInsert.route.remove();
                //  }


                // add this point to a pre atribute, so we can save all these points
                //        MapEngine.routeToInsert.polylineOptions.add( firstGeoPoint );
                //        MapEngine.routeToInsert.route = map.addPolyline( MapEngine.routeToInsert.polylineOptions.geodesic( true ).width( 2 ).color( Color.BLACK ) );
                break;

            case MotionEvent.ACTION_UP:
                L.m("Action up");
                // draw current points to map
                break;
        }


        // if ( InsertWOObjectsDelegate.getInstance().insertMode( ) ){
        //     return true;
        // }else{
        //     return false;
        // }

       // if ( InsertWOObjectsDelegate.getInstance().insertMode( ) ){
       //     return true;
       // }else{
       //     return false;
       // }

       // if ( FutureTransactionDelegate.getInstance().insertMode( ) ){
       //     return true;
       // }else{
       //     return false;
       // }

        return false;
    }

    private void initiateContextualButtons() {
        okButton = (ImageButton) findViewById(R.id.okButtonId);
        okButton.setBackgroundResource(R.drawable.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ){
                if( FutureTransactionDelegate.getInstance( ).getWoObjectsToInsert( ).isEmpty( ) ){
                    L.t( getApplicationContext( ), "Please add some locations..." );
                    return;
                }
                if( editorFragment.getView() != null ){


                    setEditorLayoutView( );
                    getEditorFragment( ).setMode( 1 );
                    getEditorFragment( ).setInsertModeEditor( );
                }
                if( editorFragment.getView( ) != null ){
                    return;
                }
            }
        });

        cancelButton = (ImageButton) findViewById(R.id.cancelButtonId);
        cancelButton.setBackgroundResource(R.drawable.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFinnishInsert( );
            }
        });
        pictureButton = (ImageButton) findViewById(R.id.pictureButtonId);
        pictureButton.setBackgroundResource(R.drawable.picture);
        pictureButton.setOnClickListener( new View.OnClickListener( ){
            @Override
            public void onClick( View v ){
                handleOnPictureButtonClick();
            }
        } );
    }


    public void handleFinnishInsert(){
        setMapLayoutFullscreen( );
        contextualLayout.setVisibility( View.INVISIBLE );
        resetTitle( );
        setDrawingButtonsBackgroundResource( true );
        FutureTransactionDelegate.getInstance( ).removeGoogleMapObjects( );
        FutureTransactionDelegate.getInstance( ).reset( );


    }

    public void handleOnPictureButtonClick(){
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult( takePictureIntent, REQUEST_IMAGE_CAPTURE );
        }
    }

    public void handleFinnishUpdate(){
       setMapLayoutFullscreen();

    }

    private void setContextTitle(String title) {
        setTitle(getString(R.string.title_activity_mobile_gateway_womap) + title);
    }

    private void resetTitle() {
        setTitle(getString(R.string.title_activity_mobile_gateway_womap));
    }


    @Override
    public void onSnapshotReady( Bitmap bitmap ){

    }
}
