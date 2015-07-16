package com.yehancha.phonecare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.support.v4.app.NotificationCompat;

import com.myvission.yehancha.phonecare.R;

/**
 * Created by yehancha on 2015-07-15.
 */
public class ChargingNotifier {
    private Context context;

    public ChargingNotifier(Context context) {
        this.context = context;
    }

    public void onBatteryChange(Intent intent) {
        int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryCapacity = Math.round(batteryLevel / (float) batteryScale * 100);

        int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean batteryCharging = batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING;

        if (batteryCapacity <= Constants.BATTERY_CAPASITY_PLUG_IN && !batteryCharging) {
            notifyPlugIn();
        } else if (batteryCapacity >= Constants.BATTERY_CAPASITY_UNPLUG && batteryCharging) {
            notifyUnplug();
        } else {
            cancelNotification();
        }
    }

    private void notifyPlugIn() {
        notifyAction(android.R.drawable.ic_lock_idle_low_battery,
                context.getString(R.string.title_plugin_phone),
                context.getString(R.string.plugin_phone));
    }

    private void notifyUnplug() {
        notifyAction(android.R.drawable.ic_lock_idle_charging,
                context.getString(R.string.title_unplug_phone),
                context.getString(R.string.unplug_phone));
    }

    private void notifyAction(int smallIcon, String contetntTitle, String contentText) {
        Uri notficationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(smallIcon).setContentTitle(contetntTitle)
                .setContentText(contentText).setSound(notficationSoundUri)
                .setOnlyAlertOnce(true);

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuider = TaskStackBuilder.create(context);
        stackBuider.addParentStack(MainActivity.class);
        stackBuider.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuider.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_BATTTERY_CHARGING, notificationBuilder.build());
    }

    private void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.NOTIFICATION_ID_BATTTERY_CHARGING);
    }

}
