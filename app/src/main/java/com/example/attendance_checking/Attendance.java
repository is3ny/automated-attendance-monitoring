package com.example.attendance_checking;

import android.widget.LinearLayout;

public class Attendance {
    Boolean[] attendance_list;

    @Override
    public String toString() {
        int plus_count=0;
        for(int i=0; i<attendance_list.length; i++){
            if(attendance_list[i]){
                plus_count++;
            }
        }
        return plus_count+"/"+attendance_list.length+ " ("+plus_count*100/attendance_list.length+"%) visited";
    }

    public Attendance(Boolean[] array){
        attendance_list = array;
    }
}
