package com.yigurn.workwaster;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ServiceCallbacks {
    private long moneyEarned_pence;

    private long salaryInPence = 29_000_00;
    private int workDays = 260;
    private int hours = 8;

    private boolean bound = false;
    private Intent intent;

    private TextView textEarned;
    private ImageView onePence;

    private PennyTimerService myService;
    private SharedPreferences mPreferences;
    private NumberFormat currencyUK = NumberFormat.getCurrencyInstance(Locale.UK);

    private static final String TAG = "pigs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        moneyEarned_pence = mPreferences.getLong("earned", 0);
       // textEarned.setText(currencyUK.format(moneyEarned_pence/100f));
        intent = new Intent(this, PennyTimerService.class);

        stopService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        Log.i(TAG,"onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"APP CLOSED");
        startService(new Intent(this, PennyTimerService.class));
        intent = new Intent(this, PennyTimerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy()");

        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }


    public int calcPennyTime() {
        return (int) ((workDays * hours * 3600f) / salaryInPence * 1000);
    }


    public void update() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(onePence, "translationY", 1000f);
        animation.setDuration(800);
        animation.start();

        //moneyEarned_pence++;
        textEarned.setText(currencyUK.format(moneyEarned_pence/100f));
        Log.i(TAG, "Earned: " + Long.toString(moneyEarned_pence));
    }

    private void save() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("Earned", moneyEarned_pence);
        editor.commit();
    }

    private void open() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        moneyEarned_pence = preferences.getLong("Earned", 0);
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
        moneyEarned_pence = mPreferences.getLong("earned", 0);
        textEarned.setText(currencyUK.format(moneyEarned_pence/100f));

    }
}