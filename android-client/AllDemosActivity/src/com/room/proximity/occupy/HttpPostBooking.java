package com.room.proximity.occupy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class HttpPostBooking {
    int result = -1;
    
    protected HttpPostBooking(String uri, String date, String startTime,String Endtime, String meetingName, String user ){
        
    }
    
    protected HttpPostBooking(){
        
    }
    
    public void setMeetingName(String name){
        String meetingName = name;
    }
    
    class myAsyncHttpPost extends AsyncTask<String, String, String>{    
        
        protected void onPreExecute() {
            result = 0;
        }
        
        protected String doInBackground(String...params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try{            
                SimpleDateFormat today = new SimpleDateFormat("dd" +"/" +"MM" + "/" +"yy",Locale.UK);
                SimpleDateFormat time = new SimpleDateFormat("HH" +":" +"mm",Locale.UK);
                Date now = new Date();
                long endtime = (now.getTime() + 60 * 60 *1000);

                String currentDate = today.format(new Date());
                String currentTime = time.format(new Date());
                String endTime = time.format(endtime);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("uri",""));
                nameValuePairs.add(new BasicNameValuePair("date",currentDate));
                nameValuePairs.add(new BasicNameValuePair("meetingName", "test"));
                nameValuePairs.add(new BasicNameValuePair("startTime", currentTime));
                nameValuePairs.add(new BasicNameValuePair("endTime", endTime));
                nameValuePairs.add(new BasicNameValuePair("users", "testUser"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

//                HttpResponse response = httpClient.execute(httppost);
                httpClient.execute(httppost);

                result = 1;
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
        
        protected void onProgressUpdate(Integer... progress) {
            // called when the background task has made any progress
        }

        protected void onPostExecute(int result) {
            if(result == 1){
//                Toast.makeText(getApplicationContext(), "Booking Made!", Toast.LENGTH_SHORT).show();
            }else if(result == 0){
//                Toast.makeText(getBaseContext(), "Booking Error!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void execute(String string) {
        // TODO Auto-generated method stub
        
    }
}
