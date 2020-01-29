package rvo.mobilegateway.engines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import rvo.mobilegateway.L;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.find_activity.FinderActivity;
import rvo.mobilegateway.logical_datamodel.LogicalObject;
import rvo.mobilegateway.login_activity.LoginAuthentificationHandlerContext;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;
import rvo.mobilegateway.physical_datamodel.AbstractSmallWorldObject;
import rvo.mobilegateway.physical_datamodel.AbstractTelcoObject;
import rvo.mobilegateway.physical_datamodel.Cable;
import rvo.mobilegateway.physical_datamodel.EquipmentObject;
import rvo.mobilegateway.physical_datamodel.Port;


public class SmallworldServicesDelegate{

    public static SmallworldServicesDelegate instance = new SmallworldServicesDelegate( );
    private Set< RequestTask > runningThreads = new LinkedHashSet<>( );

    private SmallworldServicesDelegate( ) {

    }

    public void callGSSDescribe( final Context context, final SetOnInitialisationCompleteListener initialisationCompleteListener ) {
        final ProgressDialog dialog = new ProgressDialog( context );
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        dialog.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        dialog.setTitle( "Please wait..." );
        dialog.setMessage( "Checking connection to Smallworld server..." );
        final AsyncTask< Void, Void, Boolean > connectionTestTask = new AsyncTask< Void, Void, Boolean >( ){
            @Override
            protected void onPreExecute( ) {
                super.onPreExecute( );
                dialog.show( );
            }

            @Override
            protected Boolean doInBackground( Void... params ) {
                L.m( "do in background" );
                ServiceRequest serviceRequest = new ServiceRequest( null, "describe", context );
                try {
                    URL url = new URL( serviceRequest.getGSSDescribeRequest( ) );
                    L.m( serviceRequest.getGSSDescribeRequest( ) );
                    URLConnection connection = url.openConnection( );
                    connection.setConnectTimeout( 10000 );
                    connection.connect( );
                } catch( MalformedURLException e ) {
                    L.m( e.getMessage( ) );
                    return false;
                } catch( SocketTimeoutException socketException ) {
                    // if we are here
                    return false;
                } catch( IOException e ) {
                    L.m( e.getMessage( ) );
                    return false;
                }
                return true;
            }
            @Override
            protected void onPostExecute( Boolean result ) {
                super.onPostExecute( result );
                initialisationCompleteListener.onInitialiseComplete( result, null );
                if( dialog.isShowing( ) ) {
                    dialog.dismiss( );
                }
            }
        };
        connectionTestTask.execute( );
    }


    public void callLoginService(
            final AppCompatActivity activity,
            final Context context,
            final SetOnResultAvailableListener resultAvailableListener ) {
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "login", context );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Checking user authorization...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        resultAvailableListener.onResultAvailable( result );
                    }
                }
        );
        task.execute( );
    }

    public void callGetEquipmentsService( final AbstractTelcoObject currentObject, final Context context, String collectionName, String recordId ){
        ServiceRequest request = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getEquipments", context );
        request.putParameterValue( "collection_name", collectionName );
        request.putParameterValue( "record_id", recordId );
        request.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
        request.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( request.getServiceRequest( ) );
        List< HashMap< String, String > > map = getData( request.getServiceRequest() );
        MobileGatewayDataStoreBuilder.initializeEquipments( currentObject, map, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){
                List< EquipmentObject > equipments = currentObject.internalEquipments;
                if( equipments != null ){
                    for( EquipmentObject equipment : equipments ){
                        if( !MapEngine.isSimpleStructureSelected( ) || ( MapEngine.isSimpleStructureSelected( ) && !equipment.equipmentType.equals( "rack" ) ) ){
                            callGetEquipmentsService( equipment, context, equipment.getCollectionName( ), String.valueOf( equipment.id ) );
                        }
                    }
                }
            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){
            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){
                List< Port > ports = currentObject.ports;
                if( ports != null ){
                    for( Port port : ports ){
                        callGetConnectedItems( context, port );
                    }
                }
            }
        } );

    }

    public void callGetCables( final AbstractTelcoObject rootedObject, final Context context, String collectionName, final String recordId, boolean connected ){
        ServiceRequest cablesServiceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getCables", context );
        cablesServiceRequest.putParameterValue( "collection_name", collectionName );
        cablesServiceRequest.putParameterValue( "record_id", recordId );
        cablesServiceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        cablesServiceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        if( connected ) {
            cablesServiceRequest.putParameterValue( "cable_type", "connected" );
        } else {
            cablesServiceRequest.putParameterValue( "cable_type", "passing" );

        }
        L.m( cablesServiceRequest.getServiceRequest( ) );

        MobileGatewayDataStoreBuilder.initializeCables( rootedObject, getData( cablesServiceRequest.getServiceRequest( ) ), connected, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){
                List< Cable > connectedCables = rootedObject.connectedCables;
                if( connectedCables != null ){
                    for( Cable cable : connectedCables ){
                        callGetCableConnections( cable, context );
                    }
                }

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }


    public void callGetCableConnections( Cable cable, Context context ){
        ServiceRequest request = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getCableConnections", context );
        request.putParameterValue( "collection_name", Cable.CABLE_TYPE );
        request.putParameterValue( "record_id", String.valueOf( cable.id ) );
        if( cable.equipment.equipmentType.equals( "rack" ) ) {
            request.putParameterValue( "equipment_type", "strw_connect_point" );
            request.putParameterValue( "equipment_id", String.valueOf( ( cable.equipment.strwConnectionPointId ) ) );
        } else if( cable.equipment.equipmentType.equals( "splice_closure" ) ) {
            request.putParameterValue( "equipment_type", "splice_closure" );
            request.putParameterValue( "equipment_id", String.valueOf( cable.equipment.id ) );
        }
        request.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername() );
        request.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance().getPassword( ) );
        L.m( request.getServiceRequest( ) );

        MobileGatewayDataStoreBuilder.initializeCableConnections( getData( request.getServiceRequest( ) ), cable, new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );

    }


    public void callGetConnectedItems( Context context, Port port ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getConnectedItems", context );

        serviceRequest.putParameterValue( "record_type", port.getPortCollection( ) );
        serviceRequest.putParameterValue( "record_id", String.valueOf( port.id ) );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        MobileGatewayDataStoreBuilder.initializePortConnectedItems( port, getData( serviceRequest.getServiceRequest( ) ), new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }


    public void callGetAssociatedLniObjects( final Context context, final AbstractSmallWorldObject rootedObject, String collectionName, String recordId ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getAssociatedLniObjects", context );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "record_id", recordId );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        MobileGatewayDataStoreBuilder.initLogicalObjects( rootedObject, getData( serviceRequest.getServiceRequest( ) ), new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){
                List< LogicalObject > logicalObjects = rootedObject.logicalObjects;
                if( logicalObjects != null ){
                    for( LogicalObject logicalObject : logicalObjects ){
                        callGetAssociatedLniChildrenObjects( context, logicalObject );
                    }
                }
            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }


    public void callGetAssociatedLniChildrenObjects( Context context, LogicalObject logicalObject ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getAssociatedLniChildrenObjects", context );
        serviceRequest.putParameterValue( "collection_name", logicalObject.getCollectionName( ) );
        serviceRequest.putParameterValue( "record_id", String.valueOf( logicalObject.id ) );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        MobileGatewayDataStoreBuilder.initLogicalObjects( logicalObject, getData( serviceRequest.getServiceRequest( ) ), new SetOnInitialisationCompleteListener( ){
            @Override
            public void onInitialiseComplete( boolean complete, List< HashMap< String, String > > result ){

            }

            @Override
            public void onEquipmentsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onConnectedCablesInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onLogicalObjectsInitialisationComplete( boolean initialised ){

            }

            @Override
            public void onPortsInitialisationComplete( boolean initialised ){

            }
        } );
    }

    public void callGetDesigns(
            Context context,
            String designToSearch,
            final SetOnInitialisationCompleteListener initialisationListener ) {

        for( RequestTask rt : runningThreads ) {
            ThreadManager.removeSearchTask( rt );
        }
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getDesigns", context );
        serviceRequest.putParameterValue( "design_name", designToSearch.replaceAll( " ", "%20" ) );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        PniDataStoreSingleton.instance.designSet.clear( );
        L.m( serviceRequest.getServiceRequest( ) );
        RequestTask task = ThreadManager.startSearch( serviceRequest, initialisationListener );
        runningThreads.add( task );
    }


    public void callGetNetworkConnections(
            AppCompatActivity activity,
            Context context,
            String designId,
            final SetOnInitialisationCompleteListener initialisationListener ) {
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getNetworkConnections", context );
        serviceRequest.putParameterValue( "design_id", designId );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance( ).getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Initializing task list...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        MobileGatewayDataStoreBuilder.initializeNetworkConnections( result, initialisationListener );
                    }
                } );
        task.execute( );
    }


    public void callFind(
            final AppCompatActivity activity,
            Context context,
            String collectionName,
            String fieldName,
            String fieldValue,
            final SetOnInitialisationCompleteListener initialisationListener ) {
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "find", context );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "dataset_name", "gis" );
        serviceRequest.putParameterValue( "field_name", fieldName );
        serviceRequest.putParameterValue( "field_value", fieldValue );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername() );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance().getPassword() );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Finding records...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        if( activity instanceof FinderActivity ) {
                            ( ( FinderActivity ) activity ).refreshFinderViewWithSmallWorldObjects( result );
                            initialisationListener.onInitialiseComplete( true, result );
                        }
                    }
                } );
        task.execute( );
    }


    public void callGetEnumeratedValues( final Activity activity, String collectionName, String fieldName, final SetOnInitialisationCompleteListener initialisationListener ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getEnumeratedValues", activity.getApplicationContext() );
        serviceRequest.putParameterValue( "dataset_name", "gis" );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "field_name", fieldName );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername() );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance().getPassword() );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        initialisationListener.onInitialiseComplete( true, result );
                    }
                } );
        task.execute( );
    }

    private List< HashMap< String, String > > getData( String stringUrl ){
        List< HashMap< String, String > > map = null;
        try{
            URL url = new URL( stringUrl );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            XMLParser parser = new XMLParser();
            map = parser.parse( in );

        }catch( MalformedURLException e ){
            e.printStackTrace( );
        }catch( XmlPullParserException e ){
            e.printStackTrace( );
        }catch( IOException e ){
            e.printStackTrace( );
        }
        return map;
    }


    // U P D A T E     L O C A T I O N
    public void callUpdateLocation( AppCompatActivity activity, final Context context, String collName, int recordId, LatLng newLocation ){

        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "updateLocation", context );
        serviceRequest.putParameterValue( "database_name", "update_location" );
        serviceRequest.putParameterValue( "dataset_name", "gis" );
        serviceRequest.putParameterValue( "collection_name", collName );
        serviceRequest.putParameterValue( "record_id", String.valueOf( recordId ) );
        serviceRequest.putParameterValue( "location_x", String.valueOf( newLocation.longitude ) );
        serviceRequest.putParameterValue( "location_y", String.valueOf( newLocation.latitude ) );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );

        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Updating new location...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        HashMap< String, String > response = result.get( 0 );
                        if( response.containsKey( "login_response" )){
                            L.t( context, response.get( "login_response" ) );
                        }else if( response.containsKey( "result_update" ) ){
                            String updated = response.get( "result_update" );
                            if( updated != null ){
                                if( updated.equals( "true" )){
                                    MapEngine.refreshMap( MobileGatewayMapActivity.masterActivity.getMap( ), MobileGatewayMapActivity.masterActivity.getMap( ).getCameraPosition( ), context );
                                    L.t( context, "Updated location complete" );
                                }else{
                                    L.t( context, "Updated location failed!!!! " + updated );
                                }
                            }
                        }

                    }
                } );
        task.execute( );
    }


    // U P D A T E     R E C O R D
    public void callUpdateRecord(
            Activity activity,
            final Context context,
            String collectionName,
            String recordId,
            Map< String,String> fieldsANdValues ){
        String requestFieldsAndValues = "";
        Object[] keys = fieldsANdValues.keySet().toArray( );
        for( int i = 0; i < keys.length; i++ ){
            if( i + 1 == keys.length ){
                requestFieldsAndValues += keys[ i ] + ":" + fieldsANdValues.get( keys[ i ] ).replace( " ", "%20" );
            }else{
                requestFieldsAndValues += keys[ i ]  + ":" + fieldsANdValues.get( keys[ i ] ).replace( " ", "%20" ) + ",";
            }
        }
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "updateRecord", context );
        serviceRequest.putParameterValue( "database_name", "update_record" );
        serviceRequest.putParameterValue( "dataset_name", "gis" );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "record_id", String.valueOf( recordId ) );
        serviceRequest.putParameterValue( "fields_and_values", requestFieldsAndValues );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Updating record...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        HashMap< String, String > response = result.get( 0 );
                        if( response.containsKey( "login_response" ) ){
                            L.t( context, response.get( "login_response" ) );
                        }else if( response.containsKey( "result_update" ) ){
                            String updated = response.get( "result_update" );
                            if( updated != null ){
                                if( updated.equals( "true" )){
                                    MapEngine.refreshMap( MobileGatewayMapActivity.masterActivity.getMap( ), MobileGatewayMapActivity.masterActivity.getMap( ).getCameraPosition( ), context );
                                    L.t( context, "Updated record complete" );
                                }else{
                                    L.t( context, "Updated record failed!!!! " + updated );
                                }
                            }
                        }
                    }
                } );
         task.execute( );
    }

    public void callInsertRecord( Activity activity, final Context context, String collectionName, Map< String,String> fieldsANdValues ){
        String requestFieldsAndValues = "";
        Object[] keys = fieldsANdValues.keySet().toArray( );
        for( int i = 0; i < keys.length; i++ ){
            if( i + 1 == keys.length ){
                requestFieldsAndValues += keys[ i ] + ":" + fieldsANdValues.get( keys[ i ] ).replace( " ", "%20" );
            }else{
                requestFieldsAndValues += keys[ i ]  + ":" + fieldsANdValues.get( keys[ i ] ).replace( " ", "%20" ) + ",";
            }
        }
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "insertRecord", context );
        serviceRequest.putParameterValue( "database_name", "insert_record" );
        serviceRequest.putParameterValue( "dataset_name", "gis" );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "fields_and_values", requestFieldsAndValues );
        serviceRequest.putParameterValue( "username", LoginAuthentificationHandlerContext.getInstance().getUsername( ) );
        serviceRequest.putParameterValue( "password", LoginAuthentificationHandlerContext.getInstance( ).getPassword( ) );
        L.m( serviceRequest.getServiceRequest( ) );
        SmallWorldServiceTask task = new SmallWorldServiceTask(
                serviceRequest.getServiceRequest( ),
                activity,
                "Inserting record...",
                new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                    @Override
                    public void onTaskComplete( List< HashMap< String, String > > result ) {
                        HashMap< String, String > response = result.get( 0 );
                        if( response.containsKey( "login_response" ) ){
                            L.t( context, response.get( "login_response" ) );
                        }else if( response.containsKey( "result_update" ) ){
                            String updated = response.get( "result_update" );
                            if( updated != null ){
                                if( updated.equals( "true" )){
                                    MapEngine.refreshMap( MobileGatewayMapActivity.masterActivity.getMap( ), MobileGatewayMapActivity.masterActivity.getMap( ).getCameraPosition( ), context );
                                    L.t( context, "Insert record complete" );
                                }else{
                                    L.t( context, "Insert record failed!!!! " + updated );
                                }
                            }
                        }
                    }
                } );
        task.execute( );
    }

    public void callPrepareMapService( final Activity activity, final LatLng centre, final float zoom ){
        if( zoom > 12 ){
            ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getMapServiceProperties", activity.getApplicationContext( ) );
            serviceRequest.putParameterValue( "dataset_name", "gis" );
            SmallWorldServiceTask task = new SmallWorldServiceTask( serviceRequest.getServiceRequest( ), activity, " ", new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
                @Override
                public void onTaskComplete( List< HashMap< String, String > > result ){
                    HashMap< String, String > response = result.get( 0 );
                    L.m( response.toString() );
                    callMapService( activity, centre, response, zoom );
                }
            } );
            task.execute(  );
        }
    }

    public void callMapService( Activity activity, LatLng centre, HashMap< String, String > properties, float zoom ){
        ServiceRequest serviceRequest = new ServiceRequest( "map", "map", activity.getApplicationContext( ) );
        serviceRequest.putParameterValue( "world", properties.get( "world" ) );
        serviceRequest.putParameterValue( "ace_name", properties.get( "ace_name" ).replace( " ", "%20" ) );
        serviceRequest.putParameterValue( "name", properties.get( "name" ) );
        serviceRequest.putParameterValue( "centre", String.valueOf( centre.longitude ) + "," + String.valueOf( centre.latitude ) );
        serviceRequest.putParameterValue( "crs", properties.get( "crs" ) );
        serviceRequest.putParameterValue( "display_style_name", properties.get( "display_style_name" ) );
        // view scale is calculated based on zoom level
        serviceRequest.putParameterValue( "view_scale", String.valueOf( MapEngine.calculateViewScale( zoom ) ) );
        serviceRequest.putParameterValue( "image_format", properties.get( "image_format" ) );
        serviceRequest.putParameterValue( "background", properties.get( "background" ) );
        L.m( serviceRequest.getServiceRequest() );
        SmallWorldServiceTask task = new SmallWorldServiceTask( serviceRequest.getServiceRequest( ), activity, " ", new SetOnTaskCompleteListener< List< HashMap< String, String > > >( ){
            @Override
            public void onTaskComplete( List< HashMap< String, String > > result ){
                HashMap< String, String > response = result.get( 0 );
                L.m( response.toString() );
            }
        } );
        task.execute( );
    }


}
