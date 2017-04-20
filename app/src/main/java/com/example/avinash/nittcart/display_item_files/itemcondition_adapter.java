package com.example.avinash.nittcart.display_item_files;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.R;

import com.example.avinash.nittcart.tab_and_bottombar.postadd;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.Random;

/**
 * Created by AVINASH on 1/30/2017.
 */
public class itemcondition_adapter extends BaseAdapter {

    private Context mContext;
    private final String[] tag;
    private final int[] Imageid;
    View rootView;
    static RelativeLayout prevview;
    static BadgeView prevBadge;

    public itemcondition_adapter(Context c, String[] tag, int[] Imageid) {
        mContext = c;
        rootView = ((Activity)mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        this.Imageid = Imageid;
        this.tag = tag;
    }
    @Override
    public int getCount() {
        return tag.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.customgrid, null);
            final TextView textView = (TextView) grid.findViewById(R.id.customtext);
            final ImageButton imageView = (ImageButton)grid.findViewById(R.id.customimage);
            final RelativeLayout customrl = (RelativeLayout)grid.findViewById(R.id.innercustomgrid);
            final RelativeLayout outercustomrl = (RelativeLayout)grid.findViewById(R.id.customgrid);

            final View finalGrid = grid;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postadd.condition = textView.getText().toString();
                    setSelection(outercustomrl);
                    setBadge(customrl);
                    prevview = outercustomrl;
                    switch (i){
                        case 0:postadd.QUALITY = "GOOD";
                            break;
                        case 1:postadd.QUALITY = "MEDIUM";
                            break;
                        case 2:postadd.QUALITY = "POOR";
                            break;
                    }
                }
            });

            int[] androidColors = rootView.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
             customrl.setBackgroundColor(randomAndroidColor);
            //Typeface type = Typeface.createFromAsset(getAssets(), "font/comic_sans.ttf");
            textView.setText(tag[i]);
            imageView.setImageResource(Imageid[i]);
        } else {
            grid = (View) view;
        }

        return grid;
    }

    public void setSelection(RelativeLayout view){
        if(prevview!=null){
            prevBadge.hide();
            prevview.setPadding(0,0,0,0);
        }
       view.setPadding(10,10,10,10);
    }

    public void setBadge(RelativeLayout view) {
        BadgeView badge = new BadgeView(mContext, view);
        badge.setText("");
        badge.setBackground(mContext.getResources().getDrawable(R.drawable.ic_selected));
        //badge.setBackgroundColor(Color.parseColor("#4f9b62"));
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setBadgeMargin(0);
        badge.setTextSize(0);
        badge.show();
        prevBadge = badge;
    }
}
