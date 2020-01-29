package rvo.holidayplanner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;


/**
 * Created by Robert on 7/15/2015.
 */
public class LocationInfoFragment extends Fragment{
    public static LocationInfoFragment locationInfoFragmentInstance = null;
    private OnViewClickListener parentActivity;
    private Address newAddress = null;
    private TextView localityAndCountryNameTextView = null;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.location_info_activity, null);

        locationInfoFragmentInstance = this;

        localityAndCountryNameTextView = ( TextView ) view.findViewById( R.id.localityAndCountryTextView );
        localityAndCountryNameTextView.setText( parentActivity.getAddress().getLocality() + " - " + parentActivity.getAddress().getCountryName() );

        view.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent( getActivity().getApplicationContext(), SearchActivity.class );
                startActivity(searchIntent);
            }
        });

        return view;


    }


    public interface OnViewClickListener{
        Address getAddress();
    }

    public void setAddress( Address address ){
        this.newAddress = address;
        String locality = address.getLocality();
        if( locality == null ){
            localityAndCountryNameTextView.setText( this.newAddress.getAdminArea() + " - " + this.newAddress.getCountryName() );
        }else{
            localityAndCountryNameTextView.setText( this.newAddress.getLocality() + " - " + this.newAddress.getCountryName() );
        }

    }

    public Address getNewAddress(){
        return newAddress;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            parentActivity = (OnViewClickListener ) activity;
        }catch( ClassCastException e ){
            throw new ClassCastException(activity.toString() + " must implement OnViewClickListener");
        }
    }

}
