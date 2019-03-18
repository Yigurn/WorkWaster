package com.yigurn.workwaster;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {
    private PennyTimerService myService;
    private boolean bound = false;

    private long salaryInPence = 29_000_00;
    private int workDays = 260;
    private int hours = 8;
    private long earned;
    private long pennyTime;

    public TextView total;

    private SharedPreferences mPreferences;

    private static final String TAG = "pigs";

    public void calcPennyTime() {
        pennyTime =  (long) ((workDays * hours * 3600.0) / salaryInPence * 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate()");
        startService(new Intent(this, PennyTimerService.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.i(TAG,"onRestart()");
    }
    @Override
    protected void onStart() {
        super.onStart();
       //Log.i(TAG,"onStart()");
        Intent intent = new Intent(this, PennyTimerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"APP OPEN");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        earned = mPreferences.getLong("earned", -99);
        total = (TextView)findViewById(R.id.earned);
        total.setText(earned + " pence");
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG,"onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"APP CLOSED");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG,"onDestroy()");
        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            PennyTimerService.LocalBinder binder = (PennyTimerService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(MainActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void doSomething() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        earned = mPreferences.getLong("earned", -99);
        total = (TextView)findViewById(R.id.earned);
        total.setText(earned + " pence");

    }
}
