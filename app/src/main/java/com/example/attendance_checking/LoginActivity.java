package com.example.attendance_checking;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Connection conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ((Button)findViewById(R.id.button_login)).setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_login) {
            String pass = ((EditText)findViewById(R.id.editText_pass)).getText().toString();
            String nick = ((EditText)findViewById(R.id.editText_email)).getText().toString();
            String getUser = "SELECT count(nick) FROM users " +
                    "WHERE nick='" + nick + "' and passwd='" + pass + "'";
            System.out.println(getUser);
            boolean login = false;
            ConnectTask connectTask = new ConnectTask();

            connectTask.execute(this);
            try {
                conn = connectTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }


            QueryTask querytTask = new QueryTask();

            querytTask.execute(new Pair<Connection, String>(conn, getUser));
            ResultSet rs =null;
            try {
                rs = querytTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if(rs!=null){
                try {
                    rs.next();
                    if(rs.getLong("count")>0){
                        login = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(login);

            if (login) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("nick", nick);
                CloseConnectionTask close = new CloseConnectionTask();
                close.execute(conn);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Invalid login or password", Toast.LENGTH_LONG).show();
            }
        }
    }
    private final class CloseConnectionTask extends AsyncTask<Connection, Void, Void> {

        @Override
        protected Void doInBackground(Connection... conns) {
            Connection conn = conns[0];
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private final class QueryTask extends AsyncTask<Pair<Connection, String>, Void, ResultSet> {

        @Override
        protected ResultSet doInBackground(Pair<Connection, String>... pairs) {
            Connection conn = pairs[0].first;
            String query = pairs[0].second;
            ResultSet rs = null;
            try {
                rs = conn.createStatement().executeQuery(query);


                //Toast.makeText(contexts[0], "Connected", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                //Toast.makeText(contexts[0], "Fail", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return rs;
        }
    }


    private final class ConnectTask extends AsyncTask<Context, Void, Connection> {

        @Override
        protected Connection doInBackground(Context... contexts) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://10.0.2.2:5433/attendance",
                        "postgres",
                        "1234567"
                );


                //Toast.makeText(contexts[0], "Connected", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(contexts[0], "Fail", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return conn;
        }


    }
}
