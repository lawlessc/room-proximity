package com.room.proximity.occupy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;

public class BookingActivity extends Activity{

    private int major;
    private int minor;
    String room = null;
    String roomName = null;
    JSONObject jBookings = null;
    JSONObject jRoom = null;
    JSONObject json = null;
    String available = null;
    JSONArray bookingArray = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.occupy_now);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activitiy_close_scale);

        Beacon beacon = getIntent().getParcelableExtra(ListBeaconsActivity.EXTRAS_BEACON);
        major = beacon.getMajor();
        minor = beacon.getMinor();

        populate(major,minor);

        Button btnOccupyNow = (Button) findViewById(R.id.btnOccupyNow);    
        btnOccupyNow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                myAsyncHttpPost asyncHttpPost = new myAsyncHttpPost();
                asyncHttpPost.execute("http://localhost:8888/rooms/" + roomName + "/booking");
            }
        });	

        Button btnOccupyLater = (Button) findViewById(R.id.btnOccupyLater);    
        btnOccupyLater.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), PostBookingActivity.class);
                    String rName;
                    rName = jBookings.getString("roomname").toString();
                    i.putExtra("roomname", rName );
                    i.putExtra("bookings", bookingArray.toString());
                    startActivity(i);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }); 
    }

    protected void populate(int major, int minor){

        try {
            room = new HttpAsyncTask().execute("http://localhost:8888/" + major + "/" + minor + "/redirect").get();
            json = new JSONObject(room);
            available = json.get("available").toString();
            jRoom = json.getJSONObject("roomDetails");
            roomName = jRoom.getString("name").toString();
            jBookings = json.getJSONObject("bookings");
            bookingArray = jBookings.getJSONArray("bookings");

            TextView name = new TextView(this);
            TextView location = new TextView(this);
            TextView capacity = new TextView(this);
            TextView phone = new TextView(this);
            TextView owner = new TextView(this);
            TextView conferencing = new TextView(this);

            name = (TextView)findViewById(R.id.roomName);        
            location = (TextView)findViewById(R.id.Location);
            capacity = (TextView)findViewById(R.id.Capacity);
            phone = (TextView)findViewById(R.id.Phone);  
            owner = (TextView)findViewById(R.id.Owner);          
            conferencing = (TextView)findViewById(R.id.Webcam);

            name.setText(jRoom.optString("name"));
            location.setText(jRoom.optString("location"));
            capacity.setText("Fits " + jRoom.optString("capacity"));
            phone.setText("Phone: " + jRoom.optString("phone"));
            owner.setText("Owned by " + jRoom.optString("owner"));
            conferencing.setText("Webcam: " + jRoom.optString("conferencing"));

            if(available.equalsIgnoreCase("occupied")){
                View btnOccpied = findViewById(R.id.btnOccupyNow);
                Button btnOccLtr = (Button) findViewById(R.id.btnOccupyLater);
                btnOccpied.setVisibility(View.GONE);
                btnOccLtr.setTextSize(40);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void occupyNow (MenuItem item){
        myAsyncHttpPost asyncHttpPost = new myAsyncHttpPost();
        asyncHttpPost.execute("http://localhost:8888/rooms/" + roomName + "/booking");
    }

    @Override
    protected void onPause(){
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activitiy_close_translate);
    }

    class myAsyncHttpPost extends AsyncTask<String, String, String>{
        String result = "0";

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try{			
                SimpleDateFormat today = new SimpleDateFormat("dd" +"/" +"MM" + "/" +"yy");
                SimpleDateFormat time = new SimpleDateFormat("HH" +":" +"mm");
                Date now = new Date();
                long endtime = (now.getTime() + 60 * 60 *1000);

                String currentDate = today.format(new Date());
                String currentTime = time.format(new Date());
                String endTime = time.format(endtime);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("uri",""));
                nameValuePairs.add(new BasicNameValuePair("date",currentDate));
                nameValuePairs.add(new BasicNameValuePair("meetingName", "testUser's Meeting"));
                nameValuePairs.add(new BasicNameValuePair("startTime", currentTime));
                nameValuePairs.add(new BasicNameValuePair("endTime", endTime));
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
            if(result == "1"){
                Toast.makeText(getBaseContext(), "Booking Made!", Toast.LENGTH_SHORT).show();
            }else if(result == "0"){
                Toast.makeText(getBaseContext(), "Booking Error!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void occupyLater() throws JSONException{     
        Intent i = new Intent(getApplicationContext(), PostBookingActivity.class);
        String rName = jBookings.getString("roomname").toString();
        i.putExtra("roomname", rName );
        i.putExtra("bookings", bookingArray.toString());
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

            }

            if (result == null) {
                Log.d("beacons", "No the result was null");
            }
            else{
                //Log.d("beacons", result);
            }
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

