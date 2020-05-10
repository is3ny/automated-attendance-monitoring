package com.example.attendance_checking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectTask connectTask = new ConnectTask();
        connectTask.execute();
        Connection conn = null;
        try {
            conn = connectTask.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //get students
        String getStudents = "SELECT distinct(U.nick)\n" +
                "FROM users U, eventparticipants Ep\n" +
                "WHERE Ep.uid=U.uid";
        ResultSet rs = getRsFromQuery(conn, getStudents);
        ArrayList<String> students = new ArrayList<>();
        if(rs!=null){
            while(true) {
                try {
                    if (!rs.next()) break;
                    students.add(rs.getString("nick"));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if(students.contains(getIntent().getStringExtra("nick"))){
            setUpForStudent(conn);
        }else{
            setUpForProfessor(conn);
        }
        ((TextView)findViewById(R.id.textView_Nick)).setText("Name: "+getIntent().getStringExtra("nick"));


        ((Button)findViewById(R.id.button_to_beacon)).setOnClickListener(this);
    }

    private void setUpForProfessor(Connection conn){
        System.out.println("Professors");
        String getAllCourses = "SELECT distinct(C.title)\n" +
                "FROM courses C, events E, users U, eventsforcourse Ec\n" +
                "WHERE U.nick='"+getIntent().getStringExtra("nick")+"'" +
                " and Ec.courseid=C.courseid and E.evid=Ec.evid and U.uid=E.modid";
        ResultSet rs = getRsFromQuery(conn, getAllCourses);
        ArrayList<Course> courses = new ArrayList<>();
        Professor me = new Professor(getIntent().getStringExtra("nick"));
        while(true){
            try {
                if (!rs.next()) break;
                String courseName = rs.getString("title");
                courses.add(new Course(courseName, me));
                String getStuds = "SELECT distinct(U.nick)\n" +
                        "FROM courses C, events E, users U, eventsforcourse Ec, eventparticipants Ep\n" +
                        "WHERE C.title='"+courseName+"' and Ec.courseid=C.courseid and" +
                        " E.evid=Ec.evid and Ep.evid=E.evid and Ep.uid=U.uid";
                System.out.println(getStuds);
                ResultSet rsStudents = getRsFromQuery(conn, getStuds);
                while(rsStudents.next()){
                    Student currentStudent = new Student(rsStudents.getString("nick"));
                    String getAtt = "SELECT Ep.chunks, E.real_start_time\n" +
                            "FROM courses C, events E, users U, eventparticipants Ep, eventsforcourse Ec\n" +
                            "WHERE U.nick='"+currentStudent.name+"' and C.title='"+courseName+"'\n" +
                            "\tand Ep.uid=U.uid and E.evid=Ep.evid and Ec.evid=E.evid and C.courseid=Ec.courseid\n" +
                            "ORDER BY E.real_start_time";
                    ResultSet rsAttendance = getRsFromQuery(conn, getAtt);
                    System.out.println(getAtt);
                    ArrayList<Boolean> att = new ArrayList<>();
                    while(rsAttendance.next()){
                        String chunks = rsAttendance.getString("chunks");

                        int checked=0;
                        int size = chunks.length();
                        for(int j=0; j<size; j++){
                            if(chunks.charAt(j)=='1'){
                                checked++;
                            }
                        }
                        if(checked*2>=size){
                            att.add(true);
                        }
                        else{
                            att.add(false);
                        }

                    }
                    courses.get(courses.size()-1).addAttendance(currentStudent,
                            new Attendance(att.toArray(new Boolean[0])));

                    String getCount = "SELECT count(distinct(E.real_start_time))\n" +
                            "FROM courses C, events E, users U, eventparticipants Ep, eventsforcourse Ec\n" +
                            "WHERE C.title='"+courses.get(courses.size()-1).courseName+"'\n" +
                            "\tand Ep.uid=U.uid and E.evid=Ep.evid and Ec.evid=E.evid and C.courseid=Ec.courseid";
                    ResultSet rsCount = getRsFromQuery(conn, getCount);
                    rsCount.next();
                    courses.get(courses.size()-1).eventsNumber = Math.toIntExact(rsCount.getLong("count"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Making list items appear on screen
        TeacherCourseListAdapter adapter = new TeacherCourseListAdapter(courses, this);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        ((TextView)findViewById(R.id.textView_role)).setText("Role: Professor");
    }

    private void setUpForStudent(Connection conn){
        System.out.println("Students");
        String getAllCourses = "SELECT distinct(C.title)\n" +
                "FROM courses C, events E, users U, eventparticipants Ep, eventsforcourse Ec\n" +
                "WHERE U.nick='"+getIntent().getStringExtra("nick")+"' \n" +
                "\tand Ep.uid=U.uid and E.evid=Ep.evid and Ec.evid=E.evid and C.courseid=Ec.courseid";
        ResultSet rs = getRsFromQuery(conn, getAllCourses);
        ArrayList<Course> courses = new ArrayList<>();
        while(true){
            try {
                if (!rs.next()) break;
                String courseName = rs.getString("title");
                String getProf = "SELECT distinct(U.nick)\n" +
                        "FROM courses C, events E, users U, eventsforcourse Ec\n" +
                        "WHERE C.title='"+courseName+"' and Ec.courseid=C.courseid and E.evid=Ec.evid and U.uid=E.modid";
                ResultSet rs1 = getRsFromQuery(conn, getProf);
                rs1.next();
                String profName = rs1.getString("nick");
                courses.add(new Course(courseName, new Professor(profName)));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Student me = new Student(getIntent().getStringExtra("nick"));
        System.out.println("size:"+courses.size());
        for(int i=0; i<courses.size(); i++) {
            String getAttendance = "SELECT E.chunk_count, Ep.chunks, E.real_start_time\n" +
                    "FROM courses C, events E, users U, eventparticipants Ep, eventsforcourse Ec\n" +
                    "WHERE U.nick='" + getIntent().getStringExtra("nick") + "' " +
                    "and C.title='"+courses.get(i).courseName+"'\n" +
            "\tand Ep.uid=U.uid and E.evid=Ep.evid and Ec.evid=E.evid and C.courseid=Ec.courseid\n" +
                    "ORDER BY E.real_start_time";
            System.out.println(getAttendance);
            rs = getRsFromQuery(conn, getAttendance);
            ArrayList<Boolean> att = new ArrayList<>();
            while(true){
                try {
                    if (!rs.next()) break;
                    String chunks = rs.getString("chunks");

                    int checked=0;
                    int size = chunks.length();
                    for(int j=0; j<size; j++){
                        if(chunks.charAt(j)=='1'){
                            checked++;
                        }
                    }
                    if(checked*2>=size){
                        att.add(true);
                    }
                    else{
                        att.add(false);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            courses.get(i).addAttendance(me, new Attendance(att.toArray(new Boolean[0])));
        }
        // Making list items appear on screen
        StudentCourseListAdapter adapter = new StudentCourseListAdapter(courses, me, this);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        ((TextView)findViewById(R.id.textView_role)).setText("Role: Student");

    }

    private ResultSet getRsFromQuery(Connection conn, String getStudents) {
        ResultSet rs=null;
        if(conn!=null){

            QueryTask queryTask = new QueryTask();
            queryTask.execute(new Pair<Connection, String>(conn, getStudents));
            try {
                rs = queryTask.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_to_beacon){
            Intent intent = new Intent(this, BeaconActivity.class);
            startActivity(intent);
        }
    }


    private final class QueryTask extends AsyncTask<Pair<Connection, String>, Void, ResultSet> {

        @Override
        protected ResultSet doInBackground(Pair<Connection, String>... pairs) {
            Connection conn = pairs[0].first;
            String query = pairs[0].second;
            ResultSet rs = null;
            try {
                rs = conn.createStatement().executeQuery(query);


                //Toast.makeText(contexts[0], "Connected", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                //Toast.makeText(contexts[0], "Fail", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return rs;
        }
    }
    private final class ConnectTask extends AsyncTask<Context, Void, Connection> {

        @Override
        protected Connection doInBackground(Context... contexts) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(
                        "jdbc:postgresql://10.0.2.2:5433/attendance",
                        "postgres",
                        "1234567"
                );


                //Toast.makeText(contexts[0], "Connected", Toast.LENGTH_LONG).show();
            } catch (SQLException e) {
                Toast.makeText(contexts[0], "Fail", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return conn;
        }


    }


}
