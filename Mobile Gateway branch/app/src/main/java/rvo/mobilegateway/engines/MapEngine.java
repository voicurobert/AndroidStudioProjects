package rvo.mobilegateway.engines;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import rvo.mobilegateway.bookmarks_activity.Bookmark;
import rvo.mobilegateway.bookmarks_activity.BookmarkEngine;
import rvo.mobilegateway.Constants;
import rvo.mobilegateway.L;
import rvo.mobilegateway.R;
import rvo.mobilegateway.connectivity_activity.MasterConnectivityActivity;
import rvo.mobilegateway.login_activity.LoginAuthentificationHandlerContext;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.AerialRoute;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Cable;
import rvo.mobilegateway.physical_datamodel.Design;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.IMapListener;
import rvo.mobilegateway.physical_datamodel.NetworkConnection;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.Route;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundRoute;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;

public class MapEngine{

    private static HashMap< RequestTask, LatLngBounds> taskLatLngBoundsHashMap = new HashMap<>(  );
    public static boolean drawLines = false;
    public static Route routeToInsert;
    private static AbstractTelcoObject selectedObject; // this can be any object from the map, structures, rmes or routes
    private static Cable highlighedCable;
    private static Design currentDesign;
    private static NetworkConnection selectedTask;
    private static boolean isObjectSelected;


    private MapEngine( ){

    }

    public static AbstractTelcoObject getSelectedObject( ){
        return selectedObject;
    }

    public static void setSelectedObject( AbstractTelcoObject selectedObject ){
        MapEngine.selectedObject = selectedObject;
    }

    public static Cable getHighlighedCable( ){
        return highlighedCable;
    }

    public static void setHighlighedCable( Cable highlighedCable ){
        MapEngine.highlighedCable = highlighedCable;
    }

    public static Design getCurrentDesign( ){
        return currentDesign;
    }

    public static void setCurrentDesign( Design currentDesign ){
        MapEngine.currentDesign = currentDesign;
    }

    public static NetworkConnection getSelectedTask( ){
        return selectedTask;
    }

    public static void setSelectedTask( NetworkConnection selectedTask ){
        MapEngine.selectedTask = selectedTask;
    }

    public static boolean isObjectSelected( ){
        return isObjectSelected;
    }

    public static void setIsObjectSelected( boolean isObjectSelected ){
        MapEngine.isObjectSelected = isObjectSelected;
    }

    public static void reDrawObject( GoogleMap map ){
        drawUUBs( map );
        drawUndergroundRoutes( map );
        drawAccessPoints( map );
        drawAerialRoutes( map );
        drawBuildings( map );
        drawHubs( map );
        drawTerminalEnclosures( map );
        drawPoles( map );
    }

	public static void drawUUBs( GoogleMap map ){
		for( UndergroundUtilityBox uub : PniDataStoreSingleton.instance.uubs ){
            if( isObjectSelected( uub ) | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( uub.id ) ){
                uub.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_uub );
            }else{
                uub.icon =  BitmapDescriptorFactory.fromResource(R.drawable.uub);
            }
            uub.marker = map.addMarker( uub.markerOptions.anchor( 0.5f, 0.5f ).icon( uub.icon ).draggable( true ) );
		}

	}
	
	public static void drawUndergroundRoutes( GoogleMap map ){
		for( UndergroundRoute undergroundRoute : PniDataStoreSingleton.instance.undergroundRoutes ){
            undergroundRoute.route = map.addPolyline( undergroundRoute.polylineOptions
                    .width( 2 )
                    .color( undergroundRoute.defaultColor )
                    .geodesic( true ) );

		}
	}
	
	public static void drawHubs( GoogleMap map ){
		for( Hub hub : PniDataStoreSingleton.instance.mitHubs ){
            if( isObjectSelected( hub ) | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( hub.id ) ){
                hub.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_hub);
            }else{
                hub.icon = BitmapDescriptorFactory.fromResource(R.drawable.hub);
            }
            hub.marker = map.addMarker( hub.markerOptions.anchor( 0.4f, 0.4f ).icon( hub.icon ).draggable( true ) );

		}
	}

	public static void drawAccessPoints( GoogleMap map ){
		for( AccessPoint ap : PniDataStoreSingleton.instance.accessPoints ){
            if( isObjectSelected( ap )
                    | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( ap.id ) ){
                ap.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_access_point);
            }else{
                ap.icon = BitmapDescriptorFactory.fromResource(R.drawable.access_point);
            }
            ap.marker = map.addMarker( ap.markerOptions.anchor(0.4f, 0.8f).icon( ap.icon ).draggable( true ) );
		}
	}
	
	public static void drawPoles( GoogleMap map ){
		for( Pole pole : PniDataStoreSingleton.instance.poles ){
            if( isObjectSelected( pole )
                    | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( pole.id ) ){
                pole.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_pole );
            }else{
                pole.icon = BitmapDescriptorFactory.fromResource(R.drawable.pole);
            }
            pole.marker = map.addMarker(pole.markerOptions.anchor(0.4f, 0.1f).icon( pole.icon ).draggable( true ) );
		}
	}

    public static void drawBuildings( GoogleMap map ){
        for( Building building : PniDataStoreSingleton.instance.buildings ){
            if( isObjectSelected( building )
                    | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( building.id ) ){
                building.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_building );
            }else{
                building.icon = BitmapDescriptorFactory.fromResource(R.drawable.building);
            }
            building.marker = map.addMarker(building.markerOptions.anchor(0.4f, 0.1f).icon( building.icon ).draggable( true ) );
        }
    }

    public static void drawTerminalEnclosures( GoogleMap map ){
        for( TerminalEnclosure te : PniDataStoreSingleton.instance.terminalEnclosures ){
            if( isObjectSelected( te )
                    | ConnectivityEngine.objectIdIsCommonStructureForNetworkConnection( te.id ) ){
                te.icon = BitmapDescriptorFactory.fromResource(R.drawable.selected_terminal_enclosure );
            }else{
                te.icon = BitmapDescriptorFactory.fromResource(R.drawable.terminal_enclosure);
            }
            te.marker = map.addMarker( te.markerOptions.anchor( 0.4f, 0.1f ).icon( te.icon ).draggable( true ) );
        }
    }
	
	public static void drawAerialRoutes( GoogleMap map ){
		for( AerialRoute aerialRoute : PniDataStoreSingleton.instance.aerialRoutes ){
            aerialRoute.route = map.addPolyline( aerialRoute.polylineOptions
                    .width( 2 )
                    .color( aerialRoute.defaultColor )
                    .geodesic( true ) );

		}
	}

    public static void refreshMap( GoogleMap map, CameraPosition cameraPosition, Context context ){
        // remove all working threads because we fired another refresh
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        for( RequestTask task : taskLatLngBoundsHashMap.keySet() ){
            ThreadManager.removeDownload( task, taskLatLngBoundsHashMap.get( task ) );
        }
        // x is longitude in smallworld
        // y is latitude in smallworld
        if( cameraPosition.zoom >= 15 ) {
            doRealDraw( map, context, bounds );
        }
    }

    private static void doRealDraw( GoogleMap map, Context context, LatLngBounds bounds ){
        map.clear( );
        PniDataStoreSingleton.instance.clearAll( );
        for( String collName : Constants.instance.collectionNames ) {
            ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getRecordsFromBounds", context );
            serviceRequest.putParameterValue( "dataset_name", "gis" );
            serviceRequest.putParameterValue( "collection_name", collName );
            serviceRequest.putParameterValue( "south_west_x", String.valueOf( bounds.southwest.longitude ) );
            serviceRequest.putParameterValue( "south_west_y", String.valueOf( bounds.southwest.latitude ) );
            serviceRequest.putParameterValue( "north_east_x", String.valueOf( bounds.northeast.longitude ) );
            serviceRequest.putParameterValue( "north_east_y", String.valueOf( bounds.northeast.latitude ) );
            serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
            serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance().getPassword() );
            L.m( serviceRequest.getServiceRequest( ) );
            RequestTask currentTask = ThreadManager.startDownload( map, bounds, serviceRequest, collName );
            taskLatLngBoundsHashMap.put( currentTask, bounds );
        }
    }

    public static void setBookmark( GoogleMap map, Bookmark bookmark ){
        LatLng position = new LatLng( bookmark.latitude, bookmark.longitude );
        CameraPosition cp = new CameraPosition.Builder( ).target( position ).zoom( bookmark.zoom ).bearing( bookmark.bearing ).build( );
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cp ) );
        // add this bookmark as current bookmark into shared preferences
        SharedPreferences prefs = BookmarkEngine.instance.getContext().getSharedPreferences( "CURRENT_BOOKMARK", 0 );
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString( "bookmark_name", bookmark.bookmarkName );
        editor.apply( );
    }

    public static void setDefaultLocation( GoogleMap map ){
        LatLng position = new LatLng( 0, 0 );
        CameraPosition cp = new CameraPosition.Builder( ).target( position ).zoom( 0 ).build( );
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cp ) );
    }

    public static void goToConnectivity( GoogleMap map, AbstractTelcoObject object ){
        final Intent floatingIntent = new Intent( MobileGatewayMapActivity.masterActivity, MasterConnectivityActivity.class );
        CameraPosition cameraPosition = map.getCameraPosition( );
        LatLng location = ( ( IMapListener ) object ).getLocation();
        float longitude = ( float ) location.longitude;
        float latitude = ( float ) location.latitude;
        float zoom = cameraPosition.zoom;
        floatingIntent.putExtra( "longitude", longitude );
        floatingIntent.putExtra( "latitude", latitude );
        floatingIntent.putExtra( "zoom", zoom );
       // MobileGatewayMapActivity.masterActivity.startActivity( floatingIntent );

        object.prepareTasks( MobileGatewayMapActivity.masterActivity.getApplicationContext(), selectedObject.getCollectionName(), String.valueOf( selectedObject.id ) );
        AsynchronousThreadRunner task = new AsynchronousThreadRunner( MobileGatewayMapActivity.masterActivity, object.getaSynchronousThreadExecutor( ), new SetOnTaskCompleteListener< Boolean >( ){
            @Override
            public void onTaskComplete( Boolean result ){
                MobileGatewayMapActivity.masterActivity.startActivity( floatingIntent );
            }
        } );
        task.execute( );

    }

    public static void goTo( MobileGatewayMapActivity m, GoogleMap map, LatLng location, float zoom ){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom( location, zoom );
        map.animateCamera( cameraUpdate );
    }

    public static boolean sameCoord( LatLng coord1, LatLng coord2 ){
        NumberFormat format = new DecimalFormat( "#00.0000" );

        Float lat1 = Float.valueOf( format.format( coord1.latitude ) );
        Float lat2 = Float.valueOf( format.format( coord2.latitude ) );
        Float long1 = Float.valueOf( format.format( coord1.longitude ) );
        Float long2 = Float.valueOf( format.format( coord2.longitude ) );
        boolean same = false;
        if( lat1.equals( lat2 ) && long1.equals( long2 ) ){
            same = true;
        }
        return same;
    }

    public static void highlightSmallworldObject( AbstractTelcoObject o ){
        // deselect current object
        AbstractSmallWorldObject oldSelection = selectedObject;
        selectedObject = o;
        setSelection( oldSelection );
        setSelection( o );
        isObjectSelected = true;
    }

    public static boolean isObjectSelected( AbstractSmallWorldObject o ){
        if( selectedObject != null && selectedObject.equals( o ) ){
            return true;
        }else{
            return false;
        }
    }

    public static void setSelection( AbstractSmallWorldObject o ){
        if( o == null ){
            return;
        }
        switch( o.getExternalName( ) ){
            case AccessPoint.EXTERNAL_NAME :
                ( (AccessPoint) o ).marker.setIcon( ( ( AccessPoint ) o ).getIco( ) );
                break;
            case AerialRoute.EXTERNAL_NAME :
                ( (AerialRoute) o ).route.setColor( ( ( AerialRoute ) o ).getColorId( ) );
                break;
            case Building.EXTERNAL_NAME :
                ( (Building) o ).marker.setIcon( ( ( Building ) o ).getIco( ) );
                break;
            case Hub.EXTERNAL_NAME :
                ( (Hub) o ).marker.setIcon( ( ( Hub ) o ).getIco( ) );
                break;
            case Pole.EXTERNAL_NAME :
                ( (Pole) o ).marker.setIcon( ( ( Pole ) o ).getIco( ) );
                break;
            case TerminalEnclosure.EXTERNAL_NAME :
                ( (TerminalEnclosure) o ).marker.setIcon( ( ( TerminalEnclosure ) o ).getIco( ) );
                break;
            case UndergroundRoute.EXTERNAL_NAME :
                ( (UndergroundRoute) o ).route.setColor( ( ( UndergroundRoute ) o ).getColorId( ) );
                break;
            case UndergroundUtilityBox.EXTERNAL_NAME :
                ( (UndergroundUtilityBox) o ).marker.setIcon( ( ( UndergroundUtilityBox ) o ).getIco( ) );
                break;
        }
    }

    public static void deselectObject(  ){
        if( isObjectSelected ){
            AbstractSmallWorldObject object = selectedObject;
            resetSLots();
            isObjectSelected = false;
            setSelection( object );
        }
    }


    public static void resetSLots(){
        selectedObject = null;
        isObjectSelected = false;
        highlighedCable = null;

    }

    public static void highlightRouteForCable( Cable cable, boolean highlightOrUnhighlight ){
        String[] arIds = cable.aerialRouteIds;
        String[] urIds = cable.undergroundRouteIds;
        if( arIds != null ){
            for( String idString : arIds ){
                if( !idString.equals("") ){
                    AerialRoute ar = PniDataStoreSingleton.instance.getAerialRouteById(Integer.valueOf( idString ) );
                    if( ar != null ){
                        if( highlightOrUnhighlight ){
                            ar.route.setWidth( 5 );
                            ar.route.setColor( ar.selectColor );
                        }else{
                            ar.route.setWidth( 2 );
                            ar.route.setColor( ar.defaultColor );
                        }
                    }
                }
            }
        }
        if( urIds != null ){
            for( String urId : urIds ){
                if( !urId.equals("")){
                    UndergroundRoute ur = PniDataStoreSingleton.instance.getUndergroundRouteById( Integer.valueOf( urId ));
                    if( ur != null ){
                        if( highlightOrUnhighlight ){
                            ur.route.setWidth( 5 );
                            ur.route.setColor( Color.RED );
                        }else{
                            ur.route.setWidth( 2 );
                            ur.route.setColor( Color.rgb(40, 100, 40) );
                        }
                    }
                }
            }
        }
    }

    public static boolean isSimpleStructureSelected(){
        if( selectedObject instanceof UndergroundUtilityBox || selectedObject instanceof Pole || selectedObject instanceof AccessPoint ){
            return true;
        }else if( selectedObject instanceof Hub  || selectedObject instanceof Building || selectedObject instanceof TerminalEnclosure ){
            return false;
        }
        return false;
    }


    public static boolean designIsActivated(){
        return currentDesign != null;
    }


    public static int calculateViewScale( float zoom ){
        int viewScale = 10000;

        return viewScale;
    }
}
