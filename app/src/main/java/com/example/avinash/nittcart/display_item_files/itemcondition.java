package com.example.avinash.nittcart.display_item_files;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.example.avinash.nittcart.R;

/**
 * Created by AVINASH on 3/11/2017.
 */
public class itemcondition {
    GridView grid;
    String[] tag = {
            "Good as new", "Fairly used", "Heavily used"
    };
    int[] imageId = {
            R.drawable.condnew , R.drawable.used, R.drawable.old
    };

    public itemcondition(Context context, View view) {
        itemtype_adapter adapter = new itemtype_adapter(context, tag, imageId);
        View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        grid = (GridView) view.findViewById(R.id.gridview2);

        grid.setAdapter(adapter);
    }
}