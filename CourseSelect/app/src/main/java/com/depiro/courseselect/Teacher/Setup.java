package com.depiro.courseselect.Teacher;

import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.depiro.courseselect.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

/*
This code is for the teacher to setup and register new courses
 */
public class Setup extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    EditText course, hour;
    CardView finishBtn;
    String msg;
    private static String IS_LOGGED_IN = "isLogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        course = findViewById(R.id.edt_course);
        hour = findViewById(R.id.edt_hour);
        finishBtn = findViewById(R.id.finish_btn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = course.getText().toString();
                String chour = hour.getText().toString();
                if ((!name.isEmpty()) && (!chour.isEmpty())) {
                    handleSetup(name, chour);
                } else {
                    Toast.makeText(getApplicationContext(), "fields can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.setup);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.setup:

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

    private void handleSetup(String name, String chour) {
        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        String t_id = preferences.getString("teacher_no", "");
        String major = preferences.getString("major", "");

        String[] field = new String[4];
        field[0] = "major";
        field[1] = "course_name";
        field[2] = "credit_hour";
        field[3] = "t_id";

        String[] data = new String[4];
        data[0] = major;
        data[1] = name;
        data[2] = chour;
        data[3] = t_id;

        PutData putData = new PutData(APP_URL + "setup.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                System.out.println(putData.getResult());
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String data_val = jsonObject.getString("data");
                    if (data_val.equals("Successful")) {
                        msg = "Successful";
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                    } else {
                        msg = data_val;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        toastHandler(msg);
    }

    private void toastHandler(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}