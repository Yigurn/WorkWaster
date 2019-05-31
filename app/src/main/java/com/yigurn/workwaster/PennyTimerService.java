package com.yigurn.workwaster;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class PennyTimerService extends Service {

    private final IBinder binder = new LocalBinder();
    private ServiceCallbacks serviceCallbacks;

    class LocalBinder extends Binder {
        PennyTimerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PennyTimerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //Interval time
    public static final long PENNY_INTERVAL = (long) ((260 * 8 * 3600.0) / 29_000_00 * 1000);

    private long earned;
    public boolean screenOn;

    //Run on a separate thread
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public void onCreate() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        ScreenReceiver mReceiver = new ScreenReceiver();
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
                    mEditor = mPreferences.edit();
                    earned = mPreferences.getLong("earned", 0);
                    screenOn = mPreferences.getBoolean("screenOn", true);
                    if (screenOn) {
                        mEditor.putLong("earned", ++earned);
                        Log.i("pigs","Earned:" + earned);
                    }
                    //  if 9-5
                    //      if on app
                    //      drop
                    mEditor.apply();
                    if (serviceCallbacks != null) {
                        serviceCallbacks.doSomething();
                    }
                }
            });
        }
    }
}
