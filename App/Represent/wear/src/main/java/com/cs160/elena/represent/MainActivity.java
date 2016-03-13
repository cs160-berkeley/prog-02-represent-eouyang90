package com.cs160.elena.represent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "GJI7403eVcp6kIdjyjnRNkp4b";
    private static final String TWITTER_SECRET = "YqjuUcEvXwAli2nZHc7P8HQd8VE5AfZu2YEBNyawOvc1UCLCGb";


    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        setContentView(R.layout.activity_main);

        //Attempt to setup 2d Picker
        final DotsPageIndicator mPageIndicator;
        final GridViewPager mViewPager;


        // Assigns an adapter to provide the content for this pager

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            String[] parts = extras.getString("CAT_NAME").split(";");
            //TODO; add check here
            final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
            pager.setAdapter(new MainGridPagerAdapter(this, getFragmentManager(), extras));


        }
    }

    protected void updateWatchFace(String imageName){
        Log.d("T", "Updating watch face with " + imageName);
        int id = getResources().getIdentifier(imageName, "drawable", "com.cs160.elena.represent");
        LinearLayout layout = (LinearLayout) findViewById(R.id.watch_face);
        layout.setBackground(getResources().getDrawable(id, this.getTheme()));
    }
}