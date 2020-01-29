package rvo.mobilegateway.engines;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 11/12/15.
 */
public class AsynchronousThreadRunner extends AsyncTask< Void, Void, Void >{

    SetOnTaskCompleteListener< Boolean > callback;
    ASynchronousThreadExecutor aSynchronousThreadExecutor;
    ProgressDialog pg;

    public AsynchronousThreadRunner( Activity context, ASynchronousThreadExecutor aSynchronousThreadExecutor, SetOnTaskCompleteListener< Boolean > callback ){
        this.callback = callback;
        this.aSynchronousThreadExecutor = aSynchronousThreadExecutor;
        pg = new ProgressDialog( context );
        pg.setTitle( "Please wait..." );
        pg.setMessage( "Initializing" );
    }

    @Override
    protected void onPreExecute( ){
        //super.onPreExecute( );
        pg.show();
    }

    @Override
    protected Void doInBackground( Void... params ){
        try{
            aSynchronousThreadExecutor.submitAllTasks();
        }catch( ExecutionException e ){
            e.printStackTrace( );
        }catch( InterruptedException e ){
            e.printStackTrace( );
        }
        return null;
    }

    @Override
    protected void onPostExecute( Void aVoid ){
        //super.onPostExecute( aVoid );
        callback.onTaskComplete( true );
        pg.dismiss();
    }
}
