package com.bannet.skils.Fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bannet.skils.Activitys.ActivityAdminPageUrl;
import com.bannet.skils.bottom.activity.ActivityBottom;
import com.bannet.skils.chat.activity.ActivityChatList;
import com.bannet.skils.notification.activity.ActivityNotification;
import com.bannet.skils.postingdetails.activity.ActivityPostingsDetailsScreen;
import com.bannet.skils.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            //handle the data message here
        }
        //getting the title and the
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String status = remoteMessage.getData().get("badge");
        String sound = remoteMessage.getData().get("sound");
        String opp_user_id = remoteMessage.getData().get("opp_user_id");
        String notification_id = remoteMessage.getData().get("id");
        String post_id = remoteMessage.getData().get("post_id");

        Log.e("FirebaseMessage","FCM\n"
                +"\nTitle: "+title
                +"\nBody: "+body
                +"\nStatus: "+status
                +"\nnotification_id: "+notification_id
                +"\npost_id: "+post_id
                +"\n opp_user_id:"+opp_user_id);
        sendNotification(body,title,status,sound,opp_user_id,notification_id,post_id);

    }

    private void sendNotification(String messageBody, String title, String status, String sound, String opp_user_id, String notification_id,String post_id) {

        PendingIntent pendingIntent = null;
        Intent intent = null;

        Uri defaultSoundUri;

        if (status.equals("1")) {
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            intent = new Intent(this, ActivityBottom.class);
            intent.putExtra("notification_id",notification_id);
            intent.putExtra("type","1");
            startService(intent);

//            Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + com.hbb20.R.raw.ccp_danish);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), rawPathUri);
//            r.play();
            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            int deviceVersion  ;
            deviceVersion = Integer.parseInt(Build.VERSION.RELEASE+"");
            Log.e("deviceVersion", String.valueOf(deviceVersion));

            if(deviceVersion >= 12){

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
            else {

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

            String channelId = "12";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.application_name)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build());
        }
        else if(status.equals("2")){

            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            intent = new Intent(this, ActivityChatList.class);
            intent.putExtra("notification_id",notification_id);
            startService(intent);

            int deviceVersion  ;
            deviceVersion = Integer.parseInt(Build.VERSION.RELEASE+"");
            Log.e("deviceVersion", String.valueOf(deviceVersion));

            if(deviceVersion >= 12){

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
            else {

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

            }


//            Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + com.hbb20.R.raw.ccp_danish);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), rawPathUri);
//            r.play();

            String channelId = "12";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.application_name)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build());

        }
        else if(status.equals("3")){

            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            intent = new Intent(this, ActivityPostingsDetailsScreen.class);
            intent.putExtra("notification_id",notification_id);
            intent.putExtra("post_id",post_id);
            startService(intent);

            int deviceVersion  ;
            deviceVersion = Integer.parseInt(Build.VERSION.RELEASE+"");


            if(deviceVersion >= 12){

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
            else {

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

//            Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + com.hbb20.R.raw.ccp_danish);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), rawPathUri);
//            r.play();

            String channelId = "12";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.application_name)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build());
        }
        else if(status.equals("4")){

            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            intent = new Intent(this, ActivityAdminPageUrl.class);
            intent.putExtra("notification_id",notification_id);
            startService(intent);


            int deviceVersion  ;
            deviceVersion = Integer.parseInt(Build.VERSION.RELEASE+"");


            if(deviceVersion >= 12){

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
            else {

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

//            Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + com.hbb20.R.raw.ccp_danish);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), rawPathUri);
//            r.play();

            String channelId = "12";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.application_name)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build());

        }
        else if(status.equals("5")){

            defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

            intent = new Intent(this, ActivityNotification.class);
            intent.putExtra("notification_id",notification_id);
            startService(intent);


            int deviceVersion  ;
            deviceVersion = Integer.parseInt(Build.VERSION.RELEASE+"");


            if(deviceVersion >= 12){

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
            else {

                pendingIntent = PendingIntent.getActivity(this, 0  /*Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);
            }

//            Uri rawPathUri = Uri.parse("android.resource://" + getPackageName() + "/" + com.hbb20.R.raw.ccp_danish);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), rawPathUri);
//            r.play();

            String channelId = "12";

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.application_name)
                            .setContentTitle(title)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0  /*ID of notification*/, notificationBuilder.build());

        }

        else{
            intent = new Intent(this, ActivityBottom.class);
            startService(intent);
        }
    }
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}