package rvo.mobilegateway.logical_datamodel;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.engines.SmallworldServicesDelegate;
import rvo.mobilegateway.login_activity.LoginAuthentificationHandlerContext;

/**
 * Created by Robert on 10/21/2015.
 */
public abstract class LogicalObject{

    public int id;
    public List< LogicalObject > childrens = null;

    public LogicalObject(){

    }


    public List< LogicalObject > getChildrens( ) {
        return childrens;
    }

    public abstract String getCollectionName();


    public void addChildren( LogicalObject logicalChildren ){
        if( childrens == null ){
            childrens = new ArrayList<>(  );
        }
        if( !childrens.contains( logicalChildren ) ){
            childrens.add( logicalChildren );
        }
    }

}
