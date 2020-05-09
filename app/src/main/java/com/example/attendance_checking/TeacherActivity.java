package com.example.attendance_checking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.bluetooth.*;
import android.widget.TextView;
import android.widget.Toast;

public class TeacherActivity extends AppCompatActivity {

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals("my_action")) {
                String name = intent.getStringExtra("name");
                ((TextView)findViewById(R.id.textView_list)).append(name+'\n');
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_layout);
        findViewById(R.id.button_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerReceiver(receiver, new IntentFilter("my_action"));
            }
        });
    }
}
