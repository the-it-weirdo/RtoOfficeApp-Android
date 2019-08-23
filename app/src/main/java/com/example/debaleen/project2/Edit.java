package com.example.debaleen.project2;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

public class Edit extends AppCompatActivity {


    TextView vehicleNumberField, typeField, regDateField;
    Button backButton, updateButton;

    Spinner insuranceSpinner, pollutionSpinner;
    EditText ownerNameField;

    String id="", vehId="", vehNum="", ownName="", type="", regD="", insSt="", pollSt="", action="";

    DataBaseHelper dataBaseHelper;

    int insuranceStatusPosition, pollutionStatusPosition;

    String[] insuranceSpinnerChooser = {"--select insurance status--", "Active", "Expired"};
    String[] pollutionSpinnerChooser = {"--select pollution status--", "Okay", "Check Required", "Expired"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        vehicleNumberField = (TextView)findViewById(R.id.carNumberField);
        typeField = (TextView)findViewById(R.id.typeField);
        regDateField = (TextView)findViewById(R.id.regDateField);

        ownerNameField = (EditText) findViewById(R.id.ownerNameField);

        pollutionSpinner = (Spinner) findViewById(R.id.pollutionSpinner);
        insuranceSpinner = (Spinner) findViewById(R.id.insuranceSpinner);


        backButton = (Button)findViewById(R.id.backButton);
        updateButton = (Button)findViewById(R.id.updateButton);

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
            vehNum = res.getString(1);
            ownName = res.getString(2);
            type = res.getString(3);
            regD = res.getString(4);
            insSt = res.getString(5);
            pollSt = res.getString(6);

            if(insSt.compareTo("Active")==0)
            {
                insuranceStatusPosition = 1;
            }
            else if(insSt.compareTo("Expired")==0)
            {
                insuranceStatusPosition = 2;
            }
            else
            {
                insuranceStatusPosition = 0;
            }

            if(pollSt.compareTo("Okay")==0)
            {
                pollutionStatusPosition = 1;
            }
            else if(pollSt.compareTo("Check Required")==0)
            {
                pollutionStatusPosition = 2;
            }
            else if(pollSt.compareTo("Expired")==0)
            {
                pollutionStatusPosition = 3;
            }
            else
            {
                pollutionStatusPosition = 0;
            }


            vehicleNumberField.setText(String.valueOf(vehNum));
            typeField.setText(String.valueOf(type));
            regDateField.setText(String.valueOf(regD));

            ownerNameField.setText(String.valueOf(ownName));

            ArrayAdapter<String> insuranceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, insuranceSpinnerChooser);
            insuranceSpinner.setAdapter(insuranceAdapter);

            insuranceSpinner.setSelection(insuranceStatusPosition);

            insuranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                    insuranceStatusPosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            ArrayAdapter<String> pollutionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pollutionSpinnerChooser);
            pollutionSpinner.setAdapter(pollutionAdapter);

            pollutionSpinner.setSelection(pollutionStatusPosition);

            pollutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    pollutionStatusPosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }

        backButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if(action.compareTo("EDIT")==0)
                {
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

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                ownName = ownerNameField.getText().toString();

                if(ownName.isEmpty())
                {
                    FancyToast.makeText(getApplicationContext(), "Please enter owner name!", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (pollutionStatusPosition == 0) {
                    FancyToast.makeText(getApplicationContext(), "Plese select pollution status.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (insuranceStatusPosition == 0) {
                    FancyToast.makeText(getApplicationContext(), "Plese select insurance status.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if(flag)
                {
                    //String id, String carNumber, String owner, String type, String regdate, String insurance, String pollutionstatus
                    String ownerNameUpper = ownName.toUpperCase();
                    boolean stat = dataBaseHelper.updateInT2(vehId, vehNum, ownerNameUpper, type, regD, insuranceSpinnerChooser[insuranceStatusPosition], pollutionSpinnerChooser[pollutionStatusPosition]);
                    if(stat)
                    {
                        FancyToast.makeText(getApplicationContext(), "Updated successful!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(action.compareTo("EDIT")==0)
                                {
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
                        FancyToast.makeText(getApplicationContext(), "Update failed!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }
        });
    }
}
