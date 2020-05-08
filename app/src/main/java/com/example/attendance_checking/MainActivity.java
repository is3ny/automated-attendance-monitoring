package com.example.attendance_checking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_view);

        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student());

        Professor p1 = new Professor("Luiz Jonata");
        Course c1 = new Course("Course number One", p1);
        Attendance a1 = new Attendance(new Boolean[]{true, false, true});
        c1.addAttendance(students.get(0), a1);

        Professor p2 = new Professor("Some Strange Name");
        Course c2 = new Course("Course number Two", p2);
        Attendance a2 = new Attendance(new Boolean[]{false, false, true, true, true});
        c2.addAttendance(students.get(0), a2);

        ArrayList<Course> courses = new ArrayList<>();
        courses.add(c1);
        courses.add(c2);

        // Making list items appear on screen
        CourseListAdapter adapter = new CourseListAdapter(courses, students.get(0), this);
        listView.setAdapter(adapter);
        ((Button)findViewById(R.id.button_to_beacon)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_to_beacon){
            Intent intent = new Intent(this, BeaconActivity.class);
            startActivity(intent);
        }
    }
}
