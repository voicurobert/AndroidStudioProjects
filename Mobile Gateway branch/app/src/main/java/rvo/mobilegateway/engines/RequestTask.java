package rvo.mobilegateway.engines;

/**
 * Created by Robert on 8/11/2015.
 */



import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import rvo.mobilegateway.engines.ServiceRequestDownloadRunnable.TaskDownloadMethods;
import rvo.mobilegateway.engines.ServiceRequestParserRunnable.TaskParsingMethods;



public class RequestTask implements TaskDownloadMethods, TaskParsingMethods {

    private WeakReference<GoogleMap> mapWeakReference;
    private ServiceRequest serviceRequest;
    private InputStream inputStream;
    private String currentCollectionName;
    private Runnable downloadRequestRunnable;
    private Runnable parseRequestRunnable;
    private boolean searchingForDesignsTask = false;
    private List<HashMap<String, String>> contentData;
    private Thread currentThread;
    private static ThreadManager threadManager;
    private URL serviceURL;
    private LatLngBounds currentBounds;


    RequestTask( ServiceRequest sr, boolean searchingForDesigns ){
        this.serviceRequest = sr;
        downloadRequestRunnable = new ServiceRequestDownloadRunnable( this );
        parseRequestRunnable = new ServiceRequestParserRunnable( this  );
        this.searchingForDesignsTask = searchingForDesigns;
        threadManager = ThreadManager.getInstance();
    }

    public Thread getCurrentThread() {
        synchronized( threadManager) {
            return currentThread;
        }
    }

    public ServiceRequest getServiceRequest( ){
        return serviceRequest;
    }

    public boolean isSearchingForDesignsTask( ){
        return searchingForDesignsTask;
    }

    public void setCurrentCollectionName(String currentCollectionName) {
        this.currentCollectionName = currentCollectionName;
    }

    public String getCurrentCollectionName() {
        return currentCollectionName;
    }

    void initializeDownloadRequestTask( ThreadManager threadManager, ServiceRequest sr, GoogleMap map ){
        RequestTask.threadManager = threadManager;
        this.serviceRequest = sr;
        try {
            this.serviceURL = new URL( sr.getServiceRequest() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.mapWeakReference = new WeakReference<>( map );
    }


    void initializeSearchingTask( ThreadManager threadManager, ServiceRequest sr ){

        RequestTask.threadManager = threadManager;
        try {
            this.serviceURL = new URL( sr.getServiceRequest() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public Runnable getDownloadRequestRunnable() {
        return downloadRequestRunnable;
    }

    public Runnable getParseRequestRunnable() {
        return parseRequestRunnable;
    }

    public GoogleMap getGoogleMap(){
        if( mapWeakReference == null ){
            return null;
        }else{
            return mapWeakReference.get();
        }

    }

    public LatLngBounds getCurrentBounds(){
        return this.currentBounds;
    }

    public void setCurrentBounds(LatLngBounds currentBounds) {
        this.currentBounds = currentBounds;
    }

    @Override
    public void setDownloadThread(Thread currentThread) {
        setCurrentThread( currentThread );
    }



    @Override
    public void handleDownloadState(int state) {
        int outState;
        // Converts the download state to the overall state
        switch(state) {
            case ServiceRequestDownloadRunnable.HTTP_STATE_COMPLETED:

                outState = ThreadManager.DOWNLOAD_COMPLETE;
                break;
            case ServiceRequestDownloadRunnable.HTTP_STATE_FAILED:
                outState = ThreadManager.DOWNLOAD_FAILED;
                break;
            default:
                outState = ThreadManager.DOWNLOAD_STARTED;
                break;
        }
        // Passes the state to the ThreadPool object.
        handleState(outState);
    }

    @Override
    public URL getURLRequest() {
        return serviceURL;
    }

    void handleState( int state ){
        threadManager.handleState( this, state );
    }

    public void setCurrentThread( Thread thread ){
        synchronized ( threadManager ){
            currentThread = thread;
        }
    }

    @Override
    public void setParsingThread(Thread currentThread) {
        setCurrentThread( currentThread );
    }

    @Override
    public void handleParsingState(int state) {
        int outState;

        // Converts the decode state to the overall state.
        switch(state) {
            case ServiceRequestParserRunnable.PARSING_STATE_COMPLETED:
                outState = ThreadManager.TASK_COMPLETE;
                break;
            case ServiceRequestParserRunnable.PARSING_STATE_FAILED:
                outState = ThreadManager.DOWNLOAD_FAILED;
                break;
            default:
                outState = ThreadManager.DECODE_STARTED;
                break;
        }

        // Passes the state to the ThreadPool object.
        handleState(outState);
    }

    @Override
    public void setData(List<HashMap<String, String>> data) {
        contentData = data;
    }

    @Override
    public List<HashMap<String, String>> getData() {
        return contentData;
    }

    @Override
    public void setInputStream( InputStream is ) {
        this.inputStream = is;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    void recycle(){
        if( mapWeakReference != null ){
            mapWeakReference.clear();
            mapWeakReference = null;
        }
        inputStream = null;
        contentData = null;
    }
}
