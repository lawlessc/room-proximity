package com.estimote.examples.demos;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.examples.demos.BookingActivity.HttpAsyncTask;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Displays basic information about beacon.
 *
 * @author wiktor@estimote.com (Wiktor Gworek)
 */
public class LeDeviceListAdapter extends BaseAdapter {

  private ArrayList<Beacon> beacons;
  private LayoutInflater inflater;
  
  int major = 0;
  int minor = 0;
  

  public LeDeviceListAdapter(Context context) {
    this.inflater = LayoutInflater.from(context);
    this.beacons = new ArrayList<Beacon>();
  }

  public void replaceWith(Collection<Beacon> newBeacons) {
    this.beacons.clear();
    this.beacons.addAll(newBeacons);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return beacons.size();
  }

  @Override
  public Beacon getItem(int position) {
    return beacons.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View view, ViewGroup parent) {
    view = inflateIfRequired(view, position, parent);
    bind(getItem(position), view);
    return view;
  }

  private void bind(Beacon beacon, View view) {
    ViewHolder holder = (ViewHolder) view.getTag();
    holder.macTextView.setText(String.format("MAC: %s (%.2fm)", beacon.getMacAddress(), Utils.computeAccuracy(beacon)));
    holder.majorTextView.setText("Major: " + beacon.getMajor());
    holder.minorTextView.setText("Minor: " + beacon.getMinor());
    holder.measuredPowerTextView.setText("MPower: " + beacon.getMeasuredPower());
    holder.rssiTextView.setText("RSSI: " + beacon.getRssi());
    holder.uuidTextView.setText("UUID: " + beacon.getProximityUUID());
    
    major = beacon.getMajor();
    minor = beacon.getMinor();
    String colour ="";
//    //System.out.println("about to try");
//    
//    try {
//		String beaconData = new HttpAsyncTask().execute("http://localhost:8888/" + major + "/" + minor).get();
//		JSONObject json = new JSONObject(beaconData);
//		colour =  json.getString("colour");
//	
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (ExecutionException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//    
//    
//    //System.out.println("COLOUR IS ---------------"+colour+"-----------------------");
//    //this shouldn't be hardcoded, we need to get this from the server!
    
    if(major == 24723){
    	view.setBackgroundColor(Color.rgb(173, 216, 230));
    }
    else if(major == 55149){
    	view.setBackgroundColor(Color.rgb(60, 179, 113));
    }
    else if(major == 666){
    	view.setBackgroundColor(Color.rgb(0, 0, 128));
    }
    else{
    	//Log.e("FAIL", col);
    	view.setBackgroundColor(Color.rgb(255, 0, 0));
    }
    
    
//    if(colour.equalsIgnoreCase("blue")){
//    	view.setBackgroundColor(Color.rgb(173, 216, 230));
//    }
//    else if(colour.equalsIgnoreCase("green")){
//    	view.setBackgroundColor(Color.rgb(60, 179, 113));
//    }
//    else if(colour.equalsIgnoreCase("navy")){
//    	view.setBackgroundColor(Color.rgb(0, 0, 128));
//    }
//    else{
//    	//Log.e("FAIL", col);
//    	view.setBackgroundColor(Color.rgb(255, 0, 0));
//    }
  }

  private View inflateIfRequired(View view, int position, ViewGroup parent) {
    if (view == null) {
      view = inflater.inflate(R.layout.device_item, null);
      view.setTag(new ViewHolder(view));
    }
    return view;
  }

  static class ViewHolder {
    final TextView macTextView;
    final TextView majorTextView;
    final TextView minorTextView;
    final TextView measuredPowerTextView;
    final TextView rssiTextView;
    final TextView uuidTextView;

    ViewHolder(View view) {
      macTextView = (TextView) view.findViewWithTag("mac");
      majorTextView = (TextView) view.findViewWithTag("major");
      minorTextView = (TextView) view.findViewWithTag("minor");
      measuredPowerTextView = (TextView) view.findViewWithTag("mpower");
      rssiTextView = (TextView) view.findViewWithTag("rssi");
      uuidTextView= (TextView) view.findViewWithTag("uuid");
    }
    

  }

	public class HttpAsyncTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {

			return GET(urls[0], major, minor);
		}
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			if(result != null){
			//	Toast.makeText(getBaseContext(), "Connected!", Toast.LENGTH_LONG).show();
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
