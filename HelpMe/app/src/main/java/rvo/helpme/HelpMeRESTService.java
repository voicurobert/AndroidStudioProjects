package rvo.helpme;

import android.os.AsyncTask;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Robert on 09/05/16.
 */
public class HelpMeRESTService extends AsyncTask< String, String, JSONObject >{

    private String serviceCall;

    public HelpMeRESTService( String serviceCall ){
        this.serviceCall = serviceCall;
    }

    @Override
    protected JSONObject doInBackground( String... params ){
        try{
            URL url = new URL( serviceCall );
            HttpURLConnection urlConnection = ( HttpURLConnection ) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            JSONObject jsonObject = new JSONObject(  );

        }catch( IOException e ){
            e.printStackTrace();
        }

        return null;
    }
}
