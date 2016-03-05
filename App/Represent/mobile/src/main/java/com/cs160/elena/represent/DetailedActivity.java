package com.cs160.elena.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        TextView nameText = (TextView) findViewById(R.id.rep_name);
        nameText.setText(name);

        updateData(name);
    }

    public void updateData(String name){
        HashMap<String,String> data = getData(name);

        TextView nameText = (TextView) findViewById(R.id.rep_name);
        nameText.setText(name);
        TextView partyText = (TextView) findViewById(R.id.rep_party);
        partyText.setText(data.get("party"));
        TextView termText = (TextView) findViewById(R.id.rep_term);
        termText.setText(data.get("term"));
    }

    public HashMap<String,String> getData(String name){
        HashMap<String,String> data = new HashMap<String,String>();
        data.put("name", name);
        data.put("party", "party for "+name);
        data.put("term", "term for "+name);

        return data;
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
}
