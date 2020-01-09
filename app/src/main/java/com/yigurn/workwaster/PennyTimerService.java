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

    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

        ScreenReceiver receiver = new ScreenReceiver();
        registerReceiver(receiver, intentFilter);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
