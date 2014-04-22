package com.room.proximity.occupy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class PostBookingActivity extends Activity implements OnDateSetListener, TimePickerFragment.TimePickerDialogListener{

    private static final int START_TIME_PICKER_ID = 1;
    private static final int END_TIME_PICKER_ID = 2;
    JSONArray jBookings =  null;
    Button book = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occupy_later);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activitiy_close_scale);
        final String room = getIntent().getStringExtra("roomname");
        EditText meetingName = (EditText)findViewById(R.id.edtMeetingName);
        meetingName.setText("TestUser's Meeting");

        try {
            jBookings = new JSONArray(getIntent().getStringExtra("bookings"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Button book = (Button) findViewById(R.id.btnSave);
        book.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                        AsyncHttpPost asyncHttpPost = new AsyncHttpPost();
                        asyncHttpPost.execute("http://localhost:8888/rooms/" + room + "/booking");
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
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activitiy_close_translate);
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

    class AsyncHttpPost extends AsyncTask<String, String, String>{
        String result = "0";

        @Override
        protected String doInBackground(String... params) {
 
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try{			

                View currentView = findViewById(android.R.id.content);

                EditText date = (EditText) currentView.findViewById(R.id.edtDate);
                EditText meetingName = (EditText) currentView.findViewById(R.id.edtMeetingName);
                EditText startT = (EditText) currentView.findViewById(R.id.edtStartTime);
                EditText endT = (EditText) currentView.findViewById(R.id.edtEndTime);
                //EditText users = (EditText) currentView.findViewWithTag("edtUsers");

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("uri",""));
                nameValuePairs.add(new BasicNameValuePair("date",date.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("meetingName",meetingName.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("startTime", startT.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("endTime", endT.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("users", ""));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httppost);

                result = "1";    
                //                }

            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }finally{

            }		
            return result;
        }
                
        @Override
        protected void onPostExecute(String result) {
            if(result == "1"){
                Toast.makeText(getBaseContext(), "Booking Made!", Toast.LENGTH_SHORT).show();
            }else if(result == "0"){
                Toast.makeText(getBaseContext(), "Booking Error!", Toast.LENGTH_SHORT).show();
            }
        }
             
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
            int dayOfMonth) {
        ((EditText) findViewById(R.id.edtDate)).setText( dayOfMonth + "/" + monthOfYear + "/" + year);

    }

    @Override
    public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
        switch(id) {
        case 1:
            ((EditText) findViewById(R.id.edtStartTime)).setText( hourOfDay + ":" + minute);
            break;
        case 2:
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
                    }else{
                        ((EditText) findViewById(R.id.edtEndTime)).setText( hourOfDay + ":" + minute);
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
        }
    }
}


