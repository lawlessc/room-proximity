package com.estimote.examples.demos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
//import com.estimote.examples.demos.BookingActivity.HttpAsyncTask;

public class PostBookingActivity extends Activity implements OnDateSetListener, TimePickerFragment.TimePickerDialogListener{
//	int start_hour, start_minute, end_hour, end_minute;
	private static final int START_TIME_PICKER_ID = 1;
	private static final int END_TIME_PICKER_ID = 2;
	JSONArray jBookings =  null;
	Button book = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		final String room = getIntent().getStringExtra("roomname");

		try {
			jBookings = new JSONArray(getIntent().getStringExtra("bookings"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("ROOM", room);;

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
				
				Log.e("POST", date.getText().toString());
				Log.e("POST", meetingName.getText().toString());
				Log.e("POST", startT.getText().toString());
//				Log.e("POST", endT.getText().toString());
				//Log.e("POST", users.getText().toString());


				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("uri",""));
				nameValuePairs.add(new BasicNameValuePair("date",date.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("meetingName",meetingName.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("startTime", startT.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("endTime", endT.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("users", "testUser"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse response = httpClient.execute(httppost);
				
				result = "1";
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
			if(result != "0"){
				Toast.makeText(getBaseContext(), "Booking Made!", Toast.LENGTH_LONG).show();
			}
		
			if (result == "0") {
				Log.d("beacons", "");
			}
			else{
				Toast.makeText(getBaseContext(), "Booking Error!", Toast.LENGTH_LONG).show();
			}
			
			//etResponse.setText(result);
		}
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		 ((EditText) findViewById(R.id.edtDate)).setText( dayOfMonth + "/" + monthOfYear + "/" + year);
		
	}

//	@Override
//	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//		//messy, change later
//		Log.e("Edit",((EditText) findViewById(R.id.edtStartTime)).getText().toString());
//		((EditText) findViewById(R.id.edtStartTime)).setText( hourOfDay + ":" + minute);
//	}

	@Override
	public void onTimeSet(int id, TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Log.i("TimePicker", "Time picker set from id " + id + "!");
		switch(id) {
	    case 1:
	    	((EditText) findViewById(R.id.edtStartTime)).setText( hourOfDay + ":" + minute);
	        break;
	    case 2:
	    	for(int i = 0;i<jBookings.length();i++){
	    		try {
	    			String startTime = jBookings.getJSONObject(i).optString("StartT");
	    			String endTime = jBookings.getJSONObject(i).optString("EndT");

	    			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
	    			Date start = parser.parse(startTime);
	    			Date end = parser.parse(endTime);

	    			//				String currentime = new SimpleDateFormat("HH:mm").format(new Date());
	    			String bookingTime = (Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
	    			EditText edtbookingStart = (EditText)findViewById(R.id.edtStartTime);
	    			String strbookingStart = edtbookingStart.getText().toString();
	    			Date now = parser.parse(bookingTime);
	    			Date startnow = parser.parse(strbookingStart);

	    			if(now.after(start) && now.before(end) || startnow.after(start) && startnow.before(end) ){
	    				Toast.makeText(getBaseContext(), "Room occupied during that time", Toast.LENGTH_LONG).show();
//	    				book.setEnabled(false);
	    				
	    			}else{
	    				((EditText) findViewById(R.id.edtEndTime)).setText( hourOfDay + ":" + minute);
//	    				book.setEnabled(true);
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


