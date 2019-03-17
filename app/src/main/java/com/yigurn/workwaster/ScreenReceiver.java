package com.yigurn.workwaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class ScreenReceiver extends BroadcastReceiver {
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    MainActivity main = new MainActivity();

    @Override
    public void onReceive(Context context, Intent intent) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPreferences.edit();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            mEditor.putBoolean("screenOn", true);

            main.setStartTime();

            Log.i("pigs","ON: " + String.valueOf(main.getStartTime()));
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            mEditor.putBoolean("screenOn", false);
            Calendar c = Calendar.getInstance();
            Log.i("pigs", "start: " + (main.getStartTime()));
            Log.i("pigs", "curre: " + c.getTimeInMillis());

            Log.i("pigs", "OFF: " + String.valueOf(main.calcTime()));
        }
        mEditor.commit();
    }
}
