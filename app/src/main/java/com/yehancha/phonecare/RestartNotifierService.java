package com.yehancha.phonecare;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.myvission.yehancha.phonecare.R;

public class RestartNotifierService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri notficationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_lock_power_off).setContentTitle(getString(R.string.title_restart_phone))
                .setContentText(getString(R.string.restart_phone));

//        Intent resultIntent = new Intent(this, MainActivity.class);
//
//        TaskStackBuilder stackBuider = TaskStackBuilder.create(this);
//        stackBuider.addParentStack(MainActivity.class);
//        stackBuider.addNextIntent(resultIntent);
//
//        PendingIntent resultPendingIntent = stackBuider.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        notificationBuilder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_BATTTERY_CHARGING, notificationBuilder.build());

        stopSelf();
        return START_NOT_STICKY; // No need to start again
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
