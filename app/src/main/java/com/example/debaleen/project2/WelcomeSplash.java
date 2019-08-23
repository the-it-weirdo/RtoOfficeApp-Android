package com.example.debaleen.project2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import id.pahlevikun.droidcrypt.DroidCrypt;
import id.pahlevikun.droidcrypt.model.Data;
import id.pahlevikun.droidcrypt.type.Algorithm;

public class WelcomeSplash extends AppCompatActivity {

    Typewriter typeWriter;
    ImageView logo;
    Animation anim;
    private static final String key ="2";
    int delay = 2300;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_splash);

        typeWriter = (Typewriter)findViewById(R.id.typewriter);
        logo = (ImageView)findViewById(R.id.logo);

        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce);
        logo.startAnimation(anim);

        typeWriter.setText("");
        typeWriter.setCharecterDelay(200);
        typeWriter.animateText("Loading...");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String id;
                try
                {
                    DroidCrypt droidCrypt = new DroidCrypt(getApplicationContext());
                    Data data=droidCrypt.getEncryptedFromPreferences();
                    Data idData = droidCrypt.startDecryptWithBase64(key, Algorithm.MD5.getType(), data);

                    dataBaseHelper = new DataBaseHelper(getApplicationContext());


                    id = idData.getTextInString();
                    //Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
                    Cursor res = dataBaseHelper.displayByIdFromT1(id);
                    if(!res.moveToNext())
                    {
                        //Toast.makeText(getApplicationContext(), "MOVED", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        String userType = res.getString(6);
                        //Toast.makeText(getApplicationContext(),userType,Toast.LENGTH_LONG).show();
                        if(userType.compareTo("Admin")==0)
                        {
                            Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                            i.putExtra("ID", id);
                            startActivity(i);
                            finish();
                        }
                        else if(userType.compareTo("User")==0)
                        {
                            Intent i = new Intent(getApplicationContext(), Profile.class);
                            i.putExtra("ID", id);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            //Toast.makeText(getApplicationContext(),"ELSE", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
                catch(Exception e)
                {
                    //FancyToast.makeText(getApplicationContext(), String.valueOf(e), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);
                    finish();
                }
            }
        }, delay);
    }
}
