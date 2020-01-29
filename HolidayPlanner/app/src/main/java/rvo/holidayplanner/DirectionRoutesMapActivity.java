package rvo.holidayplanner;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import rvo.holidayplanner.core.RoutePlanManager;
import rvo.holidayplanner.core.RouteSection;

/**
 * Created by Robert on 7/20/2015.
 */
public class DirectionRoutesMapActivity extends AppCompatActivity {
    private GoogleMap googleMap = null;
    private RoutePlanManager routeManager = RoutePlanManager.instance;
    private List<List<LatLng>> routes = null;
    private RouteSection section = null; // represents the current section that needs to be drawn

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction_routes_activity);
        Bundle extraInfo = getIntent().getExtras();
        setRouteSection( extraInfo );
        setUpMapIfNeeded();

        drawDirectionRoutes();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if ( googleMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routesMap)).getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }


    private void setRouteSection( Bundle extraInfo ){
        String startLocLatitude = extraInfo.getString( "startLocationLatitude" );
        String startLocLongitude = extraInfo.getString("startLocationLongitude");
        String endLocLatitude = extraInfo.getString( "endLocationLatitude" );
        String endLocLongitude = extraInfo.getString("endLocationLongitude");
        LatLng start = new LatLng( Double.valueOf( startLocLatitude ), Double.valueOf( startLocLatitude ) );
        LatLng end = new LatLng( Double.valueOf( endLocLatitude ), Double.valueOf( endLocLatitude ) );

        section = routeManager.getRouteEngine().getSectionFromLocations( routeManager.getRoutePlanner(), start, end );
    }

    private void computeRoutes(){
        DirectionRoutesAsyncTask task = new DirectionRoutesAsyncTask();
        task.setUrl( routeManager.getRouteEngine().createURLForDirectionAPI(routeManager.getRoutePlanner().getStartRouteLocation().getCoordinate(), routeManager.getRoutePlanner().getEndRouteLocation().getCoordinate() ) );
        task.execute();
        routes = new ArrayList<List<LatLng>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            String jsonString = task.get();
            JSONObject jObject = new JSONObject( jsonString );
            jRoutes = jObject.getJSONArray("routes");
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<LatLng>();


                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = ( String ) ( ( JSONObject ) ( ( JSONObject ) jSteps.get( k ) ).get( "polyline" ) ).get( "points" );
                        List<LatLng> list = PolyUtil.decode( polyline );

                        path.addAll(list);
                    }
                    routes.add(path);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void drawDirectionRoutes(){
        computeRoutes();
        for( int i = 0; i < routes.size(); i++ ){
            List<LatLng> points = routes.get( i );
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(points);
            Polyline polyline = googleMap.addPolyline(polylineOptions);
        }
        routeManager.getRouteEngine().setSavedScreenPositionOnMap( googleMap, getApplicationContext() );
    }
}
