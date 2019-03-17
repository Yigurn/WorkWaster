package com.yigurn.workwaster;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PennyTimer extends Service {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //Interval time
    public static final long PENNY_INTERVAL = (long) ((260 * 8 * 3600.0) / 29_000_00 * 1000);

    private long earned;
    public boolean screenOn;
    private ScreenReceiver mReceiver;

    //Run on a separate thread
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();


        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, intentFilter);



        //cancel if exists
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new PennyTimeTimerTask(), 0, PENNY_INTERVAL);
    }
    class PennyTimeTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    earned = mPreferences.getLong("earned", 0);
                    screenOn = mPreferences.getBoolean("screenOn", true);
                    if (screenOn) {
                        mEditor.putLong("earned", ++earned);
                    }
                    //if phone is on
                    //  if 9-5
                    //      drop
                    mEditor.commit();
                    Log.i("pigs","Earned:" + earned);

                }
            });
        }
    }
}
