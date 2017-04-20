package com.example.avinash.nittcart.display_item_files;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.GridView;

import com.example.avinash.nittcart.R;

/**
 * Created by AVINASH on 3/11/2017.
 */
public class ctgitem {
    GridView grid;
    String[] tag = {
            "Book","Drafter","Sports","Electronics","Others"
    } ;
    int[] imageId = {
            R.drawable.book, R.drawable.book,
            R.drawable.book,R.drawable.book,R.drawable.book
    };

    public ctgitem(Context context, View view){
        itemtype_adapter adapter = new itemtype_adapter(context, tag, imageId);
        View rootView = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        grid=(GridView)view.findViewById(R.id.gridview);
        grid.setAdapter(adapter);
    }
}
