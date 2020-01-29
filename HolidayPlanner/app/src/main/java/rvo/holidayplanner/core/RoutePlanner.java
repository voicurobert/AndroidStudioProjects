package rvo.holidayplanner.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 7/6/2015.
 */
public class RoutePlanner {
    private String routeName = "";
    private RouteLocation startRouteLocation = null;
    private RouteLocation endRouteLocation = null;
    private List< RouteSection > sections = new ArrayList<>();
    private boolean isRouteActive = false;

    public RoutePlanner(){

    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setStartRouteLocation(RouteLocation startRouteLocation) {
        this.startRouteLocation = startRouteLocation;
    }

    public void setEndRouteLocation(RouteLocation endRouteLocation) {
        this.endRouteLocation = endRouteLocation;
    }

    public void setSections(List<RouteSection> sections) {
        this.sections = sections;
    }

    public String getRouteName() {
        return routeName;
    }

    public RouteLocation getStartRouteLocation() {
        return startRouteLocation;
    }

    public RouteLocation getEndRouteLocation() {
        return endRouteLocation;
    }

    public List<RouteSection> getSections() {
        return sections;
    }


    public void addSection( RouteSection section ){

        for( RouteSection rs : sections ) {
            if( !rs.isSameSectionAs( section ) ){
                sections.add( section );
            }
        }
    }

    public boolean isRouteActive() {
        return isRouteActive;
    }

    public void setIsRouteActive(boolean isRouteActive) {
        this.isRouteActive = isRouteActive;
    }
}
