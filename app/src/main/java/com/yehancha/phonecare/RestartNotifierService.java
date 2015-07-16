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
import android.widget.Toast;

import com.yehancha.phonecare.R;

public class RestartNotifierService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_lock_power_off).setContentTitle(getString(R.string.title_restart_phone))
                .setContentText(getString(R.string.restart_phone));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID_PHONE_RESTART, notificationBuilder.build());

        stopSelf();
        return START_NOT_STICKY; // No need to start again
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
