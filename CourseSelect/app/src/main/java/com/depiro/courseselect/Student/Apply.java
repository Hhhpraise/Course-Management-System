package com.depiro.courseselect.Student;


import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.depiro.courseselect.MyClickInterface;
import com.depiro.courseselect.R;
import com.depiro.courseselect.Student.Adapter.Course;
import com.depiro.courseselect.Student.Adapter.CourseAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/*
This code is for the page where students can apply to register for the courses available to their major
 */
public class Apply extends AppCompatActivity implements MyClickInterface {
    BottomNavigationView bottomNavigationView;
    TextView major;
    RecyclerView recyclerView;
    CourseAdapter adapter;
    String msg;
    ArrayList<Course> courseArrayList;
    AlertDialog.Builder builder;
    String name;
    String std_no;
    boolean isEmpty;
    private static String IS_LOGGED_IN = "isLogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        major = findViewById(R.id.std_major);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.apply);
        builder = new AlertDialog.Builder(this);
        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        name = preferences.getString("user_name", "");
        String maj = preferences.getString("major", "");
        std_no = preferences.getString("user_no", "");

        major.setText(maj);
        recyclerView = findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getData(maj);
        if (isEmpty) {
        } else {
            adapter = new CourseAdapter(getApplicationContext(), courseArrayList, this);
            recyclerView.setAdapter(adapter);
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.apply:
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

    private void getData(String maj) {
        String[] field = new String[1];
        field[0] = "major";
        String[] data = new String[1];
        data[0] = maj;

        PutData putData = new PutData(APP_URL + "loadSCourse.php", "POST", field, data);
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
                            String cname = obj.getString("course_name");
                            String tname = obj.getString("name");
                            String cHour = obj.getString("credit_hour");
                            int cid = Integer.parseInt(obj.getString("id"));
                            courseArrayList.add(new Course(cid, cname, tname, cHour));
                            isEmpty = false;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    @Override
    public void onItemClick(int position) {
        builder.setTitle("Register")
                .setMessage("Do you want to register for " + courseArrayList.get(position).getCname() + "?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handleReg(std_no, courseArrayList.get(position).getCname(), courseArrayList.get(position).getTname());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .show();

    }

    private void handleReg(String no, String cname, String tname) {
        String[] field = new String[3];
        field[0] = "s_no";
        field[1] = "course_name";
        field[2] = "t_name";


        String[] data = new String[3];
        data[0] = no;
        data[1] = cname;
        data[2] = tname;
        PutData putData = new PutData(APP_URL + "courseReg.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                System.out.println(putData.getResult());
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String data_val = jsonObject.getString("data");
                    if (data_val.equals("Successful")) {
                        msg = "Successful";
                        startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                    } else {
                        msg = data_val;

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            toastHandler(msg);
        }

    }


    private void toastHandler(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}