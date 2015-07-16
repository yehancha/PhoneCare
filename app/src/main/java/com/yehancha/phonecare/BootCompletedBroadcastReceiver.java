package com.yehancha.phonecare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class BootCompletedBroadcastReceiver extends WakefulBroadcastReceiver {
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent restertServiceIntent = new Intent(context, RestartNotifierService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, restertServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME, ONE_DAY, pendingIntent);

        Intent batteryChangedServiceIntent = new Intent(context, BatteryChangedReceiverService.class);
        startWakefulService(context, batteryChangedServiceIntent);
    }
}
