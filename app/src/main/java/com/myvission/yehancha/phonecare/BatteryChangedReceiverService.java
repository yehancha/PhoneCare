package com.myvission.yehancha.phonecare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class BatteryChangedReceiverService extends Service {
    private ChargingNotifier chargingNotifier;

    private BroadcastReceiver batteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            chargingNotifier.onBatteryChange(intent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        chargingNotifier = new ChargingNotifier(this);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "BatteryNotifiationService starting", Toast.LENGTH_SHORT).show();

        return START_STICKY;    // we don't have an intent to deliver
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We do not bind
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(batteryBroadcastReceiver);

        super.onDestroy();
    }
}