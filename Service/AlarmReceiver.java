package com.greenapex.callhelper.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.greenapex.callhelper.R;
import com.greenapex.callhelper.Util.Pref;

import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by GreenApex on 21/12/17.
 */

public class AlarmReceiver extends BroadcastReceiver {


    String number, name, note;
    int notificationId = (int) System.currentTimeMillis();
    Vibrator mVibrator;
    int strVibrate;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        number = intent.getStringExtra("myId");
        name = intent.getStringExtra("myName");
        note = intent.getStringExtra("myNote");
        Log.d("CallHelper", "Alarm Done!!!");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           // String id = "w01", name = getString(R.string.weather_notification_title);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
           // String desc = getString(R.string.weather_notification_description);

            NotificationChannel channel = new NotificationChannel(number, name, importance);
            channel.setDescription(note);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context , number);
        }else {
            builder = new NotificationCompat.Builder(context);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        Intent intent1 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        builder.setContentIntent(pendingIntent);

        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + number));






        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent2, 0);
        builder.setContentIntent(pendingIntent);


        strVibrate = Integer.parseInt(Pref.getValue(context, "vibrator", "1"));

        if (strVibrate == 0) {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(000);
            long[] v = {0, 000};
            builder.setVibrate(v);
        } else if (strVibrate == 1) {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(1000);
            long[] v = {0, 1500};
            builder.setVibrate(v);
        } else if (strVibrate == 2) {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(500);
            long[] v = {0, 750};
            builder.setVibrate(v);
        } else if (strVibrate == 3) {
            mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            mVibrator.vibrate(2000);
            long[] v = {0, 3000};
            builder.setVibrate(v);
        }

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(note));
        if (name.isEmpty() || name.equalsIgnoreCase("") || name.equalsIgnoreCase("unknown")) {
            builder.setContentTitle(number);
        } else {
            builder.setContentTitle(name);
        }
        builder.setContentText(note);
        builder.setAutoCancel(true);
        builder.setCategory(Notification.CATEGORY_CALL);
        builder.addAction(R.drawable.ic_phone_black_24dp, "Call", pendingIntent);
        builder.addAction(R.drawable.ic_sms_black_24dp, "SMS", pendingIntent1);
        if (!TextUtils.isEmpty(Pref.getValue(context, "Ringtoneset", null))) {
            builder.setSound(Uri.parse(Pref.getValue(context, "Ringtoneset", null)));
        } else {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }
        builder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        builder.setLights(Color.BLUE, 2000, 2000);
        builder.setAutoCancel(false);

        notificationManager.notify(notificationId, builder.build());
    }
}
