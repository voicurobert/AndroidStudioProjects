package rvo.mobilegateway.logical_datamodel;

/**
 * Created by Robert on 10/22/2015.
 */
public class ServicePort extends LogicalObject{

    public static String COLLECTION_NAME = "cit_service_port";
    public String name;
    public String logicalName;
    public String portType;
    public String transportLevel;
    public String serviceType;
    public Object owner;


    public ServicePort(){

    }

    @Override
    public String getCollectionName( ) {
        return COLLECTION_NAME;
    }

    @Override
    public String toString( ) {
        return "[SP] " + name + " | " + transportLevel + " | " + serviceType;
    }
}
