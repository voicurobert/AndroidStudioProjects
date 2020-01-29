package rvo.holidayplanner.core;

import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Robert on 7/6/2015.
 */
public class RouteSection {
    private RouteLocation startRouteLocation = null;
    private RouteLocation endRouteLocation = null;
    private Polyline route = null;

    public RouteSection(){

    }

    public RouteSection( RouteLocation startRouteLocation, RouteLocation endRouteLocation){
        this.startRouteLocation = startRouteLocation;
        this.endRouteLocation = endRouteLocation;
    }

    public void setStartRouteLocation(RouteLocation start) {
        this.startRouteLocation = start;
    }

    public void setEndRouteLocation(RouteLocation end) {
        this.endRouteLocation = end;
    }

    public RouteLocation getStartRouteLocation() {
        return startRouteLocation;
    }

    public RouteLocation getEndRouteLocation() {
        return endRouteLocation;
    }

    public boolean isSameSectionAs( RouteSection routeSection ){
        if( this.startRouteLocation.isSameLocationAs( routeSection.getStartRouteLocation() ) ||
                this.endRouteLocation.isSameLocationAs( routeSection.getEndRouteLocation() )){
            return true;
        }
        return false;
    }
}
