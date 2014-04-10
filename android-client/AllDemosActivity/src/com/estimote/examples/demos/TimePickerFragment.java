package com.estimote.examples.demos;
import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


<<<<<<< HEAD

=======
>>>>>>> 273f354a0668a13fbc1b3174e5646d9826bddd48
public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {
	
	private int mId;
    private TimePickerDialogListener mListener;

     public static TimePickerFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("picker_id", id);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }
	
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
		 
		 mId = getArguments().getInt("picker_id");
		 mListener = getActivity() instanceof TimePickerDialogListener ? (TimePickerDialogListener) getActivity() : null;

		 // Use the current time as the default values for the picker
		 final Calendar c = Calendar.getInstance();
		 int hour = c.get(Calendar.HOUR_OF_DAY);
		 int minute = c.get(Calendar.MINUTE);
		 // Create a new instance of TimePickerDialog and return it
//		 return new TimePickerDialog(getActivity(), (PostBookingActivity)getActivity(), hour, minute,
//	                DateFormat.is24HourFormat(getActivity()));
		 return new TimePickerDialog(getActivity(), this , hour, minute, true);
	    }

	    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    	if (mListener != null) mListener.onTimeSet(mId, view, hourOfDay, minute);
	    }
	    
	    public static interface TimePickerDialogListener {
	        public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute);
	    }


}

