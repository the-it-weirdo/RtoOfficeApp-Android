package com.example.debaleen.project2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertNewCar extends AppCompatActivity {

    EditText carNumberField, ownerNameField;
    Spinner typeSpinner, insuranceSpinner, pollutionSpinner;
    Button regDateChooserButton, submitButton, backButton;

    int typePosition, insuranceStatusPosition, pollutionStatusPosition;

    String carNumber, ownerName, regDate = "";

    String id;

    DataBaseHelper dataBaseHelper;

    boolean dateFlag;

    String[] typeSpinnerChooser = {"--select car type--", "Petrol", "Diesel", "CNG", "Hybrid(CNG-Petrol)", "Electric"};
    String[] insuranceSpinnerChooser = {"--select insurance status--", "Active", "Expired"};
    String[] pollutionSpinnerChooser = {"--select pollution status--", "Okay", "Check Required", "Expired"};

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_new_car);
        carNumberField = (EditText) findViewById(R.id.carNumberField);
        ownerNameField = (EditText) findViewById(R.id.ownerNameField);
        typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        insuranceSpinner = (Spinner) findViewById(R.id.insuranceSpinner);
        pollutionSpinner = (Spinner) findViewById(R.id.pollutionSpinner);
        regDateChooserButton = (Button) findViewById(R.id.regDateButton);
        submitButton = (Button) findViewById(R.id.submitButton);
        backButton = (Button) findViewById(R.id.backButton);

        regDateChooserButton.setText(R.string.regDate);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        id = getIntent().getExtras().getString("ID");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeSpinnerChooser);
        typeSpinner.setAdapter(typeAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> insuranceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, insuranceSpinnerChooser);
        insuranceSpinner.setAdapter(insuranceAdapter);

        insuranceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                insuranceStatusPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> pollutionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pollutionSpinnerChooser);
        pollutionSpinner.setAdapter(pollutionAdapter);

        pollutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pollutionStatusPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        regDateChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFlag = true;
                final DialogFragment newDatePickerFragment = new DatePickerFragment();
                newDatePickerFragment.show(getSupportFragmentManager(), "Choose Registration Date");
                ((DatePickerFragment) newDatePickerFragment).dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                        if(datePicker.getYear()>year || datePicker.getMonth()>month || datePicker.getDayOfMonth()>dayOfMonth)
                        {
                            FancyToast.makeText(getApplicationContext(), "Invalid Date!!", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                            regDateChooserButton.setTextColor(getResources().getColor(R.color.expstat));
                            regDateChooserButton.setText("Invalid date!!");
                            dateFlag = false;
                        }
                        if(dateFlag) {
                            regDateChooserButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                            regDate = "" + datePicker.getYear() + " / " + (datePicker.getMonth() + 1) + " / " + datePicker.getDayOfMonth();
                            regDateChooserButton.setText(String.valueOf(regDate));
                        }
                    }
                };
            }
        });



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;


                carNumber = carNumberField.getText().toString().toUpperCase().trim();
                ownerName = ownerNameField.getText().toString();


                String ownerNameRegex = "^[\\p{L} .'-]+$";
                String carNumberRegex = "^[A-Z]{2}[0-9]{1,2}(?:A-Z)?(?:[A-Z]*)?[0-9]{4}$";

                Pattern patternOwnerName = Pattern.compile(ownerNameRegex);
                Pattern patternCarNumber = Pattern.compile(carNumberRegex);

                Matcher ownerNameMatcher = patternOwnerName.matcher(ownerName);
                Matcher carNumberMatcher = patternCarNumber.matcher(carNumber);

                if (carNumber.trim().isEmpty() || ownerName.trim().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Please fill all the fields.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if(!ownerNameMatcher.matches())
                {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid name.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (!carNumberMatcher.matches()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter a valid vehicle number.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (regDate.trim().isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Please select Registration Date.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
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

                if (typePosition == 0) {
                    FancyToast.makeText(getApplicationContext(), "Plese select type of vehicle.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if(flag)
                {
                    Cursor res = dataBaseHelper.displayByCarNumber(carNumber);

                    if (res.moveToNext()) {
                        FancyToast.makeText(getApplicationContext(), "Number already registered", FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                        flag = false;
                    }
                }

                if(!dateFlag)
                {
                    FancyToast.makeText(getApplicationContext(), "Please select a valid date.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }

                if (flag && dateFlag) {
                    dataBaseHelper = new DataBaseHelper(getApplicationContext());
                    String ownerNameUpper = ownerName.toUpperCase();
                    boolean regStatus = dataBaseHelper.insertIntoT2(carNumber, ownerNameUpper, typeSpinnerChooser[typePosition
                            ], regDate, insuranceSpinnerChooser[insuranceStatusPosition], pollutionSpinnerChooser[pollutionStatusPosition]);

                    if (regStatus) {
                        FancyToast.makeText(getApplicationContext(), "Inserted successful!", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                                i.putExtra("ID", id);
                                startActivity(i);
                                finish();
                            }
                        }, 1400);
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Insertion Failed!!", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfileAdmin.class);
                i.putExtra("ID", id);
                startActivity(i);
                finish();
            }
        });
    }

}
