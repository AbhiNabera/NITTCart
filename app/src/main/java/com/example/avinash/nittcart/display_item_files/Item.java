package com.example.avinash.nittcart.display_item_files;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AVINASH on 3/14/2017.
 */
public class Item {

    private String item_id;
    private String item_price;
    private String item_image;
    private String item_name;
    private String item_verified;
    private String item_description;
    private String item_quality;


    private View.OnClickListener requestBtnClickListener;

    public Item() {
    }

    public Item(String item_id, String item_price, String item_image,
                String item_name, String item_verified, String item_description, String item_quality) {
        this.item_id = item_id;
        this.item_price = item_price;
        //this.item_image = item_image;
        this.item_name = item_name;
        this.item_verified = item_verified;
        this.item_description = item_description;
        this.item_quality = item_quality;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String price) {
        this.item_price = price;
    }
    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_price = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_verified() {
        return item_verified;
    }

    public void setItem_verified(String item_verified) {
        this.item_verified = item_verified;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_quality() {
        return item_quality;
    }

    public void setItem_quality(String item_quality) {
        this.item_quality = item_quality;
    }

    /**
     * @return List of elements prepared for tests
     */
    public static ArrayList<Item> getTestingList() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        items.add(new Item("ID_XYZ","450","IMAGE_URL","HYAT","Verified by Nittcart","Description here","Good As New"));
        return items;

    }

}
