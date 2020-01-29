package rvo.holidayplanner;


import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Robert on 7/16/2015.
 */
public class SearchActivity extends AppCompatActivity {
    private HashMap<String, Address> searchedAddresses = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        InterfaceManager.instance.sendMessageToLogcat("onCreate - SearchActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        final SearchView searchView = ( SearchView ) findViewById( R.id.searchView );
        final ListView listView = ( ListView ) findViewById( R.id.listView );

        final ArrayList<String> stringArrayList = new ArrayList<>();

        final ArrayAdapter<String> adapter = new CustomListAdapter( getApplicationContext(), R.layout.custom_item_list, stringArrayList, this );
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search for addresses
                stringArrayList.clear();
                adapter.notifyDataSetChanged();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocationName(newText, 100);
                    for (int i = 0; i < addressList.size(); i++) {
                        String locality = addressList.get(i).getLocality();
                        String countryName = addressList.get(i).getCountryName();
                        String featureStreetName = addressList.get(i).getFeatureName();


                        if (featureStreetName != null) {
                            if (locality != null) {
                                if (countryName == null) {
                                    countryName = "";
                                }
                                String textViewString = featureStreetName + " - " + locality + ", " + countryName;
                                searchedAddresses.put(textViewString, addressList.get(i));
                                stringArrayList.add(textViewString);
                                adapter.notifyDataSetChanged();
                            } else {
                                if (countryName == null) {
                                    countryName = "";
                                }
                                String textViewString = featureStreetName + ", " + countryName;
                                searchedAddresses.put(textViewString, addressList.get(i));
                                stringArrayList.add(textViewString);
                                adapter.notifyDataSetChanged();
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

    }

    public HashMap<String, Address > getSearchedAddresses(){
        return searchedAddresses;
    }


}
