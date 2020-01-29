package rvo.mobilegateway.physical_datamodel;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Robert on 9/24/2015.
 */
public class NetworkConnection {

    public int commonStructureId;
    public String commonStructureType;

    public int object1Id;
    public String object1Type;
    public int object1OwnerId;
    public String object1OwnerType;

    public int object2Id;
    public String object2Type;
    public int object2OwnerId;
    public String object2OwnerType;

    public String connectionDescription;
    public String woAction;
    public LatLng commonStructureLocation;

    public String object1FiberDetail;
    public String object2FiberDetail;

    public NetworkConnection(){

    }


    @Override
    public String toString( ){
        return connectionDescription;
    }


}
