package com.component.servicedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.component.servicedemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "ServiceDemo MainActivty";
    private Button startServiceBtn, stopServiceBtn , bindServiceBtn, unbindServiceBtn;

    private ActivityMainBinding activityMainBinding;

    private Intent serviceIntent;
    private MyService myService;
    private ServiceConnection serviceConnection;

    private boolean isServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Log.i(TAG, "onCreate:  on thread "+Thread.currentThread().getName() + " with ID "+ Thread.currentThread().getId());

        serviceIntent = new Intent(getApplicationContext(),MyService.class);



       activityMainBinding.startServiceBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startTheService();
           }
       });

        activityMainBinding.stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheService();
            }
        });

        activityMainBinding.bindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindTheService();
            }
        });
        activityMainBinding.unBindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindTheService();
            }
        });
        activityMainBinding.getRandomNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandomNumber();
            }
        });

    }

    private void stopTheService() {
        stopService(serviceIntent);
    }

    private void bindTheService() {
        if(serviceConnection == null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    isServiceBound = true;
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) binder;
                    myService = myServiceBinder.getService();

                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;

                }
            };

        }
        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);

    }

    private void unbindTheService() {
        if(isServiceBound){
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNumber() {
        if(isServiceBound){
            activityMainBinding.randomNumberTV.setText("Random Number "+ myService.getRandomNumber());
        }else{
            activityMainBinding.randomNumberTV.setText("Service is Not BOUND");
        }


    }

    public void startTheService(){
        startService(serviceIntent);
    }
}
