package rvo.mobilegateway.bookmarks_activity;

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
 * Created by Robert on 9/17/2015.
 */
public class BookmarkListAdapter extends ArrayAdapter< String > {

    private List< String > bookmarkList;
    private Context context;
    private int resourceId;

    public BookmarkListAdapter( List< String > bookmarkList, Context context, int resourceId ){
        super( context, resourceId, bookmarkList );
        this.bookmarkList = bookmarkList;
        this.context = context;
        this.resourceId = resourceId;
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
        final TextView text = (TextView) convertView.findViewById(R.id.bookmarkTextView);
        text.setText( getItem( position ) );
        text.setTypeface( null, Typeface.BOLD );
        text.setTextSize( 18 );
        text.setTextColor( Color.BLACK );
        return convertView;
    }
}





