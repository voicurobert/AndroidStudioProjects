package rvo.mobilegateway.design_activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.ListView;
import java.util.HashMap;
import java.util.List;
import rvo.mobilegateway.R;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.physical_datamodel.Design;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 9/18/2015.
 */
public class DesignBrowserActivity extends AppCompatActivity{

    private SearchView searchView;
    private ListView designsListView;
    private DesignListAdapter designListAdapter;
    private AppCompatActivity designActivity;

    @Override
    public void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.design_browser_activity_layout );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        searchView = ( SearchView ) findViewById( R.id.designSearchView );
        designsListView = ( ListView ) findViewById( R.id.designsListView );
        final Context context = getApplicationContext();
        designActivity = this;
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener( ) {
            @Override
            public boolean onQueryTextSubmit( String query ){
                return false;
            }

            @Override
            public boolean onQueryTextChange( String newText ){
                SmallworldServicesDelegate.instance.callGetDesigns(
                        context,
                        newText,
                        new SetOnInitialisationCompleteListener( ){
                            @Override
                            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ) {
                                refreshListView( );
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
                        } );

                return true;
            }

        } );

        designsListView.setOnItemClickListener( new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick( AdapterView< ? > parent, View view, int position, long id ){
                final String designName = designListAdapter.getItem( position );
                final Design design = PniDataStoreSingleton.instance.getDesignByDesignName( designName );
                // generate network connections for selected design
                SmallworldServicesDelegate.instance.callGetNetworkConnections(
                        designActivity,
                        getApplicationContext( ),
                        String.valueOf( design.id ),
                        new SetOnInitialisationCompleteListener( ){
                            @Override
                            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result  ) {
                                String currentTitle = String.valueOf( MobileGatewayMapActivity.masterActivity.getTitle( ) );
                                MobileGatewayMapActivity.masterActivity.setTitle( currentTitle + " - " + designName );
                                MapEngine.setCurrentDesign( design );
                                finish( );
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
        } );
    }

    @Override
    protected void onPostCreate( Bundle savedInstanceState ){
        super.onPostCreate( savedInstanceState );

    }

    private void refreshListView(){
        if( designsListView.getAdapter() == null ){
            designListAdapter = new DesignListAdapter( getApplicationContext(), R.layout.design_list_view_layout, PniDataStoreSingleton.instance.designsAsStringList() );
        }else{

            designsListView.setAdapter( null );
            designListAdapter = new DesignListAdapter( getApplicationContext(), R.layout.design_list_view_layout, PniDataStoreSingleton.instance.designsAsStringList() );
        }
        designsListView.setAdapter( designListAdapter );
        designsListView.invalidateViews( );
    }

}
