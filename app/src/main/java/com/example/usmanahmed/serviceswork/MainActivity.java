package com.example.usmanahmed.serviceswork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnactive,btndeactive;
    TextView tvprecentage;
    EditText etseconds;
    static int timer=1;
    String string;
    Intent intent=null;
    MyRecevier myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvprecentage = findViewById(R.id.etpercentage);
        etseconds = findViewById(R.id.etno);
        btnactive = findViewById(R.id.btnactive);
        btndeactive = findViewById(R.id.btndeactive);

        btnactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                string = (etseconds.getText().toString());
                timer = Integer.parseInt(string);
                startService();

            }
        });
        btndeactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService();
            }
        });

    }
    private void startService() {
        intent = new Intent(MainActivity.this, MyService.class);
        startService(intent);
    }

    private void stopService() {
        if (intent != null) {
            stopService(intent);
        }
        intent = null;
    }

    @Override
    protected void onStart() {
        myReceiver = new MyRecevier();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.ACTION_UPDATE_CNT);
        registerReceiver(myReceiver, intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }


    class MyRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(MyService.ACTION_UPDATE_CNT)) {
                int int_from_service = intent.getIntExtra(MyService.KEY_INT_FROM_SERVICE, 0);
                float per = int_from_service;
                per = per / timer * 100;
                int k = Math.round(per);
                tvprecentage.setText(k + "%");
            }

        }

    }

   }