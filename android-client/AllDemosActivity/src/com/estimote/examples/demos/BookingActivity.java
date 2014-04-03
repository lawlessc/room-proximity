package com.estimote.examples.demos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;

public class BookingActivity extends Activity {
	
	private static final String TAG = BookingActivity.class.getSimpleName();
	//private Beacon beacon;
	private int major;
	private int minor;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
		
//		major = (TextView) findViewById(R.id.majorBk);
//		minor = (TextView) findViewById(R.id.minorBk);
		
//		major = beacon.getMajor();
//		minor = beacon.getMinor();

		//Connect(major, minor);
		//JSONResponse(response);
		
		//new HttpAsyncTask().execute("http://github.com");
		major = beacon.getMajor();
		minor = beacon.getMinor();
		
		new HttpAsyncTask().execute("http://127.0.0.1:8080/" + major + "/" + minor);
		Log.d("beacons","connected");
	}

	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {

			return GET(urls[0], major, minor);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();
			
			if (result == null) {
				Log.d("beacons", "No the result was null");
			}
			else{
				Log.d("beacons", result);
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

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				Log.d("beacons", "Did not work!");
			
			 
			
			JSONObject json = new JSONObject(result);
			
			JSONArray room = json.toJSONArray(json.names());
			

			
			Iterator<String> keys = json.keys();
			while (keys.hasNext()){
				
				JSONObject value = json.getJSONObject(keys.next());
				String rName = value.getString("name");				
				Log.e("JSON","Room Name: "+ rName);

			    String capactity = json.getString("capacity");
			    Log.e("JSON","Capacity : "+ capactity);
			}
//			JSONObject jsonRoom = json.getJSONArray(0).optJSONObject(0);
			
			
			
			Log.d("JSON", room.get(1).toString());
//			String str_value=jsonRoom.getString("Room");

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}
		Log.d("beacons", result);
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


//private BeaconConnection.ConnectionCallback createConnectionCallback() {
//    return new BeaconConnection.ConnectionCallback() {
//      @Override public void onAuthenticated(final BeaconConnection.BeaconCharacteristics beaconChars) {
//        runOnUiThread(new Runnable() {
//          @Override public void run() {
//            //statusView.setText("Status: Connected to beacon");
//            StringBuilder sb = new StringBuilder()
//                //.append("Major: ").append(beacon.getMajor()).append("\n")
//                //.append("Minor: ").append(beacon.getMinor()).append("\n")
//                .append("Advertising interval: ").append(beaconChars.getAdvertisingIntervalMillis()).append("ms\n")
//                .append("Broadcasting power: ").append(beaconChars.getBroadcastingPower()).append(" dBm\n")
//                .append("Battery: ").append(beaconChars.getBatteryPercent()).append(" %");
////            beaconDetailsView.setText(sb.toString());
////            minorEditView.setText(String.valueOf(beacon.getMinor()));
////            afterConnectedView.setVisibility(View.VISIBLE);
//          }
//        });
//      }
//
//      @Override public void onAuthenticationError() {
//        runOnUiThread(new Runnable() {
//          @Override public void run() {
//           //.setText("Status: Cannot connect to beacon. Authentication problems.");
//          }
//        });
//      }
//
//      @Override public void onDisconnected() {
//        runOnUiThread(new Runnable() {
//          @Override public void run() {
//           // statusView.setText("Status: Disconnected from beacon");
//          }
//        });
//      }
//    };
//  }
}
//	protected HttpResponse Connect(int major, int minor){




//	public static String convertStreamToString(InputStream inputStream) throws IOException {
//        if (inputStream != null) {
//            Writer writer = new StringWriter();
//
//            char[] buffer = new char[1024];
//            try {
//                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
//                int n;
//                while ((n = reader.read(buffer)) != -1) {
//                    writer.write(buffer, 0, n);
//                }
//            } finally {
//                inputStream.close();
//            }
//            return writer.toString();
//        } else {
//            return "";
//        }
//    }


//	protected void getDetails(){
//		final TextView majorTextView;
//	    final TextView minorTextView;
//	    ViewHolder(View view) {
//	        majorTextView = (TextView) view.findViewWithTag("major");
//	        minorTextView = (TextView) view.findViewWithTag("minor");
//	    }
//	}

//}
