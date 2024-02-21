package com.depiro.courseselect.Teacher;

import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.depiro.courseselect.R;
import com.depiro.courseselect.Student.Adapter.Course;
import com.depiro.courseselect.Teacher.Adapter.Student;
import com.depiro.courseselect.Teacher.Adapter.StudentAdapter;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CourseDetail extends AppCompatActivity {
    TextView course, sCount;
    TextView currentDay;
    String courseName, tName;
    RecyclerView recyclerView;
    ArrayList<Student> studentArrayList;
    StudentAdapter adapter;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        course = findViewById(R.id.teacher_course);
        recyclerView = findViewById(R.id.recycler);
        sCount = findViewById(R.id.student_count);
        currentDay = findViewById(R.id.day);
        Intent intent = getIntent();
        courseName = intent.getStringExtra("course_name");
        tName = intent.getStringExtra("t_name");
        course.setText(courseName);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        currentDay.setText(getCurrentTime());

        if (getCount(courseName, tName) == 0) {
            sCount.setText("No registered student");
        } else {
            sCount.setText("All registered students : " + count);
            getData(courseName, tName);
            adapter = new StudentAdapter(getApplicationContext(), studentArrayList);
            recyclerView.setAdapter(adapter);
        }

    }

    private int getCount(String courseName, String tName) {
        String[] field = new String[2];
        field[0] = "course_name";
        field[1] = "t_name";
        String[] data = new String[2];
        data[0] = courseName;
        data[1] = tName;
        PutData putData = new PutData(APP_URL + "loadSCount.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String value = putData.getResult();
                Log.d("details", value);
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.equals("empty")) {
                        count = 0;
                    }
                    JSONObject countObj = jsonArray.getJSONObject(0);
                    count = countObj.getInt("COUNT(*)");
                    Log.d("details", count + "");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        return count;
    }

    private void getData(String courseName, String tName) {
        String[] field = new String[2];
        field[0] = "course_name";
        field[1] = "t_name";
        String[] data = new String[2];
        data[0] = courseName;
        data[1] = tName;

        PutData putData = new PutData(APP_URL + "loadStudent.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String value = putData.getResult();
                Log.d("details", value);
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    studentArrayList = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        org.json.JSONObject obj = (org.json.JSONObject) jsonArray.get(i);
                        String name = obj.getString("name");
                        String number = obj.getString("s_no");

                        studentArrayList.add(new Student(name, number));

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
    private String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE,MMM d");
        Date date = new Date();
        return formatter.format(date).toString();
    }
}