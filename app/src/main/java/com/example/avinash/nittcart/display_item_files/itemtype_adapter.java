package com.example.avinash.nittcart.display_item_files;

import android.app.Activity;
import android.content.Context;
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
public class itemtype_adapter extends BaseAdapter {

    private Context mContext;
    private final String[] tag;
    private final int[] Imageid;
    View rootView;
    static RelativeLayout prevview;
    static BadgeView prevBadge;

    public itemtype_adapter(Context c, String[] tag, int[] Imageid) {
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
                    postadd.category = textView.getText().toString();
                    setSelection(outercustomrl);
                    setBadge(customrl);
                    prevview = outercustomrl;
                    switch (i){
                        case 0:postadd.ITEM_TYPE = "CH";
                            break;
                        case 1:postadd.ITEM_TYPE = "PH";
                            break;
                        case 2:postadd.ITEM_TYPE = "MATH";
                            break;
                        case 3:postadd.ITEM_TYPE = "ENG";
                            break;
                        case 4:postadd.ITEM_TYPE = "CSE";
                            break;
                        case 5:postadd.ITEM_TYPE = "CIVIL";
                            break;
                        case 6:postadd.ITEM_TYPE = "CHEM";
                            break;
                        case 7:postadd.ITEM_TYPE = "EEE";
                            break;
                        case 8:postadd.ITEM_TYPE = "ECE";
                            break;
                        case 9:postadd.ITEM_TYPE = "ICE";
                            break;
                        case 10:postadd.ITEM_TYPE = "MECH";
                            break;
                        case 11:postadd.ITEM_TYPE = "MME";
                            break;
                        case 12:postadd.ITEM_TYPE = "PROD";
                            break;
                        case 13:postadd.ITEM_TYPE = "OTHER";
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
