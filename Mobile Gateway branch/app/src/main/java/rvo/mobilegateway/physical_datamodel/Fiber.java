package rvo.mobilegateway.physical_datamodel;

/**
 * Created by Robert on 8/14/2015.
 */
public class Fiber implements Comparable<Fiber>{

    public int fiberNumber = 0;
    public String fiberColor = null;
    public Connection cableConnection = null;
    public OpticalBundle opticalBundle = null;

    public Fiber(){

    }

    public Fiber( int fiberNumber, String fiberColor ){
        this.fiberColor = fiberColor;
        this.fiberNumber = fiberNumber;
    }



    @Override
    public String toString() {
        return fiberColor + " #" + String.valueOf( fiberNumber );
    }


    public String computeConnectionInfo(){
        String fromFiberInfo = this.toString();
        String toCableInfo = " ";
        String toBundleInfo = " ";
        String toFiberInfo = " ";
        Connection connection = this.cableConnection;
        if( connection == null ){
            return fromFiberInfo;
        }
        Fiber toFiber = this.cableConnection.toFiber;
        Port port = this.cableConnection.toPort;
        if( toFiber != null ){
            toFiberInfo = toFiberInfo + toFiber.toString();
            OpticalBundle toOpticalBundle = toFiber.opticalBundle;
            toBundleInfo = toBundleInfo + toOpticalBundle.toString();
            Cable toCable = toOpticalBundle.cable;
            toCableInfo = toCableInfo + toCable.computeName();
            return fromFiberInfo + " => " + toFiberInfo + toBundleInfo + toCableInfo;
        }else if( port != null ){
            return fromFiberInfo + " => " + this.cableConnection.connectionDescription;
        }
        return fromFiberInfo;
    }

    @Override
    public boolean equals(Object o) {
        if( o == null ){
            return false;
        }else{
            if( this.fiberNumber == ((Fiber)o).fiberNumber & this.fiberColor.equals( ((Fiber)o).fiberColor )){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Fiber anotherFiber) {
        if( this.fiberNumber < anotherFiber.fiberNumber ){
            return -1;
        }else{
            return 1;
        }
    }
}
