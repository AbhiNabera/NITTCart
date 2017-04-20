package com.example.avinash.nittcart.tab_and_bottombar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.display_item_files.Item;
import com.example.avinash.nittcart.response_classes.cart_Item_structure;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AVINASH on 4/1/2017.
 */
public class cart_adpter extends RecyclerView.Adapter<cart_adpter.ViewHolder>{
    private List<cart_Item_structure> ItemList= Collections.emptyList();
    Context mContext;
    private String BASE_IMAGE_URL="http://139.59.79.221:80/static/images/";

    public MyAdapterListener onClickListener;

    public interface MyAdapterListener {
        public void deletefromCart(View v, int position);
        public void onImageSelected(View v, int pos);
    }

    public cart_adpter(List<cart_Item_structure> Cart_ItemList , Context mContext, MyAdapterListener onClickListener) {
        this.ItemList = Cart_ItemList;
        this.mContext = mContext;
        this.onClickListener = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
         {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cart_item_view, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return new ViewHolder(view);
        }

    }

    public void addItemAtIndex(int index,cart_Item_structure item){
        ItemList.add(index, item);
        notifyDataSetChanged();
    }

    public void set_new_list(List<cart_Item_structure> item){
        Log.d("notifyData ", item.size() + "");
        this.ItemList.clear();
        this.ItemList = item;
        notifyDataSetChanged();
    }

    public void insert(cart_Item_structure item, int position) {
        ItemList.add(position, item);
        notifyItemInserted(position);
    }

    public void insert(ArrayList<cart_Item_structure> cart_list){
        this.ItemList = cart_list;
    }

    public void remove(String item) {
        int position = ItemList.indexOf(item);
        ItemList.remove(position);
        notifyItemRemoved(position);
    }
    public void addItem(cart_Item_structure item) {
        ItemList.add(item);
        notifyDataSetChanged();
    }

    public void removeAll() {
        ItemList.clear();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(ItemList.size()!=0) {
            holder.item_name.setText(ItemList.get(position).getName());
            holder.item_price.setText("₹"+ItemList.get(position).getPrice());
            holder.item_quality.setText(ItemList.get(position).getQuality());
            holder.item_status.setText(ItemList.get(position).getStatus());
            holder.item_id.setText("Item id: "+ItemList.get(position).getItemId());
            Picasso.with(mContext).
                    load(BASE_IMAGE_URL+ItemList.get(position).getImage())
                    .placeholder(R.drawable.book).error(R.drawable.alert).into(holder.item_image);
        }
    }

    @Override
    public int getItemCount() {
        return ItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_id;
        TextView item_name;
        TextView item_price;
        TextView item_status;
        ImageView item_image;
        TextView item_quality;
        TextView delete;
        public ViewHolder(View itemView) {
            super(itemView);
            item_id = (TextView) itemView.findViewById(R.id.product_id);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            item_status = (TextView) itemView.findViewById(R.id.item_status);
            item_quality = (TextView) itemView.findViewById(R.id.item_quality);
            item_image = (ImageView) itemView.findViewById(R.id.item_image);
            delete = (TextView) itemView.findViewById(R.id.delete_item);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.deletefromCart(v,getAdapterPosition());
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
