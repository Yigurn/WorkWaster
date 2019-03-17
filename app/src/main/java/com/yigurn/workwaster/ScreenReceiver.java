package com.yigurn.workwaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onReceive(Context context, Intent intent) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreferences.edit();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            mEditor.putBoolean("screenOn", true);
            Log.i("pigs","SCREEN ON");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            mEditor.putBoolean("screenOn", false);
            Log.i("pigs", "SCREEN OFF");
        }
        mEditor.apply();
    }
}