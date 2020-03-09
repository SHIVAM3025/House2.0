package com.dtu.house.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.dtu.house.MainActivity;
import com.dtu.house.R;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.core.app.NotificationCompat;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    private static final String TAG = "Firebasemessaging";

    public FirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload" + remoteMessage.getData());
            JSONObject data = new JSONObject(remoteMessage.getData());
            String jsonMessage = null;
            try {


                jsonMessage = data.getString("extra_information");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "onMesssageReceived: \n" +
                    "Extra Information" + jsonMessage);

        }

        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();


            sendNotification(title, message, click_action);
        }
    }

    private void sendNotification(String title, String message, String click_action) {

        Intent intent;
        intent = new Intent(this , MainActivity.class);
       // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );


        PendingIntent pdd = PendingIntent.getActivity(this , 0 , intent  , PendingIntent.FLAG_UPDATE_CURRENT);



        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this ,getString(R.string.channel_one) )
                .setSmallIcon(R.drawable.ic_action_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(defaultSound)
                .setAutoCancel(true)
                .setContentIntent(pdd);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis()/* Id of notification*/, notificationBuilder.build());


    }


}
