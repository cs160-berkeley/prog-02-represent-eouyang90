package com.cs160.elena.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.cs160.represent.MainActivity.RepInfo";
    private RepAdapter madapter;
    private ListView mrepListView;

    private Button mupdateButton1;
    private Button mupdateButton2;
    private Button mupdateButton3;

    private ArrayList<HashMap<String, String>> mrepData;
    private HashMap<String,String> mvoteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String latlon = intent.getStringExtra(HomeActivity.EXTRA_MESSAGE);
        //TODO; change so that it handles random zips?
        if (latlon.equals(HomeActivity.CUR_LOC)){
//            int index = (int) Math.random()*zipcodes.length;
//            zipcode = zipcodes[index];
        }

        //get location data from latlon
        getLocData(latlon);
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

    public HashMap<String, String> voteData(String county, String state, String zipcode){
        String serviceURL = "http://congress.api.sunlightfoundation.com/legislators/locate?"
                + "zip=" + zipcode +"&apikey=787f9b8c4dd245d58e796cbca7a3aff2";
        AsyncTask<String, Void, JSONObject> asyncTask =new WebAsyncTask(new AsyncResponse(){

            @Override
            public void processFinish(JSONObject output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray jArray = output.getJSONArray("results");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(serviceURL);


        HashMap<String, String> dict = getVoteData(county, state);

        return dict;
    }

    private HashMap<String, String> getVoteData(String county, String state){
        state = state.toLowerCase();
        county = county.toLowerCase();

        InputStream stream = null;
        try {
            stream = getAssets().open("election-county-2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();

            String jsonString = new String(buffer, "UTF-8");
            HashMap<String, String> data = new HashMap<String, String>();
            try {
                JSONArray votes = new JSONArray(jsonString);
                for (int i=0; i < votes.length(); i++)
                {
                    try {
                        JSONObject ctyJSON = votes.getJSONObject(i);
                        // Pulling items from the array
                        if (ctyJSON.getString("state-postal").toLowerCase().equals(state)
                                && county.contains(ctyJSON.getString("county-name").toLowerCase())){
                            data.put("county", county);
                            data.put("state", state);
                            data.put("obama", ctyJSON.getString("obama-percentage"));
                            data.put("romney", ctyJSON.getString("romney-percentage"));
                        }
                    } catch (JSONException e) {
                        // Oops
                        e.printStackTrace();
                        return null;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void handleRepresentData(final HashMap<String, String> loc){
        final Context ctx = this;
        final String zipcode = loc.get("zipcode");
        final String state = loc.get("state");
        final String county = loc.get("county");
        String serviceURL = "http://congress.api.sunlightfoundation.com/legislators/locate?"
            + "zip=" + zipcode +"&apikey=787f9b8c4dd245d58e796cbca7a3aff2";

        AsyncTask<String, Void, JSONObject> asyncTask =new WebAsyncTask(new AsyncResponse(){

            @Override
            public void processFinish(JSONObject output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                try {
                    JSONArray jArray = output.getJSONArray("results");
                    for (int i=0; i < jArray.length(); i++)
                    {
                        HashMap<String, String> dict = new HashMap<String, String>();
                        try {
                            JSONObject repJSON = jArray.getJSONObject(i);
                            // Pulling items from the array

                            dict.put("email", repJSON.getString("oc_email"));
                            dict.put("name", repJSON.getString("first_name") + " "
                                    + repJSON.getString("last_name"));
                            dict.put("party",repJSON.getString("party"));
                            dict.put("site", repJSON.getString("website"));
                            dict.put("bioguide_id", repJSON.getString("bioguide_id"));
                            dict.put("term_end", repJSON.getString("term_end"));
                            dict.put("twitter_id", repJSON.getString("twitter_id"));

                            //TODO; temp holders
                            dict.put("tweet",
                                    "Tweet");
                            dict.put("pic",
                                    "http://www.nanigans.com/wp-content/uploads/2014/07/Generic-Avatar.png");

                            data.add(dict);
                        } catch (JSONException e) {
                            // Oops
                            Log.d("T", "JSONexception in handleRepData");
                        }
                    }
                    mrepData = data;
                    //update tweet data
                    for (int i=0; i< data.size(); i++){
                        handleTweet(i, ctx);
                    }

                    //get vote data
                    mvoteData = voteData(county, state, zipcode);

                    //Set up listview
                    mrepListView = (ListView) findViewById(R.id.listView);
                    //Create the Adapter
                    madapter = new RepAdapter(ctx, mrepData);
                    //Set the Adapter
                    mrepListView.setAdapter(madapter);

                    //update views with new data
                    //updateViewData(data);
                    madapter.notifyDataSetChanged();

                    //update watch views with new data
                    updateWatchData(mrepData, mvoteData, ctx);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(serviceURL);
    }

    public void updateViewData(ArrayList<HashMap<String, String>> data){
        //TODO; Figure out how to do with list views later

//        TextView tweet = (TextView) findViewById(R.id.rep1_tweet);
//        tweet.setText(data.get(0).get("tweet"));
//        tweet = (TextView) findViewById(R.id.rep2_tweet);
//        tweet.setText(data.get(1).get("tweet"));
//        tweet = (TextView) findViewById(R.id.rep3_tweet);
//        tweet.setText(data.get(2).get("tweet"));
//
//        TextView name = (TextView) findViewById(R.id.rep1_name);
//        name.setText(data.get(0).get("name"));
//        name = (TextView) findViewById(R.id.rep2_name);
//        name.setText(data.get(1).get("name"));
//        name = (TextView) findViewById(R.id.rep3_name);
//        name.setText(data.get(2).get("name"));
//
//        TextView party = (TextView) findViewById(R.id.rep1_party);
//        party.setText(data.get(0).get("party"));
//        party = (TextView) findViewById(R.id.rep2_party);
//        party.setText(data.get(1).get("party"));
//        party = (TextView) findViewById(R.id.rep3_party);
//        party.setText(data.get(2).get("party"));
//
//        TextView email = (TextView) findViewById(R.id.rep1_email);
//        email.setText(data.get(0).get("email"));
//        email = (TextView) findViewById(R.id.rep2_email);
//        email.setText(data.get(1).get("email"));
//        email = (TextView) findViewById(R.id.rep3_email);
//        email.setText(data.get(2).get("email"));
//
//        TextView site = (TextView) findViewById(R.id.rep1_site);
//        site.setText(data.get(0).get("site"));
//        site = (TextView) findViewById(R.id.rep2_site);
//        site.setText(data.get(1).get("site"));
//        site = (TextView) findViewById(R.id.rep3_site);
//        site.setText(data.get(2).get("site"));
//
//
//        int id = getResources().getIdentifier(data.get(0).get("pic"), "drawable", "com.cs160.elena.represent");
//        ImageView img = (ImageView) findViewById(R.id.rep1_img);
//        img.setImageResource(id);
//        id = getResources().getIdentifier(data.get(1).get("pic"), "drawable", "com.cs160.elena.represent");
//        img = (ImageView) findViewById(R.id.rep2_img);
//        img.setImageResource(id);
//        id = getResources().getIdentifier(data.get(2).get("pic"), "drawable", "com.cs160.elena.represent");
//        img = (ImageView) findViewById(R.id.rep3_img);
//        img.setImageResource(id);
    }

    public void updateWatchData(ArrayList<HashMap<String, String>> data,
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

    public void getLocData(final String latlon){
        String serviceURL = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                + latlon +"&key=AIzaSyAk26JXUjFkV_tGwDPEYwIecxzYOlC3ves";
        AsyncTask<String, Void, JSONObject> asyncTask =new WebAsyncTask(new AsyncResponse(){
            @Override
            public void processFinish(JSONObject output){
                //Here you will receive the result fired from async class
                //of onPostExecute(result) method.
                HashMap<String, String> data = new HashMap<String, String>();
                try {
                    JSONArray jArray = output.getJSONArray("results");
                    //first location object data
                    JSONArray jAddrs = jArray.getJSONObject(0).getJSONArray("address_components");
                    for (int i=0; i < jAddrs.length(); i++)
                    {
                        try {
                            JSONObject comp = jAddrs.getJSONObject(i);
                            // Pulling items from the array
                            data.put("latlon", latlon);
                            String type = comp.getString("types").toString().toLowerCase();
                            if (type.contains("administrative_area_level_2")){
                                data.put("county", comp.getString("short_name"));
                            } else if (type.contains("administrative_area_level_1")){
                                data.put("state", comp.getString("short_name"));
                            } else if (type.contains("postal_code")
                                    && !type.contains("postal_code_suffix")){
                                data.put("zipcode", comp.getString("short_name"));
                            }
                        } catch (JSONException e) {
                            // Oops
                            e.printStackTrace();
                        }
                    }

                    //TODO; update with correct location vals
                    handleRepresentData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(serviceURL);
    }

    protected void handleTweet(int index, Context ctx2){
        final HashMap<String, String> dict = mrepData.get(index);
        String twitter_id = dict.get("twitter_id");
        Log.d("T", "Tweet_id: " + twitter_id);
        // TODO: Base this Tweet ID on some data from elsewhere in your app
        final Context ctx = ctx2;
        TwitterApiClient taClient = TwitterCore.getInstance().getApiClient();
        StatusesService myStatService = taClient.getStatusesService();
        myStatService.userTimeline(null,twitter_id,null,new Long(1),null,null,null,null,null,
                new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("T", "Successful handling of tweet!");
                Log.d("T",result.data.get(0).text);
                Log.d("T", result.data.get(0).user.profileImageUrl);

                //edit imageUrl so it's not tiny
                String url = result.data.get(0).user.profileImageUrl;
                url = url.replace("_normal", "");

                dict.put("pic", url);
                dict.put("tweet", result.data.get(0).text);
                madapter.notifyDataSetChanged();
//                ImageView imageView = (ImageView) findViewById(R.id.rep1_img);
//                Picasso.with(ctx).load(url).into(imageView);
//                TextView tweet = (TextView) findViewById(R.id.rep1_tweet);
//                tweet.setText(result.data.get(0).text);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Load Tweet failure", exception);
            }
        });
    }
}
