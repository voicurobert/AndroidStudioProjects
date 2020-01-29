package rvo.mobilegateway.physical_datamodel;

import rvo.mobilegateway.L;

/**
 * Created by Robert on 8/25/2015.
 */
public class Connection {

    public Fiber fromFiber;
    public Port fromPort;
    public Fiber toFiber;
    public Port toPort;
    public String connectionDescription;

    public boolean isConnectedToFiber( ){
        if( toFiber == null ){
            return false;
        } else{
            return true;
        }
    }

    public String connectionDescription( ){
        String description = "";
        if( toPort != null ){
            description = description + " => " + "[ Jumper ] " + toPort.name + " " + connectionDescription;
        } else if( toFiber != null ){
            description = description + " => " + toFiber.toString( ) + " "
                    + toFiber.opticalBundle.toString( )
                    + " " + toFiber.opticalBundle.cable.computeName( );
        }
        return description;
    }


    @Override
    public boolean equals( Object o ){
        /*
        if( o == null ){
            return false;
        }else{
            if( this.fromPort.equals( ((Connection)o).toPort ) ||  ){

            }
        }
        */
        return super.equals( o );
    }
}
