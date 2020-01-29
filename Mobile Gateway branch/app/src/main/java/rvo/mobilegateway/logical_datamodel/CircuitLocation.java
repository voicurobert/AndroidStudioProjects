package rvo.mobilegateway.logical_datamodel;



/**
 * Created by Robert on 10/14/2015.
 */
public class CircuitLocation extends LogicalObject{
    public static String COLLECTION_NAME = "cit_circuit_location";
    public String name;
    public String description;
    public String customer;
    public Object owner;

    public CircuitLocation(){

    }

    @Override
    public String getCollectionName( ) {
        return COLLECTION_NAME;
    }

    @Override
    public String toString( ) {
        if( customer != null ){
            return "[CL] " + description + ", " + name + " - " + customer;
        }else{
            return "[CL] " + description + ", " + name;
        }
    }




}
