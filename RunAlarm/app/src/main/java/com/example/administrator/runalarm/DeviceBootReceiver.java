package com.example.administrator.runalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeviceBootReceiver extends BroadcastReceiver {
    public PendingIntent pendingIntent2;
    public PendingIntent pendingIntent3;
    public Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
            /* Setting the alarm here */
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1001, alarmIntent, 0);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            int interval = 8000;
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);

            startAt10();
            startAt20();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                String mTine = sdf.format(new Date());
                File sdcard = Environment.getExternalStorageDirectory();
                File fileA = new File(sdcard, "SynopexApp/alarmSet.txt");
                FileOutputStream fOut = new FileOutputStream(fileA);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append(mTine);
                myOutWriter.close();
                fOut.close();
            }
            catch (Exception ex)
            {
                Toast.makeText(context,"Line 42 -- " + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startAt10() {
        Intent alarmIntent2 = new Intent(context, AlarmReceiverFixTime.class);
        pendingIntent2 = PendingIntent.getBroadcast(context, 1002, alarmIntent2, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND,20);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent2);
    }

    public void startAt20()
    {
        Intent alarmIntent3 = new Intent(context, AlarmReceiverFixTime.class);
        pendingIntent3 = PendingIntent.getBroadcast(context, 1003, alarmIntent3, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 17);
        calendar2.set(Calendar.MINUTE, 20);
        calendar2.set(Calendar.SECOND,00);

        manager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent3);
    }
}