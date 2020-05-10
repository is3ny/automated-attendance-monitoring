package com.example.attendance_checking;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class TeacherCourseListAdapter extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<Course> courses;
    private Context context;

    public TeacherCourseListAdapter(ArrayList<Course> courses, Context context){
        this.courses = courses;

        this.context = context;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int i) {
        return courses.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int listRow, View view_, ViewGroup viewGroup) {
        View view =view_;
        if(view==null) {
            view = lInflater.inflate(R.layout.course_item_prof, viewGroup, false);

            Course currentCourse = (Course) getItem(listRow);
            double attendance = 0;
            Collection<Attendance> atts = currentCourse.attendanceMap.values();
            for(Attendance attendance1 : atts){
                Boolean arr[] = attendance1.attendanceList;
                double percentage = 0;
                for(int j=0; j<arr.length; j++){
                    if(arr[j]){
                        percentage+=1;
                    }
                }
                percentage=percentage*100/arr.length;
                attendance+=percentage;
            }
            attendance/=atts.size();
            attendance = (double)((int)(100*attendance))/100;



            ((TextView) view.findViewById(R.id.list_course_name)).setText(currentCourse.courseName);
            ((TextView) view.findViewById(R.id.list_course_studs_number)).setText("Students on course: "+currentCourse.attendanceMap.size());
            ((TextView) view.findViewById(R.id.list_course_studs_att)).setText("Overall attendance: "+attendance+"%");
            ((TextView) view.findViewById(R.id.list_total_events)).setText("Number of events: "+currentCourse.eventsNumber);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f);


            Set<Student> keys = currentCourse.attendanceMap.keySet();
            for(Student student : keys){
                TextView text = new TextView(context);

                text.setId(ViewCompat.generateViewId());
                Boolean arr[] = currentCourse.attendanceMap.get(student).attendanceList;
                System.out.println(arr.length+"fdfdfdf");
                double percentage=0;
                for(int j=0; j<arr.length; j++){
                    if(arr[j]){
                        percentage+=1;
                    }
                }
                percentage=percentage*100/arr.length;
                percentage = (double)((int)(100*percentage))/100;
                text.setText(student.name+": "+percentage+"%");
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                text.setLayoutParams(params);

                ((LinearLayout) view.findViewById(R.id.list_students)).addView(text);
            }
        }
        return view;
    }
}
