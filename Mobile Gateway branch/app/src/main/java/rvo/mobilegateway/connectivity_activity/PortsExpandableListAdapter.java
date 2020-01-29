package rvo.mobilegateway.connectivity_activity;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.ConnectivityEngine;
import rvo.mobilegateway.logical_datamodel.Facility;
import rvo.mobilegateway.physical_datamodel.Connection;
import rvo.mobilegateway.physical_datamodel.Fiber;
import rvo.mobilegateway.physical_datamodel.OpticalBundle;
import rvo.mobilegateway.physical_datamodel.Port;

/**
 * Created by Robert on 10/14/2015.
 */
public class PortsExpandableListAdapter extends BaseExpandableListAdapter {

    private List<Port> ports;
    private Context context;
    private ExpandableListView viewOwner;


    public PortsExpandableListAdapter( Context context, List<Port> list, ExpandableListView viewOwner ){
        this.context = context;
        this.viewOwner = viewOwner;
        this.ports = list;
    }

    public List< Port > getPorts( ) {
        return ports;
    }

    @Override
    public int getGroupCount( ){
        return ports.size();
    }

    @Override
    public int getChildrenCount( int groupPosition ){

        Port group = (Port)getGroup( groupPosition );

        return group.portConnectedObjects( ).size();
    }

    @Override
    public Object getGroup( int groupPosition ){

        return ports.get( groupPosition );
    }

    @Override
    public Object getChild( int groupPosition, int childPosition ){

        return ((Port)getGroup( groupPosition )).portConnectedObjects(  ).get( childPosition );
    }

    @Override
    public long getGroupId( int groupPosition ){

        return groupPosition;
    }

    @Override
    public long getChildId( int groupPosition, int childPosition ){

        return childPosition;
    }

    @Override
    public boolean hasStableIds( ){
        return true;
    }

    @Override
    public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ){

        String headerTitle;
        Port groupObject = (Port)getGroup( groupPosition );
        LayoutInflater layoutInflater = ( LayoutInflater ) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( R.layout.parent_list_group, null );

        headerTitle = groupObject.toString( );
        TextView textView = ( TextView ) convertView.findViewById( R.id.parentTextViewId );

        textView.setTypeface( null, Typeface.BOLD );
        textView.setTextSize( 16 );
        textView.setText( headerTitle );
        return convertView;
    }

    @Override
    public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent ){

        Object child = getChild( groupPosition, childPosition );
        LayoutInflater layoutInflater = ( LayoutInflater ) this.context
                .getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( R.layout.child_list_group, null );
        TextView textView = ( TextView ) convertView.findViewById( R.id.childTextViewId );
        textView.setTypeface( null, Typeface.BOLD );
        textView.setTextSize( 14 );
        if( child instanceof Connection ){
            List details = ConnectivityEngine.networkConnectionDetailsForId( ( ( Port ) getGroup( groupPosition ) ).id );

            if( !details.isEmpty() && (boolean)details.get( 0 ) ){
                int c = (int)details.get( 1 );
                textView.setTextColor( c );
            }
            textView.setText( ( ( Connection ) child ).connectionDescription() );
        }else if( child instanceof Facility ){
            textView.setText( ( ( Facility ) child ).connectionDescription() );
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable( int groupPosition, int childPosition ){
        return false;
    }


}
