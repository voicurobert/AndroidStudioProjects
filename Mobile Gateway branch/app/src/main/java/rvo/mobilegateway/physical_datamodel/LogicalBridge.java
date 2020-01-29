package rvo.mobilegateway.physical_datamodel;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rvo.mobilegateway.logical_datamodel.LogicalObject;

/**
 * Created by Robert on 10/21/2015.
 */
public abstract class LogicalBridge{
    public List< Object > logicalObjects = new ArrayList<>(  );

    public abstract List< Object > getLogicalObjects( );

    public void addLogicalObject( LogicalObject object ){
        if( !logicalObjects.contains( object ) ){
            logicalObjects.add( object );
        }
    }
}
