package com.room.proximity.occupy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.sdk.Beacon;

/**
 * Based on work from wiktor@estimote.com (Wiktor Gworek)
 */
public class LeDeviceListAdapter extends BaseAdapter {

    private ArrayList<Beacon> beacons;
    private LayoutInflater inflater;

    int major = -1;
    int minor = -1;
    String name = null;


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

        major = beacon.getMajor();
        minor = beacon.getMinor();
        name = beacon.getName();

        String colour = null; 
        String room = null;
        String availability= null;

        
        
        if(major == 111){
            holder.roomNameView.setText(name); 
            holder.roomNameView.setTextColor(Color.WHITE);
            if (name == "Jupiter"){
                view.setBackgroundColor(Color.rgb(91,192,222));
                holder.availabilityImage.setImageResource(R.drawable.green_dot);      
            }else if(name == "Saturn"){
                view.setBackgroundColor(Color.rgb(169,219,169));
                holder.availabilityImage.setImageResource(R.drawable.green_dot);      
            }else{
                view.setBackgroundColor(Color.rgb(45,37,86));
                holder.availabilityImage.setImageResource(R.drawable.red_dot);    
            }

        }else if (major != 111){
            
            try {
                String beaconData = new HttpAsyncTask().execute("http://localhost:8888/" + major + "/" + minor).get();
                JSONObject json = new JSONObject(beaconData);

                colour = json.getJSONObject("beacon").getString("colour");
                room =  json.getJSONObject("beacon").getString("room");
                availability =   json.getString("available");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            holder.roomNameView.setText(room); 
            holder.roomNameView.setTextColor(Color.WHITE);
            
            //TODO change beacon json to store RGB values rather than string
            //will allow only one call to set the ListView colour
            if(colour.equalsIgnoreCase("blue")){
                view.setBackgroundColor(Color.rgb(91,192,222));
            }else if(colour.equalsIgnoreCase("green")){
                view.setBackgroundColor(Color.rgb(169,219,169));
            }else if(colour.equalsIgnoreCase("navy")){
                view.setBackgroundColor(Color.rgb(45,37,86));
            }

            if(availability.equalsIgnoreCase("occupied")){
                holder.availabilityImage.setImageResource(R.drawable.red_dot);
            } else {        
                holder.availabilityImage.setImageResource(R.drawable.green_dot);
            } 

            
        }
        
        
    }

    private View inflateIfRequired(View view, int position, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.device_item, null);
            view.setTag(new ViewHolder(view));
        }
        return view;
    }

    static class ViewHolder {
        final TextView roomNameView;
        final ImageView availabilityImage;

        ViewHolder(View view) {

            availabilityImage = (ImageView) view.findViewWithTag("availability");
            roomNameView= (TextView) view.findViewWithTag("room");
        }
    }

    public class HttpAsyncTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            return GET(urls[0], major, minor);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
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
