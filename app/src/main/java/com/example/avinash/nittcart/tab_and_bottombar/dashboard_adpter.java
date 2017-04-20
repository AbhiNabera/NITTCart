package com.example.avinash.nittcart.tab_and_bottombar;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.response_classes.dashboard_Item_structure;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AVINASH on 4/1/2017.
 */
public class dashboard_adpter extends RecyclerView.Adapter<dashboard_adpter.ViewHolder> {

    private List<dashboard_Item_structure> ItemList = Collections.emptyList();
    Context mContext;
    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void editButtonpressListner(View v, int position);
        void deletebuttonpresslistener(View v, int position);
        public void onImageSelected(View v, int pos);
        Animator setupanimationutils(RelativeLayout rl, int cx, int cy,int start_rad,int end_rad);
    }

    public dashboard_adpter(List<dashboard_Item_structure> Dashboard_ItemList , Context mContext, MyAdapterListener listener) {
        this.ItemList = Dashboard_ItemList;
        this.mContext = mContext;
        onClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(!ItemList.isEmpty()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item_dashboard, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("count","inside");
        if(!ItemList.isEmpty()){
            holder.sold_out.setVisibility(View.GONE);
            holder.item_name.setText(ItemList.get(position).getName());
            holder.item_price.setText("₹" + ItemList.get(position).getPrice());
            holder.item_quality.setText(ItemList.get(position).getQuality());
            //holder.item_status.setText(ItemList.get(position).getStatus());
            holder.item_id.setText("Item ID:"+ItemList.get(position).getItemId());
            Picasso.with(mContext).
                    load(BASE_IMAGE_URL + ItemList.get(position).getImage())
                    .placeholder(R.drawable.book).error(R.drawable.alert).into(holder.item_image);

            if(ItemList.get(position).getStatus()=="sold"){
                holder.alpha.setAlpha((float) 0.3);
                holder.sold_out.setVisibility(View.VISIBLE);
            }
        }

        if(ViewCompat.isAttachedToWindow(holder.circular_reveal)) {
            {
                Log.d("inside", "adapter");
                RelativeLayout myView = holder.circular_reveal;
                // previously invisible view
                // get the center for the clipping circle
                int cx = myView.getMeasuredWidth() / 2;
                int cy = myView.getMeasuredHeight() / 2;

                // get the final radius for the clipping circle
                int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

                // create the animator for this view (the start radius is zero)
                Animator anim = onClickListener.setupanimationutils(myView, cx, cy, 0, finalRadius);

                // make the view visible and start the animation
                myView.setVisibility(View.VISIBLE);
                anim.start();

            }
        }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public void insertlist(List<dashboard_Item_structure> list){
        this.ItemList.addAll(list);
        notifyDataSetChanged();
    }
    public void addItemAtIndex(int index,dashboard_Item_structure item){
        ItemList.add(index, item);
        notifyDataSetChanged();
    }

    public void notifyDataArray(List<dashboard_Item_structure> item){
        Log.d("notifyData ", item.size() + "");
        this.ItemList = item;
        notifyDataSetChanged();
    }

    public void insert(dashboard_Item_structure item, int position) {
        ItemList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int pos) {
        ItemList.remove(pos);
        notifyItemRemoved(pos);
    }


    public void remove(dashboard_Item_structure item) {
        int position = ItemList.indexOf(item);
        ItemList.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem(dashboard_Item_structure item) {
        ItemList.add(item);
        notifyDataSetChanged();
    }

    public void removeAll() {
        ItemList.clear();
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView item_image;
        TextView item_name;
        TextView item_price;
        TextView item_status;
        TextView item_quality;
        TextView item_id;
        Button delete;
        Button edit;
        LinearLayout alpha;
        RelativeLayout sold_out , circular_reveal;

        public ViewHolder(View itemView) {
            super(itemView);

            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_image = (ImageView) itemView.findViewById(R.id.dash_item_image);
            item_price = (TextView) itemView.findViewById(R.id.dash_item_price);
            item_status = (TextView) itemView.findViewById(R.id.item_verified);
            item_quality = (TextView) itemView.findViewById(R.id.dash_item_quality);
            delete = (Button) itemView.findViewById(R.id.delete);
            edit = (Button) itemView.findViewById(R.id.edit);
            item_id = (TextView) itemView.findViewById(R.id.product_id);
            alpha = (LinearLayout) itemView.findViewById(R.id.alpha);
            sold_out = (RelativeLayout) itemView.findViewById(R.id.sold_out);
            circular_reveal = (RelativeLayout) itemView.findViewById(R.id.circular_reveal);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.editButtonpressListner(v,getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //createAlertdialog();
                    Log.d("click", "button." + getAdapterPosition());
                    onClickListener.deletebuttonpresslistener(v, getAdapterPosition());
                }
            });

            item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onImageSelected(v, getAdapterPosition());
                }
            });
        }

    }
}
