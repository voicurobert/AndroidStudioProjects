package rvo.mobilegateway.engines;




import android.content.Context;

import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Robert on 7/29/2015.
 */
public class ServiceRequest {

    //private static String serverUrl = "http://172.16.10.170:8080"; // ip ree office
    //public String serverUrl = "http://192.168.100.2:8080";  // ip home
    public final static String CORE_SEVICE_NAME = "mobilegateway";
    public final static String TELCO_SERVICE_NAME = "mobilegatewaytelco";
    private final static String serverPreamble = "http://";
    public String serverUrl = null ;
   // private String servicePreamble = "/gss/native?service=mobilegateway&method=";
    private String servicePreamble;
    private String serviceMethod;
    private LinkedHashMap< String, String > serviceParameters = new LinkedHashMap< String, String >();
    private String serviceRequest;


    public ServiceRequest( String serviceName, String serviceMethod, Context context ){
        this.serviceMethod = serviceMethod;
        this.servicePreamble = "/gss/native?service=" + serviceName + "&method=";
        setServerUrl( ServiceRequest.computeServerUrl( context ) );
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getServiceRequest() {
        // before return the service request compute it, the parameters could be changed
        computeServiceRequestString();
        return serviceRequest;
    }


    public void putParameterValue( String parameterName, String parameterValue ){
        serviceParameters.put( parameterName, parameterValue );
    }

    public String getParameterValue( String key ){
        return serviceParameters.get( key );

    }

    private void computeServiceRequestString(){
        serviceRequest = null;
        serviceRequest = serverUrl + servicePreamble + serviceMethod;
        for( String paramKey : serviceParameters.keySet() ){
           String paramValue = serviceParameters.get( paramKey );
            serviceRequest = serviceRequest + "&" + paramKey + "=" + paramValue;
        }
    }
    
    public String getGSSDescribeRequest(){
        String request = "";
        request = request + serverUrl + "/gss/" + serviceMethod;
        return request;
    }

    private static String computeServerUrl( Context context ){
        return serverPreamble + context.getSharedPreferences( "GSS_SETTINGS", 0 ).getString( "serverUrl", "localhost" ) + ":" + context.getSharedPreferences( "GSS_SETTINGS", 0 ).getString( "serverPort", "localhost" );
    }

    public String getServiceMethod( ){
        return serviceMethod;
    }
}
