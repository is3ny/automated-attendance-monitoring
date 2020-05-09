package com.example.attendance_checking;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_login);
        try
        {
            conn = getPostgreSQLConnection();

            /* You can use the connection object to do any insert, delete, query or update action to the mysql server.*/

            /* Do not forget to close the database connection after use, this can release the database connection.*/

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

        ((Button)findViewById(R.id.button_login)).setOnClickListener(this);
    }

    private Connection getPostgreSQLConnection() {
        /* Declare and initialize a sql Connection variable. */
        Connection ret = null;
        System.out.println("here");
        try
        {

            /* Register jdbc driver class. */
            Class.forName("org.postgresql.Driver");

            /* Create connection url. */
            String mysqlConnUrl = "jdbc:postgresql://10.0.2.2:5433/postgres";

            /* user name. */
            String mysqlUserName = "postgres";

            /* password. */
            String mysqlPassword = "1234567";

            /* Get the Connection object. */
            ret = DriverManager.getConnection(mysqlConnUrl, mysqlUserName , mysqlPassword);

            /* Get related meta data for this mysql server to verify db connect successfully.. */
            DatabaseMetaData dbmd = ret.getMetaData();

            String dbName = dbmd.getDatabaseProductName();

            String dbVersion = dbmd.getDatabaseProductVersion();

            String dbUrl = dbmd.getURL();

            String userName = dbmd.getUserName();

            String driverName = dbmd.getDriverName();

            System.out.println("Database Name is " + dbName);

            System.out.println("Database Version is " + dbVersion);

            System.out.println("Database Connection Url is " + dbUrl);

            System.out.println("Database User Name is " + userName);

            System.out.println("Database Driver Name is " + driverName);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return ret;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_login){
            String pass = findViewById(R.id.editText_pass).toString();
            String email = findViewById(R.id.editText_email).toString();
            String getUser="SELECT count(nick) FROM users " +
                    "WHERE nick='"+email+"' and passwd='"+pass+"'";
            Statement st;
            boolean login = false;
            try {
                st = conn.createStatement();
                ResultSet rs = st.executeQuery(getUser);
                if(rs.getBigDecimal("count").compareTo(BigDecimal.valueOf(0))>0){
                    login = true;
                }
                System.out.println("login");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println(login);
            if(login) {
                Intent intent = new Intent(this, MainActivity.class);
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        }
    }
}
