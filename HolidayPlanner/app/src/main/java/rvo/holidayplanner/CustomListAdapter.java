package rvo.holidayplanner;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Robert on 7/16/2015.
 */
public class CustomListAdapter extends ArrayAdapter {
    private Context context;
    private int id;
    private List<String> list;
    private Address addressSelected = null;
    private SearchActivity parentActivity = null;

    public CustomListAdapter( Context context, int textViewResourceId, List<String> list, SearchActivity parentActivity ){
        super(context, textViewResourceId, list);
        this.context = context;
        this.id = textViewResourceId;
        this.list = list;
        this.parentActivity = parentActivity;
    }

    public Address getAddressSelected(){
        return addressSelected;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View mView = convertView ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        final TextView text = (TextView) mView.findViewById(R.id.textView);

        if( list.get(position) != null ) {
            text.setTextColor(Color.DKGRAY);
            text.setText( list.get( position ) );
        }

        text.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address address = parentActivity.getSearchedAddresses().get(text.getText().toString());
                LocationInfoFragment.locationInfoFragmentInstance.setAddress( address );

                parentActivity.finish();
            }
        });

        return mView;
    }
}
