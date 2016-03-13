package com.cs160.elena.represent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by elena on 3/12/16.
 */
public class RepAdapter extends BaseAdapter {
    public final static String EXTRA_MESSAGE = "com.cs160.represent.MainActivity.RepInfo";
    private Context mContext;
    private ArrayList<HashMap<String, String>> mrepData;
    private ArrayList<View> mviews;

    public RepAdapter(Context context, ArrayList<HashMap<String, String>> repData) {
        this.mContext = context;
        this.mrepData = repData;
        this.mviews = new ArrayList<View>();
    }

    @Override
    public int getCount() {
        return mrepData.size();
    }

    @Override
    public Object getItem(int position) {
        return mviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View repRow = inflater.inflate(R.layout.main_list, parent, false);
        TextView nameView = (TextView) repRow.findViewById(R.id.rep1_name);
        TextView tweetView = (TextView) repRow.findViewById(R.id.rep1_tweet);
        TextView partyView = (TextView) repRow.findViewById(R.id.rep1_party);
        TextView emailView = (TextView) repRow.findViewById(R.id.rep1_email);
        TextView siteView = (TextView) repRow.findViewById(R.id.rep1_site);
        ImageView pictureView = (ImageView) repRow.findViewById(R.id.rep1_img);


        nameView.setText(mrepData.get(pos).get("name"));
        tweetView.setText("\""+mrepData.get(pos).get("tweet")+"\"");
        emailView.setText(mrepData.get(pos).get("email"));
        siteView.setClickable(true);
        siteView.setMovementMethod(LinkMovementMethod.getInstance());
        siteView.setText(Html.fromHtml("<a href=\""
                + mrepData.get(pos).get("site") + "\">"
                + mrepData.get(pos).get("site") + "</a> "));
        Picasso
                .with(mContext)
                .load(mrepData.get(pos).get("pic"))
                .fit() // will explain later
                .into(pictureView);

        //Attaching onClickListener
        final View repRow2 = repRow;
        final String bioguide_id = mrepData.get(pos).get("bioguide_id");
        final String term_end = mrepData.get(pos).get("term_end");
        final String name = mrepData.get(pos).get("name");
        final String pic = mrepData.get(pos).get("pic");
        final String party = mrepData.get(pos).get("party");
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailedActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("bioguide_id", bioguide_id);
                bundle.putString("term_end", term_end);
                bundle.putString("name", name);
                bundle.putString("pic", pic);
                bundle.putString("party", party);

                intent.putExtra(EXTRA_MESSAGE, bundle);
                mContext.startActivity(intent);
            }
        });

        if (mrepData.get(pos).get("party").equals("D")){
            repRow.findViewById(R.id.rep1).setBackgroundResource(R.color.darkblue);
            tweetView.setBackgroundResource(R.drawable.round_blue);
            partyView.setText("Democrat");
        } else {
            repRow.findViewById(R.id.rep1).setBackgroundResource(R.color.darkred);
            tweetView.setBackgroundResource(R.drawable.round_red);
            partyView.setText("Republican");
        }


        return repRow;
    }

}