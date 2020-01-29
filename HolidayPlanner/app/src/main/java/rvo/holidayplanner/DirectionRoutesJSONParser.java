package rvo.holidayplanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Robert on 7/17/2015.
 */
public class DirectionRoutesJSONParser {
    private static InputStream in = null;
    private static JSONObject jsonObject = null;
    private String jsonString = "";

    public DirectionRoutesJSONParser(){

    }

    public String getJSONStringFromURL( String stringURL ){
        try{
            URL url = new URL( stringURL );
            HttpURLConnection urlConnection = ( HttpURLConnection ) url.openConnection();
            in = urlConnection.getInputStream();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{
            BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
            StringBuilder sb = new StringBuilder();
            String stringLine = null;
            while( ( stringLine = reader.readLine() ) != null  )  {
                sb.append( stringLine + "\n");
            }
            jsonString = sb.toString();

            in.close();

        }catch ( Exception e){
            e.printStackTrace();
        }

        return jsonString;
    }
}
