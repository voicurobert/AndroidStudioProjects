package rvo.mobilegateway.connectivity_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import rvo.mobilegateway.R;

/**
 * Created by Robert on 10/14/2015.
 */
public class RmeFragmentContainer extends Fragment {


    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        View view = inflater.inflate( R.layout.rme_fragment_container_layout, container, false );
        final TabHost tabHost = ( TabHost ) view.findViewById( R.id.rmeTabHost );
        tabHost.setup( );
        tabHost.addTab( tabHost.newTabSpec( "rme_equipment_tab" )
                .setIndicator( "Equipments", null )
                .setContent( R.id.rmeEquipmentFragment ) );
        tabHost.addTab( tabHost.newTabSpec( "rme_logical_consumers_tab" )
                .setIndicator( "Logical consumers", null )
                .setContent( R.id.logicalConsumersFragment ) );

        view.setOnTouchListener( new View.OnTouchListener( ) {
            @Override
            public boolean onTouch( View v, MotionEvent event ){
                return RmeFragment.gesture.onTouchEvent( event );
            }
        } );

        return view;
    }

}

