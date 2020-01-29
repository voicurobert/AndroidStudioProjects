package com.rwee.mobilegatewaywo.engines;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;


import org.xmlpull.v1.XmlPullParserException;

public class SmallWorldServiceTask extends AsyncTask< String, String, List<HashMap<String, String>> >{

    private String requestCall = null;
	private int maxSize = 0;
    private ProgressDialog progressDialog;


	private SetOnTaskCompleteListener< List<HashMap<String, String>> > callback;

	public SmallWorldServiceTask( String requestCall,
                                  Activity activity,
                                  String message,
								  SetOnTaskCompleteListener< List< HashMap< String, String > > > callback
                                   ) {
        this.requestCall = requestCall;
		this.callback = callback;
        progressDialog = new ProgressDialog( activity );
        progressDialog.setTitle( "Please wait..." );
        progressDialog.setMessage( message );
	}

	@Override
	protected void onPreExecute( ) {
		//super.onPreExecute( );
        progressDialog.show( );
	}

	@Override
	protected List< HashMap< String, String > > doInBackground( String... params ) {
		List<HashMap<String, String>> result = getDataFromServiceRequest( requestCall );
		if ( result.isEmpty( ) ){
			return null;
		}
		return result;
	}


    @Override
    protected void onPostExecute( List< HashMap< String, String > > hashMaps ) {
		callback.onTaskComplete( hashMaps );
        progressDialog.dismiss();
    }

    public List<HashMap<String, String>> getDataFromServiceRequest( String urlString ) {
        List<HashMap<String, String>> map = new ArrayList<>();
        try {
            // defaultHttpClient
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            XMLParser parser = new XMLParser();
            map = parser.parse( in );
            in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( XmlPullParserException e ) {
            e.printStackTrace();
        }
        return map;
    }

	private List<HashMap<String, String>> executeService( ServiceRequest request, int index ){
		List<HashMap<String, String>> currentElements = new ArrayList< >();
		int startIndex = index;
		int nrOfItererations = maxSize / 1000;
		int remainingElements = maxSize % 1000;
		String newServiceCall = null;
		if( maxSize > 1000 ){

			for( int i = 0; i < nrOfItererations; i++ ){
				// change parameter
				request.putParameterValue("start_index", String.valueOf( startIndex ) );
				newServiceCall = request.getServiceRequest();
				currentElements.addAll( getDataFromServiceRequest( newServiceCall ) );
				startIndex = startIndex + 1000;
			}

			if( remainingElements > 1000 ){
				currentElements.addAll( executeService( request, startIndex  ) );
			}else{
				request.putParameterValue("start_index", String.valueOf( startIndex ) );
				newServiceCall = request.getServiceRequest();
				currentElements.addAll( getDataFromServiceRequest( newServiceCall ) );
			}

		}else{
			request.putParameterValue("start_index", String.valueOf( startIndex ) );
			newServiceCall = request.getServiceRequest();
			currentElements.addAll( getDataFromServiceRequest( newServiceCall ) );
		}
		return currentElements;
	}

}
