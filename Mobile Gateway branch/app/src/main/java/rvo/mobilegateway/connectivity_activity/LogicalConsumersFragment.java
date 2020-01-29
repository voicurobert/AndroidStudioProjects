package rvo.mobilegateway.connectivity_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.MapEngine;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.EquipmentObject;

/**
 * Created by Robert on 10/14/2015.
 */
public class LogicalConsumersFragment extends Fragment{

    private View view;
    private ExpandableListView listView;
    public static LogicalConsumersFragment instance;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        view = inflater.inflate( R.layout.logical_consumers_fragment_layout, container, false );
        listView = ( ExpandableListView ) view.findViewById( R.id.logicalObjectsExpandableListView );

        // get associated lni objects for routed object, hence hub, building, te
        final AbstractSmallWorldObject routedObject = MapEngine.getSelectedObject();
        LogicalConsumersExpandableListAdapter listAdapter = new LogicalConsumersExpandableListAdapter( view.getContext(), listView, routedObject.logicalObjects );
        listView.setAdapter( listAdapter );
        instance = this;
        return view;
    }

    public void resetListView( ) {
        final LogicalConsumersExpandableListAdapter listAdapter = ( LogicalConsumersExpandableListAdapter ) listView.getExpandableListAdapter( );
        listAdapter.getLniObjects( ).clear( );
        //prepareLogicalObjects( RmeFragment.instance.getSelectedEquipment(), getContext() );
        final EquipmentObject currentEquipment = RmeFragment.instance.getSelectedEquipment( );
    }

}
