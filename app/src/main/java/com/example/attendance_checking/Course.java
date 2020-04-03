package com.example.attendance_checking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class Course {
    String courseName;
    Professor professor;
    Map<Student, Attendance> attendanceMap;

    public Course(String courseName, Professor professor){
        this.courseName = courseName;
        this.professor = professor;
        attendanceMap = new HashMap<>();
    }

    public void addAttendance(Student s, Attendance a) {
        attendanceMap.put(s, a);
    }
}
