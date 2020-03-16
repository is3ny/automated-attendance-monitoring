<<<<<<< HEAD
package com.example.attendance_checking;
=======
package com.example.myapplication;
>>>>>>> d80f29555452d6456babe22e5c63e12e2e862d44

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;
    LinkedList<Cell> cells;
    LinearLayout.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cells = new LinkedList<Cell>();
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        layout = ((LinearLayout)findViewById(R.id.LinearLayout_0));
        ((Button)findViewById(R.id.Remove)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove_cell();
            }
        });
        ((Button)findViewById(R.id.Add_plus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_plus_cell();
            }
        });
        ((Button)findViewById(R.id.Add_minus)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_minus_cell();
            }
        });
    }
    protected void remove_cell(){
        if(cells.size()>0){
            layout.removeView((FrameLayout)findViewById(cells.getLast().id));
            cells.removeLast();

        }
    }

    protected void add_plus_cell(){
        int size = cells.size();
        cells.add(new Cell(size, true));
        FrameLayout new_frame = new FrameLayout(this);
        new_frame.setLayoutParams(params);
        new_frame.setId(cells.getLast().id);
        new_frame.setForeground(new ColorDrawable(Color.GREEN));
        layout.addView(new_frame);
    }

    protected void add_minus_cell(){
        int size = cells.size();
        cells.add(new Cell(size, false));
        FrameLayout new_frame = new FrameLayout(this);
        new_frame.setLayoutParams(params);
        new_frame.setId(cells.getLast().id);
        new_frame.setForeground(new ColorDrawable(Color.RED));
        layout.addView(new_frame);
    }


}
