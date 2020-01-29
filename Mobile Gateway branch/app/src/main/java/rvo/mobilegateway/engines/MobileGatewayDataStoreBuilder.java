package rvo.mobilegateway.engines;



import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.logical_datamodel.CircuitIssue;
import rvo.mobilegateway.logical_datamodel.CircuitLocation;
import rvo.mobilegateway.logical_datamodel.Facility;
import rvo.mobilegateway.logical_datamodel.LogicalObject;
import rvo.mobilegateway.logical_datamodel.NetworkElement;
import rvo.mobilegateway.logical_datamodel.ServicePort;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.AerialRoute;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Cable;
import rvo.mobilegateway.physical_datamodel.Connection;
import rvo.mobilegateway.physical_datamodel.Design;
import rvo.mobilegateway.physical_datamodel.EquipmentObject;
import rvo.mobilegateway.physical_datamodel.Fiber;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.NetworkConnection;
import rvo.mobilegateway.physical_datamodel.OpticalBundle;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.Port;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundRoute;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public final class MobileGatewayDataStoreBuilder {

    private MobileGatewayDataStoreBuilder( ){

    }


    public static void insert( GoogleMap map, String collectionName, List< HashMap< String, String > > data ){
        switch( collectionName ){
            case UndergroundUtilityBox.COLLECTION_NAME:
                insertUubs( map, data );
                break;
            case Hub.COLLECTION_NAME:
                insertHubs( map, data );
               // MapEngine.drawHubs( map );
                break;
            case UndergroundRoute.COLLECTION_NAME:
                insertUndergroundRoutes(map, data );
               // MapEngine.drawUndergroundRoutes( map );
                break;
            case Pole.COLLECTION_NAME:
                insertPoles( map, data );
               // MapEngine.drawPoles( map );
                break;
            case Building.COLLECTION_NAME:
                insertBuildings( map, data );
               // MapEngine.drawBuildings( map );
                break;
            case AerialRoute.COLLECTION_NAME:
                insertAerialRoutes( map, data );
               // MapEngine.drawAerialRoutes( map );
                break;
            case AccessPoint.COLLECTION_NAME:
                insertAccessPoints( map, data );
               // MapEngine.drawAccessPoints( map );
                break;
            case TerminalEnclosure.COLLECTION_NAME:
                insertTerminalEnclosures( map, data );
               // MapEngine.drawTerminalEnclosures( map );
                break;
            default:
                return;
        }
        if( MapEngine.getHighlighedCable() != null ){
            MapEngine.highlightRouteForCable( MapEngine.getHighlighedCable(), true );
        }
    }

    private static void insertHubs( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){

            int id = Integer.valueOf( keyValues.get( "id" ) );
            String type = keyValues.get( "type" );
            String oroAlias = keyValues.get( "oro_alias" );
            String owner = keyValues.get( "oro_owner" );
            String ownerAlias = keyValues.get( "oro_owner_alias" );
            String name = keyValues.get( "name" );
            String constructionStatus = keyValues.get( "construction_status" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            Hub hub = new Hub( );
            hub.id = id;
            hub.constructionStatus = constructionStatus;
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            hub.location = location;
            hub.name = name;
            hub.oroAlias = oroAlias;
            hub.owner = owner;
            hub.ownerAlias = ownerAlias;
            hub.type = type;
            hub.icon = hub.getIco();
            hub.markerOptions = new MarkerOptions( ).position( location )
                    .title( Hub.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( hub.icon );
            hub.marker = map.addMarker( hub.markerOptions );
            PniDataStoreSingleton.instance.mitHubs.add( hub );
        }
    }

    private static void insertTerminalEnclosures( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){

            int id = Integer.valueOf( keyValues.get( "id" ) );
            String oroAlias = keyValues.get( "oro_alias" );
            String owner = keyValues.get( "oro_owner" );
            String ownerAlias = keyValues.get( "oro_owner_alias" );
            String name = keyValues.get( "name" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            TerminalEnclosure te = new TerminalEnclosure( );
            te.id = id;
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            te.location = location;
            te.specificationName = keyValues.get( "spec_id" );
            te.name = name;
            te.owner = owner;
            te.ownerAlias = ownerAlias;
            te.oroAlias = oroAlias;
            te.icon = te.getIco();
            te.markerOptions = new MarkerOptions( ).position( location )
                    .title( TerminalEnclosure.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( te.icon );
            te.marker = map.addMarker( te.markerOptions );
            PniDataStoreSingleton.instance.terminalEnclosures.add( te );
        }
    }

    private static void insertBuildings( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            int id = Integer.valueOf( keyValues.get( "id" ) );
            String oroAlias = keyValues.get( "oro_alias" );
            String owner = keyValues.get( "oro_owner" );
            String ownerAlias = keyValues.get( "oro_owner_alias" );
            String type = keyValues.get( "type" );
            String name = keyValues.get( "name" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            Building building = new Building( );
            building.id = id;
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            building.location = location;
            building.name = name;
            building.oroAlias = oroAlias;
            building.owner = owner;
            building.ownerAlias = ownerAlias;
            building.type = type;
            building.oroAlias = oroAlias;
            building.icon = building.getIco();
            building.markerOptions = new MarkerOptions( ).position( location )
                    .title( Building.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( building.icon );
            building.marker = map.addMarker( building.markerOptions );
            PniDataStoreSingleton.instance.buildings.add( building );
        }
    }

    private static void insertUubs( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            int id = Integer.valueOf( keyValues.get( "id" ) );
            String type = keyValues.get( "type" );
            String oroAlias = keyValues.get( "oro_alias" );
            String label = keyValues.get( "label" );
            String owner = keyValues.get( "oro_owner" );
            String ownerALias = keyValues.get( "oro_owner_alias" );
            String constructionStatus = keyValues.get( "construction_status" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            String name = keyValues.get( "name" );
            UndergroundUtilityBox uub = new UndergroundUtilityBox( );
            uub.id = id;
            uub.constructionStatus = constructionStatus;
            uub.type = type;
            uub.oroAlias = oroAlias;
            uub.label = label;
            uub.owner = owner;
            uub.ownerAlias = ownerALias;
            uub.name = name;
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            uub.location = location;
            uub.specificationName = keyValues.get( "spec_id" );
            uub.icon = uub.getIco( );
            uub.markerOptions = new MarkerOptions( ).position( location )
                    .title( UndergroundUtilityBox.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( uub.icon )
                    .draggable( true );
            uub.marker = map.addMarker( uub.markerOptions );
            PniDataStoreSingleton.instance.uubs.add( uub );

        }
    }

    private static void insertPoles( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            Pole pole = new Pole( );
            pole.id = Integer.valueOf( keyValues.get( "id" ) );
            pole.oroAlias = keyValues.get( "oro_alias" );
            pole.owner = keyValues.get( "oro_owner" );
            pole.ownerAlias = keyValues.get( "oro_owner_alias" );
            pole.materialType = keyValues.get( "material_type" );
            pole.constructionStatus = keyValues.get( "construction_status" );
            pole.usage = keyValues.get( "usage" );
            pole.name = keyValues.get( "name" );
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            pole.icon = pole.getIco();
            pole.markerOptions = new MarkerOptions( ).position( location )
                    .title( Pole.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( pole.icon );
            pole.marker = map.addMarker( pole.markerOptions );
            pole.location = location;

            PniDataStoreSingleton.instance.poles.add( pole );
        }
    }


    private static void insertUndergroundRoutes( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            UndergroundRoute ur = new UndergroundRoute( );
            int id = Integer.valueOf( keyValues.get( "id" ) );
            String constructionStatus = keyValues.get( "construction_status" );
            ur.id = id;
            ur.constructionStatus = constructionStatus;
            ur.name = keyValues.get( "name" );
            ur.oroAlias = keyValues.get( "oro_alias" );
            ur.owner = keyValues.get( "oro_owner" );
            ur.ownerAlias = keyValues.get( "oro_owner_alias" );
            ur.length = keyValues.get( "length" ).split( Pattern.quote( "." ) )[0];
            String points = keyValues.get( "route" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[1];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                ur.polylineOptions.add( location ).width( ur.getRouteWidth() ).color( ur.getColorId() ).clickable( true );
            }
            ur.route = map.addPolyline( ur.polylineOptions );
            PniDataStoreSingleton.instance.undergroundRoutes.add( ur );
        }
    }


    private static void insertAerialRoutes( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            AerialRoute ar = new AerialRoute( );
            int id = Integer.valueOf( keyValues.get( "id" ) );
            String constructionStatus = keyValues.get( "construction_status" );
            ar.id = id;
            ar.constructionStatus = constructionStatus;
            ar.name = keyValues.get( "name" );
            ar.oroAlias = keyValues.get( "oro_alias" );
            ar.owner = keyValues.get( "oro_owner" );
            ar.ownerAlias = keyValues.get( "oro_owner_alias" );
            ar.length = keyValues.get( "length" ).split( Pattern.quote( "." ) )[0];
            String points = keyValues.get( "route" );
            String[] vec = points.split( Pattern.quote( ";" ) );
            for( String pair : vec ){
                String latitude = pair.split( Pattern.quote( "," ) )[ 1 ];
                String longitude = pair.split( Pattern.quote( "," ) )[0];
                LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
                ar.polylineOptions.add( location ).width( ar.getRouteWidth() ).color( ar.getColorId() ).clickable( true );
            }
            ar.route = map.addPolyline( ar.polylineOptions );

            PniDataStoreSingleton.instance.aerialRoutes.add( ar );
        }
    }

    private static void insertAccessPoints( GoogleMap map, List< HashMap< String, String > > elementList ){
        if( elementList == null || elementList.isEmpty( ) ){
            return;
        }
        for( HashMap< String, String > keyValues : elementList ){
            AccessPoint ap = new AccessPoint(  );
            int id = Integer.valueOf( keyValues.get( "id" ) );
            ap.id = id;
            String type = keyValues.get( "type" );
            ap.type = type;
            String longitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[0];
            String latitude = keyValues.get( "location" ).split( Pattern.quote( "," ) )[1];
            LatLng location = new LatLng( Float.valueOf( latitude ), Float.valueOf( longitude ) );
            ap.location = location;
            ap.icon = ap.getIco();
            MarkerOptions mo = new MarkerOptions( ).position( location )
                    .title( AccessPoint.EXTERNAL_NAME )
                    .anchor( 0.5f, 0.5f )
                    .icon( ap.icon );
            ap.markerOptions = mo;


            ap.marker = map.addMarker( ap.markerOptions );
            ap.specificationName = keyValues.get( "spec_id" );

            ap.icon = ap.getIco();
            PniDataStoreSingleton.instance.accessPoints.add( ap );
        }
    }


    public static void insertDesigns( List< HashMap< String, String > > elementList, SetOnInitialisationCompleteListener initialisationListener ){
        if( elementList != null ){
            for( HashMap< String, String > mapping : elementList ){
                Design desing = new Design( );
                desing.id = Integer.valueOf( mapping.get( "id" ) );
                desing.owner = mapping.get( "owner" );
                desing.name = mapping.get( "name" );
                desing.status = mapping.get( "status" );

                PniDataStoreSingleton.instance.designSet.add( desing );
            }
            initialisationListener.onInitialiseComplete( true, null );
        }
    }


    public static void initializeEquipments( AbstractTelcoObject rootedObject, List< HashMap< String, String > > list, SetOnInitialisationCompleteListener initialisationCompleteListener ){
        if( list == null ){
            return;
        }
        Integer initType = null;
        for( HashMap< String, String > mapping : list ){
            String type = mapping.get( "type" );
            switch( type ){
                case "splice_closure":
                    initType = 0;
                    rootedObject.addInternalEquipment( initSpliceClosure( rootedObject, mapping ) );
                    break;
                case "rack":
                    initType = 0;
                    rootedObject.addInternalEquipment( initRack( rootedObject, mapping ) );
                    break;
                case "shelf":
                    initType = 0;
                    rootedObject.addInternalEquipment( initShelf( rootedObject, mapping ) );
                    break;
                case "slot":
                    initType = 0;
                    rootedObject.addInternalEquipment( initSlot( rootedObject, mapping ) );
                    break;
                case "card":
                    initType = 0;
                    rootedObject.addInternalEquipment( initCard( rootedObject, mapping ) );
                    break;
                case "mit_rme_port" :
                case "int_balanced_port" :
                case "int_unbalanced_port" :
                    initType = 1;
                    rootedObject.addPort( initPort( rootedObject, mapping ) );
                    break;
                case "optical_splitter" :
                    initType = 0;
                    rootedObject.addInternalEquipment( initOpticalSplitter( rootedObject, mapping ) );
                    break;
                default :
                    // do nothing
                    break;
            }
        }
        if( initType != null ){
            if( initType == 0 ){
                // equipment
                initialisationCompleteListener.onEquipmentsInitialisationComplete( true );
            }else if( initType == 1 ){
                // port
                initialisationCompleteListener.onPortsInitialisationComplete( true );
            }
        }

    }


    private static EquipmentObject initSpliceClosure( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject spliceClosure = new EquipmentObject( );
        spliceClosure.id = ( Integer.valueOf( mapping.get( "id" ) ) );
        spliceClosure.name = mapping.get( "name" );
        spliceClosure.alias = mapping.get( "alias" );
        spliceClosure.description = mapping.get( "description" );
        spliceClosure.longDescription = mapping.get( "long_description" );
        spliceClosure.equipmentType = mapping.get( "type" );
        spliceClosure.owner = rootedObject;
        String inCables = mapping.get( "in_cables" );
        String outCables = mapping.get( "out_cables" );
        if( inCables != null ){
            spliceClosure.inCablesAsVector = inCables.split( Pattern.quote( "," ) );
        }
        if( outCables != null ){
            spliceClosure.outCablesAsVector = outCables.split( Pattern.quote( "," ) );
        }
        return spliceClosure;
    }

    private static EquipmentObject initRack( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject rack = new EquipmentObject( );
        rack.id = Integer.valueOf( mapping.get( "id" ) );
        rack.name = mapping.get( "name" );
        rack.equipmentType = mapping.get( "type" );
        rack.description = mapping.get( "description" );
        rack.longDescription = mapping.get( "long_description" );
        rack.owner = rootedObject;
        if( mapping.containsKey( "strw_connection_point" ) ){
            rack.strwConnectionPointId = Integer.valueOf( mapping.get( "strw_connection_point" ) );
            String inCables = mapping.get( "in_cables" );
            String outCables = mapping.get( "out_cables" );

            if( inCables != null ){
                rack.inCablesAsVector = mapping.get( "in_cables" ).split( Pattern.quote( "," ) );
            }
            if( outCables != null ){
                rack.outCablesAsVector = mapping.get( "out_cables" ).split( Pattern.quote( "," ) );
            }
        }
        return rack;
    }

    private static EquipmentObject initShelf( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject shelf = new EquipmentObject( );
        shelf.id = Integer.valueOf( mapping.get( "id" ) );
        shelf.name = mapping.get( "name" );
        shelf.description = mapping.get( "description" );
        shelf.longDescription = mapping.get( "long_description" );
        shelf.equipmentType = mapping.get( "type" );
        shelf.owner = rootedObject;
        return shelf;
    }

    private static EquipmentObject initSlot( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject slot = new EquipmentObject( );
        slot.id = Integer.valueOf( mapping.get( "id" ) );
        slot.name = mapping.get( "name" );
        slot.description = mapping.get( "description" );
        slot.longDescription = mapping.get( "long_description" );
        slot.equipmentType = mapping.get( "type" );
        slot.owner = rootedObject;

        return slot;
    }

    private static EquipmentObject initCard( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject card = new EquipmentObject( );
        card.id = Integer.valueOf( mapping.get( "id" ) );
        card.name = mapping.get( "name" );
        card.equipmentType = mapping.get( "type" );
        card.description = mapping.get( "description" );
        card.longDescription = mapping.get( "long_description" );
        card.owner = rootedObject;

        return card;
    }

    private static Port initPort( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        Port port = new Port( );
        port.id = Integer.valueOf( mapping.get( "id" ) );
        port.name = mapping.get( "name" );
        port.description = mapping.get( "description" );
        port.owner = rootedObject;
        port.portType = mapping.get( "type" );
        if( mapping.containsKey( "logical_object_type" ) ){
            port.facility = initFacility( rootedObject, mapping );
        }

        if( mapping.get( "port_number" ) != null ){
            port.portNumber = Integer.valueOf( mapping.get( "port_number" ) );
        }
        return port;
    }

    private static EquipmentObject initOpticalSplitter( AbstractTelcoObject rootedObject, HashMap< String, String > mapping ){
        EquipmentObject os = new EquipmentObject( );
        os.id = Integer.valueOf( mapping.get( "id" ) );
        os.name = mapping.get( "name" );
        os.owner = rootedObject;
        os.equipmentType = mapping.get( "type" );
        os.description = mapping.get( "description" );
        os.longDescription = mapping.get( "long_description" );
        os.equipmentType = mapping.get( "type" );

        return os;
    }

    public static void initializeCables( AbstractTelcoObject routedObject, List< HashMap< String, String > > list, boolean connected, SetOnInitialisationCompleteListener initialisationListener ){
        if( list == null ){
            return;
        }
        PniDataStoreSingleton pni = PniDataStoreSingleton.instance;
        for( HashMap< String, String > mapping : list ){
            Cable cable = new Cable( );
            cable.cableName = mapping.get( "cable_name" );
            cable.cableAlias = mapping.get( "cable_alias" );
            cable.cableType = mapping.get( "cable_type" );
            cable.routeType = mapping.get( "route_type" );
            cable.id = Integer.valueOf( mapping.get( "id" ) );

            String[] splittedArs = mapping.get( "aerial_route_ids" ).split( Pattern.quote( ":" ) );
            if( splittedArs.length == 2 ){
                cable.aerialRouteIds = splittedArs[1].split( Pattern.quote( "," ) );
            }
            String[] splittedUrs = mapping.get( "underground_route_ids" ).split( Pattern.quote( ":" ) );
            if( splittedUrs.length == 2 ){
                cable.undergroundRouteIds = splittedUrs[1].split( Pattern.quote( "," ) );
            }
            if( !connected ){
                cable.isPassingThrough = true;
                routedObject.addPassingThroughCable( cable );
            }else{
                cable.equipment = routedObject.getInternalEquipmentById(  Integer.valueOf( mapping.get( "equipment_id" ) ), true );
                routedObject.addConnectedCable( cable );
                // check the ids( inCables and outCables ) of cables from equipment with this cable id
                String[] inCables = cable.equipment.inCablesAsVector;
                if( inCables != null ){
                    for( String inCableId : cable.equipment.inCablesAsVector ){
                        if( cable.id == Integer.valueOf( inCableId ) ){
                            cable.equipment.addInCable( cable );
                        }
                    }
                }
                String[] outCables = cable.equipment.outCablesAsVector;
                if( outCables != null ){
                    for( String outCableId : cable.equipment.outCablesAsVector ){
                        if( cable.id == Integer.valueOf( outCableId ) ){
                            cable.equipment.addOutCable( cable );
                        }
                    }
                }
            }
            PniDataStoreSingleton.instance.cables.add( cable );
        }
        if( connected ){
            initialisationListener.onConnectedCablesInitialisationComplete( true );
        }
    }


    public  static void initializeCableConnections( List< HashMap< String, String > > list, Cable cable, SetOnInitialisationCompleteListener initialisationListener ){
        if( list == null ){
            return;
        }

        for( HashMap< String, String > mapping : list ){
            // compune "from" bundle
            OpticalBundle fromOpticalBundle = cable.addBundleIfNecessary( mapping, "from_bundle_number", "from_bundle_color" );
            fromOpticalBundle.cable = cable;
            Fiber fromFiber = fromOpticalBundle.addFiberIfNecessary( mapping, "from_fiber_number", "from_fiber_color" );
            fromFiber.opticalBundle = fromOpticalBundle;
            // add check for unconnected fiber
            if( mapping.containsKey( "to_port_id" ) ){
                Connection connection = new Connection( );
                connection.fromFiber = fromFiber;
                fromFiber.cableConnection = connection;
                // set "to" port connection
                Port port = new Port( );
                port.id = Integer.valueOf( mapping.get( "to_port_id" ) );
                port.portType = mapping.get( "to_port_type" );
                port.name = mapping.get( "to_port_name" );
                port.description = mapping.get( "to_port_description" );
                connection.connectionDescription = mapping.get( "to_port_connection_description" );
                connection.toPort = port;
                port.connection = connection;
            } else if( mapping.containsKey( "to_cable_id" ) ){
                Connection connection = new Connection( );
                connection.fromFiber = fromFiber;
                fromFiber.cableConnection = connection;
                String toCableIdString = mapping.get( "to_cable_id" );
                Cable toCable = null;
                if( toCableIdString != null ){
                    toCable = PniDataStoreSingleton.instance.getCableById( Integer.valueOf( toCableIdString ) );
                }
                // set "to" fiber connection
                OpticalBundle toOpticalBundle = toCable.addBundleIfNecessary( mapping, "to_bundle_number", "to_bundle_color" );
                toOpticalBundle.cable = toCable;
                // compute "from" fiber
                Fiber toFiber = toOpticalBundle.addFiberIfNecessary( mapping, "to_fiber_number", "to_fiber_color" );
                toFiber.cableConnection = connection;
                toFiber.opticalBundle = toOpticalBundle;
                connection.toFiber = toFiber;
            }
        }
        initialisationListener.onInitialiseComplete( true, null );
    }

    public static void initializePortConnectedItems( Port routedPort, List< HashMap< String, String > > list, SetOnInitialisationCompleteListener initialisationListener ){
        if( list != null ){
            routedPort.connectionSet.clear( );
            for( HashMap< String, String > mapping : list ){
                Connection connection = new Connection( );
                connection.fromPort = routedPort;

                routedPort.connectionSet.add( connection );
                String connectionType = mapping.get( "connection_type" );

                    if( connectionType.matches( "fiber_pin" ) ){
                        Cable toCable = new Cable( );
                        toCable.id = Integer.valueOf( mapping.get( "to_cable_id" ) );
                        toCable.cableType = mapping.get( "to_cable_type" );
                        toCable.cableAlias = mapping.get( "to_cable_alias" );

                        OpticalBundle toOpticalBundle = toCable.addBundleIfNecessary( mapping, "to_bundle_number", "to_bundle_color" );

                        Fiber toFiber = toOpticalBundle.addFiberIfNecessary( mapping, "to_fiber_number", "to_fiber_color" );

                        toOpticalBundle.cable = toCable;
                        toFiber.cableConnection = connection;
                        toFiber.opticalBundle = toOpticalBundle;
                        connection.toFiber = toFiber;
                    } else if( connectionType.matches( "port" ) ){
                        Port toPort = new Port( );
                        toPort.id = Integer.valueOf( mapping.get( "to_port_id" ) );
                        toPort.name = mapping.get( "to_port_name" );
                        if( mapping.get( "to_port_number" ) != null ){
                            toPort.portNumber = Integer.valueOf( mapping.get( "to_port_number" ) );
                        }

                        toPort.description = mapping.get( "to_port_description" );
                        connection.connectionDescription = mapping.get( "to_port_connection_description" );
                        connection.toPort = toPort;
                    }
            }
            initialisationListener.onInitialiseComplete( true, null );
        }
    }


    public static void initializeNetworkConnections( List< HashMap< String, String > > list, SetOnInitialisationCompleteListener initialisationCompleteListener ){
        if( list != null ){
            for( HashMap< String, String > mapping : list ){
                NetworkConnection networkConnection = new NetworkConnection();
                // common structure
                networkConnection.commonStructureId = Integer.valueOf( mapping.get( "commom_structure_id" ) );
                networkConnection.commonStructureType = mapping.get( "common_structure_type" );

                // object 1
                networkConnection.object1Id = Integer.valueOf( mapping.get( "object1_id" ) );
                networkConnection.object1Type = mapping.get( "object1_type" );
                networkConnection.object1OwnerId = Integer.valueOf( mapping.get( "object1_owner_id" ) );
                networkConnection.object1OwnerType = mapping.get( "object1_owner_type" );
                String obj1FiberDetail = mapping.get( "object1_fiber_detail" );
                if( obj1FiberDetail == null ){
                    obj1FiberDetail = "";
                }
                networkConnection.object1FiberDetail = obj1FiberDetail;
                // object 2
                networkConnection.object2Id = Integer.valueOf( mapping.get( "object2_id" ) );
                networkConnection.object2Type = mapping.get( "object2_type" );
                networkConnection.object2OwnerId = Integer.valueOf( mapping.get( "object2_owner_id" ) );
                networkConnection.object2OwnerType = mapping.get( "object2_owner_type" );
                String obj2FiberDetail = mapping.get( "object2_fiber_detail" );
                if( obj2FiberDetail == null ){
                    obj2FiberDetail = "";
                }
                networkConnection.object2FiberDetail = obj2FiberDetail;
                networkConnection.connectionDescription = mapping.get( "connection_description" );
                networkConnection.woAction = mapping.get( "action" );
                float lat = Float.valueOf( mapping.get( "common_structure_latitude" ) );
                float longitude = Float.valueOf( mapping.get( "common_structure_longitude" ) );
                networkConnection.commonStructureLocation = new LatLng( lat, longitude );
                PniDataStoreSingleton.instance.networkConnections.add( networkConnection );
            }
        }
        initialisationCompleteListener.onInitialiseComplete( true, null );
    }

    public static void initLogicalObjects( Object owner, List< HashMap< String, String > > list, SetOnInitialisationCompleteListener initialisationListener ){
        if( list != null ){
            for( HashMap< String, String > mapping : list ){
                switch( mapping.get( "logical_object_type" ) ){
                    case "circuit_location" :
                        // a circuit location could not be a cildren of another logical object, it's the "master" logical object
                        CircuitLocation cl = initCircuitLocation( owner, mapping );
                        if( owner instanceof AbstractSmallWorldObject ){
                            (( AbstractSmallWorldObject ) owner).addLogicalObject( cl );
                        }
                        break;
                    case "network_element" :
                        NetworkElement ne = initNetworkElement( owner, mapping );
                        if( owner instanceof LogicalObject ){
                            (( LogicalObject ) owner).addChildren( ne );
                        }
                        if( owner instanceof AbstractSmallWorldObject ){
                            (( AbstractSmallWorldObject ) owner).addLogicalObject( ne );
                        }
                        break;
                    case "service_port" :
                        ServicePort sp = initServicePort( owner, mapping );
                         if( owner instanceof LogicalObject ){
                            (( LogicalObject ) owner).addChildren( sp );
                        }
                        if( owner instanceof AbstractSmallWorldObject ){
                            (( AbstractSmallWorldObject ) owner).addLogicalObject( sp );
                        }
                        break;
                    case "circuit_issue":
                        CircuitIssue ci = initCircuitIssue( owner, mapping );
                        if( owner instanceof LogicalObject ){
                            (( LogicalObject ) owner).addChildren( ci );
                        }
                        if( owner instanceof AbstractSmallWorldObject ){
                            (( AbstractSmallWorldObject ) owner).addLogicalObject( ci );
                        }
                        break;
                    case "facility" :
                        Facility f = initFacility( owner, mapping );
                        if( owner instanceof LogicalObject ){
                            (( LogicalObject ) owner).addChildren( f );
                        }
                        if( owner instanceof AbstractSmallWorldObject ){
                            (( AbstractSmallWorldObject ) owner).addLogicalObject( f );
                        }
                        break;
                }
            }
        }
        if( owner instanceof AbstractSmallWorldObject ){
            initialisationListener.onLogicalObjectsInitialisationComplete( true );
        }

    }

    private static CircuitLocation initCircuitLocation( Object owner, HashMap< String, String > mapping ){
        CircuitLocation circuitLocation = new CircuitLocation();
        String circuitLocationId = mapping.get( "circuit_location_id" );
        if( circuitLocationId != null ){
            circuitLocation.id = Integer.valueOf( circuitLocationId );
        }
        String circuitLocationName = mapping.get( "circuit_location_name" );
        if( circuitLocationName != null ){
            circuitLocation.name = circuitLocationName;
        }
        String circuitLocationDescription = mapping.get( "circuit_location_description" );
        if( circuitLocationDescription != null ){
            circuitLocation.description = circuitLocationDescription;
        }
        String circuitLocationCustomer = mapping.get( "circuit_location_customer" );
        if( circuitLocationCustomer != null ){
            circuitLocation.customer = circuitLocationCustomer;
        }
        circuitLocation.owner = owner;
        return circuitLocation;
    }

    private static NetworkElement initNetworkElement( Object owner, HashMap< String, String > mapping ){
        NetworkElement ne = new NetworkElement();
        ne.id = Integer.valueOf( mapping.get( "network_element_id" ) );
        ne.name = mapping.get( "network_element_name" );
        ne.managerName = mapping.get( "network_element_manager_name" );
        ne.networkElementType = mapping.get( "network_element_ne_type" );
        ne.operationalStatus = mapping.get( "network_element_operational_status" );
        ne.owner = owner;
        return ne;
    }

    private static ServicePort initServicePort( Object owner, HashMap< String, String > mapping ){
        ServicePort sp = new ServicePort();
        sp.owner = owner;
        sp.id = Integer.valueOf( mapping.get( "service_port_id" ) );
        sp.name = mapping.get( "service_port_name" );
        sp.logicalName = mapping.get( "service_port_logical_name" );
        sp.transportLevel = mapping.get( "service_port_transport_level" );
        sp.serviceType = mapping.get( "service_port_service_type" );
        sp.portType = mapping.get( "service_port_port_type" );
        return sp;
    }

    private static Facility initFacility( Object owner, HashMap< String, String > mapping ){
        Facility facility = new Facility();
        String facilityId = mapping.get( "facility_id" );
        if( facilityId != null ){
            facility.id = Integer.valueOf( facilityId );
        }
        String facilityName = mapping.get( "facility_name" );
        if( facilityName != null ){
            facility.name = facilityName;
        }
        String facilityOwner = mapping.get( "facility_owner" );
        if( facilityOwner != null ){
            facility.ownerName = facilityOwner;
        }
        String facilityTransportMedium = mapping.get( "facility_transport_medium" );
        if( facilityOwner != null ){
            facility.transportMedium = facilityTransportMedium;
        }
        String facilityDatarate = mapping.get( "facility_datarate" );
        if( facilityDatarate != null ){
            facility.datarate = facilityDatarate;
        }
        facility.owner = owner;
         return facility;
    }

    private static CircuitIssue initCircuitIssue( Object owner,  HashMap< String, String > mapping ){
        CircuitIssue ci = new CircuitIssue();
        ci.id = Integer.valueOf( mapping.get( "circuit_issue_id" ) );
        ci.name = mapping.get( "circuit_issue_name" );
        ci.transportLevel = mapping.get( "circuit_issue_transport_level" );
        ci.serviceType = mapping.get( "circuit_issue_service_type" );
        ci.owner = owner;
        return ci;
    }
}
