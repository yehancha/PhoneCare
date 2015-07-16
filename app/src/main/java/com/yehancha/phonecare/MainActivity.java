package com.yehancha.phonecare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvBatteryPercent;
    private TextView tvBatteryStatus;
    private TextView tvBatteryAction;

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPercent = Math.round(batteryLevel / (float) batteryScale * 100);

            tvBatteryPercent.setText(batteryPercent + "%");

            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isBatteryCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

            if (!isBatteryCharging) {
                tvBatteryStatus.setText(getString(R.string.battery_discharging));
            } else {
                tvBatteryStatus.setText(getString(R.string.battery_charging));
            }

            if (batteryPercent <= Constants.BATTERY_CAPASITY_PLUG_IN && !isBatteryCharging) {
                tvBatteryAction.setText(getString(R.string.plugin_phone));
            } else if (batteryPercent >= Constants.BATTERY_CAPASITY_UNPLUG && isBatteryCharging) {
                tvBatteryAction.setText(getString(R.string.unplug_phone));
            } else {
                tvBatteryAction.setText(getString(R.string.def_battery_action));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBatteryPercent = (TextView) findViewById(R.id.textview_battery_percent);
        tvBatteryStatus = (TextView) findViewById(R.id.textview_battery_status);
        tvBatteryAction = (TextView) findViewById(R.id.textview_battery_action);

        startService(new Intent(this, BatteryChangedReceiverService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryBroadcastReceiver, intentFilter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(batteryBroadcastReceiver);

        super.onPause();
    }
}
