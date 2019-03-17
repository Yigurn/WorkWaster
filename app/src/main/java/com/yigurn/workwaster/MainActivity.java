package com.yigurn.workwaster;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private long salaryInPence = 29_000_00;
    private int workDays = 260;
    private int hours = 8;
    private long earned;
    private long pennyTime;

    private long startTime;
    private long currTimePassed;
    private long totalTimePassed;

    private static final String TAG = "pigs";
    private ScreenReceiver mReceiver;

    public long getCurrTime() {
        return currTimePassed;
    }

    public long calcTime() {
        Calendar c = Calendar.getInstance();
        if (startTime != 0) {
            currTimePassed += c.getTimeInMillis() - startTime;
            //startTime = 0;
        }
        return currTimePassed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime() {
        Calendar c = Calendar.getInstance();
        this.startTime = c.getTimeInMillis();
    }
    public void calcPennyTime() {
        pennyTime =  (long) ((workDays * hours * 3600.0) / salaryInPence * 1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"onCreate()");
        calcPennyTime();

        startService(new Intent(this, PennyTimer.class));

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, intentFilter);


        SharedPreferences sharedPref= getSharedPreferences("prefs", 0);
        long earned = sharedPref.getLong("earned", 99);

        TextView total = (TextView)findViewById(R.id.earned);
        total.setText(earned + " pence");
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
    }
    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG,"onResume()");
        calcTime();
        Log.i(TAG,"Current: " + String.valueOf(currTimePassed));

        totalTimePassed += currTimePassed;
        currTimePassed = 0;
        Log.i(TAG,"Total: " + String.valueOf(totalTimePassed));
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG,"onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Calendar c = Calendar.getInstance();
        startTime = c.getTimeInMillis();
        Log.i(TAG,"onStop()" + startTime);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG,"onDestroy()");
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("starttime", startTime);
    }


}
