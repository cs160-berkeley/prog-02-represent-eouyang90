package com.cs160.elena.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.cs160.represent.MainActivity.RepInfo";
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    private Button mupdateButton1;
    private Button mupdateButton2;
    private Button mupdateButton3;

    private ArrayList<HashMap<String, String>> mrepData;
    private HashMap<String,String> mvoteData;

    //Temporary data for demo purposes
    //TODO; replace with API zipcodes
    private String[] zipcodes = {"94709","94704","92009"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String zipcode = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);
        if (zipcode.equals(HomeActivity.CUR_LOC)){
            int index = (int) Math.random()*zipcodes.length;
            zipcode = zipcodes[index];
        }

        mrepData = representData(zipcode);
        mvoteData = voteData(zipcode);
        updateViewData(mrepData);
        //updateWatchData(repData.get(0), this);
        updateWatchData2(mrepData, mvoteData, this);

        mupdateButton1 = (Button) findViewById(R.id.rep1_btn);
        mupdateButton2 = (Button) findViewById(R.id.rep2_btn);
        mupdateButton3 = (Button) findViewById(R.id.rep3_btn);

        mupdateButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.rep1_name);
                String name = text.getText().toString();
                Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                startActivity(intent);

                //updateWatchData(repData.get(0), getBaseContext());
            }
        });
        mupdateButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.rep2_name);
                String name = text.getText().toString();

                Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                startActivity(intent);
                //updateWatchData(repData.get(1), getBaseContext());
            }
        });
        mupdateButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.rep3_name);
                String name = text.getText().toString();
                Intent intent = new Intent(getBaseContext(), DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                startActivity(intent);

                //updateWatchData(repData.get(2), getBaseContext());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public HashMap<String, String> voteData(String zipcode){
        //TODO; Replace with getting vote data through API
        HashMap<String, String> dict1 = new HashMap<String, String>();
        dict1.put("obama","Obama"+zipcode);
        dict1.put("romney", "Romney"+zipcode);
        dict1.put("county","county"+zipcode);
        return dict1;
    }

    public ArrayList<HashMap<String, String>> representData(String zipcode){
        //TODO; Replace with getting data through API in future
        HashMap<String, String> dict1 = new HashMap<String, String>();
        HashMap<String, String> dict2 = new HashMap<String, String>();
        HashMap<String, String> dict3 = new HashMap<String, String>();
        dict1.put("tweet","Speaking on the Senate floor now about the Supreme Court vacancy");
        dict1.put("name", "Dianne Feinstein, "+zipcode);
        dict1.put("party","Democrat");
        dict1.put("email","senator@feinstein.senate.gov");
        dict1.put("site", "feinstein.senate.gov");
        dict1.put("pic", "dianne_feinstein2");

        dict2.put("tweet","@SenateDems stood united at the Supreme Court today"
                + "to tell @Senate_GOPs: #DoYourJob");
        dict2.put("name", "Barbara Boxer, " + zipcode);
        dict2.put("party", "Democrat");
        dict2.put("email","senator@boxer.senate.gov");
        dict2.put("site", "boxer.senate.gov");
        dict2.put("pic", "barbara_boxer");

        dict3.put("tweet","In America, health care is not a privilege reserved for "
                +"the few, but should be a right");
        dict3.put("name", "Barbara Lee "+zipcode);
        dict3.put("party","Democrat");
        dict3.put("email","representative@lee.house.gov");
        dict3.put("site", "lee.house.gov");
        dict3.put("pic", "barbara_lee");

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        data.add(dict1);
        data.add(dict2);
        data.add(dict3);
        return data;
    }

    public void updateWatchData(HashMap<String, String> data, Context ctx){
        Log.d("T", "Starting new intent blahhh");
        Intent sendIntent = new Intent(ctx, PhoneToWatchService.class);
        //sendIntent.putExtra(EXTRA_MESSAGE, name);
        Bundle bundle = new Bundle();
        bundle.putString("REP_NAME", data.get("name"));
        bundle.putString("REP_PARTY", data.get("party"));
        bundle.putString("REP_PIC", data.get("pic"));

        sendIntent.putExtra(EXTRA_MESSAGE,bundle);

        startService(sendIntent);
    }

    public void updateWatchData2(ArrayList<HashMap<String, String>> data,
                                 HashMap<String,String> voteData, Context ctx) {
        Intent sendIntent = new Intent(ctx, PhoneToWatchService.class);
        //sendIntent.putExtra(EXTRA_MESSAGE, name);
        Bundle bundle = new Bundle();
        HashMap<String,String> d = data.get(0);
        bundle.putString("REP1", d.get("name")+";"+d.get("party")+";"+d.get("pic"));
        d = data.get(1);
        bundle.putString("REP2", d.get("name")+";"+d.get("party")+";"+d.get("pic"));
        d = data.get(2);
        bundle.putString("REP3", d.get("name")+";"+d.get("party")+";"+d.get("pic"));
        d = voteData;
        bundle.putString("VOTE", d.get("obama")+";"+d.get("romney")+";"+d.get("county"));

        sendIntent.putExtra(EXTRA_MESSAGE,bundle);

        startService(sendIntent);
    }


        public void updateViewData(ArrayList<HashMap<String, String>> data){
        //Figure out how to do in a loop later
//        for(int i=0; i<data.size(); i++){
//            String curRep = "rep"+i+"_";
//            TextView tweet = (TextView) findViewById(R.id.rep_tweet);
//            tweet.setText(zipcode);
//        }

        TextView tweet = (TextView) findViewById(R.id.rep1_tweet);
        tweet.setText(data.get(0).get("tweet"));
        tweet = (TextView) findViewById(R.id.rep2_tweet);
        tweet.setText(data.get(1).get("tweet"));
        tweet = (TextView) findViewById(R.id.rep3_tweet);
        tweet.setText(data.get(2).get("tweet"));

        TextView name = (TextView) findViewById(R.id.rep1_name);
        name.setText(data.get(0).get("name"));
        name = (TextView) findViewById(R.id.rep2_name);
        name.setText(data.get(1).get("name"));
        name = (TextView) findViewById(R.id.rep3_name);
        name.setText(data.get(2).get("name"));

        TextView party = (TextView) findViewById(R.id.rep1_party);
        party.setText(data.get(0).get("party"));
        party = (TextView) findViewById(R.id.rep2_party);
        party.setText(data.get(1).get("party"));
        party = (TextView) findViewById(R.id.rep3_party);
        party.setText(data.get(2).get("party"));

        TextView email = (TextView) findViewById(R.id.rep1_email);
        email.setText(data.get(0).get("email"));
        email = (TextView) findViewById(R.id.rep2_email);
        email.setText(data.get(1).get("email"));
        email = (TextView) findViewById(R.id.rep3_email);
        email.setText(data.get(2).get("email"));

        TextView site = (TextView) findViewById(R.id.rep1_site);
        site.setText(data.get(0).get("site"));
        site = (TextView) findViewById(R.id.rep2_site);
        site.setText(data.get(1).get("site"));
        site = (TextView) findViewById(R.id.rep3_site);
        site.setText(data.get(2).get("site"));


        int id = getResources().getIdentifier(data.get(0).get("pic"), "drawable", "com.cs160.elena.represent");
        ImageView img = (ImageView) findViewById(R.id.rep1_img);
        img.setImageResource(id);
        id = getResources().getIdentifier(data.get(1).get("pic"), "drawable", "com.cs160.elena.represent");
        img = (ImageView) findViewById(R.id.rep2_img);
        img.setImageResource(id);
        id = getResources().getIdentifier(data.get(2).get("pic"), "drawable", "com.cs160.elena.represent");
        img = (ImageView) findViewById(R.id.rep3_img);
        img.setImageResource(id);
    }
}
