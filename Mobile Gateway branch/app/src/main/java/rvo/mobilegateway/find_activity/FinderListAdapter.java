package rvo.mobilegateway.find_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import rvo.mobilegateway.R;

/**
 * Created by Robert on 10/9/2015.
 */
public class FinderListAdapter extends ArrayAdapter<Object>{

    private List< Object > data;
    private Context context;
    private int resourceId;

    public FinderListAdapter( Context context, int resource, List< Object > list ){
        super( context, resource, list );
        this.data = list;
        this.context = context;
        this.resourceId = resource;
    }


    public List< Object > getData( ){
        return data;
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
        LayoutInflater layoutInflater = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = layoutInflater.inflate( resourceId, null );
        TextView tv = ( TextView ) convertView.findViewById( R.id.finderTextView );
        Object obj = getItem( position );
        String text = "";
        if( obj instanceof Address ){
            String locality = ( ( Address ) obj ).getLocality();
            if( locality == null ){
                text =  ( ( Address ) obj ).getFeatureName();
            }else{
                text =  ( ( Address ) obj ).getFeatureName() + " - " + locality;
            }
        }else{
            text = ( String ) obj;
        }

        tv.setText( text );
        tv.setTypeface( null, Typeface.NORMAL );
        tv.setTextSize( 16 );
        tv.setTextColor( Color.BLACK );
        return convertView;
    }
}
