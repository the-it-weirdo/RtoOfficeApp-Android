package com.example.debaleen.project2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.shashank.sony.fancytoastlib.FancyToast;

import id.pahlevikun.droidcrypt.DroidCrypt;


public class Profile extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    Button searchButton;
    TextView noResult;
    ConstraintLayout includeReultFound;

    DroidCrypt droidCrypt;

    DataBaseHelper dataBaseHelper;

    String query = "";

    //ArrayList<String> carDetails= new ArrayList<String>();;
    //ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dataBaseHelper = new DataBaseHelper(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        noResult = (TextView) findViewById(R.id.noResultTextView);
        includeReultFound = (ConstraintLayout) findViewById(R.id.resultFoundView);
        searchButton = (Button) findViewById(R.id.searchButton);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String id = getIntent().getExtras().getString("ID");

        searchView.setQueryHint("Search by car number");

        Cursor res = dataBaseHelper.displayByIdFromT1(id);
        if (res.moveToNext()) {
            getSupportActionBar().setSubtitle(res.getString(1));
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                if(noResult.getVisibility()==View.VISIBLE)
                {
                    noResult.setVisibility(View.GONE);
                }
                boolean flag = true;
                query = searchView.getQuery().toString().toUpperCase().trim();
                if (query.isEmpty()) {
                    FancyToast.makeText(getApplicationContext(), "Please enter car number to search.", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false).show();
                    flag = false;
                }
                if (flag) {
                    Cursor res = dataBaseHelper.displayByCarNumber(query);
                    if (res.moveToNext()) {
                        includeReultFound.setVisibility(View.VISIBLE);

                        TextView vehicleNumberField, ownerNameField, typeField, pollutionStatField, insuranceStatField, regDateField;

                        vehicleNumberField = (TextView) findViewById(R.id.carNumberField);
                        ownerNameField = (TextView) findViewById(R.id.ownerNameField);
                        typeField = (TextView) findViewById(R.id.typeField);
                        pollutionStatField = (TextView) findViewById(R.id.pollutionStatusField);
                        insuranceStatField = (TextView) findViewById(R.id.insuranceStatusField);
                        regDateField = (TextView) findViewById(R.id.regDateField);

                        String vehNum = res.getString(1);
                        String ownName = res.getString(2);
                        String type = res.getString(3);
                        String regD = res.getString(4);
                        String insSt = res.getString(5);
                        String pollSt = res.getString(6);

                        if (insSt.compareTo("Active") == 0) {
                            insuranceStatField.setTextColor(getResources().getColor(R.color.okstat));
                        } else {
                            insuranceStatField.setTextColor(getResources().getColor(R.color.expstat));
                        }

                        if (pollSt.compareTo("Okay") == 0) {
                            pollutionStatField.setTextColor(getResources().getColor(R.color.okstat));
                        } else if (pollSt.compareTo("Check Required") == 0) {
                            pollutionStatField.setTextColor(getResources().getColor(R.color.checkstat));
                        } else {
                            pollutionStatField.setTextColor(getResources().getColor(R.color.expstat));
                        }


                        vehicleNumberField.setText(String.valueOf(vehNum));
                        ownerNameField.setText(String.valueOf(ownName));
                        typeField.setText(String.valueOf(type));
                        regDateField.setText(String.valueOf(regD));
                        insuranceStatField.setText(String.valueOf(insSt));
                        pollutionStatField.setText(String.valueOf(pollSt));
                    }
                    else {
                        if(includeReultFound.getVisibility()==View.VISIBLE)
                        {
                            includeReultFound.setVisibility(View.GONE);
                        }
                        noResult.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                Cursor res = dataBaseHelper.displayByCarNumber(query.toUpperCase().trim());
                if (res.moveToNext()) {
                    if(noResult.getVisibility()==View.VISIBLE)
                    {
                        noResult.setVisibility(View.GONE);
                    }
                    includeReultFound.setVisibility(View.VISIBLE);

                    TextView vehicleNumberField, ownerNameField, typeField, pollutionStatField, insuranceStatField, regDateField;

                    vehicleNumberField = (TextView) findViewById(R.id.carNumberField);
                    ownerNameField = (TextView) findViewById(R.id.ownerNameField);
                    typeField = (TextView) findViewById(R.id.typeField);
                    pollutionStatField = (TextView) findViewById(R.id.pollutionStatusField);
                    insuranceStatField = (TextView) findViewById(R.id.insuranceStatusField);
                    regDateField = (TextView) findViewById(R.id.regDateField);

                    String vehNum = res.getString(1);
                    String ownName = res.getString(2);
                    String type = res.getString(3);
                    String regD = res.getString(4);
                    String insSt = res.getString(5);
                    String pollSt = res.getString(6);

                    if (insSt.compareTo("Active") == 0) {
                        insuranceStatField.setTextColor(getResources().getColor(R.color.okstat));
                    } else {
                        insuranceStatField.setTextColor(getResources().getColor(R.color.expstat));
                    }

                    if (pollSt.compareTo("Okay") == 0) {
                        pollutionStatField.setTextColor(getResources().getColor(R.color.okstat));
                    } else if (pollSt.compareTo("Check Required") == 0) {
                        pollutionStatField.setTextColor(getResources().getColor(R.color.checkstat));
                    } else {
                        pollutionStatField.setTextColor(getResources().getColor(R.color.expstat));
                    }


                    vehicleNumberField.setText(String.valueOf(vehNum));
                    ownerNameField.setText(String.valueOf(ownName));
                    typeField.setText(String.valueOf(type));
                    regDateField.setText(String.valueOf(regD));
                    insuranceStatField.setText(String.valueOf(insSt));
                    pollutionStatField.setText(String.valueOf(pollSt));
                } else {
                    if(includeReultFound.getVisibility()==View.VISIBLE)
                    {
                        includeReultFound.setVisibility(View.GONE);
                    }
                    noResult.setVisibility(View.VISIBLE);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.profile_toolbar, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                droidCrypt = new DroidCrypt(getApplicationContext());
                droidCrypt.deleteEncryptedFromPreferences();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        }
        //return super.onOptionsItemSelected(item);
        return true;
    }
}
