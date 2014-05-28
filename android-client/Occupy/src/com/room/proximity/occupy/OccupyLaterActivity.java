package com.room.proximity.occupy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class OccupyLaterActivity extends Activity implements OnDateSetListener, TimePickerFragment.TimePickerDialogListener{

    private static final int START_TIME_PICKER_ID = 1;
    private static final int END_TIME_PICKER_ID = 2;
    private JSONArray jBookings =  null;
    private String room = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occupy_later);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        EditText meetingName = (EditText)findViewById(R.id.edtMeetingName);
        meetingName.setText("TestUser's Meeting");
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {
                jBookings = new JSONArray(intent.getStringExtra("bookings"));
                room = getIntent().getStringExtra("roomname");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        Button book = (Button) findViewById(R.id.btnSave);
        book.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(room != null){
                    String[] postValues = getValues();
                    String[] myTaskParams = {"http://localhost:8888/rooms/" + room + "/booking", postValues[0] ,  postValues[1]  , postValues[2] ,postValues[3], postValues[4] };
                    myAsyncTaskPost post = new myAsyncTaskPost();
                    post.execute(myTaskParams);
                }else{
                    Toast.makeText(getBaseContext(), "Dummy Booking Made!", Toast.LENGTH_SHORT).show();
                }          
            }
        });

        EditText mEditDate = (EditText) findViewById(R.id.edtDate);
        mEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        EditText mEditStartT = (EditText) findViewById(R.id.edtStartTime);
        mEditStartT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStartTime(v);
            }
        });

        EditText mEditEndT = (EditText) findViewById(R.id.edtEndTime);
        mEditEndT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputEndTime(v);
            }
        });
        
        Log.e("Activity","InPostActivity");
    }	

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    public void InputStartTime(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(START_TIME_PICKER_ID);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void InputEndTime(View v) {
        DialogFragment newFragment = TimePickerFragment.newInstance(END_TIME_PICKER_ID);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    public String[] getValues(){
        View currentView = findViewById(android.R.id.content);

        EditText date = (EditText) currentView.findViewById(R.id.edtDate);
        EditText meetingName = (EditText) currentView.findViewById(R.id.edtMeetingName);
        EditText startT = (EditText) currentView.findViewById(R.id.edtStartTime);
        EditText endT = (EditText) currentView.findViewById(R.id.edtEndTime);
        
        String[] postValues = {date.getText().toString(),meetingName.getText().toString(),startT.getText().toString(),endT.getText().toString(),"TestUser"};
        
        return postValues;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        ((EditText) findViewById(R.id.edtDate)).setText( dayOfMonth + "/" + monthOfYear + "/" + year);

    }

    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        Button btnBookVisibility =  (Button) findViewById(R.id.btnSave); 
        switch(id) {
        case 1:
            ((EditText) findViewById(R.id.edtStartTime)).setText( hourOfDay + ":" + minute);
            break;
        case 2:
            if(jBookings != null){
                for(int i = 0;i<jBookings.length();i++){
                    try {
                        String startTime = jBookings.getJSONObject(i).optString("StartT");
                        String endTime = jBookings.getJSONObject(i).optString("EndT");

                        SimpleDateFormat parser = new SimpleDateFormat("HH:mm",Locale.UK);
                        Date start = parser.parse(startTime);
                        Date end = parser.parse(endTime);

                        String bookingTime = (Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
                        EditText edtbookingStart = (EditText)findViewById(R.id.edtStartTime);
                        String strbookingStart = edtbookingStart.getText().toString();
                        Date now = parser.parse(bookingTime);
                        Date startnow = parser.parse(strbookingStart);

                        if(now.after(start) && now.before(end) || startnow.after(start) && startnow.before(end) ){
                            Toast.makeText(getBaseContext(), "Room occupied during that time", Toast.LENGTH_SHORT).show(); 
                            btnBookVisibility.setVisibility(8);
                        }else{
                            ((EditText) findViewById(R.id.edtEndTime)).setText( hourOfDay + ":" + minute);
                            btnBookVisibility.setVisibility(0);
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                break;
            }else{
                ((EditText) findViewById(R.id.edtEndTime)).setText( hourOfDay + ":" + minute);
            }
           
        }
    }
}


