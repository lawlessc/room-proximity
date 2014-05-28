package com.room.proximity.occupy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

//public class myAsyncTaskPost extends AsyncTask<String, Void, Void>{
public class myAsyncTaskPost extends AsyncTask<String, Void, Void>{

    private Context mContext;
    private Boolean finished = false;

    public void setContext(Context context){
        this.mContext = context;
    }

    @Override
    protected Void doInBackground(String... params) {   
        String uri = params[0];
        String setDate = params[1];
        String mName = params[2];
        String startTime = params[3];      
        String endTime = params[4];  
        String user = params[5];

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(uri);
        
        try{            
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("uri", uri));
            nameValuePairs.add(new BasicNameValuePair("date", setDate));
            nameValuePairs.add(new BasicNameValuePair("meetingName", mName));
            nameValuePairs.add(new BasicNameValuePair("startTime", startTime));
            nameValuePairs.add(new BasicNameValuePair("endTime", endTime));
            nameValuePairs.add(new BasicNameValuePair("users", user));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // HttpResponse response = httpClient.execute(httppost);
            httpClient.execute(httppost);
            finished = true;

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

    protected void onPostExecute(Boolean finished) {
        if(finished){
            Toast.makeText(mContext, R.string.booking_made, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, R.string.booking_error, Toast.LENGTH_SHORT).show();
        }
    }

}
