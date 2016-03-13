package com.cs160.elena.represent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
        Button updateButton = (Button) repRow.findViewById(R.id.rep1_btn);


        nameView.setText(mrepData.get(pos).get("name"));
        tweetView.setText(mrepData.get(pos).get("tweet"));
        emailView.setText(mrepData.get(pos).get("email"));
        partyView.setText(mrepData.get(pos).get("party"));
        siteView.setText(mrepData.get(pos).get("site"));
        Picasso
                .with(mContext)
                .load(mrepData.get(pos).get("pic"))
                .fit() // will explain later
                .into(pictureView);

        //Attaching onClickListener
        final View repRow2 = repRow;
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) repRow2.findViewById(R.id.rep1_name);
                String name = text.getText().toString();
                Intent intent = new Intent(mContext, DetailedActivity.class);
                intent.putExtra(EXTRA_MESSAGE, name);
                mContext.startActivity(intent);
            }
        });


        return repRow;
    }

}