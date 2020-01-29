package rvo.holidayplanner;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rvo.holidayplanner.core.RoutePlanManager;
import rvo.holidayplanner.core.RouteSection;

/**
 * Created by Robert on 7/6/2015.
 */
public class RouteLocationActivity extends Activity implements LocationInfoFragment.OnViewClickListener{
    private String title = "";
    private LatLng currentLocation = null;
    private Address locationAddress = null;
    private String locationType = "";
    private InterfaceManager interfaceManager = InterfaceManager.instance;
    private RoutePlanManager routeManager = RoutePlanManager.instance;
    private CheckBox finalLocationRoute = null;


    public String getActivityTitle(){
        return title;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        Bundle extraInfo = getIntent().getExtras();
        setAttributes(extraInfo);
        if( locationType.equals( "start" ) ){
            setContentView( R.layout.route_starting_location_activity );
        }else{
            setContentView( R.layout.route_intermediate_location_activity );
            finalLocationRoute = ( CheckBox ) findViewById( R.id.finalDestinationCheckBox );

        }


        openLocationInfoFragment();

        overridePendingTransition( R.anim.slide_in_bottom, R.anim.slide_out_bottom );

        setTitle(title);

        getAddressFromCoordinate();

        final ImageButton setStartingLocationButton = (ImageButton) findViewById( R.id.setLocationButton );
        setStartingLocationButton.setBackgroundResource( R.mipmap.set_location_button );
        setStartingLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( finalLocationRoute == null ){
                    // we must set the starting location to route planner
                    routeManager.initializeRoutePlanner();
                    routeManager.setStartingRouteLocation( currentLocation, locationAddress );
                    routeManager.addMarkerToMap("start");
                    finish();

                }else{
                    // check to see if finalLocationRoute is true of false
                    if( finalLocationRoute.isChecked() ){
                        // set final location of route plan
                        routeManager.setEndingRouteLocation(currentLocation, locationAddress);
                        RouteSection section = routeManager.addSectionToRoute(routeManager.getRoutePlanner().getStartRouteLocation(), routeManager.getRoutePlanner().getEndRouteLocation());
                        routeManager.addMarkerToMap("end");
                        // activate DirectionRoutesMapActivity - this class will do all the work
                        LatLng start = routeManager.getRoutePlanner().getStartRouteLocation().getCoordinate();
                        LatLng end = routeManager.getRoutePlanner().getEndRouteLocation().getCoordinate();

                        Intent directionsIntent = new Intent( getApplicationContext(), DirectionRoutesMapActivity.class );
                        directionsIntent.putExtra( "startLocationLatitude", String.valueOf( start.latitude ) );
                        directionsIntent.putExtra( "startLocationLongitude", String.valueOf( start.longitude ) );
                        directionsIntent.putExtra( "endLocationLatitude", String.valueOf( end.latitude ) );
                        directionsIntent.putExtra( "endLocationLongitude", String.valueOf( end.longitude ) );

                        routeManager.getRouteEngine().saveScreenPositionOnMap( routeManager.getGoogleMap(), getApplicationContext() );
                        startActivity( directionsIntent );

                        finish();
                    }else{
                        // add another location to route plan:
                        //
                    }
                }

            }
        });

                /*
        try {
            PlacePicker.IntentBuilder placeIntent = new PlacePicker.IntentBuilder();

            Intent i = placeIntent.build(getApplicationContext());
            int PLACE_PICKER_REQUEST = 1;
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesNotAvailableException e2) {
            e2.printStackTrace();
        } catch (GooglePlayServicesRepairableException e3) {
            e3.printStackTrace();

        }
        */
    }

    public void setLocation( LatLng loc ){
        this.currentLocation = loc;
    }

    public Address getAddress(){
        return this.locationAddress;
    }


    private void getAddressFromCoordinate(){
        Geocoder gc = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = gc.getFromLocation( currentLocation.latitude, currentLocation.longitude, 10 );
            for (int i = 0; i < addressList.size(); i++) {
                Address currentAddress = addressList.get(i);

                String countryName = currentAddress.getCountryName();
                String localityName = currentAddress.getLocality();
                String adminArea = currentAddress.getAdminArea();

                if( localityName == null || adminArea == null ){
                    continue;
                }
                if( !countryName.isEmpty() && !localityName.isEmpty()  && !adminArea.isEmpty() ){
                    interfaceManager.sendMessageToLogcat("setting location address ");
                    this.locationAddress = currentAddress;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setAttributes( Bundle bundle ){
        if( bundle != null ){
            String title = bundle.getString( "title" );
            if( !title.isEmpty() ){
                this.title = title;
            }
            locationType = bundle.getString( "type" );
            double latitude = Double.parseDouble( bundle.getString( "latitude" ) );
            double longitude = Double.parseDouble( bundle.getString( "longitude" ) );
            this.currentLocation = new LatLng( latitude, longitude );
        }
    }

    private void openLocationInfoFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LocationInfoFragment locationInfoFragment = new LocationInfoFragment();
        fragmentTransaction.add( R.id.locationInfoFragmentLayout, locationInfoFragment, "locationFragment" );
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        interfaceManager.sendMessageToLogcat("RouteLocationActivity - onResume()" );
        Address newAddress = LocationInfoFragment.locationInfoFragmentInstance.getNewAddress();
        if( newAddress != null ){
            this.currentLocation = new LatLng( newAddress.getLatitude(), newAddress.getLongitude() );
            this.locationAddress = newAddress;
        }

    }
}
