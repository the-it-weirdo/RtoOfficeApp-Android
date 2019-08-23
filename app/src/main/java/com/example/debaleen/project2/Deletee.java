package com.example.debaleen.project2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class Deletee extends AppCompatActivity {

    TextView vehicleNumberField, ownerNameField, typeField, pollutionStatField, insuranceStatField, regDateField;
    Button backButton, yesButton;

    DataBaseHelper dataBaseHelper;

    String id, vehId, action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletee);

        vehicleNumberField = (TextView)findViewById(R.id.carNumberField);
        ownerNameField = (TextView)findViewById(R.id.ownerNameField);
        typeField = (TextView)findViewById(R.id.typeField);
        pollutionStatField = (TextView)findViewById(R.id.pollutionStatusField);
        insuranceStatField = (TextView)findViewById(R.id.insuranceStatusField);
        regDateField = (TextView)findViewById(R.id.regDateField);

        backButton = (Button)findViewById(R.id.backButton);
        yesButton = (Button)findViewById(R.id.yesButton);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        id = getIntent().getStringExtra("ID");
        vehId = getIntent().getStringExtra("VEHID");
        action = getIntent().getStringExtra("ACTION");
        if(action == null)
        {
            action = "NULL";
        }

        Cursor res = dataBaseHelper.displayByIdFromT2(vehId);

        if(!res.moveToNext())
        {
            FancyToast.makeText(getApplicationContext(), "Invalid Vehicle ID!!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
        }
        else
        {
            String vehNum = res.getString(1);
            String ownName = res.getString(2);
            String type = res.getString(3);
            String regD = res.getString(4);
            String insSt = res.getString(5);
            String pollSt = res.getString(6);

            if(insSt.compareTo("Active")==0)
            {
                insuranceStatField.setTextColor(getResources().getColor(R.color.okstat));
            }
            else
            {
                insuranceStatField.setTextColor(getResources().getColor(R.color.expstat));
            }

            if(pollSt.compareTo("Okay")==0)
            {
                pollutionStatField.setTextColor(getResources().getColor(R.color.okstat));
            }
            else if(pollSt.compareTo("Check Required")==0)
            {
                pollutionStatField.setTextColor(getResources().getColor(R.color.checkstat));
            }
            else
            {
                pollutionStatField.setTextColor(getResources().getColor(R.color.expstat));
            }

            vehicleNumberField.setText(String.valueOf(vehNum));
            ownerNameField.setText(String.valueOf(ownName));
            typeField.setText(String.valueOf(type));
            regDateField.setText(String.valueOf(regD));
            insuranceStatField.setText(String.valueOf(insSt));
            pollutionStatField.setText(String.valueOf(pollSt));
        }

        backButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(action.compareTo("DELETE")==0) {
                    Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                    i.putExtra("ID", id);
                    startActivity(i);
                    finish();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), ViewVehicles.class);
                    i.putExtra("ID", id);
                    //i.putExtra("ACTION", "NULL");
                    startActivity(i);
                    finish();
                }
            }
        });

        yesButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean stat = dataBaseHelper.deleteFromT2(vehId);
                if(stat)
                {
                    FancyToast.makeText(getApplicationContext(), "Deleted successfully.", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(action.compareTo("DELETE")==0) {
                                Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                                i.putExtra("ID", id);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Intent i = new Intent(getApplicationContext(), ViewVehicles.class);
                                i.putExtra("ID", id);
                                //i.putExtra("ACTION", "NULL");
                                startActivity(i);
                                finish();
                            }
                        }
                    }, 1400);
                }
                else
                {
                    FancyToast.makeText(getApplicationContext(), "Delete failed.", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
            }
        });
    }
}
