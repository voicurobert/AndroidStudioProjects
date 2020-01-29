package rvo.mobilegateway.connectivity_activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import rvo.mobilegateway.R;
import rvo.mobilegateway.physical_datamodel.Cable;

/**
 * Created by Robert on 8/31/2015.
 */
public class PassingThroughListAdapter extends BaseAdapter{

    public List<Cable> passingThroughCables = null;
    private Context context = null;

    public PassingThroughListAdapter( Context context, List< Cable > passingThroughCables){
        this.context = context;
        this.passingThroughCables = passingThroughCables;
    }

    @Override
    public int getCount() {
        return passingThroughCables.size();
    }

    @Override
    public Object getItem(int position) {
        if( passingThroughCables != null ){
            return passingThroughCables.get( position );
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cable cable = (Cable) getItem( position );
        //L.m( "in get view " + cable.toString() );
        if( convertView == null ){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            convertView = layoutInflater.inflate( R.layout.simple_item_layout, null );
            TextView tv = (TextView) convertView.findViewById(R.id.simpleTextView);
            tv.setTypeface( null, Typeface.BOLD );
            tv.setTextSize( 14 );
            tv.setTextColor( Color.BLACK );
            tv.setText(cable.computeName());
        }
        return convertView;
    }
}
