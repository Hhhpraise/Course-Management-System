package com.depiro.courseselect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.depiro.courseselect.Student.Login;
import com.depiro.courseselect.Teacher.TLogin;

public class SelectMode extends AppCompatActivity {
    CardView teacher, student;
    private static String IS_LOGGED_IN = "isLogIn";
    //update your app URL ...
    public static String APP_URL = "http://192.168.1.12/CourseSelectApp/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
        teacher = findViewById(R.id.techer);
        student = findViewById(R.id.stdent);
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TLogin.class));
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

    }
}