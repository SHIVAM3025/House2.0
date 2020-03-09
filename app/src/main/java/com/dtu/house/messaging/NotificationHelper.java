package com.dtu.house.messaging;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.dtu.house.R;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    public static final String channelL1TD = "channelL1TD";
    public static final String channelL1Name = "Channel 1";


    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            cretaeChannels();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void cretaeChannels() {
        NotificationChannel channel1 = new NotificationChannel(channelL1TD , channelL1Name , NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);




    }



    public NotificationManager getManager()
    {
        if(manager == null)
        {
            manager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return manager;

    }

    public  NotificationCompat.Builder getChannel1Notification(String title , String message , PendingIntent pdf)
    {

        Uri alaram_sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getApplicationContext() , channelL1TD)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(alaram_sound)
                .setSmallIcon(R.drawable.ic_action_logo)
                .setContentIntent(pdf)
                ;
    }




}
