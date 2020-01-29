package com.rwee.mobilegatewaywo.engines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.rwee.mobilegatewaywo.Constants;
import com.rwee.mobilegatewaywo.L;
import com.rwee.mobilegatewaywo.connectivity_activity.SetOnInitialisationCompleteListener;
import com.rwee.mobilegatewaywo.find_activity.FinderActivity;
import com.rwee.mobilegatewaywo.login_activity.LoginAuthentificationHandlerContext;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;

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
        serviceRequest.putParameterValue( "dataset_name", Constants.instance.WS_DATASET_NAME );
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


    public void callFind(
            final AppCompatActivity activity,
            Context context,
            String collectionName,
            String fieldName,
            String fieldValue,
            final SetOnInitialisationCompleteListener initialisationListener ) {
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "find", context );
        serviceRequest.putParameterValue( "collection_name", collectionName );
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
    /*
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
        WODataStoreSingleton.instance.designSet.clear( );
        L.m( serviceRequest.getServiceRequest( ) );
        RequestTask task = ThreadManager.startSearch( serviceRequest, initialisationListener );
        runningThreads.add( task );
    }

    */

    public void callGetEnumeratedValues( final Activity activity, String datasetName, String collectionName, String fieldName, final SetOnInitialisationCompleteListener initialisationListener ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getEnumeratedValues", activity.getApplicationContext() );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "dataset_name", datasetName );
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

    public void callGetSpecifications( final Activity activity, String datasetName, String collectionName, final SetOnInitialisationCompleteListener initialisationListener ){
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "getSpecifications", activity.getApplicationContext() );
        serviceRequest.putParameterValue( "collection_name", collectionName );
        serviceRequest.putParameterValue( "dataset_name", datasetName );
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
                                    MapEngine.refreshMap( MobileGatewayWOMapActivity.masterActivity.getMap( ), MobileGatewayWOMapActivity.masterActivity.getMap( ).getCameraPosition( ), context );
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
    public void callUpdateRecord( Activity activity, final Context context, String datasetName, String collectionName, String recordId, Map< String,String> fieldsANdValues ){
        String requestFieldsAndValues = formatFieldsAndValues( fieldsANdValues );
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "updateRecord", context );
        serviceRequest.putParameterValue( "database_name", "update_record" );
        serviceRequest.putParameterValue( "dataset_name", datasetName );
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
                                    MobileGatewayWOMapActivity.masterActivity.handleFinnishUpdate( );
                                    L.t( context, "Updated record complete" );
                                }else{
                                    L.t( context, "Updated record failed!!!! " + updated );
                                    MobileGatewayWOMapActivity.masterActivity.handleFinnishUpdate( );
                                }
                            }
                        }
                    }
                } );
         task.execute( );
    }

    public void callInsertRecord( Activity activity, final Context context, String datasetName, String collectionName, Map< String,String> fieldsANdValues ){
        String requestFieldsAndValues = formatFieldsAndValues( fieldsANdValues );
        ServiceRequest serviceRequest = new ServiceRequest( ServiceRequest.CORE_SEVICE_NAME, "insertRecord", context );
        serviceRequest.putParameterValue( "database_name", "insert_record" );
        serviceRequest.putParameterValue( "dataset_name", datasetName );
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
                                    L.t( context, "Insert record complete" );
                                    //MapEngine.refreshMap( MobileGatewayWOMapActivity.masterActivity.getMap( ), MobileGatewayWOMapActivity.masterActivity.getMap( ).getCameraPosition( ), context );

                                }else{
                                    L.t( context, "Insert record failed!!!! " + updated );
                                }
                            }
                        }
                    }
                } );
        task.execute( );
    }


    private String formatFieldsAndValues( Map< String,String> fieldsANdValues ){
        String requestFieldsAndValues = "";
        Object[] keys = fieldsANdValues.keySet().toArray( );
        for( int i = 0; i < keys.length; i++ ){
            String currentKey = (String)keys[ i ];
            if( i + 1 == keys.length ){
                requestFieldsAndValues += currentKey + ":" + fieldsANdValues.get( currentKey ).replace( " ", "%20" );
            }else{
                requestFieldsAndValues += currentKey  + ":" + fieldsANdValues.get( currentKey ).replace( " ", "%20" ) + ",";
            }
        }
        return requestFieldsAndValues;
    }

}
