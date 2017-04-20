package com.example.avinash.nittcart.retrofit_api_calls;


import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.response_classes.cart_Item_list_response;
import com.example.avinash.nittcart.response_classes.dashboard_Item_list_response;
import com.example.avinash.nittcart.tab_and_bottombar.postadd_body;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.HashMap;
import java.util.Observable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;

/**
 * Created by AVINASH on 3/22/2017.
 */
public class ApiObservable {

    private Retrofit mRetrofit;
    private ApiClient apibuilder;
    private ApiService apiInterface;

    public ApiObservable() {
        apibuilder= apibuilder.getInstance();
        mRetrofit = apibuilder.getRetrofit();
        apiInterface = mRetrofit.create(ApiService.class);
    }


    public Call<JsonObject> getStudentInfo(String userid, String auth_token) {
        return apiInterface.getStudentInfo(userid, auth_token);
    }

    public Call<JsonObject> postStudentInfo( String userid,String auth_token,String user_email, String name,String address,String contact) {
        return apiInterface.postStudentInfo(userid, auth_token, user_email, name, address, contact);
    }

    public Call<JsonObject> loginApi(String rollno,String password) {
        return apiInterface.login(rollno, password);
    }

    public Call<JsonObject> logout(String auth_token,String user_id){
        return apiInterface.logout(auth_token, user_id);
    }

    public rx.Observable<Item_list_response> loadItems( HashMap<String,String> field){
        return  apiInterface.loadItem(field);
    }

    public Call<cart_Item_list_response> getCartView(String user_id, String auth_token) {
        return apiInterface.getCartView(user_id, auth_token);
    }

    public Call<JsonObject> get_my_orders(String user_id, String auth_token) {
        return apiInterface.load_placed_item(user_id, auth_token);
    }

    public Call<JsonObject> postItem(HashMap<String,RequestBody> body,MultipartBody.Part image){
        return apiInterface.addItem(body, image);
    }

    public Call<JsonObject> editItem(HashMap<String,String> body,MultipartBody.Part image){
        return apiInterface.editItem(body);
    }

    public Call<JsonObject> edit_image_Item(HashMap<String,RequestBody> body,MultipartBody.Part image){
        return apiInterface.edit_image_Item(body, image);
    }

    public Call<JsonObject> add_to_Cart(String user_id,String auth_token,String item_id){
        return apiInterface.add_to_cart(user_id, auth_token, item_id);
    }

    public Call<dashboard_Item_list_response> getdashboard(String user_id, String auth_token){
        return apiInterface.getDashboard(user_id, auth_token);
    }

    public Call<JsonObject> delete_dashboard_item(String user_id, String auth_token, String item_id){
        return apiInterface.delete_dashboard_item(user_id, auth_token, item_id);
    }

    public Call<JsonObject> delete_from_cart(String user_id, String auth_token, String item_id){
        return apiInterface.delete_from_cart(user_id, auth_token, item_id);
    }

    public Call<JsonObject> checkout(String user_id, String auth_token){
        return apiInterface.checkout_from_cart(user_id, auth_token);
    }

    public rx.Observable<Item_list_response> getSearchResults(HashMap<String, String> map) {
        return apiInterface.getSearchResults(map);
    }


    public Call<JsonObject> get_seller_info(String user_id, String auth_token, String item_id){
        return apiInterface.get_seller_info(user_id, auth_token, item_id);
    }

    public Call<JsonObject> get_buyer_info(String user_id, String auth_token, String item_id){
        return apiInterface.get_buyer_info(user_id, auth_token, item_id);
    }
}
