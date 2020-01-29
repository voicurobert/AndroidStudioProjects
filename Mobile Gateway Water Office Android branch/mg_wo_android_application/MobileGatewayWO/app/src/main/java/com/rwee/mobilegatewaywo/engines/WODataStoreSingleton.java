package com.rwee.mobilegatewaywo.engines;


import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.rwee.mobilegatewaywo.wo_datamodel.AbstractWaterOfficeObject;
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
import java.util.LinkedHashSet;
import java.util.Set;

public class WODataStoreSingleton{

    public static final WODataStoreSingleton instance = new WODataStoreSingleton( );

    public Set< WDConnectionPipe > wdConnectionPipeSet = new LinkedHashSet<>(  );
    public Set< WDManhole > wdManholeSet = new LinkedHashSet<>(  );
    public Set< WDServiceConnection > wdServiceConnectionSet = new LinkedHashSet<>(  );
    public Set< WDSewerSection > wdSewerSectionSet = new LinkedHashSet<>(  );
    public Set< WSHydrant > wsHydrantSet = new LinkedHashSet<>(  );
    public Set< WSMainSection > wsMainSectionSet = new LinkedHashSet<>(  );
    public Set< WSServicePoint > wsServicePointSet = new LinkedHashSet<>(  );
    public Set< WSStation > wsStationSet = new LinkedHashSet<>(  );
    public Set< WSTank > wsTankSet = new LinkedHashSet<>(  );
    public Set< WSTee > wsTeeSet = new LinkedHashSet<>(  );
    public Set< WSTreatmentPlant > wsTreatmentPlantSet = new LinkedHashSet<>(  );
    public Set< WSValve > wsValveSet = new LinkedHashSet<>(  );


    public WODataStoreSingleton( ){

    }


    /*
    public UndergroundUtilityBox getUubByLocation( LatLng location ){
        for( UndergroundUtilityBox uub : uubs ){
            if( uub.location.equals( location ) ){
                return uub;
            }
        }

        return null;
    }
    */
    public WDManhole getWdManholeByLocation( LatLng location ){
        for( WDManhole manhole : wdManholeSet ){
            if( manhole.woLocation.marker.getPosition().equals( location ) ){
                return manhole;
            }
        }

        return null;
    }


    public WDServiceConnection getWdServiceConnectionByLocation( LatLng location ){
        for( WDServiceConnection serviceConnection : wdServiceConnectionSet ){
            if( serviceConnection.woLocation.marker.getPosition().equals( location ) ){
                return serviceConnection;
            }
        }
        return null;
    }

    public WSHydrant getWsHydrantByLocation( LatLng location ){
        for( WSHydrant hydrant : wsHydrantSet ){
            if( hydrant.woLocation.marker.getPosition().equals( location ) ){
                return hydrant;
            }
        }
        return null;
    }

    public WSServicePoint getWsServicePointByLocation( LatLng location ){
        for( WSServicePoint servicePoint : wsServicePointSet ){
            if( servicePoint.woLocation.marker.getPosition().equals( location ) ){
                return servicePoint;
            }
        }
        return null;
    }

    public WSTee getWsTeeByLocation( LatLng location ){
        for( WSTee tee : wsTeeSet ){
            if( tee.woLocation.marker.getPosition().equals( location ) ){
                return tee;
            }
        }
        return null;
    }

    public WSValve getWsValveByLocation( LatLng location ){
        for( WSValve valve : wsValveSet ){
            if( valve.woLocation.marker.getPosition().equals( location ) ){
                return valve;
            }
        }
        return null;
    }
    /*
    public Object getObjectFromMarkerPosition( LatLng position ){
        Object o = null;
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

    */

    public AbstractWaterOfficeObject getWoRouteObjectByPolyline( Polyline polyline ){
        for( WDConnectionPipe connectionPipe : wdConnectionPipeSet ){
            if( connectionPipe.woRoute.line.equals( polyline ) ){
                return connectionPipe;
            }
        }

        for( WDSewerSection sewerSection : wdSewerSectionSet ){
            if( sewerSection.woRoute.line.equals( polyline ) ){
                return sewerSection;
            }
        }
        for( WSMainSection mainSection : wsMainSectionSet ){
            if( mainSection.woRoute.line.equals( polyline ) ){
                return mainSection;
            }
        }

        return null;
    }

    public AbstractWaterOfficeObject getWoExtentObjectByPolygon( Polygon polygon ){
        for( WSStation station : wsStationSet ){
            if( station.woExtent.polygon.equals( polygon ) ){
                return station;
            }
        }

        for( WSTank tank : wsTankSet ){
            if( tank.woExtent.polygon.equals( polygon )){
                return tank;
            }
        }

        for( WSTreatmentPlant treatmentPlant : wsTreatmentPlantSet ){
            if( treatmentPlant.woExtent.polygon.equals( polygon ) ){
                return treatmentPlant;
            }
        }
        return null;
    }

    public void clearAll(){
        wdServiceConnectionSet.clear();
        wdManholeSet.clear();
        wdSewerSectionSet.clear();
        wdConnectionPipeSet.clear();
        wsHydrantSet.clear();
        wsMainSectionSet.clear();
        wsServicePointSet.clear();
        wsStationSet.clear();
        wsTankSet.clear();
        wsTreatmentPlantSet.clear();
        wsValveSet.clear();
        wsTeeSet.clear();
    }


}
