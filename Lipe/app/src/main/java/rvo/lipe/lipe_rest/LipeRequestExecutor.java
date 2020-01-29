package rvo.lipe.lipe_rest;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robert on 28/06/2017.
 */

public class LipeRequestExecutor extends AsyncTask< Void, Void, Void >{
    private String request;
    
    public LipeRequestExecutor( String request ){
        this.request = request;
    }
    
    @Override
    protected Void doInBackground( Void... params ){
        try {
            // defaultHttpClient
            URL url = new URL( request );
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
          //  in.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    protected void onPostExecute( Void aVoid ){
        super.onPostExecute( aVoid );
    }
}
