package com.example.debaleen.project2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.pahlevikun.droidcrypt.DroidCrypt;
import id.pahlevikun.droidcrypt.model.Data;
import id.pahlevikun.droidcrypt.type.Algorithm;

public class Login extends AppCompatActivity {

    EditText userNameField, passwordField;
    Button loginButton, newUserButton;
    Spinner userTypeSpinner;
    DataBaseHelper dataBaseHelper;

    private static final String key ="2";

    String username = "", password = "";
    int usertypePosition = 0;

    String[] userType = {"--select User type--", "User", "Admin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);
        userTypeSpinner = (Spinner) findViewById(R.id.userTypeSpinner);
        loginButton = (Button) findViewById(R.id.loginButton);
        newUserButton = (Button) findViewById(R.id.newUserButton);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userType);
        userTypeSpinner.setAdapter(adapter);

        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usertypePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dataBaseHelper = new DataBaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                username = userNameField.getText().toString();
                password = passwordField.getText().toString();
                String usertypeName = userTypeSpinner.getItemAtPosition(usertypePosition).toString();
                String id = "";

                if (username.trim().isEmpty() || password.trim().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Please fill both fields.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:" +
                        "[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

                Pattern patternEmail = Pattern.compile(emailRegex);
                Pattern patternPassword = Pattern.compile(passwordRegex);

                Matcher matcherEmail = patternEmail.matcher(username);
                Matcher matcherPassword = patternPassword.matcher(password);

                if (!matcherEmail.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid username.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }
                if (!matcherPassword.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid password.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (usertypePosition == 0) {
                    FancyToast.makeText(getApplicationContext(), "Please select user type", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if(flag && username.compareTo("debaleen@root.com")==0 && password.compareTo("12345678a@A")==0)
                {
                    flag = false;
                    Intent i = new Intent(getApplicationContext(), AdminRegistration.class);
                    startActivity(i);
                    finish();
                }

                if (flag) {
                    Cursor res = dataBaseHelper.displayByEmailPasswordType(username, password, userType[usertypePosition].trim());
                    if (!res.moveToNext()) {
                        FancyToast.makeText(getApplicationContext(), "Invalid username & password.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        flag = false;
                    }
                    else
                    {
                        id = res.getString(0);
                    }
                }
                if (flag && !id.isEmpty()) {
                    //all check ok. goto profile of username. id should be string. use database.displayByIdFromT1
                    // in target intent to fetch user details.
                    /*
                    if(item.getTitle()=="View")
        {
            Toast.makeText(this,item.getTitle(),Toast.LENGTH_SHORT).show();
            Intent i1=new Intent(getApplicationContext(),View1.class);
            //String v1=B.substring(0,2);
            Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
            i1.putExtra("var1",id);
            startActivity(i1);
        }
                     */

                    //encryption start

                    try {
                        DroidCrypt droidCrypt = new DroidCrypt(getApplicationContext());
                        Data encryptedId = droidCrypt.startEncryptWithBase64(key, Algorithm.MD5.getType(), id);

                        droidCrypt.saveEncryptedToPreferences(encryptedId);
                    }
                    catch(Exception e)
                    {
                        FancyToast.makeText(getApplicationContext(), String.valueOf(e), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                    //encryption end

                    /* Decryption start

                    String userNameDecrypted="", passwordDecrypted="", userTypeDecrypted="";
                    try
                    {
                        DroidCrypt droidCrypt = new DroidCrypt(getApplicationContext());
                        Data userNameDecryptedData = droidCrypt.startDecryptWithBase64(key, Algorithm.MD5.getType(), username);
                        Data passwordDecryptedData = droidCrypt.startDecryptWithBase64(key, Algorithm.MD5.getType(), password);
                        Data userTypeDecryptedData = droidCrypt.startDecryptWithBase64(key, Algorithm.MD5.getType(), userType[usertypePosition]);

                        userNameDecrypted = userNameDecryptedData.getTextInString();
                        passwordDecrypted = passwordDecryptedData.getTextInString();
                        userTypeDecrypted = userTypeDecryptedData.getTextInString();

                    }
                    catch(Exception e)
                    {
                        FancyToast.makeText(getApplicationContext(), String.valueOf(e), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }

                     Decryption end*/

                    /*SharedPreferences.Editor editor = mPref.edit();
                    editor.putString("1", userNameEncrypted);
                    editor.putString("2", passwordEncrypted);
                    editor.putString("3", userTypeEncrypted);
                    editor.apply();*/

                    if(usertypeName.compareTo("Admin")==0)
                    {
                        Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                        i.putExtra("ID", id);
                        startActivity(i);
                        finish();
                    }
                    else if(usertypeName.compareTo("User")==0)
                    {
                        Intent i = new Intent(getApplicationContext(), Profile.class);
                        i.putExtra("ID", id);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        FancyToast.makeText(getApplicationContext(), "ERROR!!", FancyToast.LENGTH_SHORT, FancyToast.ERROR,false).show();
                    }
                    //Toast.makeText(getApplicationContext(), username + " " + password + " " +id+" "+ usertypeName, Toast.LENGTH_SHORT).show();
                }

            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
                finish();
            }
        });
    }
}
