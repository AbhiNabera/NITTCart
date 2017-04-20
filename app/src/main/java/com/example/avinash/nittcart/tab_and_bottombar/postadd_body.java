package com.example.avinash.nittcart.tab_and_bottombar;

/**
 * Created by AVINASH on 4/11/2017.
 */
public class postadd_body {
    String user_id;
    String auth_token;
    String name;
    String description;
    String item_type;
    String price;
    String quality;

    public postadd_body(String user_id,
            String auth_token,
            String name,
            String description,
            String item_type,
            String price,
            String quality){

        this.user_id = user_id;
        this.auth_token = auth_token;
        this.name = name;
        this.description = description;
        this.item_type = item_type;
        this.price = price;
        this.quality = quality;

    }
}
