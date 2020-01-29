package com.rwee.mobilegatewaywo.engines;





import android.graphics.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.R;
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


public final class MobileGatewayDataStoreBuilder {

    private MobileGatewayDataStoreBuilder( ){

    }


    public static void insert( GoogleMap map, String collectionName, List< HashMap< String, String > > data ){
        switch( collectionName ){
            case WDConnectionPipe.COLLECTION_NAME:
                insertWDConnectionPipe( data, map );
                //MapEngine.drawWdConnectionPipe( map );
                break;
            case WDManhole.COLLECTION_NAME:
                insertWDManhole( data, map );
            //    MapEngine.drawWdManhole( map );
                break;
            case WDSewerSection.COLLECTION_NAME:
                insertWdSewerSection( data, map );
            //    MapEngine.drawWdSewerSection( map );
                break;
            case WDServiceConnection.COLLECTION_NAME :
                insertWdServiceConnection( data, map );
            //    MapEngine.drawWdServiceConnection( map );
                break;
            case WSHydrant.COLLECTION_NAME :
                insertWsHydrant( data, map );
            //    MapEngine.drawWsHydrant( map );
                break;
            case WSMainSection.COLLECTION_NAME :
                insertWsMainSection( data, map );
            //    MapEngine.drawWsMainSection( map );
                break;
            case WSServicePoint.COLLECTION_NAME :
                insertWsServicePoint( data, map );
            //    MapEngine.drawWsServicePoint( map );
                break;
            case WSTank.COLLECTION_NAME :
                insertWsTank( data, map );
            //    MapEngine.drawWsTank( map );
                break;
            case WSStation.COLLECTION_NAME :
                insertWsStation( data, map );
            //    MapEngine.drawWsStation( map );
                break;
            case WSTee.COLLECTION_NAME :
                insertWsTee( data, map );
             //   MapEngine.drawWsTee( map );
                break;
            case WSTreatmentPlant.COLLECTION_NAME :
                insertWsTreatmentPlant( data, map );
            //    MapEngine.drawWsTreatmentPlant( map );
                break;
            case WSValve.COLLECTION_NAME :
                insertWsValve( data, map );
            //    MapEngine.drawWsValve( map );
                break;
            default:
                break;
        }

    }

    private static void insertWDConnectionPipe( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WDConnectionPipe connectionPipe = new WDConnectionPipe();
            connectionPipe.id = Integer.valueOf( keyValues.get( "id" ) );
            connectionPipe.endPipeBaseLevel = keyValues.get( "end_pipe_base_level" );
            connectionPipe.connectionType = keyValues.get( "connection_type" );
            connectionPipe.startPipeBaseLevel = keyValues.get( "start_pipe_base_level" );
            connectionPipe.calculatedPipeLength = keyValues.get( "calculated_pipe_length" );
            connectionPipe.specificationName = keyValues.get( "wd_connection_pipe_spec" );
            connectionPipe.waterType = keyValues.get( "water_type" );
            connectionPipe.woRoute = new WORoute();
            String points = keyValues.get( "route" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[ 1 ];
                String longitude = pair.split( Pattern.quote( "," ) )[ 0 ];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                connectionPipe.woRoute.polylineOptions.add( location );
            }
            connectionPipe.woRoute.line = map.addPolyline( connectionPipe.woRoute.polylineOptions
                    .clickable( true )
                    .width( connectionPipe.getRouteWidth( ) )
                    .color( connectionPipe.getColorId( ) )
                    .geodesic( true ) );

            WODataStoreSingleton.instance.wdConnectionPipeSet.add( connectionPipe );
        }
    }

    private static void insertWDManhole( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WDManhole manhole = new WDManhole();
            manhole.id = Integer.valueOf( keyValues.get( "id" ) );
            manhole.waterType = keyValues.get( "water_type" );
            manhole.constructionStatus = keyValues.get( "construction_status" );
            manhole.coverLevel = keyValues.get( "cover_level" );
            manhole.baseHeight = keyValues.get( "base_height" );
            manhole.depth = keyValues.get( "depth" );
        //    manhole.manholeInspection = keyValues.get( "manhole_inspection" );
            manhole.specificationName = keyValues.get( "wd_manhole_spec" );
            manhole.manholeNumber = keyValues.get( "manhole_number" );
            manhole.woLocation = new WOLocation();
            String coords = keyValues.get( "location_centre_manhole" );
            if( coords == null ){
                continue;
            }
            String longitude = coords.split( Pattern.quote( "," ) )[ 0 ];
            String latitude = coords.split( Pattern.quote( "," ) )[ 1 ];
            manhole.woLocation.coordinate = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            manhole.woLocation.icon = manhole.getIco();
            manhole.woLocation.markerOptions.position( manhole.woLocation.coordinate ).
                    icon( manhole.woLocation.icon ).
                    anchor( 0.5f, 0.5f ).
                    title( WDManhole.EXTERNAL_NAME );
            manhole.woLocation.marker = map.addMarker( manhole.woLocation.markerOptions );
            WODataStoreSingleton.instance.wdManholeSet.add( manhole );
        }
    }

    private static void insertWdSewerSection( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WDSewerSection sewerSection = new WDSewerSection();
            sewerSection.id = Integer.valueOf( keyValues.get( "id" ) );
            sewerSection.waterType = keyValues.get( "water_type" );
            sewerSection.situation = keyValues.get( "situation" );
            sewerSection.baseHeightStart = keyValues.get( "base_height_start" );
            sewerSection.baseHeightEnd = keyValues.get( "base_height_end" );
           // sewerSection.crossSectionalArea = keyValues.get( "cross_sectional_area" );
            sewerSection.sewerSectionLength = keyValues.get( "sewer_section_calc_length" );
            sewerSection.specificationName = keyValues.get( "ws_sewer_section_spec" );
            sewerSection.woRoute = new WORoute();
            String points = keyValues.get( "route" );
            if( points == null ){
                continue;
            }
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                sewerSection.woRoute.polylineOptions.add( location );
            }
            sewerSection.woRoute.line = map.addPolyline( sewerSection.woRoute.polylineOptions
                    .clickable( true )
                    .width( sewerSection.getRouteWidth( ) )
                    .color( sewerSection.getColorId( ) )
                    .geodesic( true ) );
            WODataStoreSingleton.instance.wdSewerSectionSet.add( sewerSection );
        }
    }

    private static void insertWdServiceConnection( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WDServiceConnection serviceConnection = new WDServiceConnection();
            serviceConnection.id = Integer.valueOf( keyValues.get( "id" ) );
            serviceConnection.waterType = keyValues.get( "water_type" );
            serviceConnection.connectorHeight = keyValues.get( "connector_height" );
            serviceConnection.property = keyValues.get( "property" );
            serviceConnection.woLocation = new WOLocation();
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 0 ];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 1 ];
            LatLng loc = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            serviceConnection.woLocation.coordinate = loc;

            serviceConnection.woLocation.icon = serviceConnection.getIco();
            serviceConnection.woLocation.markerOptions.
                    position( loc ).
                    icon( serviceConnection.woLocation.icon ).
                    anchor( 0.5f, 0.5f ).
                    title( WDServiceConnection.EXTERNAL_NAME );
            serviceConnection.woLocation.marker = map.addMarker( serviceConnection.woLocation.markerOptions );

            WODataStoreSingleton.instance.wdServiceConnectionSet.add( serviceConnection );
        }
    }

    private static void insertWsHydrant( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSHydrant hydrant = new WSHydrant();
            hydrant.id = Integer.valueOf( keyValues.get( "id" ) );
            hydrant.network = keyValues.get( "network" );
            hydrant.state = keyValues.get( "state" );
            hydrant.performance = keyValues.get( "performance" );
            hydrant.relationToMain = keyValues.get( "relation_to_main" );
            hydrant.specificationName = keyValues.get( "ws_hydrant_spec" );
            hydrant.woLocation = new WOLocation();
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 0 ];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 1 ];
            LatLng loc = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            hydrant.woLocation.coordinate = loc;
            hydrant.woLocation.icon = BitmapDescriptorFactory.fromResource( R.drawable.hydrant );
            hydrant.woLocation.markerOptions.
                    position( loc ).
                    anchor( 0.5f, 0.5f ).
                    icon( hydrant.woLocation.icon ).
                    title( WSHydrant.EXTERNAL_NAME );
            hydrant.woLocation.marker = map.addMarker( hydrant.woLocation.markerOptions );

            WODataStoreSingleton.instance.wsHydrantSet.add( hydrant );
        }
    }

    private static void insertWsMainSection( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSMainSection mainSection = new WSMainSection();
            mainSection.id = Integer.valueOf( keyValues.get( "id" ) );
            mainSection.network = keyValues.get( "network" );
            mainSection.waterType = keyValues.get( "water_type" );
            mainSection.length = keyValues.get( "length" );
            mainSection.state = keyValues.get( "state" );
            mainSection.specificationName = keyValues.get( "ws_main_section_spec" );
            mainSection.woRoute = new WORoute();
            String points = keyValues.get( "route" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                mainSection.woRoute.polylineOptions.add( location );
            }
            mainSection.woRoute.line = map.addPolyline( mainSection.woRoute.polylineOptions
                    .clickable( true )
                    .width( mainSection.getRouteWidth( ) )
                    .color( mainSection.getColorId( ) )
                    .geodesic( true ) );

            WODataStoreSingleton.instance.wsMainSectionSet.add( mainSection );
        }
    }

    private static void insertWsServicePoint( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSServicePoint servicePoint = new WSServicePoint();
            servicePoint.id = Integer.valueOf( keyValues.get( "id" ) );
            servicePoint.network = keyValues.get( "network" );
            servicePoint.state = keyValues.get( "state" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 0 ];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 1 ];
            LatLng loc = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            servicePoint.woLocation = new WOLocation();
            servicePoint.woLocation.coordinate = loc;
            servicePoint.woLocation.icon = BitmapDescriptorFactory.fromResource( R.drawable.service_point );
            servicePoint.woLocation.markerOptions.
                    position( loc ).
                    anchor( 0.5f, 0.5f ).
                    icon( servicePoint.woLocation.icon ).
                    title( WSServicePoint.EXTERNAL_NAME );

            servicePoint.woLocation.marker = map.addMarker( servicePoint.woLocation.markerOptions );
            WODataStoreSingleton.instance.wsServicePointSet.add( servicePoint );
        }
    }

    private static void insertWsStation( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSStation station = new WSStation();
            station.id = Integer.valueOf( keyValues.get( "id" ) );
            station.name = keyValues.get( "name" );
            station.capacity = keyValues.get( "capacity" );
            station.state = keyValues.get( "state" );
            station.woExtent = new WOExtent();
            String points = keyValues.get( "extent" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                station.woExtent.polygonOptions.add( location )
                        .zIndex( 1 )
                        .strokeWidth( 3 )
                        .clickable( true )
                        .strokeColor( station.getColorId() )
                        .geodesic( true ) ;
            }
            station.woExtent.polygon = map.addPolygon( station.woExtent.polygonOptions );
            WODataStoreSingleton.instance.wsStationSet.add( station );
        }
    }

    private static void insertWsTank( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSTank tank = new WSTank();
            L.m( "draw tank" );
            tank.id = Integer.valueOf( keyValues.get( "id" ) );
            tank.name = keyValues.get( "name" );
            tank.state = keyValues.get( "state" );
            tank.usedCapacity = keyValues.get( "used_capacity" );
            tank.specificationName = keyValues.get( "ws_tank_spec" );
            tank.woExtent = new WOExtent();
            String points = keyValues.get( "extent" );
            String[] vec = points.split( Pattern.quote( ";" ) );

            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                tank.woExtent.polygonOptions.add( location )
                        .zIndex( 5 )
                        .geodesic( true )
                        .strokeColor( tank.getColorId() )
                        .clickable( true )
                        .strokeWidth( 3 );
            }
            tank.woExtent.polygon = map.addPolygon( tank.woExtent.polygonOptions );
            WODataStoreSingleton.instance.wsTankSet.add( tank );
        }
    }

    private static void insertWsTee( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSTee tee = new WSTee();
            tee.id = Integer.valueOf( keyValues.get( "id" ) );
            tee.network = keyValues.get( "network" );
            tee.state = keyValues.get( "state" );
            tee.teeDirection = keyValues.get( "tee_direction" );
            tee.specificationName = keyValues.get( "ws_tee_spec" );
            tee.woLocation = new WOLocation();
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 0 ];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[ 1 ];
            LatLng loc = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            tee.woLocation.coordinate = loc;
            tee.woLocation.icon = BitmapDescriptorFactory.fromResource( R.drawable.tee );
            tee.woLocation.markerOptions.position( loc ).anchor( 0.5f, 0.5f ).icon( tee.woLocation.icon ).title( WSTee.EXTERNAL_NAME );
            tee.woLocation.marker = map.addMarker( tee.woLocation.markerOptions );

            WODataStoreSingleton.instance.wsTeeSet.add( tee );
        }
    }

    private static void insertWsTreatmentPlant( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSTreatmentPlant treatmentPlant = new WSTreatmentPlant();
            treatmentPlant.id = Integer.valueOf( keyValues.get( "id" ) );
            treatmentPlant.worksName = keyValues.get( "works_name" );
            treatmentPlant.state = keyValues.get( "state" );
            treatmentPlant.specificationName = keyValues.get( "ws_treatment_plant_spec" );
            treatmentPlant.woExtent = new WOExtent();
            String points = keyValues.get( "extent" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                treatmentPlant.woExtent.polygonOptions.add( location )
                        .zIndex( 5 )
                        .geodesic( true )
                        .strokeColor( treatmentPlant.getColorId() )
                        .clickable( true ).strokeWidth( 3 ) ;
            }
            treatmentPlant.woExtent.polygon = map.addPolygon( treatmentPlant.woExtent.polygonOptions);

            WODataStoreSingleton.instance.wsTreatmentPlantSet.add( treatmentPlant );
        }
    }

    private static void insertWsValve( List< HashMap< String, String > > elementList, GoogleMap map ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            WSValve valve = new WSValve();
            valve.id = Integer.valueOf( keyValues.get( "id" ) );
            valve.network = keyValues.get( "network" );
            valve.state = keyValues.get( "state" );
            valve.torque = keyValues.get( "torque" );
            valve.specificationName = keyValues.get( "ws_valve_spec" );
            valve.woLocation = new WOLocation();
            String locationString = keyValues.get( "location" );
            if( locationString == null ){
                return;
            }
            String longitude = locationString.split( Pattern.quote( "," ) )[ 0 ];
            String latitude = locationString.split( Pattern.quote( "," ) )[ 1 ];
            LatLng loc = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            valve.woLocation.coordinate = loc;
            valve.woLocation.icon = BitmapDescriptorFactory.fromResource( R.drawable.valve );
            valve.woLocation.markerOptions.position( loc )
                    .anchor( 0.5f, 0.5f )
                    .icon( valve.woLocation.icon )
                    .title( WSValve.EXTERNAL_NAME );
            valve.woLocation.marker = map.addMarker( valve.woLocation.markerOptions );

            WODataStoreSingleton.instance.wsValveSet.add( valve );
        }
    }

    /*
    public static void insertDesigns( List< HashMap< String, String > > elementList, SetOnInitialisationCompleteListener initialisationListener ){
        if( elementList != null ){
            for( HashMap< String, String > mapping : elementList ){
                Design desing = new Design( );
                desing.id = Integer.valueOf( mapping.get( "id" ) );
                desing.owner = mapping.get( "owner" );
                desing.name = mapping.get( "name" );
                desing.status = mapping.get( "status" );

                WODataStoreSingleton.instance.designSet.add( desing );
            }
            initialisationListener.onInitialiseComplete( true, null );
        }
    }



    */






}
