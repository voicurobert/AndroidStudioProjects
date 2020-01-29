package rvo.holidayplanner;

import android.os.AsyncTask;

/**
 * Created by Robert on 7/17/2015.
 */
public class DirectionRoutesAsyncTask extends AsyncTask< Void, Void, String >{
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected String doInBackground(Void... params) {
        DirectionRoutesJSONParser parser = new DirectionRoutesJSONParser();
        return parser.getJSONStringFromURL( url );
    }


}
