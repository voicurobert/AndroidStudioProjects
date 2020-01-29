package com.rwee.mobilegatewaywo.engines;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.rwee.mobilegatewaywo.main_activity.MobileGatewayWOMapActivity;
import java.util.concurrent.ExecutionException;


/**
 * Created by Robert on 11/12/15.
 */
public class AsynchronousThreadRunner extends AsyncTask< Void, Void, Void >{

    SetOnTaskCompleteListener< Boolean > callback;
    ASynchronousThreadExecutor aSynchronousThreadExecutor;
    ProgressDialog pg;

    public AsynchronousThreadRunner( ASynchronousThreadExecutor aSynchronousThreadExecutor, SetOnTaskCompleteListener< Boolean > callback ){
        this.callback = callback;
        this.aSynchronousThreadExecutor = aSynchronousThreadExecutor;
        pg = new ProgressDialog( MobileGatewayWOMapActivity.masterActivity );
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
