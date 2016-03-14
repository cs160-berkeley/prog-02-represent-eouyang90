package com.cs160.elena.represent;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by elena on 3/4/16.
 */
public class VoteCardFragment extends CardFragment {
    TextView ostatView, rstatView, countyView, stateView;

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
                                    Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vote, container, false);

        ostatView = (TextView) root.findViewById(R.id.obama_stat);
        rstatView = (TextView) root.findViewById(R.id.romney_stat);
        countyView = (TextView) root.findViewById(R.id.county);
        stateView = (TextView) root.findViewById(R.id.state);

        ostatView.setText(getArguments().getString("obama")+"%");
        rstatView.setText(getArguments().getString("romney")+"%");
        countyView.setText(getArguments().getString("county"));
        stateView.setText(getArguments().getString("state"));

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int w = (int) (Double.valueOf(getArguments().getString("obama")) * scale + 0.5f);
        int l = (int) (20 * scale + 0.5f);
        TextView obar = (TextView) root.findViewById(R.id.obama_bar);
        obar.setLayoutParams(new RelativeLayout.LayoutParams(w,l));

        return root;
    }

}
