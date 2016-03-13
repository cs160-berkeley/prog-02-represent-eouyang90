package com.cs160.elena.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainGridPagerAdapter extends FragmentGridPagerAdapter {

    ArrayList<String> names;
    ArrayList<String> parties;
    ArrayList<String> imgs;
    String voteObama;
    String voteRomney;
    String county;

    private final Context mContext;
    private List mRows;
    private FragmentManager fm;

    public MainGridPagerAdapter(Context ctx, FragmentManager fm, Bundle info) {
        super(fm);
        mContext = ctx;
        names = new ArrayList<String>();
        parties = new ArrayList<String>();
        imgs = new ArrayList<String>();

        String[] reps = info.getString("CAT_NAME").split("\\$");
        String[] data = reps[0].split(";");
        Log.d("T", data.toString());
        voteObama = data[0];
        voteRomney = data[1];
        county = data[2];

        for(int i=1; i<reps.length;i++){
            data = reps[i].split(";");
            names.add(data[0]);
            parties.add(data[1]);
            imgs.add(data[2]);
        }
    }

    @Override
    public Fragment getFragment(int row, int column) {
        //return (MainCardFragment.create(names[row][column], parties[column]));
        if (column == names.size()){
            //return the 2012 vote view
            VoteCardFragment myCard = new VoteCardFragment();
            Bundle args = new Bundle();
            args.putString("obama", voteObama);
            args.putString("romney", voteRomney);
            args.putString("county", county);
            Log.d("T", "Vote View" + voteRomney);
            Log.d("T", "Vote  " + county);
            myCard.setArguments(args);
            return myCard;
        }
        MyCard myCard = new MyCard();
        Bundle args = new Bundle();
        args.putString("name", names.get(column));
        args.putString("party", parties.get(column));
        myCard.setArguments(args);

        //fm.beginTransaction().replace(R.id.container, myCard, "myCard").commit();
        return myCard;
        //return cardFragment(row,column);
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        int id;
        if (column == names.size()){
            id = mContext.getResources().getIdentifier("people", "drawable",
                    "com.cs160.elena.represent");
        } else {
            //id = mContext.getResources().getIdentifier(imgs.get(column), "drawable",
            id = mContext.getResources().getIdentifier("people", "drawable",
                    "com.cs160.elena.represent");
        }
        return mContext.getResources().getDrawable(id, mContext.getTheme());
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return names.size() + 1;
    }

}