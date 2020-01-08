package com.yigurn.workwaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    long startTime;
    long endTime;
    long storedTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            mEditor.putBoolean("screenOn", true);
            Log.i("pigs","SCREEN ON");
            startTime = System.currentTimeMillis();
            Log.i("pigs", Long.toString(startTime));
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            mEditor.putBoolean("screenOn", false);
            Log.i("pigs", "SCREEN OFF");
            endTime = System.currentTimeMillis();
            storedTime += endTime - startTime;
            Log.i("pigs", Long.toString(endTime));
            Log.i("pigs", "Total Time: " + Long.toString(storedTime));
        }
        mEditor.apply();
    }
}