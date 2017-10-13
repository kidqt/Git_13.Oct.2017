package com.example.administrator.runalarm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 02/10/2017.
 */

public class AlarmReceiverFixTime extends BroadcastReceiver {
    public Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String mHour = sdf.format(new Date());
        if (mHour.equals("07"))
        {
            showNotification("Notice","Please Check Today Schedules !!");
        }
        else  if(mHour.equals("17"))
        {
            showNotification("Notice","Please Check Today Results !!");
        }
    }

    private  void showNotification(String title,String body)
    {
        Intent intent=new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        try {
            Ringtone r = RingtoneManager.getRingtone(context, sound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.vintrig)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setTicker(body)
                .setContentIntent(pendingIntent)
                .setPriority(2)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        try {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            wl.acquire(15000);

            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());
        }
        catch (Exception ex)
        {
            Log.i("LOI_DOC_TIN", ex.toString());
        }
    }
}
