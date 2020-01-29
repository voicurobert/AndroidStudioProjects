package rvo.mobilegateway.engines;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;
import rvo.mobilegateway.physical_datamodel.NetworkConnection;


/**
 *
 */
public final class ConnectivityEngine {


   private ConnectivityEngine( ){

   }

    /**
     *
     * @param id
     * @return
     */
    public static boolean objectIdIsCommonStructureForNetworkConnection( int id ){
        for( NetworkConnection nc : PniDataStoreSingleton.instance.networkConnections ){
            if( nc.commonStructureId == id ){
                return true;
            }
        }
        return false;
    }

    public static boolean objectIdBelongsToNetworkConnections( int id ){
        for( NetworkConnection nc : PniDataStoreSingleton.instance.networkConnections ){
            if( nc.object1Id == id | nc.object2Id == id ){
                return true;
            }
        }
        return false;
    }

    public static List< Object > networkConnectionDetailsForId( int id ){
        List< Object > details = new ArrayList<>(  );
        for( NetworkConnection nc : PniDataStoreSingleton.instance.networkConnections ){
            if( nc.object1Id == id | nc.object1OwnerId == id | nc.object2Id == id | nc.object2OwnerId == id ){
                details.add( true );
                if( nc.woAction.contentEquals( "wiring" ) ){
                    details.add( Color.rgb( 10, 110, 10 ) ); // a dark green color
                    return details;
                }else if( nc.woAction.contentEquals( "unwiring" ) ){
                    details.add( Color.RED );
                    return details;
                }
            }

        }
        details.add( false );
        return details;
    }



    public static List< Object > fiberConnectionDetailsForId( String fiberNumber ){
        List< Object > details = new ArrayList<>(  );
        for( NetworkConnection nc : PniDataStoreSingleton.instance.networkConnections ){
            if( nc.object1FiberDetail.contentEquals( fiberNumber ) | nc.object2FiberDetail.contentEquals( fiberNumber ) ){
                details.add( true );
                if( nc.woAction.contentEquals( "wiring" ) ){
                    details.add( Color.rgb( 10, 150, 10 ) ); // a dark green color
                    return details;
                }else if( nc.woAction.contentEquals( "unwiring" ) ){
                    details.add( Color.rgb( 200, 10, 10 ) );
                    return details;
                }
            }
        }
        details.add( false );
        return details;
    }

}
