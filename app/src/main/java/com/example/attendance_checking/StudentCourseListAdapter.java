package com.example.attendance_checking;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;
import java.util.Locale;

public class StudentCourseListAdapter extends BaseAdapter {
    private LayoutInflater lInflater;
    private ArrayList<Course> courses;
    private Context context;
    private Student student;

    public StudentCourseListAdapter(ArrayList<Course> courses, Student student, Context context){
        this.courses = courses;
        this.student = student;
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
            view = lInflater.inflate(R.layout.course_item, viewGroup, false);

            Course currentCourse = (Course) getItem(listRow);
            Attendance attendanceStat = currentCourse.attendanceMap.get(student);
            assert attendanceStat != null;

            String profNameField
                    = String.format(Locale.ENGLISH, "Professor: %s", currentCourse.professor.name);
            ((TextView) view.findViewById(R.id.list_course_name)).setText(currentCourse.courseName);
            ((TextView) view.findViewById(R.id.list_course_prof_name)).setText(profNameField);
            ((TextView) view.findViewById(R.id.list_course_percentage)).setText(attendanceStat.toString());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f);

            // Building attendance progress bar, cell at a time
            System.out.println("len: " + attendanceStat.attendanceList.length);
            for (int celli = 0; celli < attendanceStat.attendanceList.length; celli++) {

                FrameLayout new_frame = new FrameLayout(context);
                new_frame.setLayoutParams(params);
                new_frame.setId(ViewCompat.generateViewId());

                int backgroundId;

                // Because borders contribute to the width of the drawable, we'll shift the second and
                // onwards cells 1px to the left in order to maintain 1px distance between all cells.
                if (attendanceStat.attendanceList[celli]) {
                    if (celli == 0)
                        backgroundId = R.drawable.green_cell;
                    else
                        backgroundId = R.drawable.add_green_cell;
                } else {
                    if (celli == 0)
                        backgroundId = R.drawable.red_cell;
                    else
                        backgroundId = R.drawable.add_red_cell;
                }
                new_frame.setBackground(context.getResources().getDrawable(backgroundId));
                ((LinearLayout) view.findViewById(R.id.list_attendance)).addView(new_frame);
            }
        }
        return view;
    }
}
