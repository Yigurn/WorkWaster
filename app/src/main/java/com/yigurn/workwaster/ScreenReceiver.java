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
    long storedTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        storedTime = preferences.getLong("storedTime", 0);
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            editor.putBoolean("screenOn", true);
            startTime = System.currentTimeMillis();
            editor.putLong("startTime", startTime);
            Log.i("pigs","SCREEN ON");
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            editor.putBoolean("screenOn", false);
            startTime = preferences.getLong("startTime", System.currentTimeMillis());
            endTime = System.currentTimeMillis();
            storedTime += endTime - startTime;
            editor.putLong("endTime", endTime);
            editor.putLong("storedTime", storedTime);
            Log.i("pigs", "SCREEN OFF");
            Log.i("pigs", "Stored Time: " + Long.toString(storedTime));
        }
        editor.apply();
    }
}