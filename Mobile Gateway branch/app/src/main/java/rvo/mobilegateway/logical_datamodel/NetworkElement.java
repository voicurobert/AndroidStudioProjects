package rvo.mobilegateway.logical_datamodel;

/**
 * Created by Robert on 10/20/2015.
 */
public class NetworkElement extends LogicalObject{
    public static String COLLECTION_NAME = "cit_network_element";
    public String name;
    public String managerName;
    public String networkElementType;
    public String operationalStatus;
    public Object owner;


    public NetworkElement(){

    }

    @Override
    public String getCollectionName( ) {
        return COLLECTION_NAME;
    }

    @Override
    public String toString( ) {
        return "[NE] " + name + ", " + networkElementType ;
    }
}
