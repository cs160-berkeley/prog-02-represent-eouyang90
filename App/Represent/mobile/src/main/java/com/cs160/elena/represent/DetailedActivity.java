package com.cs160.elena.represent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.
    private ArrayList<String> commValues;
    private ArrayList<String> billValues;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        Bundle extras = (Bundle) intent.getExtras().get(RepAdapter.EXTRA_MESSAGE);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", extras.getString("name"));
        data.put("term_end", extras.getString("term_end"));
        data.put("pic", extras.getString("pic"));
        data.put("party", extras.getString("party"));

        Log.d("T", "Pic URL: " + extras.getString("pic"));

        updateData(data);

        commValues = new ArrayList<String>();
        billValues = new ArrayList<String>();


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        listDataHeader = new ArrayList<String>();
        listDataHeader.add("Committees");
        listDataHeader.add("Bills");
        listDataChild = new HashMap<String, List<String>>();
        listDataChild.put(listDataHeader.get(0), commValues);
        listDataChild.put(listDataHeader.get(1), billValues);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        String bioguide_id = extras.getString("bioguide_id");
        getCommitteeData(bioguide_id);
        getBillData(bioguide_id);
    }

    public void updateData(HashMap<String, String> data){
        TextView nameText = (TextView) findViewById(R.id.rep_name);
        nameText.setText(data.get("name"));
        TextView partyText = (TextView) findViewById(R.id.rep_party);
        if (data.get("party").equals("D")){
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rep_info);
            layout.setBackgroundResource(R.color.darkblue);
            partyText.setText("Democrat");
        } else if (data.get("party").equals("R")){
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rep_info);
            layout.setBackgroundResource(R.color.darkred);
            partyText.setText("Republican");
        } else {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rep_info);
            layout.setBackgroundResource(R.color.purple);
            partyText.setText(data.get("party"));
        }



        TextView termText = (TextView) findViewById(R.id.rep_term);
        termText.setText(data.get("term_end"));
        int id = getResources().getIdentifier(data.get("pic"), "drawable", "com.cs160.elena.represent");
        ImageView img = (ImageView) findViewById(R.id.rep_img);
        Picasso
                .with(this)
                .load(data.get("pic"))
                .fit() // will explain later
                .into(img);
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

    public void getCommitteeData(String bioguide_id){
        final Context ctx = this;
        String serviceURL = "http://congress.api.sunlightfoundation.com/committees?member_ids="
                    + bioguide_id + "&apikey=787f9b8c4dd245d58e796cbca7a3aff2";

        AsyncTask<String, Void, JSONObject> asyncTask =new WebAsyncTask(new AsyncResponse(){
            @Override
            public void processFinish(JSONObject output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                try {
                    JSONArray jArray = output.getJSONArray("results");
                    Log.d("T", "Output: " + jArray);
                    for (int i=0; i < jArray.length(); i++)
                    {
                        try {
                            JSONObject commJSON = jArray.getJSONObject(i);
                            // Pulling items from the array
                            Log.d("T", "Added comm: " + commJSON.getString("name"));
                            commValues.add(commJSON.getString("name"));
                        } catch (JSONException e) {
                            // Oops
                            Log.d("T", "JSONexception in getCommData");
                        }
                    }
                    listAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(serviceURL);
    }


    public void getBillData(String bioguide_id) {
        final Context ctx = this;
        String serviceURL = "http://congress.api.sunlightfoundation.com/bills?sponsor_id="
                                + bioguide_id + "&apikey=787f9b8c4dd245d58e796cbca7a3aff2";

        AsyncTask<String, Void, JSONObject> asyncTask = new WebAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(JSONObject output) {
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                try {
                    JSONArray jArray = output.getJSONArray("results");
                    for (int i = 0; i < jArray.length(); i++) {
                        try {
                            JSONObject billJSON = jArray.getJSONObject(i);
                            // Pulling items from the array
                            billValues.add(billJSON.getString("introduced_on") + ": "
                            + billJSON.getString("official_title"));
                        } catch (JSONException e) {
                            // Oops
                            Log.d("T", "JSONexception in getCommData");
                        }
                    }
                    listAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(serviceURL);
    }
}
