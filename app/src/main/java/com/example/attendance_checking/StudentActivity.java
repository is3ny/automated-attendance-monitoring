package com.example.attendance_checking;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;

public class StudentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ip_et, port_et, msg_et;
    Button start_bcast_btn, stop_bcast_btn;

    Beacon beacon;
    BeaconParser beaconParser;
    BeaconTransmitter beaconTransmitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_layout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setDebug(true);

        beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
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

        start_bcast_btn = findViewById(R.id.start_bcast_btn);
        stop_bcast_btn = findViewById(R.id.stop_bcast_btn);
        start_bcast_btn.setOnClickListener(this);
        stop_bcast_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.start_bcast_btn) {
            Log.i("ratata", "Was " + beaconTransmitter.isStarted());
            String name = msg_et.getText().toString();
            beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2("1")
                    .setId3("2")
                    .setManufacturer(0x0118)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(StringToLongArrayPacker.toLongArray(name)))
                    //.setDataFields(Collections.singletonList((long) msg_et.getText().toString().length()))
                    .build();
            beaconTransmitter.stopAdvertising();
            beaconTransmitter.setBeacon(beacon);
            beaconTransmitter.startAdvertising();
            Toast.makeText(this, "Started transmitting..." + beaconTransmitter.isStarted(), Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.stop_bcast_btn) {
            beaconTransmitter.stopAdvertising();
            Toast.makeText(this, "Stopped transmitting...", Toast.LENGTH_SHORT).show();
        }
    }
}
