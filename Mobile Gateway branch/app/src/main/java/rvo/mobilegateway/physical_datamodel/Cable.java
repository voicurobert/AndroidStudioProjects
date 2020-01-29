package rvo.mobilegateway.physical_datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 8/10/2015.
 */
public class Cable implements Comparable{
    public static String CABLE_TYPE = "Sheath";
    public static String SHEATH_COLLECTION_NAME = "sheath";
    public int id;
    public String cableType;
    public String cableName;
    public String cableAlias;
    public String routeType;
    public String specificationName;
    public List< OpticalBundle > bundles;
    public boolean isPassingThrough = false;
    public EquipmentObject equipment;
    public String[] aerialRouteIds;
    public String[] undergroundRouteIds;

    public Cable(){

    }


    public String computeName(){

        String cableType = "";
        if( this.cableType.equals( "sheath_cable" )){
            cableType = this.cableType + "Sheath";
        }
        String name = null;
        if( cableAlias == null ){
            name = cableName;
        }else{
            name = cableAlias;
        }
        String orientation = getOrientation();
        if( orientation != null ){
            return getOrientation().toUpperCase() + " - " + name;
        }else{
            return name;
        }

    }

    public OpticalBundle addBundleIfNecessary( HashMap< String, String > mapping, String numberKey, String colorKey ){
        Integer bundleNumber;
        if( mapping.containsKey( numberKey ) ){
            bundleNumber = Integer.valueOf( mapping.get( numberKey ) );
        } else{
            return null;
        }

        String bundleColor = mapping.get( colorKey );
        if( bundleColor == null ){
            return null;
        }
        OpticalBundle opticalBundle = getBundleByColorAndNumber( bundleNumber, bundleColor );
        if( opticalBundle == null ){
            opticalBundle = new OpticalBundle( bundleNumber, bundleColor );
        }
        addBundle( opticalBundle );
        return opticalBundle;
    }

    private OpticalBundle getBundleByColorAndNumber( int bundleNumber, String bundleColor ){
        if( bundles == null ){
            return null;
        }
        for( OpticalBundle bundle : bundles ){
            if( ( bundle.bundleNumber == bundleNumber ) && ( bundle.bundleColor.equals( bundleColor ) ) ){
                return bundle;
            }
        }
        return null;
    }

    private boolean addBundle( OpticalBundle opticalBundle ){
        if( bundles == null ){
            bundles = new ArrayList<>(  );
        }

        if( !bundles.contains( opticalBundle ) ){
            bundles.add( opticalBundle );
            return true;
        }else{
            return false;
        }
    }

    public String getOrientation(){
        String orientation = null ;
        if( equipment != null ){
            if( equipment.inCables.contains( this )){
                orientation = "in";
            }else{
                orientation = "out";
            }
        }
        return orientation;
    }


    @Override
    public boolean equals( Object o ){
        if( this.id == ( ( Cable )o).id ) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo( Object another ){
        int returnInt = 0;
        String anotherAlias = ( ( Cable )another) .cableAlias;
        if( this.cableAlias != null && ( ( Cable )another).cableAlias != null ){
            returnInt = this.cableAlias.compareTo( anotherAlias );
        }else{
            returnInt = this.cableName.compareTo( ( (Cable)another ).cableName );
        }
        return returnInt;
    }
}
