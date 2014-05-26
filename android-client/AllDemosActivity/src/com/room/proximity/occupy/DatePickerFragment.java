package com.room.proximity.occupy;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends android.app.DialogFragment
implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),(PostBookingActivity)getActivity(), year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {		
    }

    public void onDateChanged (DatePicker view, int year, int month, int day){
    }
}