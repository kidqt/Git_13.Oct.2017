package com.example.administrator.runalarm;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.Manifest;
import android.content.pm.PackageManager;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public PendingIntent pendingIntent;
    public PendingIntent pendingIntent2;
    public PendingIntent pendingIntent3;
    private RecyclerView rvNotification;
    private RecycleAdapter mNotiAdapter;
    private List<listNotification> mNoti;
    public String[] listTitle = new String[1000];
    public String[] listContent = new String[1000];
    public String[] listID = new String[1000];
    public String[] listUser = new String[1000];
    public String[] listDate = new String[1000];
    public String[] listColumn = new String[1000];
    public ProgressDialog progress;
    private static final int REQUEST_WRITE_STORAGE = 112;
    public static String userN,Dept;
    int count = 0;
    public static int Calen=-1, Refer=-1, Not=-1, Qa=-1, Mas=-1, Bu=-1;
    public static Boolean checkRun;
    public static int ReadCalen=-1, ReadRefer=-1, ReadNot=-1, ReadQa=-1, ReadMas=-1, ReadBu=-1;
    public String[] listReadCalen,lisrReadRefer,listReadBu,listReadNot,listReadQa,listReadMas;
    public String mUrl="";
    public static Boolean checkVersion = false;
    public String versionApp;
    public String versionGet;
    public String mUrlgetUpdate = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.vintrigver1);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        setTitle("    Communication Tool");
        menu.add("Change Dept");
        menu.add("About");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.toString().equals("About"))
        {
            try {
                Toast.makeText(this, "Version Application : " + this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName, Toast.LENGTH_LONG).show();
            }
            catch (Exception ex)
            {
                Toast.makeText(this, "Line 93 Version Menu NG " + versionGet + "---" + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
        else if (item.toString().equals("Change Dept"))
        {
            Intent changeDept = new Intent(MainActivity.this,ChangeDept.class);
            startActivity(changeDept);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause(){
        super.onPause();
        checkRun=null;
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkRun = false;

        setContentView(R.layout.activity_main);
        mNoti = new ArrayList<>();
        rvNotification = (RecyclerView) findViewById(R.id.rv_list);

        versionGet = null;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionApp = pInfo.versionName.split("-")[1];
            Boolean checkss = false;
            while (versionGet == null) {
                if (checkss == false) {
                    new GetVersion().execute();
                    checkss = true;
                }
            }
            try {
                if (versionGet.length() > 10) {
                    String[] a = versionGet.split("##");
                    versionGet = a[0].substring(74, a[0].length());
                    mUrlgetUpdate = a[1].substring(0, a[1].length() - 9);
                }
            } catch (Exception ex) {
                Toast.makeText(this, "Line 87 Version -- NG -- " + versionGet + "---" + ex.toString(), Toast.LENGTH_LONG).show();
            }

            if (Integer.parseInt(versionGet) > Integer.parseInt(versionApp)) {
                //showNotification("Update","Please Update Software Vintrid Tool !!");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("New Update avaiable. Plese download new Version !");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrlgetUpdate));
                        startActivity(browserIntent);
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(0);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                checkVersion = true;
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Line 111 Version -- NG -- " + ex.toString(), Toast.LENGTH_LONG).show();
        }

        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
            Toast.makeText(this, "Read Data on Your Phone Permission Allow Please !!", Toast.LENGTH_SHORT).show();
        } else {
            File sdcard = Environment.getExternalStorageDirectory();
            File myDirectory = new File(sdcard, "SynopexApp");
            if (!myDirectory.exists()) {
                try {
                    myDirectory.mkdir();
                    myDirectory = new File(sdcard, "SynopexApp/mysdfile.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/userinfo.txt");
                    myDirectory.createNewFile();
                } catch (Exception ex) {
                    Toast.makeText(this, "Read Data on Your Phone Permission Allow Please  !!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            myDirectory = new File(sdcard, "SynopexApp/deleCalen.txt");
            if (!myDirectory.exists()) {
                try {
                    myDirectory = new File(sdcard, "SynopexApp/deleCalen.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/deleNot.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/deleQa.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/deleRefer.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/deleBu.txt");
                    myDirectory.createNewFile();
                    myDirectory = new File(sdcard, "SynopexApp/deleMas.txt");
                    myDirectory.createNewFile();
                } catch (Exception ex) {
                    Toast.makeText(this, "Không Thể Tạo File deleCalen_" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            userN = getUser();
            if ((userN == "") || (userN == null)) {
                Intent intent = new Intent(this, Login.class);
                finish();
                startActivity(intent);
            } else {
                try {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                    myDirectory = new File(sdcard, "SynopexApp/readFrom.txt");
                    if (!myDirectory.exists()) {
                        myDirectory.createNewFile();
                        new MyJsonTask().execute("http://www.snpx.somee.com/WebService.asmx/getMax");
                    } else {
                        File file = new File(sdcard, "SynopexApp/readFrom.txt");
                        if (file.exists()) {
                            String[] listTemp = new String[7];
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(file));
                                String line;
                                int p = 0;
                                while ((line = br.readLine()) != null) {
                                    listTemp[p] = line.toString();
                                    p = p + 1;
                                }
                                br.close();
                                if (listTemp[0] != null) {
                                    MainActivity.Calen = Integer.parseInt(listTemp[0]);
                                    MainActivity.Refer = Integer.parseInt(listTemp[1]);
                                    MainActivity.Bu = Integer.parseInt(listTemp[2]);
                                    MainActivity.Not = Integer.parseInt(listTemp[3]);
                                    MainActivity.Qa = Integer.parseInt(listTemp[4]);
                                    MainActivity.Mas = Integer.parseInt(listTemp[5]);
                                    MainActivity.Dept = listTemp[6].toString();
                                } else {
                                    new MyJsonTask().execute("http://www.snpx.somee.com/WebService.asmx/getMax");
                                }
                            } catch (Exception ex) {
                                Toast.makeText(this, "readFrom.txt Can't read  !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(this, "Read Data on Your Phone Permission Allow Please  !!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
                String[] a = getRead();
                if (a[0] != null) {
                    listReadCalen = a[0].split(",");
                }
                if (a[1] != null) {
                    lisrReadRefer = a[1].split(",");
                }
                if (a[2] != null) {
                    listReadBu = a[2].split(",");
                }
                if (a[3] != null) {
                    listReadNot = a[3].split(",");
                }
                if (a[4] != null) {
                    listReadQa = a[4].split(",");
                }

                if (a[5] != null) {
                    listReadMas = a[5].split(",");
                }

                if (listReadCalen == null) {
                    ReadCalen = 0;
                } else {
                    ReadCalen = listReadCalen.length;
                }

                if (lisrReadRefer == null) {
                    ReadRefer = 0;
                } else {
                    ReadRefer = lisrReadRefer.length;
                }

                if (listReadNot == null) {
                    ReadNot = 0;
                } else {
                    ReadNot = listReadNot.length;
                }

                if (listReadQa == null) {
                    ReadQa = 0;
                } else {
                    ReadQa = listReadQa.length;
                }

                if (listReadMas == null) {
                    ReadMas = 0;
                } else {
                    ReadMas = listReadMas.length;
                }

                if (listReadBu == null) {
                    ReadBu = 0;
                } else {
                    ReadBu = listReadBu.length;
                }

                File file = new File(sdcard, "SynopexApp/mysdfile.txt");
                if (file.exists()) {
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String line;
                        String mKorean = "";
                        while ((line = br.readLine()) != null) {
                            mKorean = "";
                            String[] temp = line.split("#");
                            listID[count] = temp[0].toString();
                            listTitle[count] = temp[1].toString();
                            listContent[count] = temp[2].toString();
                            listDate[count] = temp[3].toString();
                            listColumn[count] = temp[4].toString();
                            listUser[count] = temp[5].toString();

                            if (listColumn[count].equals("Calendar")) {
                                mKorean = "캘린더";
                            } else if (listColumn[count].equals("Business")) {
                                mKorean = "업무기록";
                            } else if (listColumn[count].equals("Notice")) {
                                mKorean = "공지사항";
                            } else if (listColumn[count].equals("Qa")) {
                                mKorean = "업무요청";
                            } else if (listColumn[count].equals("Master")) {
                                mKorean = "마스터플랜";
                            } else if (listColumn[count].equals("Reference")) {
                                mKorean = "자료실";
                            }
                            mNoti.add(new listNotification(temp[3].toString(), temp[1].toString(), temp[2].toString(), mKorean, temp[0].toString()));
                            count = count + 1;
                        }
                        br.close();
                        if (count != 0) {
                            mNotiAdapter = new RecycleAdapter(this, mNoti, listReadCalen, lisrReadRefer, listReadBu, listReadNot, listReadQa, listReadMas);
                            rvNotification.setAdapter(mNotiAdapter);

                            ItemTouchHelper.Callback callback = new SwipeHelp(mNotiAdapter);
                            ItemTouchHelper helper = new ItemTouchHelper(callback);
                            helper.attachToRecyclerView(rvNotification);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                            rvNotification.setLayoutManager(linearLayoutManager);
                        } else {
                            File file1 = new File(sdcard, "SynopexApp/readCalen.txt");
                            File file2 = new File(sdcard, "SynopexApp/readRefer.txt");
                            File file3 = new File(sdcard, "SynopexApp/readBu.txt");
                            File file4 = new File(sdcard, "SynopexApp/readNot.txt");
                            File file5 = new File(sdcard, "SynopexApp/readQa.txt");
                            File file6 = new File(sdcard, "SynopexApp/readMas.txt");

                            ReadCalen = 0;
                            ReadRefer = 0;
                            ReadMas = 0;
                            ReadQa = 0;
                            ReadNot = 0;
                            ReadBu = 0;

                            FileOutputStream fOut = new FileOutputStream(file1);
                            OutputStreamWriter myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();

                            fOut = new FileOutputStream(file2);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();

                            fOut = new FileOutputStream(file3);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();

                            fOut = new FileOutputStream(file4);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();

                            fOut = new FileOutputStream(file5);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();

                            fOut = new FileOutputStream(file6);
                            myOutWriter =
                                    new OutputStreamWriter(fOut);
                            myOutWriter.append("");
                            myOutWriter.close();
                            fOut.close();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Line 320 -- " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                start();
                startAt10();
                startAt20();
            }
        }
    }

    public class GetVersion extends AsyncTask<String, JSONObject, Void> {
        @Override
        protected void onPreExecute() {
// TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params) {
                try {

                    InputStream is = new URL("http://www.snpx.somee.com/WebService.asmx/version").openStream();
                    BufferedReader rd = new BufferedReader
                            (new InputStreamReader(is, Charset.forName("UTF-8")));
                    versionGet = readAll(rd);
                    rd.close();
                    is.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Get Version Name Error !!" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            return null;
        }
    }

    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //reload my activity with permission granted or use the features what required the permission
                } else
                {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public String[] getRead() {
        String[] list = new String[6];
        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file1 = new File(sdcard, "SynopexApp/readCalen.txt");
        File file2 = new File(sdcard, "SynopexApp/readRefer.txt");
        File file3 = new File(sdcard, "SynopexApp/readBu.txt");
        File file4 = new File(sdcard, "SynopexApp/readNot.txt");
        File file5 = new File(sdcard, "SynopexApp/readQa.txt");
        File file6 = new File(sdcard, "SynopexApp/readMas.txt");

        if (file1.exists()) {
//Read text from file
            try {
                BufferedReader br1 = new BufferedReader(new FileReader(file1));
                list[0] = br1.readLine();
                br1.close();

                BufferedReader br2 = new BufferedReader(new FileReader(file2));
                list[1] = br2.readLine();
                br2.close();

                BufferedReader br3 = new BufferedReader(new FileReader(file3));
                list[2] = br3.readLine();
                br3.close();

                BufferedReader br4 = new BufferedReader(new FileReader(file4));
                list[3] = br4.readLine();
                br4.close();

                BufferedReader br5 = new BufferedReader(new FileReader(file5));
                list[4] = br5.readLine();
                br5.close();

                BufferedReader br6 = new BufferedReader(new FileReader(file6));
                list[5] = br6.readLine();
                br6.close();
            }
            catch (Exception ex)
            {
                Toast.makeText(this, "aa" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            try {
                file1.createNewFile();
                file2.createNewFile();
                file3.createNewFile();
                file4.createNewFile();
                file5.createNewFile();
                file6.createNewFile();
                list[0] = "0";
                list[1] = "0";
                list[2] = "0";
                list[3] = "0";
                list[4] = "0";
                list[5] = "0";

                FileOutputStream fOut = new FileOutputStream(file1);
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();

                fOut = new FileOutputStream(file2);
                myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();

                fOut = new FileOutputStream(file3);
                myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();

                fOut = new FileOutputStream(file4);
                myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();

                fOut = new FileOutputStream(file5);
                myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();

                fOut = new FileOutputStream(file6);
                myOutWriter =
                        new OutputStreamWriter(fOut);
                myOutWriter.append("");
                myOutWriter.close();
                fOut.close();
            }
            catch (Exception ex)
            {
                Toast.makeText(this,  "aa" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        return  list;
    }
    public String getUser() {
        String list = "";
        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard, "SynopexApp/userinfo.txt");
        if (file.exists()) {
//Read text from file
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                list = br.readLine();
                Dept = br.readLine();
                br.close();
            }
            catch (Exception ex)
            {
                Toast.makeText(this, "Không Thể Đọc File User Infor", Toast.LENGTH_SHORT).show();
            }
        }
        return  list;
    }

    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1001, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        //int interval = 120000;
        int interval = 8000;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), interval, pendingIntent);
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt10() {
        Intent alarmIntent2 = new Intent(MainActivity.this, AlarmReceiverFixTime.class);
        pendingIntent2 = PendingIntent.getBroadcast(MainActivity.this, 1002, alarmIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 07);
        calendar.set(Calendar.MINUTE,50 );
        calendar.set(Calendar.SECOND,20);

        if (now.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent2);
    }

    public void startAt20()
    {
        Intent alarmIntent3 = new Intent(MainActivity.this, AlarmReceiverFixTime.class);
        pendingIntent3 = PendingIntent.getBroadcast(MainActivity.this, 1003, alarmIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar2 = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 17);
        calendar2.set(Calendar.MINUTE, 20);
        calendar2.set(Calendar.SECOND,20);

        if (now.after(calendar2)) {
            calendar2.add(Calendar.DATE, 1);
        }
        manager2.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
              AlarmManager.INTERVAL_DAY, pendingIntent3);
    }


    public class MyJsonTask extends AsyncTask<String, JSONObject, Void> {
        @Override
        protected Void doInBackground(String... params) {
//Lấy URL truyền vào
            String[] listMax= new String[6];
            String url=params[0];
            try {
                listMax = readJsonFromUrl(url);
                MainActivity.Calen = Integer.parseInt(listMax[0]);
                MainActivity.Refer = Integer.parseInt(listMax[1]);
                MainActivity.Bu = Integer.parseInt(listMax[2]);
                MainActivity.Not = Integer.parseInt(listMax[3]);
                MainActivity.Qa = Integer.parseInt(listMax[4]);
                MainActivity.Mas = Integer.parseInt(listMax[5]);
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        public  String[] readJsonFromUrl(String url) throws IOException, JSONException {
            String[] list = new String[6];
            InputStream is = new URL(url).openStream();
            try {
//đọc nội dung với Unicode:
                BufferedReader rd = new BufferedReader
                        (new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                //jsonText = jsonText.replace("[","##");
                File sdcard = Environment.getExternalStorageDirectory();
                File myFile = new File(sdcard, "SynopexApp/readFrom.txt");
                FileOutputStream fOut = new FileOutputStream(myFile, true);
                jsonText = jsonText.substring(74, jsonText.length() - 9);
                String[] valueMax = jsonText.split(",");
                OutputStreamWriter myOutWriter =
                        new OutputStreamWriter(fOut);
                for (int i = 0; i < 6; i++) {
                    list[i] = valueMax[i];
                    myOutWriter.append(valueMax[i] + "\n");
                }
                myOutWriter.append(Dept);
                myOutWriter.close();
                fOut.close();
                return list;
            }
            finally {
            }
        }

        public String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }
}

