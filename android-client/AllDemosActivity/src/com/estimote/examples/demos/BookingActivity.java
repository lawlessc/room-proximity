package com.estimote.examples.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.estimote.examples.demos.PostBookingActivity.AsyncHttpPost;
import com.estimote.sdk.Beacon;

public class BookingActivity extends Activity  implements OnDateSetListener, OnTimeSetListener{
	
	private static final String TAG = BookingActivity.class.getSimpleName();
	private int major;
	private int minor;
	String room = null;
	String testRoom = null;
	JSONObject jBookings = null;
	JSONObject json = null;
	JSONObject testJson = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_ui);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);

		major = beacon.getMajor();
		minor = beacon.getMinor();

		ArrayAdapter<JSONObject> adapter;
		ArrayList<JSONObject> listItems = new ArrayList<JSONObject>();

		try {
			testJson = makeJson();
			testRoom = testJson.getString("name");
			
			TextView name = new TextView(this);
			TextView location = new TextView(this);
			TextView capacity = new TextView(this);
			TextView phone = new TextView(this);
			TextView owner = new TextView(this);
			TextView conferencing = new TextView(this);
			
			name = (TextView)findViewById(R.id.txtName);		
			location = (TextView)findViewById(R.id.txtLocation);
			capacity = (TextView)findViewById(R.id.txtCapacity);
			phone = (TextView)findViewById(R.id.txtPhone);	
			owner = (TextView)findViewById(R.id.txtOwner);			
			conferencing = (TextView)findViewById(R.id.txtConferencing);	
			
			name.setText(testJson.getString("name"));
			location.setText(testJson.getString("location"));
			capacity.setText(testJson.getString("capacity"));
			phone.setText(testJson.getString("phone"));
			owner.setText(testJson.getString("owner"));
			conferencing.setText(testJson.getString("conferencing"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//			room = new HttpAsyncTask().execute("http://localhost:8080/" + major + "/" + minor).get();
//			json = new JSONObject(room);
//			jBookings = json.getJSONObject("bookingsDetails");
//			JSONArray bookingArray = jBookings.getJSONArray("bookings");
//			Log.e("JSON", bookingArray.toString());
//
//			for(int i = 0 ; i < bookingArray.length() ; i++ ){		
//				listItems.add(bookingArray.getJSONObject(i));
//			}

//			adapter=new ArrayAdapter<JSONObject>(this, android.R.layout.simple_list_item_1,listItems);
//			setListAdapter(adapter);
		
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
			
			Button book = (Button) findViewById(R.id.btnBook);
			book.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AsyncHttpPost asyncHttpPost = new AsyncHttpPost();
					asyncHttpPost.execute("http://localhost:8080/rooms/" + testRoom + "/booking");
				}
			});
			
			EditText mEditDate = (EditText) findViewById(R.id.editBDate);
			mEditDate.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	showDatePickerDialog(v);
		        }

		    });
			
			EditText mEditStartT = (EditText) findViewById(R.id.editBStartT);
			mEditStartT.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		        	showTimePickerDialog(v);
		        }

		    });
			
			EditText mEditEndT = (EditText) findViewById(R.id.editBEndT);
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

	
	JSONObject makeJson(){
		
		JSONObject room1 = new JSONObject();
		
		try {
			room1.put("name", "solas");
			room1.put("location", "1st floor");
			room1.put("capacity", "10");
			room1.put("phone", "087123456");
			room1.put("owner", "CATE");
			room1.put("conferencing", "Yes");
			room1.put("uri", "/rooms/solas");
			room1.put("beacon", "/beacons/beacon1");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return room1;
		
	}
		
	public void makeBooking(View view) throws JSONException{		
		Intent i = new Intent(getApplicationContext(), PostBookingActivity.class);
		Log.e("Context",getApplicationContext().toString());
//		String rName = jBookings.getString("roomname").toString();
		String rName = "solas";
		Log.e("JSON", rName);
		i.putExtra("roomname", rName );
		startActivity(i);

	}

	public class HttpAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {

			return GET(urls[0], major, minor);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
				Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();
			}
		
			if (result == null) {
				Log.d("beacons", "No the result was null");
			}
			else{
//				Log.d("beacons", result);
			}
			
			//etResponse.setText(result);
		}
	}

	public static String GET(String url, int major, int minor){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			result = convertInputStreamToString(inputStream);

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		Log.d("beacons","connected");
		Log.d("beacons",result);
		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;
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
		 ((EditText) findViewById(R.id.editBDate)).setText( dayOfMonth + "/" + monthOfYear + "/" + year);
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Log.e("Edit",((EditText) findViewById(R.id.editBStartT)).getText().toString());
		((EditText) findViewById(R.id.editBStartT)).setText( hourOfDay + ":" + minute);

	}

}
