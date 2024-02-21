package com.depiro.courseselect.Student;

import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.depiro.courseselect.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

/*
This code is for the Login function for the students
 */
public class Login extends AppCompatActivity {
    TextView switchToReg;
    CardView loginBtn;
    private static final String IS_LOGGED_IN = "isLogIn";

    EditText number, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginBtn = findViewById(R.id.login_btn);
        switchToReg = findViewById(R.id.new_std_btn);
        number = findViewById(R.id.edt_stdno);
        password = findViewById(R.id.edt_password);


        switchToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String std_no = number.getText().toString();
                String pass = password.getText().toString();
                if ((!std_no.isEmpty()) && (!pass.isEmpty())) {
                    handleLogin(std_no, pass);
                } else {
                    Toast.makeText(Login.this, "fields can't be empty", Toast.LENGTH_SHORT).show();
                }
                // startActivity(new Intent(getApplicationContext(), StudentSchedule.class));
            }
        });
    }

    private void handleLogin(String std_no, String pass) {
        String[] field = new String[2];
        field[0] = "std_no";
        field[1] = "password";

        String[] data = new String[2];
        data[0] = std_no;
        data[1] = pass;
        PutData putData = new PutData(APP_URL + "stdlogin.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String data_val = jsonObject.getString("data");
                    Log.d("Login", data_val);
                    if (data_val.equals("Number or Password wrong")) {
                        Toast.makeText(getApplicationContext(), "Wrong Number or Password ", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject object = new JSONObject(data_val);
                        String test = object.getString("data");
                        String msg = object.getString("msg");

                        if (msg.equals("Successful")) {
                            Log.d("Login", test);
                            JSONObject obj = new JSONObject(test);
                            String name = obj.getString("name");
                            String stdno = obj.getString("std_no");
                            String major = obj.getString("major");
                            Log.d("Login", name + " " + stdno);
                            startActivity(new Intent(getApplicationContext(), StudentDashboard.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                            SharedPreferences.Editor editor = getSharedPreferences(IS_LOGGED_IN, MODE_PRIVATE).edit();
                            editor.putBoolean("isLog", true);
                            editor.putString("user_name", name);
                            editor.putBoolean("isTeacher",false);
                            editor.putString("user_no", stdno);
                            editor.putString("major",major);
                            editor.apply();

                        } else {
                            Toast.makeText(getApplicationContext(), "failed to log in", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }
}