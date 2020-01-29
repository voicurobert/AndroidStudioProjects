package rvo.com.book.main_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import rvo.com.book.R;
import rvo.com.book.common.Eight;
import rvo.com.book.eight_db.Firm;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,
                                                         GoogleMap.OnMyLocationButtonClickListener,
                                                         GoogleMap.OnMyLocationClickListener,
                                                         GoogleMap.OnInfoWindowClickListener {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int ACCESS_FINE_LOCATION_REQ_CODE = 12346;
    private List<Marker> markers = new ArrayList<>();
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.active_firms_google_map, container, false);
        MapView mapView = view.findViewById(R.id.activeFirmsMapFragment);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        context = getContext();
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());

        this.googleMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setGravity(Gravity.CENTER);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(this);
            googleMap.setOnMyLocationClickListener(this);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            });
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQ_CODE);
        }
        drawActiveFirms();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_FINE_LOCATION_REQ_CODE) {
            if (permissions.length == 1 &&
                permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                    googleMap.setOnMyLocationButtonClickListener(this);
                    googleMap.setOnMyLocationClickListener(this);
                }
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    public void drawActiveFirms() {
        List<Firm> activeFirms = Eight.dataModel.getActiveFirms();
        for (Firm firm : activeFirms) {
            GeoPoint point = firm.getPoint();
            if (point != null) {
                LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(firm.getName());
                if (firm.getSchedule() == null) {
                    Eight.dataModel.initialiseScheduleForFirm(firm, () -> {
                        markerOptions.snippet(firm.getSchedule().toString());
                        Marker marker = googleMap.addMarker(markerOptions);
                        markers.add(marker);
                    });
                } else {
                    markerOptions.snippet(firm.getSchedule().toString());
                    Marker marker = googleMap.addMarker(markerOptions);
                    markers.add(marker);
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        for (Marker m : markers) {
            if (m.getId().equals(marker.getId())) {
                Intent firmIntent = new Intent(getActivity(), FirmDetailsMasterActivity.class);
                firmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Firm firm = Eight.dataModel.getActiveFirmFromName(marker.getTitle());
                Eight.dataModel.initialiseDataStoreForSelectedFirm(firm, () -> context.startActivity(firmIntent));
            }
        }
    }
}
