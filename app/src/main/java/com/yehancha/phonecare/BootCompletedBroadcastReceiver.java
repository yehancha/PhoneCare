package com.yehancha.phonecare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {
    private static final int ONE_DAY = 24 * 60 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(context, RestartNotifierService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.ELAPSED_REALTIME, ONE_DAY, pendingIntent);
    }
}
