package com.yigurn.workwaster;

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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {//implements ServiceCallbacks, View.OnClickListener {

    //third time lucky
    private long moneyEarned_pence;
    private int pennyTime_mil;


    private long salaryInPence = 29_000_00;
    private int workDays = 260;
    private int hours = 8;

    private TextView textEarned;
    private Counter counter = new Counter();
    private static final String TAG = "pigs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate()");
        calcPennyTime();

        textEarned = findViewById(R.id.textEarned);
        //textPointsReal = findViewById(R.id.textPointsReal);
        //random = new Random();
        // open();
        // startService(new Intent(this, PennyTimerService.class));
    }

    public void calcPennyTime() {
        pennyTime_mil = (int) ((workDays * hours * 3600f) / salaryInPence * 1000);
        //cps = salaryInPence / (workDays * hours * 3600f);
        Log.i(TAG, "pennyTime: " + Integer.toString(pennyTime_mil));
    }


    private void update() {
        moneyEarned_pence++;
        textEarned.setText(Long.toString(moneyEarned_pence));
        Log.i(TAG, "Earned: " + Long.toString(moneyEarned_pence));
    }


    public class Counter {
        private Timer timer;
        private TimerTask task;

        public Counter() {
            task = new TimerTask() {

                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            update();
                        }
                    });
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(task, 1000, 2583);
        }
    }
}
/*
    private TextView textPointsReal;
    private float gamePoints = 0;
    private float cps = 0;


    private float investScale = 1.01f;

    private Random random;

    private int[] upgradeImages = {R.drawable.water, R.drawable.water};
    private String[] upgradeNames = {"Water Bottle", "Pencil"};
    private String[] upgradeDescription = {"Stay Hydrated!", "Better than nothing"};

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.imgContainer) {
            Animation a = AnimationUtils.loadAnimation(this, R.anim.container_animation);
            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    containerClick();
                }
            });
            v.startAnimation(a);
        } else if (v.getId() == R.id.buttonUpgrades) {
            showUpgradeFragment();
            save();
        }
    }

    private void containerClick() {
        textPointsReal.setText(Float.toString(gamePoints++));
        showToast(R.string.clicked);
    }

    private void showToast(int stringID) {
        final Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER|Gravity.LEFT, random.nextInt(600) + 100, random.nextInt(600) - 300);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView textView = new TextView(this);
        textView.setText(stringID);
        textView.setTextSize(40f);
        textView.setTextColor(Color.BLACK);
        toast.setView(textView);
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();
            }

            @Override
            public void onFinish() {
                toast.cancel();
            }
        };
        toast.show();
        toastCountDown.start();
    }



    private PennyTimerService myService;
    private boolean bound = false;

    private long earned;

    public TextView total;

    private SharedPreferences mPreferences;




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showIdleFragment();

            //textPointsReal = findViewById(R.id.textPointsReal);
            random = new Random();
            //open();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showIdleFragment() {
        ViewGroup container = findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.activity_main, null));
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

        Intent intent = new Intent(this, PennyTimerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"APP OPEN");
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        earned = mPreferences.getLong("earned", -99);
        total = (TextView)findViewById(R.id.earned);
        total.setText(earned + " pence");
    }
    @Override
    protected void onPause() {
        super.onPause();
        save();
        //Log.i(TAG,"onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"APP CLOSED");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.i(TAG,"onDestroy()");
        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
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
        earned = mPreferences.getLong("earned", -99);
        total = (TextView)findViewById(R.id.earned);
        total.setText(earned + " pence");

    }


    private void save() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("cps", cps);
        editor.putFloat("gamePoints", gamePoints);
        editor.commit();
    }

    private void open() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        cps = preferences.getFloat("cps", 0);
        gamePoints = preferences.getFloat("gamePoints", 0);
    }

    private void updateCps(int amount) {
        cps += amount;
    }

    private void updatePoints(int amount) {
        gamePoints -= amount;
    }


    private void showUpgradeFragment() {
        ViewGroup container = findViewById(R.id.container);
        UpgradeAdapter upgradeAdapter = new UpgradeAdapter();
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.activity_upgrades, null));
        ((ListView)findViewById(R.id.listUpgrades)).setAdapter(upgradeAdapter);
    }

    public class UpgradeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return upgradeImages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_listview, null);

            ((ImageView)convertView.findViewById(R.id.imgItem)).setImageResource(upgradeImages[position]);
            ((TextView)convertView.findViewById(R.id.textName)).setText(upgradeNames[position]);
            ((TextView)convertView.findViewById(R.id.textDescription)).setText(upgradeDescription[position]);

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (getCount() == 1) {
                        if (gamePoints >= 100) {
                            updateCps(100);
                            updatePoints(100);
                            save();
                        } else {
                            (new AlertDialog.Builder(MainActivity.this)).setMessage("You don't have enough!")
                                    .show();
                        }
                    }
                }
            });

            return convertView;
        }
    }
}*/
