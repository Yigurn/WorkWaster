package com.yigurn.workwaster;

import android.content.Intent;
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
    }
    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG,"onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"onStop()" + startTime);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG,"onDestroy()");
    }

    @Override

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("starttime", startTime);
    }


}
