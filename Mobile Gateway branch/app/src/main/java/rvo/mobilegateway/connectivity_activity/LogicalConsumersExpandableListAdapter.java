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

import java.util.List;

import rvo.mobilegateway.R;
import rvo.mobilegateway.logical_datamodel.CircuitLocation;
import rvo.mobilegateway.logical_datamodel.LogicalObject;


/**
 * Created by Robert on 10/21/2015.
 */
public class LogicalConsumersExpandableListAdapter extends BaseExpandableListAdapter{

    private List< LogicalObject > lniObjects;
    private Context context;
    private ExpandableListView viewOwner;

    public LogicalConsumersExpandableListAdapter( Context context, ExpandableListView viewOwner, List< LogicalObject > objects ) {
        this.context = context;
        this.lniObjects = objects;
        this.viewOwner = viewOwner;
    }

    public void setLniObjects( List< LogicalObject > objects ) {
        this.lniObjects.addAll( objects );
    }

    public List< LogicalObject > getLniObjects( ) {
        return lniObjects;
    }

    @Override
    public int getGroupCount( ) {
        return lniObjects.size( );
    }

    @Override
    public int getChildrenCount( int groupPosition ) {
        List< LogicalObject > childLogicalObjects = ( ( LogicalObject ) getGroup( groupPosition ) ).getChildrens( );
        if( childLogicalObjects != null ){
            return childLogicalObjects.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getGroup( int groupPosition ) {
        return lniObjects.get( groupPosition );
    }

    @Override
    public Object getChild( int groupPosition, int childPosition ) {
        List< LogicalObject > childLogicalObjects = ( ( LogicalObject ) getGroup( groupPosition ) ).getChildrens( );
        if( childLogicalObjects != null ){
            return childLogicalObjects.get( childPosition );
        }else{
            return null;
        }
    }

    @Override
    public long getGroupId( int groupPosition ) {
        return groupPosition;
    }

    @Override
    public long getChildId( int groupPosition, int childPosition ) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds( ) {
        return false;
    }

    @Override
    public View getGroupView( int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ) {
        String headerTitle;

        LogicalObject groupObject = ( LogicalObject ) getGroup( groupPosition );

        viewOwner.expandGroup( groupPosition );

        LayoutInflater layoutInflater = ( LayoutInflater ) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( R.layout.parent_list_group, null );
        TextView textView = ( TextView ) convertView.findViewById( R.id.parentTextViewId );
        headerTitle = groupObject.toString( );
        textView.setText( headerTitle );

        textView.setTypeface( null, Typeface.BOLD );
        textView.setTextSize( 16 );
        return convertView;
    }

    @Override
    public View getChildView( int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent ) {
        Object child = getChild( groupPosition, childPosition );
        LayoutInflater layoutInflater = ( LayoutInflater ) this.context
                .getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( R.layout.child_list_group, null );
        TextView textView = ( TextView ) convertView.findViewById( R.id.childTextViewId );
        textView.setTypeface( null, Typeface.BOLD );
        textView.setTextSize( 14 );
        textView.setText( child.toString( ) );
        return convertView;
    }

    @Override
    public boolean isChildSelectable( int groupPosition, int childPosition ) {
        return false;
    }
}
