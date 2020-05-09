package com.example.attendance_checking;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ip_et, port_et, msg_et;
    Button send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_layout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /*
        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText)findViewById(R.id.ip_et)).getText().toString();
                Intent intent = new Intent();
                intent.putExtra("name", name);
                intent.setAction("my_action");
                sendBroadcast(intent);
            }
        });
         */
        ip_et = findViewById(R.id.ip_et);
        port_et = findViewById(R.id.port_et);
        msg_et = findViewById(R.id.msg_et);

        send_btn = findViewById(R.id.send_msg_btn);
        send_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_msg_btn) {
            int port = 1;
            DatagramSocket socket;
            InetAddress address;

            // Check if correct port number was supplied before we do all this net mumbo-jumbo.
            try {
                port = Integer.parseInt(port_et.getText().toString());
            } catch (Exception e) {
                Toast.makeText(this, "Invalid port number.", Toast.LENGTH_LONG).show();
            }

            try {
                address = InetAddress.getByName(ip_et.getText().toString());
            } catch (UnknownHostException e) {
                Toast.makeText(this, "Failed to parse IP address.", Toast.LENGTH_LONG)
                     .show();
                e.printStackTrace();
                return;
            }

            try {
                socket = new DatagramSocket();
            } catch (SocketException e) {
                Toast.makeText(this, "Failed to init a socket.", Toast.LENGTH_LONG)
                        .show();
                e.printStackTrace();
                return;
            }

            byte[] buf = msg_et.getText().toString().getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);

            try {
                socket.send(packet);
            } catch (IOException e) {
                Toast.makeText(this, "Failed to send packet.", Toast.LENGTH_LONG).show();
                //e.printStackTrace();
            }

            socket.close();
        }
    }
}
