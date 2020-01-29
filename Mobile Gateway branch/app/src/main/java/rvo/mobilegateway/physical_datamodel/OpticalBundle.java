package rvo.mobilegateway.physical_datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import rvo.mobilegateway.engines.PniDataStoreSingleton;

/**
 * Created by Robert on 8/14/2015.
 */
public class OpticalBundle implements Comparable<OpticalBundle>{

    public int bundleNumber = 0;
    public String bundleColor = null;
    public List<Fiber> fibers;
    public Cable cable = null;

    public OpticalBundle(int bundleNumber, String bundleColor){
        this.bundleColor = bundleColor;
        this.bundleNumber = bundleNumber;
    }


    @Override
    public String toString() {
        return bundleColor + " #" + String.valueOf( bundleNumber );
    }


    public Fiber addFiberIfNecessary( HashMap< String, String > mapping, String numberKey, String colorKey ){
        Integer fiberNumber;
        if( mapping.containsKey( numberKey ) ){
            fiberNumber = Integer.valueOf( mapping.get( numberKey ) );
        } else{
            return null;
        }
        String fiberColor = mapping.get( colorKey );
        if( fiberColor == null ){
            return null;
        }
        Fiber fiber = getFiberByColorAndNumber( fiberNumber, fiberColor );
        if( fiber == null ){
            fiber = new Fiber( fiberNumber, fiberColor );
        }
        addFiber( fiber );
        return fiber;
    }

    private Fiber getFiberByColorAndNumber( int fiberNumber, String fiberColor ){
        if( fibers == null  ){
            return null;
        }
        for( Fiber fiber : fibers ){
            if( fiber.fiberColor.matches( fiberColor ) && fiber.fiberNumber == fiberNumber ){
                return fiber;
            }
        }
        return null;
    }

    private boolean addFiber( Fiber fiber ){
        if( fibers == null ){
            fibers = new ArrayList<>(  );
        }

        if( !fibers.contains( fiber ) ){
            fibers.add( fiber );
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if( o == null ){
            return false;
        }else{
            if( !(o instanceof OpticalBundle) ){
                return false;
            }else{
                if( this.bundleNumber == ((OpticalBundle)o).bundleNumber && this.bundleColor.equals( ((OpticalBundle)o).bundleColor )){
                    return true;
                }
            }

        }
        return false;
    }

    @Override
    public int compareTo(OpticalBundle another) {
        if( this.bundleNumber < another.bundleNumber ){
            return -1;
        }else{
            return 1;
        }
    }
}
