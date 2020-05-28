package com.component.randomnumberservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startServiceBtn = findViewById(R.id.startServiceBTN);
        Button stopServiceBtn = findViewById(R.id.stopServiceBTN);

        serviceIntent = new Intent(getApplicationContext(),MyServiceClass.class);
        
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTheService();
            }
        });
        
        stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTheService();
                
            }
        });
    }


    private void startTheService() {
        startService(serviceIntent);
    }
    
    private void stopTheService() {
        stopService(serviceIntent);
    }


}
