package com.estimote.examples.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import android.text.format.DateFormat;
import android.text.format.Time;
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


import com.estimote.sdk.Beacon;

public class BookingActivity extends Activity{
	
	private static final String TAG = BookingActivity.class.getSimpleName();
	private int major;
	private int minor;
	String room = null;
	JSONObject jBookings = null;
	JSONObject jRoom = null;
	JSONObject json = null;
	JSONArray bookingArray = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_info);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
		major = beacon.getMajor();
		minor = beacon.getMinor();
		
//		ArrayAdapter<JSONObject> adapter;
//		ArrayList<JSONObject> listItems = new ArrayList<JSONObject>();

		try {
			room = new HttpAsyncTask().execute("http://localhost:8888/" + major + "/" + minor + "/redirect").get();
			json = new JSONObject(room);
			jRoom = json.getJSONObject("roomDetails");
			jBookings = json.getJSONObject("bookings");
			bookingArray = jBookings.getJSONArray("bookings");
			Log.e("JSON", bookingArray.toString());

//			for(int i = 0 ; i < bookingArray.length() ; i++ ){		
//				listItems.add(bookingArray.getJSONObject(i));
//			}
//			adapter=new ArrayAdapter<JSONObject>(this, android.R.layout.simple_list_item_1,listItems);
//			setListAdapter(adapter);

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

			name.setText(jRoom.optString("name"));
			location.setText(jRoom.optString("location"));
			capacity.setText(jRoom.optString("capacity"));
			phone.setText(jRoom.optString("phone"));
			owner.setText(jRoom.optString("owner"));
			conferencing.setText(jRoom.optString("conferencing"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Button book = (Button) findViewById(R.id.btnBook);
		book.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					makeBooking(v);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			}
		});
		
		String[] nextMeetingTime = null;
		
		for(int i = 0; i<bookingArray.length();i++){
			try {
				//Log.e("ARRAY",bookingArray.getJSONObject(i).toString());
				//nextMeetingTime[i] = bookingArray.getJSONObject(i).get("StartT").toString();
				Log.e("ARRAY",bookingArray.getJSONObject(i).get("StartT").toString());
				//Log.e("ARRAY",nextMeetingTime[i]);
				//nextMeetingTime[i] = bookingArray.getJSONObject(i).optString("startT").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
//		for(int i = 0; i<nextMeetingTime.length;i++){
//			
//		};
	
	}
		
	public void makeBooking(View view) throws JSONException{		
		Intent i = new Intent(getApplicationContext(), PostBookingActivity.class);
		Log.e("Context",getApplicationContext().toString());
		String rName = jBookings.getString("roomname").toString();
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
	
}
