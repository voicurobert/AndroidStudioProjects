package rvo.com.book.android.main_app.firm_login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Locale;

import rvo.com.book.R;
import rvo.com.book.datamodel.entities.DataModel;
import rvo.com.book.datamodel.entities.Firm;
import rvo.com.book.android.main_app.SetScheduleActivity;
import rvo.com.book.datamodel.repositories.FirmRepository;

public class FirmLocationMapActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                         GoogleMap.OnMyLocationButtonClickListener,
                                                                         GoogleMap.OnMyLocationClickListener,
                                                                         GoogleMap.OnCameraIdleListener {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int ACCESS_FINE_LOCATION_REQ_CODE = 12345;
    private TextView selectedAddressTextView;
    private String address;
    private LatLng addressLocation;
    private String context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firm_location_map_activity);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.firmLocationMapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        selectedAddressTextView = findViewById(R.id.selectedAddressTextView);
        Button nextOrUpdateButton = findViewById(R.id.firmLocationNextButtonId);

        context = (String) getIntent().getSerializableExtra("context");
        if (context != null) {
            Firm firm = DataModel.getInstance().getFirm();
            if (context.equals("updateAddress")) {
                nextOrUpdateButton.setText(getString(R.string.update_address));
                nextOrUpdateButton.setOnClickListener(v -> {
                    if (!address.equals("")) {
                        firm.setAddress(address);
                        firm.setPoint(new GeoPoint(addressLocation.latitude, addressLocation.longitude));
                        FirmRepository.getInstance().updateRecord(firm, Firm.POINT, firm.getPoint(), Firm.ADDRESS, firm.getAddress());
                        finish();
                    }
                });
            } else {
                nextOrUpdateButton.setOnClickListener(v -> {
                    if (!address.equals("")) {
                        firm.setAddress(address);
                        GeoPoint point = new GeoPoint(addressLocation.latitude, addressLocation.longitude);
                        firm.setPoint(point);
                        activateScheduleActivity();
                        finish();
                    }
                });
            }
        }
    }

    private void activateScheduleActivity() {
        Intent intent = new Intent(getApplicationContext(), SetScheduleActivity.class);
        intent.putExtra("context", "firm");
        intent.putExtra("mode", "insert");
        startActivity(intent);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(this);
            googleMap.setOnMyLocationClickListener(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (context.equals("updateAddress")) {
                    Firm firm = DataModel.getInstance().getFirm();
                    addressLocation = firm.pointAsLatLng();
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressLocation, 15));
                    address = getAddressFromLocation(addressLocation.latitude, addressLocation.longitude);
                    selectedAddressTextView.setText(address);
                } else {
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        addressLocation = latLng;
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        String address = getAddressFromLocation(location.getLatitude(), location.getLongitude());
                        selectedAddressTextView.setText(address);
                    }
                }

            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQ_CODE);
        }
        googleMap.setOnCameraIdleListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_REQ_CODE) {
            if (permissions.length == 1 &&
                permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    public String getAddressFromLocation(double latitude, double longitude) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    @Override
    public void onCameraIdle() {
        addressLocation = googleMap.getCameraPosition().target;
        address = getAddressFromLocation(addressLocation.latitude, addressLocation.longitude);
        selectedAddressTextView.setText(address);
    }
}
