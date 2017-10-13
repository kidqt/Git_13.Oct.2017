package com.example.administrator.runalarm;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import android.content.Intent;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Login extends AppCompatActivity {
    public final static String mURL = "http://www.snpx.somee.com/WebService.asmx?WSDL";
    public final static String NAMESPACE = "http://tmtien.vn/";

    String userNameText = "";
    String passWordText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText userName = (EditText) findViewById(R.id.user);
        final EditText passWord = (EditText) findViewById(R.id.pass);

        Button btLogin = (Button) findViewById(R.id.button);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameText = userName.getText().toString().trim();
                passWordText = passWord.getText().toString().trim();
                if ((userName.getText().toString().trim().length() == 0) || (passWord.getText().toString().trim().length() == 0)) {
                    Toast toast = Toast.makeText(Login.this, "Input User and Password Please !!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                        Login.JSonAsyncTaskUser task = new Login.JSonAsyncTaskUser();
                        task.execute();
                }
            }
        });
    }
    public class JSonAsyncTaskUser extends AsyncTask<Void, Void, ArrayList<JSONObject>> {
        public ProgressDialog progress;
        @Override
        protected void onPreExecute() {
// TODO Auto-generated method stub
            super.onPreExecute();
        }
        @Override
        protected ArrayList<JSONObject> doInBackground(Void... params) {
            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    progress = ProgressDialog.show(Login.this, "Login...",
                            "Please waiting for connect to Server..", true);
                }
            });

            final String METHOD_NAME = "getUser" ;
            final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
            try {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
                request.addProperty("userName", userNameText);
                request.addProperty("passWord", passWordText);
//thiết lập version
                SoapSerializationEnvelope envelope =
                        new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
//thiết lập output
                envelope.setOutputSoapObject(request);
//tạo đối tượng HttpTransportSE
                HttpTransportSE androidHttpTransport =
                        new HttpTransportSE(mURL);
//tiến hành triệu gọi Service
                androidHttpTransport.call(SOAP_ACTION, envelope);
                SoapObject soapArray = (SoapObject) envelope.getResponse();
                if (soapArray.getPropertyCount() > 0) {
                    SoapObject soapItem = (SoapObject) soapArray.getProperty(0);
                    File sdcard = Environment.getExternalStorageDirectory();
                    File myFile = new File(sdcard, "SynopexApp/userinfo.txt");
                    try {
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(soapItem.getProperty("USER_CD").toString() + "\n");
                        try {
                            myOutWriter.append(soapItem.getProperty("USER_DEPT").toString());
                        } catch (Exception ex) {
                            myOutWriter.append("All Dept");
                        }
                        myOutWriter.close();
                        fOut.close();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Can't record user !!" + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    h = new Handler(Looper.getMainLooper());
                    h.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "User or Password incorrect !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (Exception ex)
            {
                h = new Handler(Looper.getMainLooper());
                h.post(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Can't Login !!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                public void run() {
                    progress.dismiss();
                }
            });
            return null;
        }
    }
}
