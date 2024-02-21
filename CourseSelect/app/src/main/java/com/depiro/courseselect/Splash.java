package com.depiro.courseselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.depiro.courseselect.Student.StudentDashboard;
import com.depiro.courseselect.Teacher.Dashboard;

/*
This code is for the Splash page
 */
public class Splash extends AppCompatActivity {

    CardView start;
    private static String IS_LOGGED_IN = "isLogIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start = findViewById(R.id.start_btn);
        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean is_logged_in = preferences.getBoolean("isLog", false);
                if (is_logged_in) {
                    boolean is_teacher = preferences.getBoolean("isTeacher",false);
                    if (is_teacher){
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                    }
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                } else {
                    startActivity(new Intent(getApplicationContext(), SelectMode.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                }
            }
        });
    }
}