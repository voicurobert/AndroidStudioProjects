package com.rwee.mobilegatewaywo.engines;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rwee.mobilegatewaywo.Constants;
import com.rwee.mobilegatewaywo.GlobalHandler;
import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.login_activity.LoginAuthentificationHandlerContext;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;
import com.rwee.mobilegatewaywo.offline_mode.OfflineModeEngine;
import com.rwee.mobilegatewaywo.wo_datamodel.FutureWOObject;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDConnectionPipe;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDManhole;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDServiceConnection;
import com.rwee.mobilegatewaywo.wo_datamodel.drainage.WDSewerSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSHydrant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSMainSection;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSServicePoint;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSStation;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTank;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTee;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSTreatmentPlant;
import com.rwee.mobilegatewaywo.wo_datamodel.water_supply.WSValve;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Set;


public class MapEngine{

    private static HashMap< RequestTask, LatLngBounds> taskLatLngBoundsHashMap = new HashMap<>(  );
    private static LatLng centre;

    private MapEngine( ){

    }



	public static void drawWdConnectionPipe( GoogleMap map ){
		for( WDConnectionPipe connectionPipe : WODataStoreSingleton.instance.wdConnectionPipeSet ){
            connectionPipe.woRoute.line = map.addPolyline(connectionPipe.woRoute.polylineOptions
                    .width(connectionPipe.getRouteWidth())
                    .color(connectionPipe.getColorId())
                    .geodesic(true));
		}
	}

    public static void drawWdManhole( GoogleMap map ){
        for( WDManhole manhole : WODataStoreSingleton.instance.wdManholeSet ){
            manhole.woLocation.marker = map.addMarker(manhole.woLocation.markerOptions);
        }
    }

    public static void drawWdSewerSection( GoogleMap map  ){
        for( WDSewerSection sewerSection : WODataStoreSingleton.instance.wdSewerSectionSet ){

            sewerSection.woRoute.line = map.addPolyline(sewerSection.woRoute.polylineOptions
                    .width(sewerSection.getRouteWidth())
                    .color(sewerSection.getColorId())
                    .geodesic(true));
        }
    }

    public static void drawWdServiceConnection( GoogleMap map ){
        for( WDServiceConnection serviceConnection : WODataStoreSingleton.instance.wdServiceConnectionSet ){
            serviceConnection.woLocation.marker = map.addMarker( serviceConnection.woLocation.markerOptions );
        }
    }

    public static void drawWsHydrant( GoogleMap map ){
        for( WSHydrant hydrant : WODataStoreSingleton.instance.wsHydrantSet ){
            hydrant.woLocation.marker = map.addMarker(hydrant.woLocation.markerOptions);
        }
    }

    public static void drawWsMainSection( GoogleMap map ){
        for( WSMainSection mainSection : WODataStoreSingleton.instance.wsMainSectionSet ){
            mainSection.woRoute.line = map.addPolyline(mainSection.woRoute.polylineOptions
                    .width(mainSection.getRouteWidth())
                    .color(mainSection.getColorId())
                    .geodesic(true));
        }
    }

    public static void drawWsServicePoint( GoogleMap map ){
        for( WSServicePoint servicePoint : WODataStoreSingleton.instance.wsServicePointSet ){
            servicePoint.woLocation.marker = map.addMarker(servicePoint.woLocation.markerOptions);
        }
    }

    public static void drawWsStation( GoogleMap map ){
        for( WSStation station : WODataStoreSingleton.instance.wsStationSet ){
            station.woExtent.polygon = map.addPolygon( station.woExtent.polygonOptions );
        }
    }

    public static void drawWsTank( GoogleMap map ){
        for( WSTank tank : WODataStoreSingleton.instance.wsTankSet ){

            tank.woExtent.polygon = map.addPolygon(tank.woExtent.polygonOptions.geodesic(true).strokeColor(Color.BLUE));
        }
    }

    public static void drawWsTee( GoogleMap map ){
        for( WSTee tee : WODataStoreSingleton.instance.wsTeeSet ){
            tee.woLocation.marker = map.addMarker(tee.woLocation.markerOptions);
        }
    }

    public static void drawWsTreatmentPlant( GoogleMap map ){
        for( WSTreatmentPlant treatmentPlant : WODataStoreSingleton.instance.wsTreatmentPlantSet ){
            treatmentPlant.woExtent.polygon = map.addPolygon(treatmentPlant.woExtent.polygonOptions.geodesic(true).strokeColor(Color.BLACK));
        }
    }

    public static void drawWsValve( GoogleMap map ){
        for( WSValve valve : WODataStoreSingleton.instance.wsValveSet ){
            valve.woLocation.marker = map.addMarker( valve.woLocation.markerOptions );
        }
    }

    public static void refreshMap( GoogleMap map, CameraPosition cameraPosition, Context context ){
        // remove all working threads because we fired another refresh
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        LatLng centreCoord = bounds.getCenter();
        if( centreCoord.latitude != 0.0 && centreCoord.latitude != 1.3296793380135341E-5 ){
            if( centre == null  ){
                centre = centreCoord;
            }
        }
        for( RequestTask task : taskLatLngBoundsHashMap.keySet() ){
            ThreadManager.removeDownload( task, taskLatLngBoundsHashMap.get( task ) );
        }
        // x is longitude in smallworld
        // y is latitude in smallworld
        if( cameraPosition != null ){
            float currentZoom = cameraPosition.zoom;
            if( currentZoom >= 17 ) {
                if(OfflineModeEngine.instance.isOfflineMode() ){
                    // do save

                    // set offline mode to false
                }
                doRealDraw( map, context, bounds );
            }
        }
    }

    private static void doRealDraw( GoogleMap map, Context context, LatLngBounds bounds){
        map.clear( );
        GlobalHandler.resetSLots();
        WODataStoreSingleton.instance.clearAll( );
        Set< String > collectionNames = Constants.instance.collectionMapping.keySet();
        for( String collName : collectionNames ) {
            executeTask( map,context,bounds,collName );
        }
    }

    private static void executeTask( GoogleMap map, Context context, LatLngBounds bounds, String collName){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getRecordsFromBounds", context );
        serviceRequest.putParameterValue( "collection_name", collName );
        serviceRequest.putParameterValue( "dataset_name", Constants.instance.collectionMapping.get( collName ) );
        serviceRequest.putParameterValue( "south_west_x", String.valueOf( bounds.southwest.longitude ) );
        serviceRequest.putParameterValue( "south_west_y", String.valueOf( bounds.southwest.latitude) );
        serviceRequest.putParameterValue( "north_east_x", String.valueOf( bounds.northeast.longitude ) );
        serviceRequest.putParameterValue( "north_east_y", String.valueOf( bounds.northeast.latitude ) );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        RequestTask currentTask = ThreadManager.startDownload( map, bounds, serviceRequest, collName );
        taskLatLngBoundsHashMap.put( currentTask, bounds );
    }


    /*
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
    */
    public static void setDefaultLocation( GoogleMap map ){
        LatLng position = new LatLng( 0, 0 );
        CameraPosition cp = new CameraPosition.Builder( ).target( position ).zoom( 0 ).build( );
        map.animateCamera( CameraUpdateFactory.newCameraPosition( cp ) );
    }



    public static void goTo( MobileGatewayWOMapActivity m, GoogleMap map, LatLng location, float zoom ){
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


    private static LatLng getCoordinateFromClickEvent( GoogleMap map, MotionEvent event ){
        int X1 = (int) event.getX();
        int Y1 = (int) event.getY();
        Point point = new Point();
        point.x = X1;
        point.y = Y1;
        return map.getProjection().fromScreenLocation( point );
    }

    public static void handleInsertWOFutureObject( LatLng coordinate ){
        if( FutureTransactionDelegate.getInstance( ).isPointsMode() ){
            FutureWOObject locationFutureWOObject = FutureTransactionDelegate.getInstance( ).createLocationWaterOfficeObject();
            FutureTransactionDelegate.getInstance( ).setLocationOptionsForFutureLocationWOObject( locationFutureWOObject, coordinate );
            FutureTransactionDelegate.getInstance( ).addWOObject( locationFutureWOObject );

        }else if( FutureTransactionDelegate.getInstance( ).isRoutesMode() ){

            if( FutureTransactionDelegate.getInstance( ).getCurrentFutureObject() == null ){
                FutureWOObject routeFutureWoObject = FutureTransactionDelegate.getInstance( ).createRouteWaterOfficeObject();
                FutureTransactionDelegate.getInstance( ).setCurrentFutureObject( routeFutureWoObject );
                FutureTransactionDelegate.getInstance( ).addWOObject( routeFutureWoObject );
            }

            // add coordinate to route
            FutureTransactionDelegate.getInstance( ).addLocationToWoRouteObject( coordinate );

        }else if( FutureTransactionDelegate.getInstance( ).isPolygonsMode() ){
            if( FutureTransactionDelegate.getInstance( ).getCurrentFutureObject() == null ){
                FutureWOObject extentFutureWoObject = FutureTransactionDelegate.getInstance( ).createExtentWaterOfficeObject( );
                FutureTransactionDelegate.getInstance( ).setCurrentFutureObject( extentFutureWoObject );
                FutureTransactionDelegate.getInstance( ).addWOObject( extentFutureWoObject );
            }
            // add coordinate to polygon
            FutureTransactionDelegate.getInstance( ).addLocationToWoExtendObject( coordinate );
        }
    }
}
