package rvo.mobilegateway.connectivity_activity;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import rvo.mobilegateway.R;
import rvo.mobilegateway.engines.ConnectivityEngine;
import rvo.mobilegateway.physical_datamodel.Fiber;
import rvo.mobilegateway.physical_datamodel.OpticalBundle;

/**
 * Created by Robert on 8/13/2015.
 */
public class FirstLevelListAdapter extends BaseExpandableListAdapter {

    private List objects = null;
    private Context context = null;

    public FirstLevelListAdapter( Context context, List objects ){
        this.context = context;
        Collections.sort( objects );
        this.objects = objects;
    }

    @Override
    public int getGroupCount( ){
        return objects.size( );
    }

    @Override
    public int getChildrenCount( int groupPosition ){

        Object groupObject = objects.get( groupPosition );

        int size = 0;
        if( groupObject instanceof OpticalBundle ){
            size = ( ( OpticalBundle ) groupObject ).fibers.size( );
        }
        return size;
    }

    @Override
    public Object getGroup( int groupPosition ){
        return objects.get( groupPosition );
    }

    @Override
    public Object getChild( int groupPosition, int childPosition ){

        Object groupObject = getGroup( groupPosition );
        Object childObject = null;
        if( groupObject instanceof OpticalBundle ){
            List fibers = ( ( OpticalBundle ) groupObject ).fibers;
            Collections.sort( fibers );
            childObject = fibers.get( childPosition );
        }
        return childObject;
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
        Object groupObject = objects.get( groupPosition );

        LayoutInflater layoutInflater = ( LayoutInflater ) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( R.layout.parent_list_group, null );
        if( groupObject instanceof OpticalBundle ){
            headerTitle = groupObject.toString( );
            TextView textView = ( TextView ) convertView.findViewById( R.id.parentTextViewId );

            textView.setTypeface( null, Typeface.BOLD );
            textView.setTextSize( 18 );
            textView.setText( headerTitle );

        }

        return convertView;
    }

    @Override
    public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent ){

        Object childObject = getChild( groupPosition, childPosition );

        if( childObject instanceof Fiber ){

            LayoutInflater infalInflater = ( LayoutInflater ) this.context
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE );

            convertView = infalInflater.inflate( R.layout.child_list_group, null );
            TextView textView = ( TextView ) convertView.findViewById( R.id.childTextViewId );
            List details = ConnectivityEngine.fiberConnectionDetailsForId( String.valueOf( ( ( Fiber ) childObject ).fiberNumber ) );

            if( ! details.isEmpty( ) && ( boolean ) details.get( 0 ) ){
                int c = ( int ) details.get( 1 );
                textView.setTextColor( c );
            }
            textView.setTypeface( null, Typeface.BOLD );
            textView.setTextSize( 16 );
            textView.setText( ( ( Fiber ) childObject ).computeConnectionInfo( ) );

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable( int groupPosition, int childPosition ){
        return false;
    }


}
