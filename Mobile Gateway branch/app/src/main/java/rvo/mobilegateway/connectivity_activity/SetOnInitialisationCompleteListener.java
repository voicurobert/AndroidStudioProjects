package rvo.mobilegateway.connectivity_activity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 11/13/2015.
 */
public interface SetOnInitialisationCompleteListener{

    void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result );
    void onEquipmentsInitialisationComplete( boolean initialised );
    void onConnectedCablesInitialisationComplete( boolean initialised );
    void onLogicalObjectsInitialisationComplete( boolean initialised );
    void onPortsInitialisationComplete( boolean initialised );
}
