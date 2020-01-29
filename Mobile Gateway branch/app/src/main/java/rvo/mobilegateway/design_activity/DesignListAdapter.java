package rvo.mobilegateway.design_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import rvo.mobilegateway.R;

/**
 * Created by Robert on 9/19/2015.
 */
public class DesignListAdapter extends ArrayAdapter< String > {

    private List< String > designList;
    private Context context;
    private int resourceId;

    public DesignListAdapter( Context context, int resource, List< String > designList ){
        super( context, resource,designList );
        this.designList = designList;
        this.resourceId = resource;
        this.context = context;
    }


    @Override
    public long getItemId( int position ){
        return position;
    }

    @Override
    public boolean hasStableIds( ){
        return true;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ){
        LayoutInflater vi = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = vi.inflate( resourceId, null );
        final TextView text = (TextView) convertView.findViewById( R.id.designTextView );
        text.setText( getItem( position ) );
        text.setTypeface( null, Typeface.BOLD );
        text.setTextSize( 20 );
        text.setTextColor( Color.BLACK );
        return convertView;
    }
}
