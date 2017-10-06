package com.techanalogy.hopein.hopein;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shivraj
 */
public class FoundBTDevices extends ListActivity
{
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> arrayOfFoundBTDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        displayListOfFoundDevices();
    }

private void displayListOfFoundDevices()
{
    arrayOfFoundBTDevices = new ArrayList<BluetoothObject>();

    // start looking for bluetooth devices
    mBluetoothAdapter.startDiscovery();

    // Discover new devices
    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // Get the bluetoothDevice object from the Intent
                Log.d("bhelte","chlra");
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Get the "RSSI" to get the signal strength as integer,
                // but should be displayed in "dBm" units
                final int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);

                // Create the device object and add it to the arrayList of devices
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());  // this = context
                StringRequest postRequest = new StringRequest(Request.Method.POST,"https://hoppin.000webhostapp.com/check.php",
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("yabla",error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("name",device.getName());
                        params.put("mac",device.getAddress());
                        params.put("rssi",""+rssi);
                        Log.d("jarha","ha");
                        return params;
                    }
                };
                queue.add(postRequest);
                BluetoothObject bluetoothObject = new BluetoothObject();
                bluetoothObject.setBluetooth_name(device.getName());
                Log.d("device","Name :"+device.getName()+" Address :"+device.getAddress());
                bluetoothObject.setBluetooth_address(device.getAddress());
                bluetoothObject.setBluetooth_state(device.getBondState());
                bluetoothObject.setBluetooth_type(device.getType());    // requires API 18 or higher
                bluetoothObject.setBluetooth_uuids(device.getUuids());
                bluetoothObject.setBluetooth_rssi(rssi);

                arrayOfFoundBTDevices.add(bluetoothObject);

                // 1. Pass context and data to the custom adapter
                FoundBTDevicesAdapter adapter = new FoundBTDevicesAdapter(getApplicationContext(), arrayOfFoundBTDevices);

                // 2. setListAdapter
                setListAdapter(adapter);


            }
        }
    };
    // Register the BroadcastReceiver
    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(mReceiver, filter);
}

    @Override
    protected void onPause()
    {
        super.onPause();

        mBluetoothAdapter.cancelDiscovery();
    }
}

