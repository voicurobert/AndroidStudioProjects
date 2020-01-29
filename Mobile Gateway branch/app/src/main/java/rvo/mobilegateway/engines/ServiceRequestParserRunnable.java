package rvo.mobilegateway.engines;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import rvo.mobilegateway.L;
import rvo.mobilegateway.engines.XMLParser;
import rvo.mobilegateway.main_activity.MobileGatewayMapActivity;

/**
 * Created by Robert on 8/11/2015.
 */
public class ServiceRequestParserRunnable  implements Runnable {

    private static final int NUMBER_OF_PARSING_TRIES = 2;
    private static final long SLEEP_TIME_MILLISECONDS = 250;
    static final int PARSING_STATE_FAILED = -1;
    static final int PARSING_STATE_STARTED = 0;
    static final int PARSING_STATE_COMPLETED = 1;

    final TaskParsingMethods parsingTask;

    public TaskParsingMethods getParsingTask() {
        return parsingTask;
    }

    private InputStream inputStream = null;

    ServiceRequestParserRunnable( TaskParsingMethods parsingTask ){
        this.parsingTask = parsingTask;

    }

    interface TaskParsingMethods{

        void setParsingThread( Thread currentThread );
        void handleParsingState( int state );
        void setData(List<HashMap<String, String>> data );
        List<HashMap<String, String>> getData();
        InputStream getInputStream();
    }


    @Override
    public void run() {
        parsingTask.setParsingThread(Thread.currentThread());
        parsingTask.handleParsingState(PARSING_STATE_STARTED);

        if (Thread.interrupted()) {

            return;
        }

        List<HashMap<String, String>> parsedData = null;
        inputStream = parsingTask.getInputStream();
        for (int i = 0; i < NUMBER_OF_PARSING_TRIES; i++) {
            try {

                parsedData = new XMLParser().parse( inputStream );
                parsingTask.setData( parsedData );
            } catch (Throwable e) {
                if (Thread.interrupted()) {
                    return;
                }
                try {
                    Thread.sleep(SLEEP_TIME_MILLISECONDS);
                } catch (java.lang.InterruptedException interruptException) {
                    return;
                }
            }
        }

        if( parsedData == null ){

            parsingTask.handleParsingState( PARSING_STATE_FAILED );

        }else{
            parsingTask.setData( parsedData );

            parsingTask.handleParsingState( PARSING_STATE_COMPLETED );
        }

        parsingTask.setParsingThread(null);
        Thread.interrupted();
    }
}
