package com.component.randomnumberconsumer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG = "ServiceDemo Consumer";

    private int mRandomValue;
    private Intent serviceIntent;
    TextView randomNumberTV;
    private Context mContext;

    private boolean mIsServiceBound;

    private Messenger randomNumberRequestMessenger, randomNumberReceiveMesseneger;
    private static final int GET_RANDOM_NUMBER_FLAG = 0;

    class ReceiveRandomNumberHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            mRandomValue = 0;

            switch (msg.what) {
                case GET_RANDOM_NUMBER_FLAG:
                    mRandomValue = msg.arg1;
                    randomNumberTV.setText("Random number is "+ mRandomValue);

                    break;

                default:
                    break;
            }


            super.handleMessage(msg);
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            randomNumberRequestMessenger = new Messenger(service);
            randomNumberReceiveMesseneger = new Messenger(new ReceiveRandomNumberHandler());
            mIsServiceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            randomNumberRequestMessenger = null;
            randomNumberReceiveMesseneger = null;
            mIsServiceBound = false;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        randomNumberTV = findViewById(R.id.randomNumberTV);
        Button bindServiceBtn = findViewById(R.id.bindServiceBtn);
        Button unBindServiceBtn = findViewById(R.id.unBindServiceBtn);
        Button getRandomNumberBtn = findViewById(R.id.getRandomNumberBtn);
        mContext = getApplicationContext();

        serviceIntent = new Intent();
        serviceIntent.setComponent(new ComponentName("com.component.randomnumberservice","com.component.randomnumberservice.MyServiceClass"));


        bindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindToRemoteService();

            }
        });


        unBindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
                Log.i(TAG, "onservice unBind: ");
                mIsServiceBound = false;

            }
        });


        getRandomNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchRandomNumber();

            }
        });

    }

    private void fetchRandomNumber() {
        if(mIsServiceBound){
            Message requestMessage = Message.obtain(null,GET_RANDOM_NUMBER_FLAG);
            requestMessage.replyTo = randomNumberReceiveMesseneger;
            try {
                randomNumberRequestMessenger.send(requestMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }else{
            Toast.makeText(mContext,"Comnsumer Application is not bound ",Toast.LENGTH_SHORT).show();
        }
    }

    private void bindToRemoteService() {
        bindService(serviceIntent,serviceConnection,BIND_AUTO_CREATE);
        Log.i(TAG, "bindToRemoteService: invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serviceConnection = null;
    }
}
