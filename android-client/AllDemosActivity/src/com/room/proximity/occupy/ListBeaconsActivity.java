package com.room.proximity.occupy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.utils.L;
import com.room.proximity.occupy.R.color;

/**
 * Based on work of wiktorgworek@google.com (Wiktor Gworek)
 */
public class ListBeaconsActivity extends Activity {

    private static final String TAG = ListBeaconsActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";

    private static final int REQUEST_ENABLE_BT = 1234;
    private static final String ESTIMOTE_BEACON_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    private static final String ESTIMOTE_IOS_PROXIMITY_UUID = "8492E75F-4FD6-469D-B132-043FE94921D8";
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);


    private BeaconManager beaconManager;
    private LeDeviceListAdapter adapter;
    private int scanningTime =0;

    Beacon jupiter_test = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "Jupiter", "FE:85:EE:7F:E7:6B", 111, 49567, 5, 45);
    Beacon saturn_test = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "Saturn", "FE:85:EE:7F:E7:6B", 111, 49567, 5, 45);
    Beacon neptune_test = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "Neptune", "FE:85:EE:7F:E7:6B", 111, 49567, 5, 45);
    
    Beacon jupiter = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "test_beacon", "FE:85:EE:7F:E7:6B", 666, 49567, 5, 45);
    Beacon saturn = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "test_beacon", "FE:85:EE:7F:E7:6B", 55149, 5016, 5, 45);
    Beacon neptune = new Beacon("B9407F30-F5F8-466E-AFF9-25556B57FE6D", "test_beacon", "FE:85:EE:7F:E7:6B", 24723, 63838, 5, 45);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        final View pb = findViewById(R.id.Progress);
        final TextView dummyInfo = (TextView)findViewById(R.id.dummyModeInfo);
        final Button btnScan = (Button)findViewById(R.id.btnScan);
        
        // Configure device list.
        adapter = new LeDeviceListAdapter(this);
        ListView list = (ListView) findViewById(R.id.device_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(createOnItemClickListener());

        // Configure verbose debug logging.
        L.enableDebugLogging(false);

        // Configure BeaconManager.
        beaconManager = new BeaconManager(this);
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
            beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
            beaconManager.setBackgroundScanPeriod(5000, 30000);
            beaconManager.setForegroundScanPeriod(7000, 5000);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        btnScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                scan(null);
            }
        }); 

        final ToggleButton toggleDummy = (ToggleButton) findViewById(R.id.DummyMode);
        toggleDummy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggleDummy.setBackgroundColor(color.red);
                    dummyBeacon();
                    pb.setVisibility(View.GONE);
                    dummyInfo.setVisibility(View.VISIBLE);
                    btnScan.setEnabled(false);
                    try {
                        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
                        beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);  
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    
                } else {
                    // The toggle is disabled
                    toggleDummy.setBackgroundDrawable(null);
                    dummyInfo.setVisibility(View.GONE);
                    btnScan.setEnabled(true);
                    adapter.replaceWith(Collections.<Beacon>emptyList());
//                    scan(null);
                }
            }
        });

        final ToggleButton toggleTest = (ToggleButton) findViewById(R.id.TestMode);
        toggleTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    toggleTest.setBackgroundColor(color.red);
                    testBeacon();
                    pb.setVisibility(View.GONE);
                    dummyInfo.setVisibility(View.VISIBLE);
                    btnScan.setEnabled(false);
                    try {
                        beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
                        beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);  
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    
                } else {
                    // The toggle is disabled
                    toggleTest.setBackgroundDrawable(null);
                    dummyInfo.setVisibility(View.GONE);
                    btnScan.setEnabled(true);
                    adapter.replaceWith(Collections.<Beacon>emptyList());
//                    scan(null);
                }
            }
        });
        
        //scan the first time the app is opened
        scan(null);
    }
    
    public void dummyBeacon(){ 
        List<Beacon> dummyBeacon = new ArrayList<Beacon>(2);
        dummyBeacon.add(jupiter);
        dummyBeacon.add(saturn);
        dummyBeacon.add(neptune);
        adapter.replaceWith(dummyBeacon);     
    }  
    
    public void testBeacon(){
        List<Beacon> dummyBeacon = new ArrayList<Beacon>(2);
        dummyBeacon.add(jupiter_test);
        dummyBeacon.add(saturn_test);
        dummyBeacon.add(neptune_test);
        adapter.replaceWith(dummyBeacon);     
    }
        
    public void scan(View view){   
        final LinearLayout pb = (LinearLayout)findViewById(R.id.Progress);
        final View Scan = (View)findViewById(R.id.btnScan);
        pb.setVisibility(View.VISIBLE);
        scanningTime=0; 
        connectToService();
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {  
                // Note that results are not delivered on UI thread.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scanningTime +=1;
                        List<Beacon> estimoteBeacons = filterBeacons(beacons);
                        adapter.replaceWith(estimoteBeacons);
                        pb.setVisibility(View.GONE);  
                        Scan.setVisibility(View.VISIBLE);
                        if(scanningTime>0) {
                            try {
                                beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
                                beaconManager.stopMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
                            } catch (RemoteException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }                      
                    }
                });
            }  
        });  
    }

    private List<Beacon> filterBeacons(List<Beacon> beacons) {
        List<Beacon> filteredBeacons = new ArrayList<Beacon>(beacons.size());
        for (Beacon beacon : beacons) {
            if (beacon.getProximityUUID().equalsIgnoreCase(ESTIMOTE_BEACON_PROXIMITY_UUID)
                    || beacon.getProximityUUID().equalsIgnoreCase(ESTIMOTE_IOS_PROXIMITY_UUID)) {
                filteredBeacons.add(beacon);
                
            }
        }
        return filteredBeacons;
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.scan_menu, menu);
    ////        MenuItem refreshItem = menu.findItem(R.id.refresh);
    //        ///refreshItem = menu.findItem(R.id.);
    ////        refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
    //        return super.onCreateOptionsMenu(menu);
    //    }

    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        
    //        switch(item.getItemId()){
    //        case R.id.action_search:
    //            menuItem = item;
    //            menuItem.setActionView(R.layout.actionbar_indeterminate_progress);
    //            menuItem.expandActionView();
    ////            TestTask task = new TestTask();
    ////            task.execute("test");
    //            break;
    //        default:
    //            break;
    //        }
    //        return true;

    //        if (item.getItemId() == android.R.id.home) {
    //            finish();
    //            return true;
    //        }
    //
    //        return super.onOptionsItemSelected(item);
    //    }

    //    private class TestTask extends AsyncTask<String, Void, String> {
    //
    //        @Override
    //        protected String doInBackground(String... params) {
    //          // Simulate something long running
    //          try {
    //            Thread.sleep(2000);
    //          } catch (InterruptedException e) {
    //            e.printStackTrace();
    //          }
    //          return null;
    //        }
    //
    //        @Override
    //        protected void onPostExecute(String result) {
    //          menuItem.collapseActionView();
    //          menuItem.setActionView(null);
    //        }
    //      };
    //    
    @Override
    protected void onDestroy() {
        beaconManager.disconnect();

        super.onDestroy();
    }
    
    @Override
    protected void onPause(){
        super.onPause();
//        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            return;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {

        }
    }

    @Override
    protected void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        super.onStop();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                connectToService();
            } else {
                Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG).show();
                getActionBar().setSubtitle("Bluetooth not enabled");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void connectToService() {
        // getActionBar().setSubtitle("Scanning...");
        adapter.replaceWith(Collections.<Beacon>emptyList());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                    Log.e("ranging", "started searching");
                    
                } catch (RemoteException e) {
                    Toast.makeText(ListBeaconsActivity.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListBeaconsActivity.this, BookingActivity.class);
                intent.putExtra(EXTRAS_BEACON, adapter.getItem(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.e("Activity","StartActivtiy - ListBeacons");
                startActivity(intent);
            }
        };
    }
}
