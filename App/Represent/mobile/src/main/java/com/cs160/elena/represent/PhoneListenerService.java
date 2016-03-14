package com.cs160.elena.represent;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    private static final String TOAST = "/send_toast";
    private static final String SENSOR = "/send_sensor";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in PhoneListenerService, got: " + messageEvent.getPath());
        if( messageEvent.getPath().equalsIgnoreCase(TOAST) ) {
            // Value contains the String we sent over in WatchToPhoneService, "good job"
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            // Make a toast with the String
            Context context = getApplicationContext();

            Intent intent = new Intent(context, DetailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle bundle = new Bundle();
            String[] data = value.split(";");
            bundle.putString("name", data[0]);
            bundle.putString("party", data[1]);
            bundle.putString("pic", data[2]);
            bundle.putString("bioguide_id", data[3]);
            bundle.putString("term_end", data[4]);

            intent.putExtra(MainActivity.EXTRA_MESSAGE, bundle);
            startActivity(intent);
        } else if(messageEvent.getPath().equalsIgnoreCase(SENSOR)) {
            //make random zipcode
            String zipcode = null;
            InputStream stream = null;
            try {
                stream = getAssets().open("valid-zips.json");
                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();

                String jsonString = new String(buffer, "UTF-8");
                HashMap<String, String> data = new HashMap<String, String>();
                try {
                    JSONArray zips = new JSONArray(jsonString);
                    int rand = (int) Math.random()*zips.length();
                    zipcode = zips.getString((int) Math.random()*zips.length());

                    String latlon = getLatlon(zipcode);

                    // Value contains the String we sent over in WatchToPhoneService, "good job"
                    String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);

                    // Make a toast with the String
                    Context context = getApplicationContext();

                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra(HomeActivity.EXTRA_MESSAGE, latlon);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            super.onMessageReceived( messageEvent );
        }
    }

    public String getLatlon(String zipcode){
        final Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(zipcode, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                // Use the address as needed
                return address.getLatitude() + "," + address.getLongitude();
            } else {
                // Display appropriate message when Geocoder services are not available
                Log.d("T", "Unable to get Latlon");
            }
        } catch (IOException e) {
            // handle exception
            e.printStackTrace();
        }
        return null;
    }
}
