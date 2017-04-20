package com.example.avinash.nittcart.retrofit_api_calls;

import android.content.Context;

import com.example.avinash.nittcart.response_classes.Item_list_response;

/**
 * Created by AVINASH on 4/5/2017.
 */
public interface ApiCalls {

    void loaditems(String user_id, String auth_token, String name,
                   String subject, String price, String quality);

    void first_loaditems(String user_id, String auth_token, String name,
                   String subject, String price, String quality);


    void RefreshItems();

    void Searchitems(String query);

    void onErrorLoading();

    void onCompleteLoading(Item_list_response list_response,Context context);
}
