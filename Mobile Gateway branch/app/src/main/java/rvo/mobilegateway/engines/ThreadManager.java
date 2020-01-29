package rvo.mobilegateway.engines;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import rvo.mobilegateway.L;
import rvo.mobilegateway.connectivity_activity.SetOnInitialisationCompleteListener;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 8/11/2015.
 */
public class ThreadManager {
    private static final String TAG = MobileGatewayMapActivity.class.getName( );
    static final int DOWNLOAD_FAILED = - 1;
    static final int DOWNLOAD_STARTED = 1;
    static final int DOWNLOAD_COMPLETE = 2;
    static final int DECODE_STARTED = 3;
    static final int TASK_COMPLETE = 4;

    private static final int REQUEST_CACHE_SIZE = 1024 * 4;
    // Sets the amount of time an idle thread will wait for a task before terminating
    private static final int KEEP_ALIVE_TIME = 1;

    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT;

    // Sets the initial threadpool size to 8
    private static final int CORE_POOL_SIZE = 8;

    // Sets the maximum threadpool size to 8
    private static final int MAXIMUM_POOL_SIZE = 64;

    private static int NUMBER_OF_CORES = Runtime.getRuntime( ).availableProcessors( );

    private final LruCache< LatLngBounds, HashMap< String, List< HashMap< String, String > > > > requestCache;

    // A queue of Runnables for the request download pool
    private final BlockingQueue< Runnable > downloadWorkQueue;
    private final BlockingQueue< Runnable > parsingWorkQueue;

    // A queue of Request tasks. Tasks are handed to a ThreadPool.
    private final Queue< RequestTask > requestTaskWorkQueue;

    // A managed pool of background download threads
    private final ThreadPoolExecutor downloadThreadPool;

    private final ThreadPoolExecutor parsingThreadPool;

    // An object that manages Messages in a Thread
    private Handler handler;
    private SetOnInitialisationCompleteListener initialisationCompleteListener;
    private static ThreadManager instance = null;

    static{
        KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
        instance = new ThreadManager( );
    }

    private ThreadManager( ){
        /*
         * Creates a work queue for the pool of Thread objects used for downloading, using a linked
         * list queue that blocks when the queue is empty.
         */
        downloadWorkQueue = new LinkedBlockingDeque< Runnable >( );

        parsingWorkQueue = new LinkedBlockingDeque< Runnable >( );

        /*
         * Creates a work queue for the pool of Thread objects used for decoding, using a linked
         * list queue that blocks when the queue is empty.
         */
        requestTaskWorkQueue = new LinkedBlockingDeque< RequestTask >( );
        /*
         * Creates a new pool of Thread objects for the download work queue
         */
        downloadThreadPool = new ThreadPoolExecutor( CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, downloadWorkQueue );
        parsingThreadPool = new ThreadPoolExecutor( NUMBER_OF_CORES, NUMBER_OF_CORES, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, parsingWorkQueue );

        requestCache = new LruCache< LatLngBounds, HashMap< String, List< HashMap< String, String > > > >( REQUEST_CACHE_SIZE ) {
            @Override
            protected int sizeOf( LatLngBounds bounds, HashMap< String, List< HashMap< String, String > > > mapping ){
                return mapping.size( );
            }
        };

        handler = new Handler( Looper.getMainLooper( ) ) {

            /*
             * handleMessage() defines the operations to perform when the
             * Handler receives a new Message to process.
             */
            @Override
            public void handleMessage( Message inputMessage ){

                // Gets the request task from the incoming Message object.
                RequestTask requestTask = ( RequestTask ) inputMessage.obj;
                switch( inputMessage.what ){
                    case DOWNLOAD_STARTED:
                        break;
                    case DOWNLOAD_COMPLETE:
                        break;
                    case TASK_COMPLETE:
                        Log.d( TAG, "TASK_COMPLETE" );
                        handleCompleteTask( requestTask );
                      //  recycleTask( requestTask );
                        break;
                    case DOWNLOAD_FAILED:
                        // Attempts to re-use the Task object
                      //  recycleTask( requestTask );
                        break;
                    default:
                        // Otherwise, calls the super method
                        super.handleMessage( inputMessage );
                }
            }
        };

    }

    public Queue< RequestTask > getRequestTaskWorkQueue( ){
        return requestTaskWorkQueue;
    }

    public static ThreadManager getInstance( ){
        return instance;
    }

    public LruCache< LatLngBounds, HashMap< String, List< HashMap< String, String > > > > getRequestCache( ){
        return requestCache;
    }

    public void handleState( RequestTask task, int state ){
        switch( state ){
            case TASK_COMPLETE:
                if( !task.isSearchingForDesignsTask() ){
                    if( task.getCurrentBounds( ) != null ){
                        if( requestCache.get( task.getCurrentBounds( ) ) != null ){
                            HashMap< String, List< HashMap< String, String > > > currentValues = requestCache.get( task.getCurrentBounds( ) );
                            currentValues.put( task.getCurrentCollectionName( ), task.getData( ) );
                        } else{
                            // put into cache a mapping between the bounds and content mapping
                            HashMap< String, List< HashMap< String, String > > > mapping = new HashMap< String, List< HashMap< String, String > > >( );
                            mapping.put( task.getCurrentCollectionName( ), task.getData( ) );
                            requestCache.put( task.getCurrentBounds( ), mapping );
                        }
                    }
                }
                Message completeMessage = handler.obtainMessage( state, task );
                completeMessage.sendToTarget( );
                break;
            case DOWNLOAD_COMPLETE:
                parsingThreadPool.execute( task.getParseRequestRunnable( ) );
            default:
                handler.obtainMessage( state, task ).sendToTarget( );
                break;
        }
    }

    static public RequestTask startDownload( GoogleMap map, LatLngBounds bounds, ServiceRequest sr, String collName ){
        RequestTask task = instance.requestTaskWorkQueue.poll( );
        if( task == null ){
            task = new RequestTask( sr, false );
        } else{
            task.setData( null );
        }
        task.initializeDownloadRequestTask( instance, sr, map );
        task.setCurrentBounds( bounds );
        task.setCurrentCollectionName( collName );
        if( bounds != null ){
            if( instance.requestCache.get( bounds ) == null ){
                task.setData( null );
            }
        } else{
            task.setData( null );
        }
        if( task.getData( ) == null ){
            instance.downloadThreadPool.execute( task.getDownloadRequestRunnable( ) );
        }
        return task;
    }

    public static RequestTask startSearch( ServiceRequest serviceRequest, SetOnInitialisationCompleteListener initialisationCompleteListener ){

        RequestTask task = instance.requestTaskWorkQueue.poll( );
        instance.initialisationCompleteListener = initialisationCompleteListener;
        if( task == null ){
            task = new RequestTask( serviceRequest, true );
        } else{
            task.setData( null );
        }

        task.initializeSearchingTask( instance, serviceRequest );
        if( task.getData( ) == null ){
            instance.downloadThreadPool.execute( task.getDownloadRequestRunnable( ) );
        }

        return task;
    }


    static public void removeDownload( RequestTask task, LatLngBounds bounds ){
        if( task != null && bounds != null && task.getCurrentBounds( ) != null && task.getCurrentBounds().equals( bounds ) ){
            synchronized( instance ){
                Thread thread = task.getCurrentThread( );
                if( null != thread ){
                    thread.interrupt( );
                }
            }
            instance.downloadThreadPool.remove( task.getDownloadRequestRunnable( ) );
            instance.parsingThreadPool.remove( task.getParseRequestRunnable( ) );
        }
    }

    static public void removeSearchTask( RequestTask task ){
        if( task != null ){
            synchronized( instance ){
                Thread thread = task.getCurrentThread( );
                if( thread != null ){
                    thread.interrupt( );
                }
            }
            instance.downloadThreadPool.remove( task.getDownloadRequestRunnable( ) );
            instance.parsingThreadPool.remove( task.getParseRequestRunnable() );
        }
    }

    public static void cancelAll(){
        /*
         * Creates an array of tasks that's the same size as the task work queue
         */
        RequestTask[] taskArray = new RequestTask[ instance.downloadWorkQueue.size()];

        // Populates the array with the task objects in the queue
        instance.downloadWorkQueue.toArray(taskArray);

        // Stores the array length in order to iterate over the array
        int taskArraylen = taskArray.length;

        /*
         * Locks on the singleton to ensure that other processes aren't mutating Threads, then
         * iterates over the array of tasks and interrupts the task's current Thread.
         */
        synchronized (instance) {

            // Iterates over the array of tasks
            for (int taskArrayIndex = 0; taskArrayIndex < taskArraylen; taskArrayIndex++) {

                // Gets the task's current thread
                Thread thread = taskArray[taskArrayIndex].getCurrentThread();

                // if the Thread exists, post an interrupt to it
                if (null != thread) {
                    thread.interrupt();
                }
            }
        }
    }

    void recycleTask( RequestTask task ){
        L.m( "recicle task" );
        task.recycle( );
        requestTaskWorkQueue.offer( task );
    }

    private void handleCompleteTask( RequestTask requestTask ){

        switch( requestTask.getServiceRequest().getServiceMethod() ){
            case "getRecordsFromBounds" :
                handleGetRecordsFromBoundsAction( requestTask );
                break;
            case "getDesigns" :
                handleGetDesignsAction( requestTask );
                break;
            case "find" :
                handleFindAction( requestTask );
                break;
        }
    }

    private void handleGetRecordsFromBoundsAction( RequestTask requestTask ){
        GoogleMap map = requestTask.getGoogleMap();
        LatLngBounds bounds = requestTask.getCurrentBounds( );
        LatLngBounds currentBounds = map.getProjection( ).getVisibleRegion( ).latLngBounds;
        if( bounds.equals( currentBounds ) ){
            try{
                MobileGatewayDataStoreBuilder.insert( requestTask.getGoogleMap( ), requestTask.getCurrentCollectionName( ), requestCache.get( bounds ).get( requestTask.getCurrentCollectionName( ) ) );
            }catch( NullPointerException e ){
                L.m( "hadndle get records from bounds action: " + e.getMessage() );
            }
        }
    }

    private void handleGetDesignsAction( RequestTask requestTask ){
        List< HashMap< String, String > > mapping = requestTask.getData();
        MobileGatewayDataStoreBuilder.insertDesigns( mapping, initialisationCompleteListener );

    }

    private void handleFindAction( RequestTask requestTask ){
       // L.m( requestTask.getData().toString() );
    }

}
