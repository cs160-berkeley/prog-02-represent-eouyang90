package com.cs160.elena.represent;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by elena on 3/4/16.
 */
public class VoteCardFragment extends CardFragment {
    TextView mTextView1, mTextView2, mTextView3;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mTextView1 = (TextView) root.findViewById(R.id.textView1);
        mTextView2 = (TextView) root.findViewById(R.id.textView2);
        mTextView3 = (TextView) root.findViewById(R.id.textView3);

        mTextView1.setText("Obama: " + getArguments().getString("obama")+"%");
        mTextView2.setText("Romney: " + getArguments().getString("romney")+"%");
        mTextView3.setText("County: " + getArguments().getString("county"));

        return root;
    }

}
