package rvo.mobilegateway.logical_datamodel;

/**
 * Created by Robert on 10/13/2015.
 */
public class Facility extends LogicalObject{

    public String name;
    public String ownerName;
    public String transportMedium;
    public String datarate;
    public Object owner;

    public Facility(){

    }

    @Override
    public String getCollectionName( ) {
        return "cit_facility";
    }


    @Override
    public String toString( ){
        return "[F] " + name;
    }

    public String connectionDescription(){
        return " => " + this.toString();

    }
}
