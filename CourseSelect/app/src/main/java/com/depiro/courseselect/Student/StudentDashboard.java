package com.depiro.courseselect.Student;

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

import com.depiro.courseselect.R;
import com.depiro.courseselect.Student.Adapter.Course;
import com.depiro.courseselect.Student.Adapter.ScheduleAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentDashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView name, major;
    TextView currentDay;
    ScheduleAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Course> courseArrayList;
    boolean isEmpty;

    private static String IS_LOGGED_IN = "isLogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_schedule);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        currentDay = findViewById(R.id.day);
        name = findViewById(R.id.user_name);
        major = findViewById(R.id.std_major);
        currentDay.setText(getCurrentTime());
        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        String nme = preferences.getString("user_name", "");
        String maj = preferences.getString("major", "");
        String id = preferences.getString("user_no", "");
        name.setText("Welcome " + nme);
        major.setText("Major : " + maj);
        getData(id);
        if (isEmpty) {
        } else {
            adapter = new ScheduleAdapter(getApplicationContext(), courseArrayList);
            recyclerView.setAdapter(adapter);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        return true;
                    case R.id.apply:
                        startActivity(new Intent(getApplicationContext(), Apply.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.user:
                        startActivity(new Intent(getApplicationContext(), StudentProfile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

    }

    private void getData(String id) {
        String[] field = new String[1];
        field[0] = "s_no";
        String[] data = new String[1];
        data[0] = id;

        PutData putData = new PutData(APP_URL + "loadRegCourse.php", "POST", field, data);

        if (putData.startPut()) {
            if (putData.onComplete()) {
                String value = putData.getResult();
                Log.d("details", value);
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String check = jsonObject.get("data").toString();
                    if (check.equals("empty")) {
                        System.out.println(check);
                        isEmpty = true;
                    } else {
                        courseArrayList = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject obj = (org.json.JSONObject) jsonArray.get(i);
                            String name = obj.getString("course_name");
                            String cHour = obj.getString("credit_hour");
                            String tname = obj.getString("t_name");
                            int cid = Integer.parseInt(obj.getString("id"));
                            courseArrayList.add(new Course(cid, name, tname, cHour));
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
}