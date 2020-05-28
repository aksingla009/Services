package com.component.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {
    public static final String TAG = "ServiceDemo MyService";
    private int randomNumber = 0;
    private boolean isRandomNumberGeneratorON = false;

    private int MIN = 0;
    private int MAX = 100;

    class MyServiceBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }

    }

    private IBinder mBinder = new MyServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: on thread "+ Thread.currentThread().getName() +" with ID "+ Thread.currentThread().getId());
        isRandomNumberGeneratorON = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        stopRandomNumberGenerator();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void startRandomNumberGenerator(){
        while (isRandomNumberGeneratorON){
            try {
                Thread.sleep(1000);
                if(isRandomNumberGeneratorON){
                    Log.i(TAG, "Random Number Generating on "+ Thread.currentThread().getName() +" with ID "+ Thread.currentThread().getId());
                    randomNumber = new Random().nextInt(MAX);
                    Log.i(TAG, "Random Number is "+ randomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private void stopRandomNumberGenerator(){
        isRandomNumberGeneratorON= false;

    }


    public int getRandomNumber(){
        return randomNumber;
    }
}
