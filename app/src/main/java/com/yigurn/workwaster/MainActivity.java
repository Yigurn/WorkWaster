package com.yigurn.workwaster;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private long moneyEarned_pence;

    private long salaryInPence = 29_000_00;
    private int workDays = 260;
    private int hours = 8;

    private TextView textEarned;
    private ImageView onePence;

    long storedTime = 0;
    long totalTime;



    private SharedPreferences preferences;
    private NumberFormat currencyUK = NumberFormat.getCurrencyInstance(Locale.UK);

    private static final String TAG = "pigs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, PennyTimerService.class));

        textEarned = findViewById(R.id.textEarned);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("startTime", System.currentTimeMillis());
        editor.apply();
        totalTime = preferences.getLong("totalTime", 0);
        moneyEarned_pence = earned();
        textEarned.setText(currencyUK.format(moneyEarned_pence/100f));
    }

    private long earned() {
        Log.i(TAG, "Total Time: " + Long.toString(totalTime));
        return (long)(salaryInPence * totalTime / (workDays * hours * 60.0 * 60 * 1000));
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
        Log.i(TAG,"APP OPEN");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        storedTime = preferences.getLong("storedTime", 0);
        totalTime = preferences.getLong("totalTime", 0);
        totalTime += storedTime;
        storedTime = 0;
        editor.putLong("storedTime", storedTime);
        editor.putLong("totalTime", totalTime);
        editor.apply();

        moneyEarned_pence = earned();
        Log.i(TAG, "Earned: " + Long.toString(moneyEarned_pence));
        textEarned.setText(currencyUK.format(moneyEarned_pence/100f));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"APP CLOSED");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");

    }
}