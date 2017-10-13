package com.example.administrator.runalarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class ChangeDept extends AppCompatActivity {
    public String userCode, dept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dept);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final TextView tvOld = (TextView) findViewById(R.id.curentDept);
        final

        // Initializing a String Array
                String[] plants = new String[]{
                "Select Dept",
                "All Dept",
                "IT",
                "Purchasing"
        };
        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, plants
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);


        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard, "SynopexApp/userinfo.txt");
        if (file.exists()) {
//Read text from file
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                userCode = br.readLine();
                dept = br.readLine();
                if (dept != null) {
                    tvOld.setText(dept);
                }
                br.close();
            } catch (Exception ex) {
                Toast.makeText(this, "Không Thể Đọc File User Infor", Toast.LENGTH_SHORT).show();
            }
        }

        Button btLogin = (Button) findViewById(R.id.buttonChangeDept);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String deptR = spinner.getSelectedItem().toString();
                if (deptR.equals("Select Dept")) {
                    Toast toast = Toast.makeText(ChangeDept.this, "Please Select Your Department !!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeDept.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure you want to change to " + deptR);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            File sdcard = Environment.getExternalStorageDirectory();
                            File myFile = new File(sdcard, "SynopexApp/userinfo.txt");
                            try {
                                FileOutputStream fOut = new FileOutputStream(myFile);
                                OutputStreamWriter myOutWriter =
                                        new OutputStreamWriter(fOut);
                                myOutWriter.append(userCode + "\n");
                                myOutWriter.append(deptR.toString());
                                myOutWriter.close();
                                fOut.close();
                                Toast.makeText(getApplicationContext(), "Change finish !!", Toast.LENGTH_SHORT).show();
                                //Intent intent = new Intent(ChangeDept.this, MainActivity.class);
                                dialog.dismiss();
                                finish();
                                //startActivity(intent);
                            } catch (Exception ex) {
                                Toast.makeText(getApplicationContext(), "Can't change user !!" + ex.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        Button btCancel = (Button) findViewById(R.id.buttonCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeDept.this);
                builder.setTitle("Confirm");
                builder.setMessage("Quit ? ");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
