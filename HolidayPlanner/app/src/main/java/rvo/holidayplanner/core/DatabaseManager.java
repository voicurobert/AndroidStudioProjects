package rvo.holidayplanner.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Robert on 7/14/2015.
 */

public class DatabaseManager extends SQLiteOpenHelper {


    private static final HolidayPlannerContract contract = new HolidayPlannerContract();
    private static final String DATABASE_NAME = "holidayPlanner.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseManager( Context context ){
        super( context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( contract.SQL_CREATE_ROUTE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
