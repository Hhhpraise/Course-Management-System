package com.depiro.courseselect.Teacher;

import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.depiro.courseselect.MyClickInterface;
import com.depiro.courseselect.R;
import com.depiro.courseselect.Teacher.Adapter.Course;
import com.depiro.courseselect.Teacher.Adapter.CourseAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dashboard extends AppCompatActivity implements MyClickInterface {
    BottomNavigationView bottomNavigationView;
    TextView name, major;
    TextView currentDay;
    CourseAdapter courseAdapter;
    RecyclerView recyclerView;
    ArrayList<Course> courseArrayList;
    String nme;
    boolean isEmpty ;
    private static String IS_LOGGED_IN = "isLogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        name = findViewById(R.id.user_name);
        major = findViewById(R.id.teacher_course);
        currentDay = findViewById(R.id.day);
        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        nme = preferences.getString("user_name", "");
        String maj = preferences.getString("major", "");
        String id = preferences.getString("teacher_no", "");
        name.setText("Welcome " + nme);
        major.setText("Major : " + maj);
        currentDay.setText(getCurrentTime());
        getData(id);
        if (isEmpty){

        }
        else {
            courseAdapter = new CourseAdapter(getApplicationContext(), courseArrayList, this);
            recyclerView.setAdapter(courseAdapter);
        }

        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.setup:
                        startActivity(new Intent(getApplicationContext(), Setup.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }

    private void getData(String id) {
        String[] field = new String[1];
        field[0] = "id";
        String[] data = new String[1];
        data[0] = id;

        PutData putData = new PutData(APP_URL + "loadTCourse.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String value = putData.getResult();
                Log.d("details", value);
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String course = jsonObject.get("data").toString();
                    if(course.equals("empty")){
                        System.out.println(course);
                        isEmpty = true;
                    }
                    else {
                        courseArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject obj = (org.json.JSONObject) jsonArray.get(i);
                            String name = obj.getString("course_name");
                            String cHour = obj.getString("credit_hour");
                            int cid = Integer.parseInt(obj.getString("id"));
                            courseArrayList.add(new Course(cid, name, cHour));
                            isEmpty = false;

                        }
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

    @Override
    public void onItemClick(int position) {
        String courseName = courseArrayList.get(position).getName();
        Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
        intent.putExtra("course_name", courseName);
        intent.putExtra("t_name", nme);
        startActivity(intent);


    }
}