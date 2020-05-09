package com.example.attendance_checking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BeaconActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        findViewById(R.id.button_s).setOnClickListener(this);
        findViewById(R.id.button_t).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(!(requestCode==1&&resultCode==RESULT_OK)){
            this.finishAffinity();
        }

    }

    @Override
    public void onClick(View view) {
        System.out.println("click");
        if(view.getId()==R.id.button_t){
            Intent intent = new Intent(this, TeacherActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.button_s){
            Intent intent = new Intent(this, StudentActivity.class);
            startActivity(intent);
        }
    }
}
