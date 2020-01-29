package com.rwee.mobilegatewaywo.engines;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by Robert on 09/12/15.
 */
public class ASynchronousThreadExecutor{
    ExecutorService threadPool = null;
    List< Runnable > runnables;

    public ASynchronousThreadExecutor( int poolSize ){
        threadPool = Executors.newFixedThreadPool( poolSize );
        runnables = new ArrayList<>(  );
    }

    public void addTaskToRun( Runnable runnable ){
        if( !runnables.contains( runnable ) ){
            runnables.add( runnable );
        }
    }

    public void submitAllTasks() throws ExecutionException, InterruptedException{
        for( Runnable r : runnables ){
            if( r != null ){
                Future futureTask = threadPool.submit( r );
                futureTask.get();
            }
        }
    }

}
