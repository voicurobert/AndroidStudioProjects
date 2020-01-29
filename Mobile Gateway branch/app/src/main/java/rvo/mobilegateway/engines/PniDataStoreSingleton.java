package rvo.mobilegateway.engines;



import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.AccessPoint;
import rvo.mobilegateway.physical_datamodel.AerialRoute;
import rvo.mobilegateway.physical_datamodel.Building;
import rvo.mobilegateway.physical_datamodel.Design;
import rvo.mobilegateway.physical_datamodel.NetworkConnection;
import rvo.mobilegateway.physical_datamodel.Cable;
import rvo.mobilegateway.physical_datamodel.Hub;
import rvo.mobilegateway.physical_datamodel.Pole;
import rvo.mobilegateway.physical_datamodel.TerminalEnclosure;
import rvo.mobilegateway.physical_datamodel.UndergroundRoute;
import rvo.mobilegateway.physical_datamodel.UndergroundUtilityBox;

public class PniDataStoreSingleton {

    public static final PniDataStoreSingleton instance = new PniDataStoreSingleton( );

    public Set< UndergroundUtilityBox > uubs = new LinkedHashSet<  >();
    public Set< UndergroundRoute > undergroundRoutes = new LinkedHashSet<>( );
    public Set< Hub > mitHubs = new LinkedHashSet<>( );
    public Set< Building > buildings = new LinkedHashSet<>( );
    public Set< TerminalEnclosure > terminalEnclosures = new LinkedHashSet<>( );
    public Set< Pole > poles = new LinkedHashSet<>( );
    public Set< AccessPoint > accessPoints = new LinkedHashSet<>( );
    public Set< AerialRoute > aerialRoutes = new LinkedHashSet<>( );
    public List< Cable > cables = new ArrayList<>( );
    public Set< Design > designSet = new LinkedHashSet<>(  );
    public Set< NetworkConnection > networkConnections = new LinkedHashSet<>(  );

    public PniDataStoreSingleton( ){

    }


    public UndergroundUtilityBox getUubByLocation( LatLng location ){
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.location.equals( location ) ){
                return uub;
            }
        }

        return null;
    }

    public UndergroundUtilityBox getUubByMarkerId( String markerId ){
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.marker.getId().equals( markerId ) ){
                return uub;
            }
        }

        return null;
    }

    public UndergroundRoute getUndergroundRouteById( int id ){
        for( UndergroundRoute aUR : undergroundRoutes ){
            if( aUR.id == id ){
                return aUR;
            }
        }

        return null;
    }

    public AerialRoute getAerialRouteById( int id ){
        for( AerialRoute aR : aerialRoutes ){
            if( aR.id == id ){
                return aR;
            }
        }

        return null;
    }


    public Hub getHubByLocation( LatLng location ){
        for( Hub hub : mitHubs ){
            if( hub.location.equals( location ) ){
                return hub;
            }
        }

        return null;
    }

    public Hub getHubByMarkerId( String markerId ){
        for( Hub hub : mitHubs ){
            if( hub.marker.getId( ).equals( markerId ) ){
                return hub;
            }
        }

        return null;
    }

    public Pole getPoleByLocation( LatLng location ){
        for( Pole pole : poles ){
            if( pole.location.equals( location ) ){
                return pole;
            }
        }

        return null;
    }

    public Pole getPoleByMarkerId( String markerId ){
        for( Pole pole : poles ){
            if( pole.marker.getId().equals( markerId ) ){
                return pole;
            }
        }

        return null;
    }

    public AccessPoint getAccessPointByLocation( LatLng location ){
        for( AccessPoint ap : accessPoints ){
            if( ap.location.equals( location ) ){
                return ap;
            }
        }

        return null;
    }

    public AccessPoint getAccessPointByMarkerId( String markerId ){
        for( AccessPoint ap : accessPoints ){
            if( ap.marker.getId().equals( markerId ) ){
                return ap;
            }
        }

        return null;
    }


    public Building getBuildingByLocation( LatLng location ){
        for( Building b : buildings ){
            if( b.location.equals( location ) ){
                return b;
            }
        }

        return null;
    }

    public Building getBuildingByMarkerId( String markerId ){
        for( Building b : buildings ){
            if( b.marker.getId().equals( markerId ) ){
                return b;
            }
        }

        return null;
    }

    public TerminalEnclosure getTerminalEnclosureByLocation( LatLng location ){
        for( TerminalEnclosure te : terminalEnclosures ){
            if( te.location.equals( location ) ){
                return te;
            }
        }

        return null;
    }


    public TerminalEnclosure getTerminalEnclosureByMarkerId( String markerId ){
        for( TerminalEnclosure te : terminalEnclosures ){
            if( te.marker.getId().equals( markerId ) ){
                return te;
            }
        }

        return null;
    }

    public Cable getCableById( int id ){
        for( Cable cable : cables ){
            if( cable.id == id ){
                return cable;
            }
        }
        return null;
    }

    public AbstractTelcoObject getObjectFromMarkerPosition( LatLng position ){
        AbstractTelcoObject o = null;
        for( TerminalEnclosure te : terminalEnclosures ){
            if( te.marker.getPosition( ).equals( position ) ){
                o = te;
            }
        }
        for( Building b : buildings ){
            if( b.marker.getPosition( ).equals( position ) ){
                o = b;
            }
        }

        for( AccessPoint ap : accessPoints ){
            if( ap.marker.getPosition( ).equals( position ) ){
                o = ap;
            }
        }
        for( Pole pole : poles ){
            if( pole.marker.getPosition( ).equals( position ) ){
                o = pole;
            }
        }
        for( Hub hub : mitHubs ){
            if( hub.marker.getPosition( ).equals( position ) ){
                o = hub;
            }
        }
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.marker.getPosition( ).equals( position ) ){
                o = uub;
            }
        }
        return o;
    }

    public Object getObjectById( int id ){
        Object o = null;
        for( TerminalEnclosure te : terminalEnclosures ){
            if( te.id == id ){
                o = te;
            }
        }
        for( Building b : buildings ){
            if( b.id == id ){
                o = b;
            }
        }

        for( AccessPoint ap : accessPoints ){
            if( ap.id == id ){
                o = ap;
            }
        }
        for( Pole pole : poles ){
            if( pole.id == id ){
                o = pole;
            }
        }
        for( Hub hub : mitHubs ){
            if( hub.id == id ){
                o = hub;
            }
        }
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.id == id ){
                o = uub;
            }
        }
        return o;
    }

    public LatLng getLocationForObject( Object o ){
        LatLng location = null;
        for( TerminalEnclosure te : terminalEnclosures ){
            if( te.equals( o ) ){
                location = te.location;
            }
        }
        for( Building b : buildings ){
            if( b.equals( o ) ){
                location = b.location;
            }
        }

        for( AccessPoint ap : accessPoints ){
            if( ap.equals( o ) ){
                location = ap.location;
            }
        }
        for( Pole pole : poles ){
            if( pole.equals( o ) ){
                location = pole.location;
            }
        }
        for( Hub hub : mitHubs ){
            if( hub.equals( o ) ){
                location = hub.location;
            }
        }
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.equals( o ) ){
                location = uub.location;
            }
        }
        return location;
    }

    public List< String > designsAsStringList(){
        List< String > designs = new ArrayList<>(  );
        for( Design d : designSet ){
            designs.add( d.name );
        }
        return designs;
    }

    public List< String > tasksAsStringList(){

        List< String > networkConnectionsStrings = new ArrayList<>(  );
        for( NetworkConnection nc : networkConnections ){
            networkConnectionsStrings.add( nc.connectionDescription );
        }
        return networkConnectionsStrings;
    }

    public Design getDesignByDesignName( String designName ){
        for( Design d : designSet ){
            if( d.name.contentEquals( designName ) ){
                return d;
            }
        }
        return null;
    }

    public NetworkConnection getNetworkConnectionByConnectionDescription( String description ){
        for( NetworkConnection nc : networkConnections ){
            if( nc.connectionDescription.contentEquals( description ) ){
                return nc;
            }
        }
        return null;
    }

    public AbstractTelcoObject getRouteObjectByPolyline( Polyline line ){
        for( UndergroundRoute ur : undergroundRoutes){
            if( ur.route.equals( line ) ){
                return ur;
            }
        }
        for( AerialRoute ar : aerialRoutes ){
            if( ar.route.equals( line ) ){
                return ar;
            }
        }
        return null;
    }

    public void clearAll( ){
        uubs.clear( );
        aerialRoutes.clear( );
        mitHubs.clear( );
        poles.clear( );
        undergroundRoutes.clear( );
        accessPoints.clear( );
        buildings.clear( );
        terminalEnclosures.clear( );
        cables.clear();
    }

    public void clearPassingThroughCables( ){
        try{
            for( Cable cable : cables ){
                if( cable.isPassingThrough ){
                    cables.remove( cable );
                }
            }
        }catch( ConcurrentModificationException e ){
        }
    }
}
