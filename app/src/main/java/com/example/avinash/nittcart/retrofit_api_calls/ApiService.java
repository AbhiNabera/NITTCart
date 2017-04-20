package com.example.avinash.nittcart.retrofit_api_calls;

import android.util.Log;

import com.example.avinash.nittcart.response_classes.Item_list_response;
import com.example.avinash.nittcart.response_classes.cart_Item_list_response;
import com.example.avinash.nittcart.response_classes.dashboard_Item_list_response;
import com.example.avinash.nittcart.tab_and_bottombar.postadd_body;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import rx.Observable;
import retrofit2.http.Query;

/**
 * Created by AVINASH on 3/22/2017.
 */
public interface ApiService {

    @FormUrlEncoded
    @POST("/api/item/page")
    Observable<Item_list_response> getSearchResults(@FieldMap HashMap<String,String> map);

    //--------------------------login--------------------------------
    @FormUrlEncoded
    @POST("/api/login")
    Call<JsonObject> login(@Field("roll_no") String rollno,
                           @Field("password") String password);

    //--------------------------logout--------------------------------
    @FormUrlEncoded
    @POST("/api/logout")
    Call<JsonObject> logout(@Field("auth_token") String auth_token,@Field("user_id") String user_id);


    //-----------------------student info----------------------
    @FormUrlEncoded
    @POST("/api/user/profile")
    Call<JsonObject> getStudentInfo(@Field("user_id") String user_id,
                                    @Field("auth_token") String auth_token);

    //-----------------------student info----------------------

    @FormUrlEncoded
    @POST("/api/user/update")
    Call<JsonObject> postStudentInfo(@Field("user_id") String user_id,
                                     @Field("auth_token") String auth_token,
                                     @Field("email") String user_email,
                                     @Field("name") String name,
                                     @Field("address") String address,
                                     @Field("contact_no") String contact_no);

    //----------------------observable for item list(filter included)-----------------
    @FormUrlEncoded
    @POST("/api/item/page")
    Observable<Item_list_response> loadItem(@FieldMap HashMap<String,String> field);


    //------------------------------POST_ITEM-------------------------------------------
    //@FormUrlEncoded
    @Multipart
    @POST("/api/item/add")
    Call<JsonObject> addItem(@PartMap Map<String, RequestBody> fields,@Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("/api/item/edit")
    Call<JsonObject> editItem(@FieldMap Map<String, String> fields);

    @Multipart
    @POST("/api/item/edit")
    Call<JsonObject> edit_image_Item(@PartMap Map<String, RequestBody> fields,@Part MultipartBody.Part image);

    //----------------------------------------dashboard view--------------------------------
    @FormUrlEncoded
    @POST("/api/user/dashboard")
    Call<dashboard_Item_list_response> getDashboard(@Field("user_id") String user_id,
                                                    @Field("auth_token") String auth_token);

    //----------------------------------------dashboard view--------------------------------
    @FormUrlEncoded
    @POST("/api/item/delete")
    Call<JsonObject> delete_dashboard_item(@Field("user_id") String user_id,
                                           @Field("auth_token") String auth_token,
                                           @Field("item_id") String item_id);

    //-------------------------CART VIEW-----------------------------------------------
    @FormUrlEncoded
    @POST("/api/cart/view")
    Call<cart_Item_list_response> getCartView(@Field("user_id") String user_id,
                                 @Field("auth_token") String auth_token);

    //----------------------------------ADD_TO_CART------------------------------------------
    @FormUrlEncoded
    @POST("/api/cart/add")
    Call<JsonObject> add_to_cart(@Field("user_id") String user_id,
                                 @Field("auth_token") String auth_token,
                                 @Field("item_id") String item_id);

    //-----------------------------------------delete_from_cart------------------------------
    @FormUrlEncoded
    @POST("/api/cart/delete")
    Call<JsonObject> delete_from_cart(@Field("user_id") String user_id,
                                      @Field("auth_token") String auth_token,
                                      @Field("item_id") String item_id);

    //------------------------------checkout-------------------------------------------------
    @FormUrlEncoded
    @POST("/api/cart/checkout")
    Call<JsonObject> checkout_from_cart(@Field("user_id") String user_id,
                                        @Field("auth_token") String auth_token);

    //--------------------------------firebase notification----------------------------------
    @FormUrlEncoded
    @POST("/api/user/my_orders")
    Call<JsonObject> load_placed_item(@Field("user_id") String user_id,
                                      @Field("auth_token") String auth_token);

    //--------------------------------seller info----------------------------------
    @FormUrlEncoded
    @POST("/api/item/buyer")
    Call<JsonObject> get_seller_info(@Field("user_id") String user_id,
                                      @Field("auth_token") String auth_token,
                                     @Field("item_id") String item_id);

    //--------------------------------seller info----------------------------------
    @FormUrlEncoded
    @POST("/api/item/seller")
    Call<JsonObject> get_buyer_info(@Field("user_id") String user_id,
                                     @Field("auth_token") String auth_token,
                                     @Field("item_id") String item_id);
}
