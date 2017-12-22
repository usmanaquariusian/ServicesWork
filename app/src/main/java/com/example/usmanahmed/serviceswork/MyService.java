package com.example.usmanahmed.serviceswork;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service { int val;
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    final static String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";

    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;
    int c = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(getApplicationContext(),
                "onCreate", Toast.LENGTH_LONG).show();
        myServiceReceiver = new MyServiceReceiver();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),
                "Activated", Toast.LENGTH_LONG).show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSG_TO_SERVICE);
        registerReceiver(myServiceReceiver, intentFilter);

        myServiceThread = new MyServiceThread();
        myServiceThread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(),
                "Deactivated", Toast.LENGTH_LONG).show();
        myServiceThread.setRunning(false);
        unregisterReceiver(myServiceReceiver);
        super.onDestroy();
    }

    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ACTION_MSG_TO_SERVICE)) {

                String msg = intent.getStringExtra(KEY_MSG_TO_SERVICE);
                val = Integer.parseInt(msg);
                Log.d("MTAG", "onReceive: " + val);
            }
        }
    }

    private class MyServiceThread extends Thread {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            c = 0;
            running = true;
            while (running && c <= MainActivity.timer) {
                try {
                    Thread.sleep(1000);

                    Intent intent = new Intent();
                    intent.setAction(ACTION_UPDATE_CNT);
                    intent.putExtra(KEY_INT_FROM_SERVICE, c);
                    sendBroadcast(intent);

                    c++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}