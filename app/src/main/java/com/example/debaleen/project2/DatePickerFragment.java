package com.example.debaleen.project2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;



public class DatePickerFragment extends DialogFragment {
    /**
     * Date Set Listener
     **/
    public String date = "";

    public DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    FancyToast.makeText(getActivity(), "Selected date is " + view.getYear() + " / " + (view.getMonth() + 1) + " / " + view.getDayOfMonth(), FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show();
                    date = "" + view.getYear()+" / " + (view.getMonth()+1) + " / " + view.getDayOfMonth();
                }
            };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**Init Calendar**/
        final Calendar c = Calendar.getInstance();

        /**Get Year**/
        int year = c.get(Calendar.YEAR);

        /**Get Month**/
        int month = c.get(Calendar.MONTH);

        /**Get Date of Month**/
        int day = c.get(Calendar.DAY_OF_MONTH);

        /**Return a DatePickerDialog**/
        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    public String getDate() {
        return date;
    }
}
