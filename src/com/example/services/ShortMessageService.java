package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by kbushko on 11/25/13.
 */
public class ShortMessageService extends Service{

    ExecutorService executorService;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        executorService = Executors.newFixedThreadPool(10);
        Log.i("info"," - ShortMessageService [ onCreate ]");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("info"," - ShortMessageServices [ onDestroy ]");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        Log.i("info"," - ShortMessageService [ onStartCommand ]");
        executorService.execute(new runnableTask(startId));
        //return super.onStartCommand(intent, flag, startId);
        return START_REDELIVER_INTENT;
    }


    private void runTask() {
        Log.i("info"," Run Task");
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(i<10) {
                    Log.i("info"," i = " + Integer.toString(i));
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        }).start();
    }

    class runnableTask implements Runnable {
        int startId = 0;
        public runnableTask(int startId){
            this.startId = startId;
        }

        @Override
        public void run() {
            for(int i=0; i<30; i++) {
                Log.i("info",Integer.toString(startId) + " : i = " + Integer.toString(i));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            stopSelf(startId);
        }
    }
}
