package com.estimote.examples.demos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

//import com.estimote.examples.demos.BookingActivity.HttpAsyncTask;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class PostBookingActivity extends Activity implements OnDateSetListener, OnTimeSetListener{
//	int start_hour, start_minute, end_hour, end_minute;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_item);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		final String room = getIntent().getStringExtra("roomname");
		Log.e("ROOM", room);
		//btnBook(room, this.findViewById(android.R.id.content));

		Button book = (Button) findViewById(R.id.btnSave);
		book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AsyncHttpPost asyncHttpPost = new AsyncHttpPost();
				asyncHttpPost.execute("http://localhost:8080/rooms/" + room + "/booking");
			}
		});
		
		 //initialize them to current date in onStart()/onCreate()
//		DatePickerDialog.OnDateSetListener from_dateListener,to_dateListener;
		
		EditText mEditDate = (EditText) findViewById(R.id.editDate);
		mEditDate.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	showDatePickerDialog(v);
	        }

	    });
		
		EditText mEditStartT = (EditText) findViewById(R.id.editStartTime);
		mEditStartT.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	showTimePickerDialog(v);
	        }

	    });
		
		EditText mEditEndT = (EditText) findViewById(R.id.editEndTime);
		mEditEndT.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	showTimePickerDialog(v);
	        }

	    });
		}	

	public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}

	class AsyncHttpPost extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(params[0]);
			
			try{

				View currentView = findViewById(android.R.id.content);
				////					View currentView = view.
				////				     macTextView = (TextView) view.findViewWithTag("mac");

				EditText date = (EditText) currentView.findViewWithTag("edtDate");
				EditText meetingName = (EditText) currentView.findViewWithTag("edtMeetingName");
				EditText startT = (EditText) currentView.findViewWithTag("edtStartT");
				EditText endT = (EditText) currentView.findViewWithTag("edtEndT");
				EditText users = (EditText) currentView.findViewWithTag("edtUsers");
				
				Log.e("POST", date.getText().toString());
				Log.e("POST", startT.getText().toString());
				Log.e("POST", endT.getText().toString());
				Log.e("POST", users.getText().toString());
				Log.e("POST", meetingName.getText().toString());

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("uri","testURI"));
				nameValuePairs.add(new BasicNameValuePair("date",date.getText().toString()));

				nameValuePairs.add(new BasicNameValuePair("startTime", startT.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("endTime", endT.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("meetingName",meetingName.getText().toString()));
				nameValuePairs.add(new BasicNameValuePair("users", "testUser"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httppost);
				
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
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				Toast.makeText(getBaseContext(), "Booking Made!", Toast.LENGTH_LONG).show();
			}
		
			if (result == null) {
				Log.d("beacons", "");
			}
			else{
//				Log.d("beacons", result);
			}
			
			//etResponse.setText(result);
		}
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		 ((EditText) findViewById(R.id.editDate)).setText( dayOfMonth + "/" + monthOfYear + "/" + year);
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		//messy, change later
		Log.e("Edit",((EditText) findViewById(R.id.editStartTime)).getText().toString());
		
//		if (((EditText) findViewById(R.id.editStartTime)).getText().toString() != ""){
			((EditText) findViewById(R.id.editStartTime)).setText( hourOfDay + ":" + minute);
//		} else{
//			((EditText) findViewById(R.id.editStartTime)).setText( hourOfDay + ":" + minute);
//		}
		
		
	}

	
//	int TIME_PICKER_START = 0;
//	int TIME_PICKER_END = 1;
//	
//	@Override
//	protected Dialog onCreateDialog(int id) {
//
//	    switch(id){
//	        case TIME_PICKER_START:
//	                return new TimePickerDialog(this, start_timeListener, start_hour, start_minute, true); 
//	            case TIME_PICKER_END:
//	                return new TimePickerDialog(this, end_timeListener, end_hour, end_minute, true);
//	    }
//	        return null;
//	}
	
}	
		
	
//	public void postData(String room){
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost("http://localhost:8080/rooms/" + room + "/booking");
//		
//		try{
//
//			View currentView = findViewById(android.R.id.content);
//			////					View currentView = view.
//			////				     macTextView = (TextView) view.findViewWithTag("mac");
//
//			EditText date = (EditText) currentView.findViewWithTag("edtDate");
//			EditText meetingName = (EditText) currentView.findViewWithTag("edtMeetingName");
//			EditText startT = (EditText) currentView.findViewWithTag("edtStartT");
//			EditText endT = (EditText) currentView.findViewWithTag("edtEndT");
//			EditText users = (EditText) currentView.findViewWithTag("edtUsers");
//			Log.e("POST", date.getText().toString());
//			Log.e("POST", startT.getText().toString());
//			Log.e("POST", endT.getText().toString());
//			Log.e("POST", users.getText().toString());
//			Log.e("POST", meetingName.getText().toString());
//
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//			nameValuePairs.add(new BasicNameValuePair("URI","testURI"));
//			nameValuePairs.add(new BasicNameValuePair("date",date.getText().toString()));
//
//			nameValuePairs.add(new BasicNameValuePair("StartT", startT.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("EndT", endT.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("MeetingName",meetingName.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("Users", users.getText().toString()));
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpClient.execute(httppost);
//			
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//
//		}		
//
//		return;
//
//	}
//	}


//	private static null Post(String url){
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost("http://localhost:8080/rooms/" + url + "/booking");
//		//				
//		try{
//
//			View currentView = findViewById(android.R.id.content);
//			////					View currentView = view.
//			////				     macTextView = (TextView) view.findViewWithTag("mac");
//
//			EditText date = (EditText) currentView.findViewWithTag("edtDate");
//			EditText meetingName = (EditText) currentView.findViewWithTag("edtMeetingName");
//			EditText startT = (EditText) currentView.findViewWithTag("edtStartT");
//			EditText endT = (EditText) currentView.findViewWithTag("edtEndT");
//			EditText users = (EditText) currentView.findViewWithTag("edtUsers");
//			Log.e("POST", date.getText().toString());
//			Log.e("POST", startT.getText().toString());
//			Log.e("POST", endT.getText().toString());
//			Log.e("POST", users.getText().toString());
//			Log.e("POST", meetingName.getText().toString());
//
//			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//			nameValuePairs.add(new BasicNameValuePair("URI","testURI"));
//			nameValuePairs.add(new BasicNameValuePair("date",date.getText().toString()));
//
//			nameValuePairs.add(new BasicNameValuePair("StartT", startT.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("EndT", endT.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("MeetingName",meetingName.getText().toString()));
//			nameValuePairs.add(new BasicNameValuePair("Users", users.getText().toString()));
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			HttpResponse response = httpClient.execute(new HttpPost(url));
//
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//
//		}		

//		return null;
//
//	}

//}

//public class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
//	protected String doInBackground(String... urls) {
//
//		return Post(urls[0]);
//	}

//class HttpAsyncTask extends AsyncTask<String,Void, String> {
//	protected String doInBackground(String... urls) {
//		return Post(urls[0]);
//
//	}


//private String Post1(String string) {
//		// TODO Auto-generated method stub
//		return null;
//	}


//void btnBook(String room, View view){
//
//	new HttpAsyncTask().execute("http://localhost:8080/rooms"+ room + "/booking");	
//}
	


