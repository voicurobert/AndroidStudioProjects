package rvo.holidayplanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import rvo.holidayplanner.core.RoutePlanManager;

/**
 * Created by Robert on 7/3/2015.
 */


/**
 * Starting activity of this project. This activity activates a Google Map and has the following functionalities:
 * -> verifications:
 *      - check for google play service
 *      - check for internet connection
 * -> get route plans, if they exists, from database and draws a route plan in map if it is "active"
 * -> onLongMapCLick() - activate the RouteLocationActivity
 */
public class MapActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private InterfaceManager interfaceManager = InterfaceManager.instance;
    private Context context = null;
    private ShowProgressDialog progressDialog = new ShowProgressDialog();
    private RoutePlanManager routeManager = RoutePlanManager.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.map_activity);
        context = getApplicationContext();
        checkMandatoryFunctyonalities();
        setUpMapIfNeeded();

        routeManager.setGoogleMap(mMap);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if( !routeManager.currentRoutePlannerHasEndingLocation() ){
                    startRouteLocationActivity(latLng);
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_holiday_planner, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }


    private void checkMandatoryFunctyonalities() {

        progressDialog.setMessage("Checking internet connection...");
        progressDialog.show(this.getFragmentManager(), "progressDialog");
        if (interfaceManager.checkForInternetConnection(context)) {
            progressDialog.dismiss();
        } else {
            interfaceManager.showToast(context, "No internet connection...");
            progressDialog.dismiss();
        }

        //progressDialog.setMessage("Test");
        // progressDialog.show( this.getFragmentManager(), "progressDialog" );
    }


    public void startRouteLocationActivity(LatLng coord) {
        // check for proper coord and address
        if (routeManager.getRouteEngine().iscurrentLocationAddressCorrect(context, coord)) {
            Intent routeLocationIntent = new Intent(context, RouteLocationActivity.class);
            String locationType = "";
            String title = "";
            if ( routeManager.currentRoutePlannerHasStartingLocation() ) {
                locationType = "intermediate";
                title = "Set your intermediate holiday location!";
            } else {
                locationType = "start";
                title = "Set your starting holiday location!";
            }
            routeLocationIntent.putExtra("title", title);
            routeLocationIntent.putExtra("type", locationType);
            routeLocationIntent.putExtra("latitude", String.valueOf(coord.latitude));
            routeLocationIntent.putExtra("longitude", String.valueOf(coord.longitude));
            startActivity(routeLocationIntent);
        } else {
            InterfaceManager.instance.showToast(context, "Please set another location!");
        }

    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        interfaceManager.sendMessageToLogcat("b");
    }
}


