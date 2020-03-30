package com.example.attendance_checking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.list_view);
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student());
        Professor p1 = new Professor("Luiz Jonata");
        Professor p2 = new Professor("Some Strange Name");
        Attendance a1 = new Attendance(new Boolean[]{true, false, true});
        Attendance a2 = new Attendance(new Boolean[]{false, false, true, true, true});
        Course c1 = new Course("Course number One", p1);
        Course c2 = new Course("Course number Two", p2);
        c1.addAttendance(students.get(0), a1);
        c2.addAttendance(students.get(0), a2);
        ArrayList<Course> courses = new ArrayList<>();
        courses.add(c1);
        courses.add(c2);


        CourseListAdapter adapter = new CourseListAdapter(courses, students.get(0), this);

        // настраиваем список

        listView.setAdapter(adapter);
    }
}
