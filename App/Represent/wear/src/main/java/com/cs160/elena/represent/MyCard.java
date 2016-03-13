package com.cs160.elena.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by elena on 3/4/16.
 */
public class MyCard extends CardFragment {
    Button mButton;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mButton = (Button) root.findViewById(R.id.feed_btn);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("T", "YAYYYY CLICK");
                Log.d("T", getArguments().get("name").toString());
                Intent sendIntent = new Intent(getActivity().getBaseContext(),
                        WatchToPhoneService.class);
                String repdata = getArguments().get("name").toString() + ";"
                        + getArguments().get("party").toString() + ";"
                        + getArguments().get("pic").toString() + ";"
                        + getArguments().get("bioguide_id").toString() + ";"
                        + getArguments().get("term_end").toString();
                sendIntent.putExtra("REP_NAME", repdata);
                getActivity().startService(sendIntent);
            }
        });

        updateContents();
        return root;
    }

    public void updateContents(){
        mButton.setText(getArguments().getString("name") + getArguments().getString("party"));
    }

}
