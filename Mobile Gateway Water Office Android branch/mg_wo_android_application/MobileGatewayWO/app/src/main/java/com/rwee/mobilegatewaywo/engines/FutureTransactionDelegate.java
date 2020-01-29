package com.rwee.mobilegatewaywo.engines;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;
import com.rwee.mobilegatewaywo.wo_datamodel.FutureWOObject;
import com.rwee.mobilegatewaywo.wo_datamodel.WOExtent;
import com.rwee.mobilegatewaywo.wo_datamodel.WOLocation;
import com.rwee.mobilegatewaywo.wo_datamodel.WORoute;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Robert on 19/02/16.
 */
public final class FutureTransactionDelegate{

    private List< FutureWOObject > woObjectsToInsert = new ArrayList<>(  );
    private FutureWOObject currentFutureObject;

    private boolean pointsMode = false;
    private boolean routesMode = false;
    private boolean polygonsMode = false;

    private static final FutureTransactionDelegate instance = new FutureTransactionDelegate( );

    public static FutureTransactionDelegate getInstance( ){
        return instance;
    }

    public boolean isPointsMode( ){
        return pointsMode;
    }

    public boolean isPolygonsMode( ){
        return polygonsMode;
    }

    public boolean isRoutesMode( ){
        return routesMode;
    }

    public void setPointsMode( boolean pointsMode ){
        this.pointsMode = pointsMode;
    }

    public void setRoutesMode( boolean routesMode ){
        this.routesMode = routesMode;
    }

    public void setPolygonsMode( boolean polygonsMode ){
        this.polygonsMode = polygonsMode;
    }

    public void setCurrentFutureObject( FutureWOObject currentFutureObject ){
        this.currentFutureObject = currentFutureObject;
    }

    public FutureWOObject getCurrentFutureObject( ){
        return currentFutureObject;
    }

    public FutureWOObject createLocationWaterOfficeObject( ){
        FutureWOObject futureWOObject = new FutureWOObject( );
        futureWOObject.woLocation = new WOLocation();
        return futureWOObject;
    }

    public FutureWOObject createRouteWaterOfficeObject(  ){
        FutureWOObject futureWOObject = new FutureWOObject( );
        futureWOObject.woRoute = new WORoute();
        return futureWOObject;
    }

    public FutureWOObject createExtentWaterOfficeObject(  ){
        FutureWOObject futureWOObject = new FutureWOObject( );
        futureWOObject.woExtent = new WOExtent();
        return futureWOObject;
    }

    public void addLocation( LatLng coord ){
        if( isRoutesMode() ){
            currentFutureObject.woRoute.polylineOptions.add( coord );
        }else if( isPolygonsMode() ){
            currentFutureObject.woExtent.polygonOptions.add( coord );
        }

    }

    public boolean addWOObject( FutureWOObject futureWOObject ){
        if( !woObjectsToInsert.contains( futureWOObject ) ){
            woObjectsToInsert.add( futureWOObject );
            return true;
        }
        return false;
    }

    public void setLocationOptionsForFutureLocationWOObject( FutureWOObject futureLocationWOObject, LatLng coordinate ){
        futureLocationWOObject.woLocation.markerOptions.position( coordinate );
        futureLocationWOObject.woLocation.coordinate = coordinate;
        futureLocationWOObject.woLocation.marker = MobileGatewayWOMapActivity.masterActivity.getMap().addMarker( futureLocationWOObject.woLocation.markerOptions );
    }

    public void addLocationToWoRouteObject( LatLng coordinate ){
        // add this coordinate as a marker
        currentFutureObject.markers.add( MobileGatewayWOMapActivity.masterActivity.getMap().addMarker( new MarkerOptions().position( coordinate ) ) );
        if( currentFutureObject.woRoute.polylineOptions.getPoints().size() >= 1 ){
            currentFutureObject.woRoute.polylineOptions.add( coordinate );
            // draw line
            if( currentFutureObject.woRoute.line != null ){
                currentFutureObject.woRoute.line.remove();
            }
            currentFutureObject.woRoute.line = MobileGatewayWOMapActivity.masterActivity.getMap().addPolyline( currentFutureObject.woRoute.polylineOptions.color( Color.RED ).geodesic( true ).width( 5 ) );
        }else{
            currentFutureObject.woRoute.polylineOptions.add( coordinate );
        }
    }

    public void addLocationToWoExtendObject( LatLng coordinate ){
        if( currentFutureObject.woExtent.polygonOptions.getPoints().size() == 5 ){
            return;
        }
        // add this coordinate as a marker
        currentFutureObject.markers.add( MobileGatewayWOMapActivity.masterActivity.getMap().addMarker( new MarkerOptions().position( coordinate ) ) );
        // add this coordinate to polygon options
        currentFutureObject.woExtent.polygonOptions.add( coordinate );
        // draw a line between each coordinates
        if( currentFutureObject.woExtent.polygonOptions.getPoints().size() > 1 ){
            if( currentFutureObject.prePolygonRoute != null ){
                currentFutureObject.prePolygonRoute.remove();
            }
            currentFutureObject.prePolygonRoute = MobileGatewayWOMapActivity.masterActivity.getMap().addPolyline( new PolylineOptions().addAll( currentFutureObject.woExtent.polygonOptions.getPoints() ) );
        }
        if( currentFutureObject.woExtent.polygonOptions.getPoints().size() == 4 ){
            currentFutureObject.woExtent.polygonOptions.add( currentFutureObject.woExtent.polygonOptions.getPoints().get( 0 ) );
            if( currentFutureObject.prePolygonRoute != null ){
                currentFutureObject.prePolygonRoute.remove();
            }
            currentFutureObject.woExtent.polygon = MobileGatewayWOMapActivity.masterActivity.getMap().addPolygon( currentFutureObject.woExtent.polygonOptions );
        }
    }


    public boolean insertMode(){
        if( isPointsMode() || isRoutesMode() || isPolygonsMode() ){
            return true;
        }else{
            return false;
        }
    }

    public void removeGoogleMapObjects(){
        for( FutureWOObject futureWOObject : woObjectsToInsert ){
            if( futureWOObject.woLocation != null ){
                futureWOObject.woLocation.marker.remove();
            }else if( futureWOObject.woRoute != null ){
                futureWOObject.woRoute.line.remove();
                for( Marker marker : futureWOObject.markers ){
                    marker.remove( );
                }
                futureWOObject.markers.clear();
            }else if( futureWOObject.woExtent != null ){
                futureWOObject.woExtent.polygon.remove();
                for( Marker marker : futureWOObject.markers ){
                    marker.remove();
                }
                futureWOObject.markers.clear();
            }
        }
    }

    public void redrawMapObjects(){
        for( FutureWOObject futureWOObject : woObjectsToInsert ){
            if( futureWOObject.woLocation != null ){
                futureWOObject.woLocation.marker = MobileGatewayWOMapActivity.masterActivity.getMap().addMarker( futureWOObject.woLocation.markerOptions );
            }else if( futureWOObject.woRoute != null ){
                // add a marker at every location
                for( LatLng loc : futureWOObject.woRoute.polylineOptions.getPoints() ){
                    futureWOObject.markers.add( MobileGatewayWOMapActivity.masterActivity.getMap( ).addMarker( new MarkerOptions( ).position( loc ) ) );
                }
                futureWOObject.woRoute.line = MobileGatewayWOMapActivity.masterActivity.getMap().addPolyline( futureWOObject.woRoute.polylineOptions );
            }else if( futureWOObject.woExtent != null ){
                for( LatLng loc : futureWOObject.woExtent.polygonOptions.getPoints() ){
                    futureWOObject.markers.add( MobileGatewayWOMapActivity.masterActivity.getMap( ).addMarker( new MarkerOptions( ).position( loc ) ) );
                }
                futureWOObject.woExtent.polygon = MobileGatewayWOMapActivity.masterActivity.getMap().addPolygon( futureWOObject.woExtent.polygonOptions );
            }
        }
    }

    public void reset(){
        FutureTransactionDelegate.getInstance( ).setPointsMode( false );
        FutureTransactionDelegate.getInstance( ).setRoutesMode( false );
        FutureTransactionDelegate.getInstance( ).setPolygonsMode( false );
        currentFutureObject = null;
        FutureTransactionDelegate.getInstance( ).woObjectsToInsert.clear();
    }

    public List< FutureWOObject > getWoObjectsToInsert( ){
        return woObjectsToInsert;
    }


    public String[] getMetadata( String objectType ){
        String[] basicMetadata = new String[ 2 ];
        switch( objectType ){
            case WDManhole.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WDManhole.COLLECTION_NAME;
                basicMetadata[ 1 ] = WDManhole.DATASET_NAME;
                break;
            case WDServiceConnection.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WDServiceConnection.COLLECTION_NAME;
                basicMetadata[ 1 ] = WDServiceConnection.DATASET_NAME;
                break;
            case WSHydrant.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSHydrant.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSHydrant.DATASET_NAME;
                break;
            case WSServicePoint.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSServicePoint.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSServicePoint.DATASET_NAME;
                break;
            case WSTee.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSTee.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSTee.DATASET_NAME;
                break;
            case WSValve.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSValve.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSValve.DATASET_NAME;
                break;
            case WDConnectionPipe.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WDConnectionPipe.COLLECTION_NAME;
                basicMetadata[ 1 ] = WDConnectionPipe.DATASET_NAME;
                break;
            case WDSewerSection.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WDSewerSection.COLLECTION_NAME;
                basicMetadata[ 1 ] = WDSewerSection.DATASET_NAME;
                break;
            case WSMainSection.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSMainSection.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSMainSection.DATASET_NAME;
                break;
            case WSStation.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSStation.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSStation.DATASET_NAME;
                break;
            case WSTank.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSTank.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSTank.DATASET_NAME;
                break;
            case WSTreatmentPlant.EXTERNAL_NAME :
                basicMetadata[ 0 ] = WSTreatmentPlant.COLLECTION_NAME;
                basicMetadata[ 1 ] = WSTreatmentPlant.DATASET_NAME;
                break;
        }
        return basicMetadata;
    }


}
