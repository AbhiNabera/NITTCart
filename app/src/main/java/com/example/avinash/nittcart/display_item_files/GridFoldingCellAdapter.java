package com.example.avinash.nittcart.display_item_files;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.response_classes.Item_structure;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by AVINASH on 3/25/2017.
 */
public class GridFoldingCellAdapter extends RecyclerView.Adapter<GridFoldingCellAdapter.ViewHolder> {

    private List<Item_structure> ItemList= Collections.emptyList();
    Context mContext;
    int lastPosition = -1;
    static Animation animation,animation2;
    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {

        void addtocart(View v, int position);
    }

    public GridFoldingCellAdapter(List<Item_structure> ItemList , Context mContext ,
                                  Animation animation, Animation animation2, MyAdapterListener onClickListener) {
        this.ItemList = ItemList;
        this.mContext = mContext;
        this.animation = animation;
        this.animation2 = animation2;
        this.onClickListener = onClickListener;

    }

    public void clearadapterList(){
       ItemList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        {
            if(MainActivity.layoutmode.matches("LIST_LAYOUT")) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.cell, parent, false);
            }
            else if(MainActivity.layoutmode.matches("GRID_LAYOUT")) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_item_view, parent, false);
            }
            ViewHolder holder = new ViewHolder(view);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(!ItemList.isEmpty()) {
            if(MainActivity.layoutmode.matches("LIST_LAYOUT")&& viewHolder.fc!=null) {
                viewHolder.fc.fold(true);
                viewHolder.item_id.setText("Item ID:" + ItemList.get(position).getItemId());
                viewHolder.item_price.setText("₹"+ItemList.get(position).getPrice());
                viewHolder.unfolded_price.setText("₹"+ItemList.get(position).getPrice());
                viewHolder.item_name.setText(ItemList.get(position).getName());
                viewHolder.unfolded_name.setText(ItemList.get(position).getName());
                viewHolder.unfolded_item_verified.setText(ItemList.get(position).getStatus());
                viewHolder.item_verified.setText(ItemList.get(position).getStatus());
                viewHolder.item_description.setText(ItemList.get(position).getDescription());
                viewHolder.item_quality.setText(String.valueOf(ItemList.get(position).getQuality()));
                Picasso.with(mContext).
                    load(BASE_IMAGE_URL+ItemList.get(position).getImage())
                    .placeholder(R.drawable.book).error(R.drawable.alert).resize(300, 350).into(viewHolder.item_image);

                Picasso.with(mContext).
                        load(BASE_IMAGE_URL+ItemList.get(position).getImage())
                        .placeholder(R.drawable.book).error(R.drawable.alert).resize(300, 350).into(viewHolder.unfolded_image);
            }
            else if(MainActivity.layoutmode.matches("GRID_LAYOUT")) {
                viewHolder.item_price.setText("₹"+ItemList.get(position).getPrice());
                viewHolder.item_name.setText(ItemList.get(position).getName());
                Picasso.with(mContext).
                        load(BASE_IMAGE_URL+ItemList.get(position).getImage())
                        .placeholder(R.drawable.book).error(R.drawable.alert).resize(300, 350).into(viewHolder.item_image);
            }
        }

        if(position >lastPosition) {
            if(position%2==0)
                viewHolder.itemView.startAnimation(animation);
            else
                viewHolder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public void addItemAtIndex(int index, Item_structure item){
        ItemList.add(index, item);
        notifyDataSetChanged();
    }

    public void setnewList(List<Item_structure> item){
        Log.d("notifyData ", item.size() + "");
        ItemList.clear();
        this.ItemList = item;
        notifyDataSetChanged();
    }

    public void insert(Item_structure item, int position) {
        ItemList.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Item_structure item) {
        int position = ItemList.indexOf(item);
        ItemList.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem(Item_structure item) {
        ItemList.add(item);
        notifyDataSetChanged();
    }

    public void removeAll() {
        ItemList.clear();
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_id;
        TextView item_price, unfolded_price;
        ImageView item_image, unfolded_image;
        TextView item_name, unfolded_name;
        TextView unfolded_item_verified;
        TextView item_verified;
        TextView item_description;
        TextView item_quality;
        TextView add_to_cart;
        RelativeLayout image_background;
        FoldingCell fc;

        public ViewHolder(View itemView) {
            super(itemView);
            if(MainActivity.layoutmode.matches("LIST_LAYOUT")) {
                Log.d("inside","main");
                item_id = (TextView) itemView.findViewById(R.id.item_id);
                item_price = (TextView) itemView.findViewById(R.id.item_price);
                unfolded_price = (TextView) itemView.findViewById(R.id.unfolded_item_price);
                item_image = (ImageView) itemView.findViewById(R.id.item_image);
                unfolded_image = (ImageView) itemView.findViewById(R.id.unfolded_item_image);
                item_name = (TextView) itemView.findViewById(R.id.item_name);
                unfolded_name = (TextView) itemView.findViewById(R.id.unfolded_item_name);
                item_verified = (TextView) itemView.findViewById(R.id.item_verified);
                unfolded_item_verified = (TextView) itemView.findViewById(R.id.unfold_item_verified);
                item_description = (TextView) itemView.findViewById(R.id.item_description);
                item_quality = (TextView) itemView.findViewById(R.id.item_quality);
                add_to_cart = (TextView) itemView.findViewById(R.id.add_to_cart);
                image_background = (RelativeLayout) itemView.findViewById(R.id.item_image_background);
                fc = (FoldingCell) itemView.findViewById(R.id.folding_cell);

                int[] androidColors = mContext.getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                //image_background.setBackgroundColor(randomAndroidColor);
                add_to_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        onClickListener.addtocart(v,getAdapterPosition());
                    }
                });
                fc.initialize(1000, Color.WHITE, 5);
                //fc.setTag(cv);
                fc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fc.toggle(false);
                    }
                });
            }
            else if(MainActivity.layoutmode.matches("GRID_LAYOUT")){
                    item_image = (ImageView) itemView.findViewById(R.id.item_image);
                    item_name = (TextView) itemView.findViewById(R.id.item_name);
                    item_price = (TextView) itemView.findViewById(R.id.item_price);
            }
        }
    }
}
