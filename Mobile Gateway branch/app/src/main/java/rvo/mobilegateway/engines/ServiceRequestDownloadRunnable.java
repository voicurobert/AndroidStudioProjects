package rvo.mobilegateway.engines;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rvo.mobilegateway.L;


/**
 * Created by Robert on 8/11/2015.
 */
public class ServiceRequestDownloadRunnable implements Runnable {
    // Constants for indicating the state of the download
    static final int HTTP_STATE_FAILED = - 1;
    static final int HTTP_STATE_STARTED = 0;
    static final int HTTP_STATE_COMPLETED = 1;


    final TaskDownloadMethods downloadTask;

    public ServiceRequestDownloadRunnable( TaskDownloadMethods downloadTask ){
        this.downloadTask = downloadTask;
    }

    interface TaskDownloadMethods {
        void setDownloadThread( Thread currentThread );

        InputStream getInputStream( );

        void setInputStream( InputStream is );

        void handleDownloadState( int state );

        URL getURLRequest( );
    }

    @Override
    public void run( ){
        downloadTask.setDownloadThread( Thread.currentThread( ) );
        android.os.Process.setThreadPriority( android.os.Process.THREAD_PRIORITY_BACKGROUND );
        InputStream downloadedContent = downloadTask.getInputStream( );
        try{
            if( Thread.interrupted( ) ){
                throw new InterruptedException( );
            }
            if( downloadedContent == null ){
                downloadTask.handleDownloadState( HTTP_STATE_STARTED );
                InputStream is = null;
                try{
                    HttpURLConnection httpConn = ( HttpURLConnection ) downloadTask.getURLRequest( ).openConnection( );
                    if( Thread.interrupted( ) ){
                        return;
                    }
                    is = httpConn.getInputStream( );
                    if( Thread.interrupted( ) ){
                        return;
                    }
                    downloadTask.setInputStream( is );
                    downloadTask.handleDownloadState( HTTP_STATE_COMPLETED );

                } catch( IOException e ){
                    e.printStackTrace( );
                } finally{
                    if( is == null ){
                        downloadTask.handleDownloadState( HTTP_STATE_FAILED );
                    }
                    downloadTask.setDownloadThread( null );
                }
            }
        } catch( InterruptedException e ){
            e.printStackTrace( );
        }
    }
}
