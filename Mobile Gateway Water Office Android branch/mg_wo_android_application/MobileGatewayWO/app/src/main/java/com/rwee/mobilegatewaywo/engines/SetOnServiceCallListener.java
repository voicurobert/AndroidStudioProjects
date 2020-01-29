package com.rwee.mobilegatewaywo.engines;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.rwee.mobilegatewaywo.connectivity_activity.SetOnInitialisationCompleteListener;


/**
 * Created by Robert on 11/16/2015.
 */
public interface SetOnServiceCallListener{

    void callGSSDescribe(
            AppCompatActivity activity,
            Context context,
            SetOnInitialisationCompleteListener initialisationCompleteListener

    );

    void callLoginService( AppCompatActivity activity, Context context, String username, String password, SetOnResultAvailableListener resultAvailableListener );

    void callGetEquipmentsService( AppCompatActivity activity, Context context, Object rootedObject, String collectionName, String recordId, SetOnInitialisationCompleteListener initialisationListener );

    void callGetCables( AppCompatActivity activity, Context context, String collectionName, String recordId, boolean connected, SetOnInitialisationCompleteListener initialisationListener );

    //void callGetCableConnections( AppCompatActivity activity, Context context, Cable cable, SetOnInitialisationCompleteListener initialisationListener );

   // void callGetConnectedItems( AppCompatActivity activity, Context context, Port port, SetOnInitialisationCompleteListener initialisationListener );

    void callGetNetworkConnections( AppCompatActivity activity, Context context, String designId, SetOnInitialisationCompleteListener initialisationListener );

   // void callGetAssociatedLniObjects( AppCompatActivity activity, Context context, LogicalBridge routedObject, String collectionName, String recordId, SetOnInitialisationCompleteListener initialisationListener );

   // void callGetAssociatedLniChildrenObjects( AppCompatActivity activity, Context context, LogicalObject logicalObject, SetOnInitialisationCompleteListener initialisationListener );

    void callGetDesigns( AppCompatActivity activity, Context context, String designToSearch, SetOnInitialisationCompleteListener initialisationListener );

    void callFind( AppCompatActivity activity, Context context, String collectionName, String fieldName, String fieldValue, SetOnInitialisationCompleteListener initialisationListener );

}
