package rvo.mobilegateway.physical_datamodel;

import java.util.regex.Pattern;

/**
 * Created by Robert on 9/18/2015.
 */
public class Design {

    public int id;
    public String name;
    public String owner;
    public String status;

    public Design(){

    }

    @Override
    public String toString( ){
        return name + " " + this.owner;

    }
}
