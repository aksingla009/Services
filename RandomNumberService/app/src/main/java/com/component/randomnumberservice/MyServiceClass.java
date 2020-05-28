package com.component.randomnumberservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Random;

public class MyServiceClass extends Service {
    public static final String TAG = "ServiceDemo MyService";
    private int randomNumber = 0;
    private boolean isRandomNumberGeneratorON;

    private int MIN = 0;
    private int MAX = 100;

    private static final int GET_RANDOM_NUMBER_FLAG = 0;

    private class RandomNumberRequestHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case GET_RANDOM_NUMBER_FLAG:
                    Message messageContent = Message.obtain(null, GET_RANDOM_NUMBER_FLAG); // GEt HOLD of the msg
                    messageContent.arg1 = getRandomNumber();

                    try {
                        msg.replyTo.send(messageContent);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;


            }

            super.handleMessage(msg);
        }
    }

    private Messenger randomNumberMessenger = new Messenger(new RandomNumberRequestHandler());


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return randomNumberMessenger.getBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: on thread " + Thread.currentThread().getName() + " with ID " + Thread.currentThread().getId());
        isRandomNumberGeneratorON = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();


        return START_STICKY;
    }

    private void startRandomNumberGenerator() {
        while (isRandomNumberGeneratorON) {
            try {
                Thread.sleep(1000);
                if (isRandomNumberGeneratorON) {
                    Log.i(TAG, "Random Number Generating on " + Thread.currentThread().getName() + " with ID " + Thread.currentThread().getId());
                    randomNumber = new Random().nextInt(MAX);
                    Log.i(TAG, "Random Number is " + randomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private void stopRandomNumberGenerator() {
        isRandomNumberGeneratorON = false;

    }


    public int getRandomNumber() {
        return randomNumber;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        stopRandomNumberGenerator();

    }
}
