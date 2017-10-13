package com.example.administrator.runalarm;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import android.os.AsyncTask;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public final static String mURL = "http://www.snpx.somee.com/WebService.asmx?WSDL";
    public String Dept;
    public final static String NAMESPACE = "http://tempuri.org/";
    public int Calen=-11, Refer, Not, Qa, Mas, Bu,ReadMas=-11,ReadQa=-11,ReadNot=-11,ReadBu=-11,ReadRefer=-11,ReadCalen=-11;
    public String listdeleCalen="",listdeleRefer="",listdeleNot="",listdeleQa="",listdeleMas="",listdeleBu="";
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
        Calen = MainActivity.Calen;
        Refer = MainActivity.Refer;
        Not = MainActivity.Not;
        Qa  = MainActivity.Qa;
        Mas = MainActivity.Mas;
        Bu = MainActivity.Bu;
*/
        if (ReadMas == -11) {
            ReadMas = MainActivity.ReadMas;
            ReadQa = MainActivity.ReadQa;
            ReadNot = MainActivity.ReadNot;
            ReadBu = MainActivity.ReadBu;
            ReadRefer = MainActivity.ReadRefer;
            ReadCalen = MainActivity.ReadCalen;
        }
        if ((MainActivity.checkRun == null) && ((MainActivity.checkVersion == false) || (MainActivity.checkVersion==true)))
        {
            readDatabaseFrom();
                if (Calen !=-11) {
                    readlistDelete();
                    new MyJsonTask().execute("http://www.snpx.somee.com/WebService.asmx/getSchedule?Calen=" + Calen +
                            "&Refer=" + Refer +
                            "&Not=" + Not +
                            "&Qa=" + Qa +
                            "&Mas=" + Mas +
                            "&Bu=" + Bu +
                            "&Dept=" + Dept +
                            "&deleCalen=" + listdeleCalen +
                            "&deleRefer=" + listdeleRefer +
                            "&deleNot=" + listdeleNot +
                            "&deleQa=" + listdeleQa +
                            "&deleMas=" + listdeleMas +
                            "&deleBu=" + listdeleBu
                    );
                    this.context = context;
                }
        }
    }

    public void readlistDelete()
    {
        File sdcard = Environment.getExternalStorageDirectory();
        File file1 = new File(sdcard, "SynopexApp/deleCalen.txt");
        File file2 = new File(sdcard, "SynopexApp/deleNot.txt");
        File file3 = new File(sdcard, "SynopexApp/deleQa.txt");
        File file4 = new File(sdcard, "SynopexApp/deleBu.txt");
        File file5 = new File(sdcard, "SynopexApp/deleMas.txt");
        File file6 = new File(sdcard, "SynopexApp/deleRefer.txt");

        try {
            BufferedReader br1 = new BufferedReader(new FileReader(file1));
            String temp = br1.readLine();
            if (temp != null) {
                listdeleCalen = temp;
            }
            br1.close();

            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            temp = br2.readLine();
            if (temp != null) {
                listdeleNot = temp;
            }
            br2.close();

            BufferedReader br3 = new BufferedReader(new FileReader(file3));
            temp = br3.readLine();
            if (temp != null) {
                listdeleQa = temp;
            }
            br3.close();

            BufferedReader br4 = new BufferedReader(new FileReader(file4));
            temp = br4.readLine();
            if (temp != null) {
                listdeleBu = temp;
            }
            br4.close();

            BufferedReader br5 = new BufferedReader(new FileReader(file5));
            temp = br5.readLine();
            if (temp != null) {
                listdeleMas = temp;
            }
            br5.close();

            BufferedReader br6 = new BufferedReader(new FileReader(file6));
            temp = br6.readLine();
            if (temp != null) {
                listdeleRefer = temp;
            }
            br6.close();
        }
        catch (Exception ex)
        {
            Toast.makeText(context, "87_1 -- deleCalen.txt Can't read  !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void readDatabaseFrom () {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "SynopexApp/readFrom.txt");
        if (file.exists()) {
            String[] listTemp = new String[7];
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                listTemp[0] = br.readLine().toString();
                listTemp[1] = br.readLine().toString();
                listTemp[2] = br.readLine().toString();
                listTemp[3] = br.readLine().toString();
                listTemp[4] = br.readLine().toString();
                listTemp[5] = br.readLine().toString();
                listTemp[6] = br.readLine().toString();
                br.close();
                if (listTemp[0] != null) {
                    Calen = Integer.parseInt(listTemp[0]);
                    Refer = Integer.parseInt(listTemp[1]);
                    Bu = Integer.parseInt(listTemp[2]);
                    Not = Integer.parseInt(listTemp[3]);
                    Qa = Integer.parseInt(listTemp[4]);
                    Mas = Integer.parseInt(listTemp[5]);
                    if (listTemp[6].toString() == "All Dept") {
                        Dept = "";
                    } else {
                        Dept = listTemp[6].toString();
                    }
                }
            } catch (Exception ex) {
                Toast.makeText(context, "351 -- readFrom.txt Can't read  !!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class MyJsonTask extends AsyncTask<String, JSONObject, Void> {
        @Override
        protected void onPreExecute() {
// TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                readJsonFromUrl(url);
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
// TODO Auto-generated catch block
            }
            return null;
        }

        public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            JSONObject json = null;
            String[][] MessaCalen = new String[1][3];
            String[][] MessaRefer = new String[1][3];
            String[][] MessaBu = new String[1][3];
            String[][] MessaNot = new String[1][3];
            String[][] MessaQa = new String[1][3];
            String[][] MessaMas = new String[1][3];
            String[] listNoti = getNotifi();
            int countCalen = 0,countRefer=0,countBu = 0,countNot=0,countQa=0,countMas=0;
            InputStream is = new URL(url).openStream();
            try {
//đọc nội dung với Unicode:
                BufferedReader rd = new BufferedReader
                        (new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                //jsonText = jsonText.replace("[","##");
                jsonText = jsonText.substring(74,jsonText.length()-9);
                if (jsonText.length()>2) {
                    //json = new JSONObject(jsonText);
                    File sdcard = Environment.getExternalStorageDirectory();
                    File myFile = new File(sdcard, "SynopexApp/readOK.txt");
                    myFile = new File(sdcard, "SynopexApp/mysdfile.txt");
                    if (!myFile.exists()) {
                        myFile.createNewFile();
                    }
                    try {
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        JSONArray JsonArray = new JSONArray(jsonText);
                        Boolean checkChage = false;
                        for (int i = 0; i < JsonArray.length(); ++i) {
                            String textRecord = JsonArray.getJSONObject(i).getString("No").toString() + "#"
                                    + JsonArray.getJSONObject(i).getString("Tittle").toString() + "#"
                                    + JsonArray.getJSONObject(i).getString("Content").toString() + "#"
                                    + JsonArray.getJSONObject(i).getString("Date").toString() + "#"
                                    + JsonArray.getJSONObject(i).getString("Column").toString() + "#"
                                    + JsonArray.getJSONObject(i).getString("User").toString() + "\n";
                            myOutWriter.append(textRecord);
                            if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Calendar")) {
                                countCalen = countCalen + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[0])) {
                                    MessaCalen[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaCalen[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaCalen[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            } else if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Reference")) {
                                countRefer = countRefer + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[1])) {
                                    MessaRefer[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaRefer[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaRefer[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            } else if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Business")) {
                                countBu = countBu + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[2])) {
                                    MessaBu[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaBu[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaBu[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            } else if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Notice")) {
                                countNot = countNot + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[3])) {
                                    MessaNot[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaNot[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaNot[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            } else if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Qa")) {
                                countQa = countQa + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[4])) {
                                    MessaQa[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaQa[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaQa[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            } else if (JsonArray.getJSONObject(i).getString("Column").toString().equals("Master")) {
                                countMas = countMas + 1;
                                if (Integer.parseInt(JsonArray.getJSONObject(i).getString("No").toString()) > Integer.parseInt(listNoti[5])) {
                                    MessaMas[0][0] = JsonArray.getJSONObject(i).getString("Tittle").toString();
                                    MessaMas[0][1] = JsonArray.getJSONObject(i).getString("Content").toString();
                                    MessaMas[0][2] = JsonArray.getJSONObject(i).getString("No").toString();
                                    checkChage = true;
                                }
                            }
                        }
                        myOutWriter.close();
                        fOut.close();

                        myFile = new File(sdcard, "SynopexApp/notifi.txt");
                        fOut = new FileOutputStream(myFile);
                        myOutWriter =
                                new OutputStreamWriter(fOut);

                        String tempNo1,tempNo2,tempNo3,tempNo4,tempNo5,tempNo6;

                        if (MessaCalen[0][2] == null)
                        {
                            tempNo1 = listNoti[0];
                        }
                        else
                        {
                            tempNo1 = MessaCalen[0][2];
                        }


                        if (MessaRefer[0][2] == null)
                        {
                            tempNo2 = listNoti[1];
                        }
                        else
                        {
                            tempNo2 = MessaRefer[0][2];
                        }

                        if (MessaBu[0][2] == null)
                        {
                            tempNo3 = listNoti[2];
                        }
                        else
                        {
                            tempNo3 = MessaBu[0][2];
                        }

                        if (MessaNot[0][2] == null)
                        {
                            tempNo4 = listNoti[3];
                        }
                        else
                        {
                            tempNo4 = MessaNot[0][2];
                        }

                        if (MessaQa[0][2] == null)
                        {
                            tempNo5 = listNoti[4];
                        }
                        else
                        {
                            tempNo5 = MessaQa[0][2];
                        }

                        if (MessaMas[0][2] == null)
                        {
                            tempNo6 = listNoti[5];
                        }
                        else
                        {
                            tempNo6 = MessaMas[0][2];
                        }

                        myOutWriter.append(tempNo1 + "," + tempNo2 + "," +tempNo3 + "," +tempNo4 + "," +tempNo5 + "," +tempNo6);
                        myOutWriter.close();
                        fOut.close();

                        removeDelete();

                        int tempBag = (countCalen - ReadCalen) + (countRefer - ReadRefer) + (countBu - ReadBu) + (countNot - ReadNot) +
                                (countQa - ReadQa) + (countMas - ReadMas);
                        if (tempBag<0)
                        {
                            tempBag = 0;
                        }
                        setBadgeSamsung(context,tempBag);
                        if(checkChage == true)
                        {
                            if (MessaCalen[0][0]!=null)
                            {
                                showNotification(MessaCalen[0][0],MessaCalen[0][1]);
                            }
                            if (MessaRefer[0][0]!=null)
                            {
                                showNotification(MessaRefer[0][0],MessaRefer[0][1]);
                            }
                            if (MessaBu[0][0]!=null)
                            {
                                showNotification(MessaBu[0][0],MessaBu[0][1]);
                            }
                            if (MessaNot[0][0]!=null)
                            {
                                showNotification(MessaNot[0][0],MessaNot[0][1]);
                            }
                            if (MessaQa[0][0]!=null)
                            {
                                showNotification(MessaQa[0][0],MessaQa[0][1]);
                            }
                            if (MessaMas[0][0]!=null)
                            {
                                showNotification(MessaMas[0][0],MessaMas[0][1]);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    File sdcard = Environment.getExternalStorageDirectory();
                    File myFile = new File(sdcard, "SynopexApp/mysdfile.txt");
                    if (!myFile.exists()) {
                        myFile.createNewFile();
                    }
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append("");
                    myOutWriter.close();
                    fOut.close();
                    setBadgeSamsung(context,0);
                }
            }
            catch (Exception ex)
            {
                Toast.makeText(context,ex.toString(),Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        public void removeDelete() {
            File sdcard = Environment.getExternalStorageDirectory();
            File myFile = new File(sdcard, "SynopexApp/mysdfile.txt");
            File file1 = new File(sdcard, "SynopexApp/readCalen.txt");
            File file2 = new File(sdcard, "SynopexApp/readRefer.txt");
            File file3 = new File(sdcard, "SynopexApp/readBu.txt");
            File file4 = new File(sdcard, "SynopexApp/readNot.txt");
            File file5 = new File(sdcard, "SynopexApp/readQa.txt");
            File file6 = new File(sdcard, "SynopexApp/readMas.txt");

            if (file1.exists()) {
                try {
                    int count = 0;
                    String[] listID = new String[1000];
                    BufferedReader br = new BufferedReader(new FileReader(myFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] temp = line.split("#");
                        listID[count] = temp[0].toString();
                        count = count + 1;
                    }
                    br.close();

                    if(count>0)
                    {
                        int temp;
                        temp = reCord(count,listID,file1);
                        if (MainActivity.ReadCalen != temp)
                        {
                            ReadCalen = temp;
                        }

                        temp = reCord(count,listID,file2);
                        if (MainActivity.ReadRefer != temp)
                        {
                            ReadRefer = temp;
                        }

                        temp = reCord(count,listID,file3);
                        if (MainActivity.ReadBu != temp)
                        {
                            ReadBu = temp;
                        }

                        temp = reCord(count,listID,file4);
                        if (MainActivity.ReadNot != temp)
                        {
                            ReadNot = temp;
                        }

                        temp = reCord(count,listID,file5);
                        if (MainActivity.ReadQa != temp)
                        {
                            ReadQa = temp;
                        }

                        temp = reCord(count,listID,file6);
                        if (MainActivity.ReadMas != temp)
                        {
                            ReadMas = temp;
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(context,"sjdhsjd--" + ex.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        }

        public int reCord(int  mCount,String[] mList,File mFile)
        {
            int countREad=0;
            String[] list = null;
            try {
                BufferedReader br = new BufferedReader(new FileReader(mFile));
                String tempp = br.readLine();
                if ((tempp) != null) {
                    list = tempp.split(",");
                }
                br.close();
                if (list != null) {
                    countREad = 0;
                    String listRead = "";
                    for (int i = 0; i < list.length; i++) {
                        for (int l = 0; l < mCount; l++) {
                            if (list[i].equals(mList[l])) {
                                if (listRead.equals(""))
                                {
                                    listRead = list[i];
                                }
                                else {
                                    listRead = listRead + "," + list[i];
                                }
                                countREad = countREad+1;
                            }
                        }
                    }
                    FileOutputStream fOut = new FileOutputStream(mFile);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append(listRead);
                    myOutWriter.close();
                    fOut.close();
                }
            }
            catch (Exception ex) {
                Toast.makeText(context,"abccc--" + ex.toString(),Toast.LENGTH_SHORT).show();
            }
            return countREad;
        }




        public String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
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

        private void setBadgeSamsung(Context context, int count) {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        }

        private String getLauncherClassName(Context context) {
            PackageManager pm = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : resolveInfos) {
                String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
                if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                    String className = resolveInfo.activityInfo.name;
                    return className;
                }
            }
            return null;
        }

        public String[] getNotifi() {
            String[] list = new String[6];
            File sdcard = Environment.getExternalStorageDirectory();
            //Get the text file
            File file = new File(sdcard, "SynopexApp/notifi.txt");
            if (file.exists()) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        list[0] = line.split(",")[0];
                        list[1] = line.split(",")[1];
                        list[2] = line.split(",")[2];
                        list[3] = line.split(",")[3];
                        list[4] = line.split(",")[4];
                        list[5] = line.split(",")[5];
                    }
                    br.close();
                }
                catch (Exception ex)
                {
                    Toast.makeText(context,"Không Thể Đọc File Notifi.txt",Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                try {
                    file.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(file);
                    OutputStreamWriter myOutWriter =
                            new OutputStreamWriter(fOut);
                    myOutWriter.append("0,0,0,0,0,0");
                    myOutWriter.close();
                    fOut.close();
                    list[0] = "0";
                    list[1] = "0";
                    list[2] = "0";
                    list[3] = "0";
                    list[4] = "0";
                    list[5] = "0";
                }
                catch (Exception ex)
                {
                    Toast.makeText(context,"Không Thể Đọc File Notifi.txt",Toast.LENGTH_SHORT).show();
                }
            }
            return  list;
        }
    }
}