package com.example.debaleen.project2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminRegistration extends AppCompatActivity {

    EditText nameField, emailField, phoneField, passwordField, confrimPasswordField;
    RadioGroup genderRadioGroup;
    Button registerButton, backButton;

    String name, email, phone, password, confirmPassword, gender, type="Admin";

    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);

        dataBaseHelper = new DataBaseHelper(this);

        nameField = (EditText) findViewById(R.id.regName);
        emailField = (EditText) findViewById(R.id.regEmail);
        phoneField = (EditText) findViewById(R.id.regPhone);
        passwordField = (EditText) findViewById(R.id.regPassword);
        confrimPasswordField = (EditText) findViewById(R.id.regPasswordConfirm);

        genderRadioGroup = (RadioGroup) findViewById(R.id.regGenderRadio);

        registerButton = (Button) findViewById(R.id.registerButton);
        backButton = (Button) findViewById(R.id.backButton);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioMale: {
                        gender = "Male";
                        break;
                    }
                    case R.id.radioFemale: {
                        gender = "Female";
                        break;
                    }
                    case R.id.radioNotDisclosed: {
                        gender = "Not Disclosed";
                        break;
                    }
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                name = nameField.getText().toString();
                email = emailField.getText().toString();
                phone = phoneField.getText().toString();
                password = passwordField.getText().toString();
                confirmPassword = confrimPasswordField.getText().toString();

                if (name.trim().isEmpty() || email.trim().isEmpty() || phone.trim().isEmpty()
                        || password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Please fill all fields.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                        "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
                String phoneRegex = "\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}";
                String nameRegex = "^[\\p{L} .'-]+$";


                Pattern patternName = Pattern.compile(nameRegex);
                Pattern patternEmail = Pattern.compile(emailRegex);
                Pattern patternPassword = Pattern.compile(passwordRegex);
                Pattern patternPhone = Pattern.compile(phoneRegex);

                Matcher nameMatcher = patternName.matcher(name);
                Matcher matcherEmail = patternEmail.matcher(email);
                Matcher matcherPassword = patternPassword.matcher(password);
                Matcher matcherPhone = patternPhone.matcher(phone);

                if(!nameMatcher.matches())
                {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid name.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (!matcherEmail.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid email.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }
                if (!matcherPassword.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid password." +
                            "Your password should contain atleast 1 number, 1 capital letter, 1 small letter" +
                            "and 1 special character. Minimum password length should be 8 characters.", FancyToast.LENGTH_LONG, FancyToast.INFO, false).show();
                    flag = false;
                }

                if(password.compareTo(confirmPassword)!=0)
                {
                    FancyToast.makeText(getApplicationContext(), "Password and confirm password doesn't match.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    flag = false;
                }
                if (!matcherPhone.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid phone number.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (flag) {
                    Cursor res = dataBaseHelper.displayByEmail(email);

                    if (res.moveToNext()) {
                        FancyToast.makeText(getApplicationContext(), "Email already exist", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                        flag = false;
                    }
                }

                if (flag) {
                    boolean regStatus = dataBaseHelper.insertIntoT1(name, email, phone, gender, password, type);
                    if (regStatus) {
                        FancyToast.makeText(getApplicationContext(), "Registration successful!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                                finish();
                            }
                        }, 1400);
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Registration Failed!!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });
    }

}
