package rvo.mobilegateway.tasks_activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.engines.PniDataStoreSingleton;
import rvo.mobilegateway.physical_datamodel.NetworkConnection;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 9/24/2015.
 */
public class TasksActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ListView tasksListView;
    private TaskListAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.tasks_activity );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
        tasksListView = ( ListView ) findViewById( R.id.tasksListView );
        List< String > taskStringList = PniDataStoreSingleton.instance.tasksAsStringList();
        adapter = new TaskListAdapter( getApplicationContext(), R.layout.tasks_list_view_layout, taskStringList );
        tasksListView.setAdapter( adapter );

        tasksListView.setOnItemClickListener( this );
    }


    @Override
    public void onItemClick( AdapterView< ? > parent, View view, int position, long id ){
        String ncDescription = adapter.getItem( position );
        NetworkConnection nc = PniDataStoreSingleton.instance.getNetworkConnectionByConnectionDescription( ncDescription );
        MapEngine.setSelectedTask( nc );
        LatLng location = nc.commonStructureLocation;

        MapEngine.goTo( MobileGatewayMapActivity.masterActivity, MobileGatewayMapActivity.masterActivity.getMap( ), location, 18 );
        finish( );
    }
}
