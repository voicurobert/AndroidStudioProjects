package rvo.mobilegateway.find_activity;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import rvo.mobilegateway.R;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 10/8/2015.
 */
public class FinderActivity extends AppCompatActivity {

    private FinderListAdapter finderListAdapter;
    private ListView finderListView;
    private SearchView searchView;
    private List< HashMap< String, String > > currentData;
    private FinderActivity activity;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.find_activity );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        final Spinner mainSpinner = ( Spinner ) findViewById( R.id.mainSpinner );
        ArrayAdapter<CharSequence> mainSpinnerAdapter = ArrayAdapter.createFromResource( this, R.array.main_array, android.R.layout.simple_spinner_item );
        mainSpinner.setDrawingCacheBackgroundColor( Color.BLACK );
        mainSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mainSpinner.setAdapter( mainSpinnerAdapter );
        activity = this;
        final Spinner secondSpinner = ( Spinner ) findViewById( R.id.secondSpinner );

        final FinderActivity context = this;
        ArrayAdapter<CharSequence> secondSpinnerAdapter = null;
        if( mainSpinner.getSelectedItem().equals( "Address" ) ){
            secondSpinnerAdapter = ArrayAdapter.createFromResource( context, R.array.address_array, android.R.layout.simple_spinner_item );
        }else{
            secondSpinnerAdapter = ArrayAdapter.createFromResource( context, R.array.second_array, android.R.layout.simple_spinner_item );
        }

        secondSpinner.setAdapter( secondSpinnerAdapter );
        secondSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        searchView = ( SearchView ) findViewById( R.id.finderSearchView );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean onQueryTextSubmit( String query ){
                if( mainSpinner.getSelectedItem( ).equals( "Address" ) ){
                    // search google adresses
                    emptyFinderListView();
                    Geocoder geocoder = new Geocoder( context );
                    try{
                        refreshFinderViewWithAddresses( geocoder.getFromLocationName( query, 5 ) );
                    } catch( IOException e ){
                        e.printStackTrace( );
                    }
                } else{
                    // search PNI/LNI objects
                    emptyFinderListView( );
                    if( query != null && !query.equals( "" )){
                        SmallworldServicesDelegate.instance.callFind(
                                activity,
                                context,
                                ( ( String ) mainSpinner.getSelectedItem( ) ).replaceAll( " ", "%20" ),
                                ( String ) secondSpinner.getSelectedItem( ),
                                query.replaceAll( " ", "%20" ),
                                new SetOnInitialisationCompleteListener( ){
                                    @Override
                                    public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ) {

                                    }

                                    @Override
                                    public void onEquipmentsInitialisationComplete( boolean initialised ){

                                    }

                                    @Override
                                    public void onConnectedCablesInitialisationComplete( boolean initialised ){

                                    }

                                    @Override
                                    public void onLogicalObjectsInitialisationComplete( boolean initialised ){

                                    }

                                    @Override
                                    public void onPortsInitialisationComplete( boolean initialised ){

                                    }
                                }
                        );
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange( String newText ){
                /*
                if( mainSpinner.getSelectedItem( ).equals( "Address" ) ){
                    // search google adresses
                    emptyFinderListView();
                    Geocoder geocoder = new Geocoder( context );
                    try{
                        refreshFinderViewWithAddresses( geocoder.getFromLocationName( newText, 5 ) );
                    } catch( IOException e ){
                        e.printStackTrace( );
                    }
                } else{
                    // search PNI/LNI objects
                    emptyFinderListView( );
                    if( newText != null && !newText.equals( "" )){

                        for( RequestTask rt : ThreadManager.getInstance( ).getRequestTaskWorkQueue( ) ){
                            ThreadManager.removeSearchTask( rt );
                            ThreadManager.removeDownload( rt, rt.getCurrentBounds() );
                        }
                        ThreadManager.getInstance( ).getRequestTaskWorkQueue( ).clear( );
                        ServiceRequest serviceRequest = new ServiceRequest( "find" );
                        serviceRequest.setServerUrl( serviceRequest.getServerPreamble( ) + context.getSharedPreferences( "GSS_SETTINGS", 0 ).getString( "serverUrl", "localhost" ) + ":" +
                                context.getSharedPreferences( "GSS_SETTINGS", 0 ).getString( "serverPort", "localhost" ) );
                        serviceRequest.putParameterValue( "collection_name", (( String ) mainSpinner.getSelectedItem( )).replaceAll( " ", "%20" ) );
                        serviceRequest.putParameterValue( "field_name", (String)secondSpinner.getSelectedItem() );
                        serviceRequest.putParameterValue( "field_value", newText.replaceAll( " ", "%20" ) );
                        L.m( serviceRequest.getServiceRequest() );
                        ThreadManager.startSearch( serviceRequest, new SetOnRequestThreadListener( ) {
                            @Override
                            public void handleRequestData( RequestTask task ){
                                if( task.getServiceRequest().getParameterValue( "field_value" ).equals( searchView.getQuery().toString() ) ){
                                    refreshFinderViewWithSmallWorldObjects( task.getData() );
                                }

                            }
                        } );
                    }

                }
                */
                return false;

            }

        } );

        finderListView = ( ListView ) findViewById( R.id.finderListView );
        finderListView.setOnItemClickListener( new ListView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView< ? > parent, View view, int position, long id ){
                List< Object > adapterListViewData = ((FinderListAdapter)( ( ListView )parent).getAdapter() ).getData();
                Object selection = adapterListViewData.get( position );
                if( selection instanceof Address ){
                    LatLng objectLocation = new LatLng( ( ( Address ) selection ).getLatitude(), ( ( Address ) selection ).getLongitude() );
                    MapEngine.goTo( MobileGatewayMapActivity.masterActivity, MobileGatewayMapActivity.masterActivity.getMap( ), objectLocation, 17 );
                    finish();
                }else{

                    for( HashMap<String, String > mapping : currentData){
                        if( mapping.get( "name" ).equals( selection )){
                            String longitude = mapping.get( "location" ).split( Pattern.quote( "," ) )[0];
                            String latitude = mapping.get( "location" ).split( Pattern.quote( "," ) )[1];
                            LatLng objectLocation = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                            MapEngine.goTo( MobileGatewayMapActivity.masterActivity, MobileGatewayMapActivity.masterActivity.getMap( ), objectLocation, 17 );
                            finish();
                            break;
                        }
                    }
                }

            }
        } );

        mainSpinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener( ) {
            @Override
            public void onItemSelected( AdapterView< ? > parent, View view, int position, long id ){
                emptyFinderListView( );

                ArrayAdapter< CharSequence > currentAdapter = null;
                secondSpinner.setAdapter( null );
                if( ( ( TextView ) view ).getText( ).equals( "Address" ) ){
                    currentAdapter = ArrayAdapter.createFromResource( context, R.array.address_array, android.R.layout.simple_spinner_item );
                } else{
                    currentAdapter = ArrayAdapter.createFromResource( context, R.array.second_array, android.R.layout.simple_spinner_item );
                }
                currentAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                secondSpinner.setAdapter( currentAdapter );
                secondSpinner.invalidate( );
                searchView.setIconified( true );
                searchView.setQuery( null, false );

            }

            @Override
            public void onNothingSelected( AdapterView< ? > parent ){

            }
        } );

    }


    public void refreshFinderViewWithSmallWorldObjects( List< HashMap< String, String > > data ){
        if( data != null ){
            currentData = data;
            List< Object > searchedStrings = new ArrayList<>(  );
            for( HashMap< String, String > dataMapping : data ){
                searchedStrings.add( dataMapping.get( "name" ) );
            }
            if( finderListView.getAdapter() != null ){
                finderListView.setAdapter( null );
            }
            finderListAdapter = new FinderListAdapter( this, R.layout.finder_item_layout, searchedStrings );
            finderListView.setAdapter( finderListAdapter );
            finderListView.invalidateViews();
        }

    }

    private void refreshFinderViewWithAddresses( List< Address > addressList ){
        List< Object > filteredAddresses = new ArrayList<>(  );
        for( int i = 0; i < addressList.size(); i++ ){
            Address address = addressList.get( i );
            String countryName = address.getCountryName();
            if( countryName != null && countryName.contentEquals( "Romania" ) ){
                filteredAddresses.add( address );
            }
        }
        if( finderListView.getAdapter() == null ){
            finderListAdapter = new FinderListAdapter( this, R.layout.finder_item_layout, filteredAddresses );
        }else{
            finderListView.setAdapter( null );
            finderListAdapter = new FinderListAdapter( this, R.layout.finder_item_layout, filteredAddresses );
        }

        finderListView.setAdapter( finderListAdapter );
        finderListView.invalidateViews();
    }

    private void emptyFinderListView(){
        finderListView.setAdapter( null );
        finderListView.invalidateViews();
    }

}
