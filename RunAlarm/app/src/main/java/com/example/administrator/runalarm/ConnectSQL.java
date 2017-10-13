package com.example.administrator.runalarm;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectSQL {
    String ip = "219.255.132.65";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "MES_VN_PBA3";
    String un = "sa";
    String password = "dldydwns1$";


    @SuppressLint("NewApi")
    public String CONN() {
        String z = "";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERRO", e.getMessage());
        } catch (Exception e) {
            Log.e("ERRO", e.getMessage());
        }

        try {
            if (conn == null) {
                z = "Error in connection with SQL server";
            } else {
                String query = "select * from SW_TEST1 where SEQ_No =" + 198;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    z = "Đăng Nhập Thành Công";
                } else {
                    z = "Sai User hoặc Mật Khẩu";
                }

            }
        } catch (Exception ex) {
            z = "Exceptions";
        }
        return z;
    }
}
