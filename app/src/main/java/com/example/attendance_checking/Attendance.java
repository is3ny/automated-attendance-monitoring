package com.example.attendance_checking;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Attendance {
    Boolean[] attendanceList;

    @Override @NonNull
    public String toString() {
        int timesAttended = 0;
        for (Boolean attended : attendanceList) {
            timesAttended += attended ? 1 : 0;
        }

        int percentage = 100 * timesAttended / attendanceList.length;
        return String.format(Locale.ENGLISH, "%d/%d (%d%%) visited",
                             timesAttended, attendanceList.length, percentage);
    }

    public Attendance(Boolean[] attendanceList){
        this.attendanceList = attendanceList;
    }
}
