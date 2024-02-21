package com.depiro.courseselect.Student;

import static com.depiro.courseselect.SelectMode.APP_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.depiro.courseselect.R;
import com.depiro.courseselect.Teacher.TLogin;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
This code Handles the Sign up function for students ...
 */
public class Signup extends AppCompatActivity {
    Spinner spinner;
    TextView switchToLogin;
    CardView signupBtn;
    EditText name, number, password;
    String stdName, stdNumber, pass, major, msg;
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        spinner = findViewById(R.id.dept_spinner);
        switchToLogin = findViewById(R.id.switch_to_login);
        signupBtn = findViewById(R.id.signup_btn);
        name = findViewById(R.id.edtName);
        number = findViewById(R.id.edt_stdno);
        password = findViewById(R.id.edtPassword);
        String[] majors = getResources().getStringArray(R.array.Majors);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, majors);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stdName = name.getText().toString();
                stdNumber = number.getText().toString();
                pass = password.getText().toString();
                major = spinner.getSelectedItem().toString();
                if (!stdName.isEmpty()) {
                    if (!stdNumber.isEmpty()) {
                        if (major != "Select Major") {
                            if (validatePassword(pass)) {
                                handleSignUp(stdName, stdNumber, major, pass);
                            } else {
                                msg = "Password must include a capital letter, a number and at least 8 characters";
                            }
                        } else {
                            msg = "Select Major";
                        }
                    } else {
                        msg = "Enter Number";
                    }
                } else {
                    msg = "Enter Name";
                }
                toastHandler(msg);
            }
        });

        switchToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }

    private void handleSignUp(String stdName, String stdNumber, String major, String pass) {

        String[] field = new String[4];
        field[0] = "name";
        field[1] = "std_no";
        field[2] = "major";
        field[3] = "password";

        String[] data = new String[4];
        data[0] = stdName;
        data[1] = stdNumber;
        data[2] = major;
        data[3] = pass;
        PutData putData = new PutData(APP_URL + "stdSignup.php", "POST", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                try {
                    JSONObject jsonObject = new JSONObject(putData.getResult());
                    String data_val = jsonObject.getString("data");
                    if (data_val.equals("Successful")) {
                        msg = "Successful";
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                    } else {
                        msg = data_val;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    private boolean validatePassword(String pass) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(pass);
        return matcher.find();
    }

    private void toastHandler(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}