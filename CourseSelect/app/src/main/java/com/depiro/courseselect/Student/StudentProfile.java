package com.depiro.courseselect.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.depiro.courseselect.R;
import com.depiro.courseselect.SelectMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StudentProfile extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    CardView logout;
    TextView name, number,major;
    AlertDialog.Builder builder;
    private static String IS_LOGGED_IN = "isLogIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        bottomNavigationView= findViewById(R.id.bottom_nav);
        logout = findViewById(R.id.logout_btn);
        name = findViewById(R.id.user_name);
        number = findViewById(R.id.user_no);
        major = findViewById(R.id.user_major);


        SharedPreferences preferences = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE);
        String nme = preferences.getString("user_name", "");
        String maj = preferences.getString("major", "");
        String id = preferences.getString("user_no", "");
        name.setText(nme);
        number.setText(id);
        major.setText(maj);

        builder = new AlertDialog.Builder(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogOut();
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.user);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.apply:
                        startActivity(new Intent(getApplicationContext(), Apply.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.user:

                        return true;

                }
                return false;
            }
        });
    }

    private void handleLogOut() {
        builder.setTitle("Sign out")
                .setMessage("Do you want to sign out?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE).edit();
                        editor.putBoolean("isLog", false);
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), SelectMode.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.stay);
                        finish();
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
}