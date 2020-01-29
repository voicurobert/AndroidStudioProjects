package rvo.mobilegateway.engines;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 11/16/2015.
 */
public interface SetOnResultAvailableListener {
    void onResultAvailable( List< HashMap< String, String > > result );

}
