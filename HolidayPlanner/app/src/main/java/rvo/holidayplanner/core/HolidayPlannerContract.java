package rvo.holidayplanner.core;


import android.provider.BaseColumns;

/**
 * Created by Robert on 7/14/2015.
 */
public final class HolidayPlannerContract {

    public static final String TEXT_TYPE = "TEXT";
    public static final String REAL_TYPE = "REAL";

    public static final String SQL_CREATE_ROUTE_TABLE = "CREATE TABLE " + HolidayPlannerRouteTable.TABLE_NAME + " ( " +
            HolidayPlannerRouteTable._ID + " INTEGER PRIMARY KEY, " + HolidayPlannerRouteTable.COLUMN_NAME_ROUTE_ID + TEXT_TYPE + " , " +
            HolidayPlannerRouteTable.COLUMN_NAME_ROUTE_NAME + TEXT_TYPE + " , " +
            HolidayPlannerRouteTable.COLUMN_NAME_START_LATITUDE + REAL_TYPE + " , " +
            HolidayPlannerRouteTable.COLUMN_NAME_START_LONGITUDE + REAL_TYPE + " , " +
            HolidayPlannerRouteTable.COLUMN_NAME_END_LATITUDE + REAL_TYPE + " , " +
            HolidayPlannerRouteTable.COLUMN_NAME_END_LONGITUDE + REAL_TYPE + " )";

    public static final String SQL_CREATE_ROUTE_SECTION_TABLE = "";

    public HolidayPlannerContract(){

    }

    public static abstract class HolidayPlannerRouteTable implements BaseColumns{
        public static final String TABLE_NAME = "route";
        public static final String COLUMN_NAME_ROUTE_ID = "routeID";
        public static final String COLUMN_NAME_ROUTE_NAME = "routeName";
        public static final String COLUMN_NAME_START_LATITUDE = "startLatitude";
        public static final String COLUMN_NAME_START_LONGITUDE = "startLongitude";
        public static final String COLUMN_NAME_END_LATITUDE = "endLatitude";
        public static final String COLUMN_NAME_END_LONGITUDE = "endLongitude";
    }

    public static abstract class HolidayPlannerRouteSectionTable implements BaseColumns{
        public static final String TABLE_NAME = "routeSection";
        public static final String COLUMN_NAME_ROUTE_SECTION_ID = "routeSectionID";
        public static final String COLUMN_NAME_START_LATITUDE = "startLatitude";
        public static final String COLUMN_NAME_START_LONGITUDE = "startLongitude";
        public static final String COLUMN_NAME_END_LATITUDE = "endLatitude";
        public static final String COLUMN_NAME_END_LONGITUDE = "endLongitude";
        public static final String COLUMN_NAME_ROUTE_ID_FK = "routeIDFK";
    }


}
