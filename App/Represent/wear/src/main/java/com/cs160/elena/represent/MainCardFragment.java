package com.cs160.elena.represent;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by elena on 3/4/16.
 */
public class MainCardFragment extends CardFragment implements View.OnClickListener{
    private View fragmentView;
    private View.OnClickListener listener;

    @Override
    public View onCreateContentView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        fragmentView = super.onCreateContentView(inflater, container, savedInstanceState);
        Log.d("T", "MERP");
        fragmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (listener != null) {
                    Log.d("T", "clicked a button woo");
                    listener.onClick(view);
                }
            }

        });
        return fragmentView;
    }

    public void setOnClickListener(final View.OnClickListener listener) {
        Log.d("T", "MERP2");
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        Log.d("T", "Got view click! " + v.getId());
    }
}
