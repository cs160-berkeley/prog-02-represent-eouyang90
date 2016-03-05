package com.cs160.elena.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.List;

public class MainGridPagerAdapter extends FragmentGridPagerAdapter {

    String[][] names ={
            { "", "", "" }
    };
    String[] parties ={"", "", ""};
    String[] imgs ={"", "", "" };
    String voteObama;
    String voteRomney;
    String county;

    private final Context mContext;
    private List mRows;
    private FragmentManager fm;

    public MainGridPagerAdapter(Context ctx, FragmentManager fm, Bundle info) {
        super(fm);
        mContext = ctx;
        fm = fm;

        String[] reps = info.getString("CAT_NAME").split("\\$");
        for(int i=0; i<3;i++){
            String[] data = reps[i].split(";");
            names[0][i] = data[0];
            parties[i] = data[1];
            imgs[i] = data[2];
        }
        String[] data = reps[3].split(";");
        Log.d("T", data.toString());
        voteObama = data[0];
        voteRomney = data[1];
        county = data[2];
    }

    @Override
    public Fragment getFragment(int row, int column) {
        //return (MainCardFragment.create(names[row][column], parties[column]));
        if (column == 3){
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
        args.putString("name", names[row][column]);
        args.putString("party", parties[column]);
        myCard.setArguments(args);

        //fm.beginTransaction().replace(R.id.container, myCard, "myCard").commit();
        return myCard;
        //return cardFragment(row,column);
    }

    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        int id;
        if (column == 3){
            id = mContext.getResources().getIdentifier("people", "drawable", "com.cs160.elena.represent");
        } else {
            id = mContext.getResources().getIdentifier(imgs[column], "drawable", "com.cs160.elena.represent");
        }
        return mContext.getResources().getDrawable(id, mContext.getTheme());
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int row) {
        return 4;
    }

}