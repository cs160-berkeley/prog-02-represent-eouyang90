package com.cs160.elena.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class HomeActivity extends Activity {
    //there's not much interesting happening. when the buttons are pressed, they start
    //the PhoneToWatchService with the cat name passed in.
    public final static String EXTRA_MESSAGE = "com.cs160.represent.HomeActivity.ZIPCODE";
    public final static String CUR_LOC = "currentLocation";

    private Button mupdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mupdateButton = (Button) findViewById(R.id.button);

        mupdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.zipcode);
                String zipcode = editText.getText().toString();

                Log.d("T", "about to start intenthdjhgfjhgf");

                //send information containing presidential vote data
                //Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
                //sendIntent.putExtra(EXTRA_MESSAGE, zipcode);
//                sendIntent.putExtra(EXTRA_MESSAGE, "FRED");
//                startService(sendIntent);
//                Log.d("T", "ended intent");
//
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, zipcode);
                startActivity(intent);
            }
        });

        TextView btn=(TextView) findViewById(R.id.curLoc);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra(EXTRA_MESSAGE, CUR_LOC);
                startActivity(intent);
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
}
