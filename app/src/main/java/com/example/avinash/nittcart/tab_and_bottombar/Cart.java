package com.example.avinash.nittcart.tab_and_bottombar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.avinash.nittcart.MainActivity;
import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.display_item_files.Cart_DetailActivity;
import com.example.avinash.nittcart.display_item_files.Cart_DetailActivity$$ViewBinder;
import com.example.avinash.nittcart.display_item_files.Item;
import com.example.avinash.nittcart.response_classes.cart_Item_list_response;
import com.example.avinash.nittcart.response_classes.cart_Item_structure;
import com.example.avinash.nittcart.response_classes.dashboard_Item_structure;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AVINASH on 3/26/2017.
 */
public class Cart extends AppCompatActivity {

    private ApiObservable apiObservable;
    Call<cart_Item_list_response> cart_item;
    Call<JsonObject> cart_item_delete;
    Call<JsonObject> cart_checkout;

    CatLoadingView mView;
    private List<cart_Item_structure> Cart_ItemList= Collections.emptyList();
    private int position_to_delete;
    private cart_adpter adpter;
    String USER_ID,AUTH_TOKEN;

    @Bind(R.id.home)
    Button home;
    @Bind(R.id.closecart)
    ImageButton closecart;
    @Bind(R.id.reload)
    TextView reload;
    @Bind(R.id.hidecartview)
    RelativeLayout hidecartview;
    @Bind(R.id.cart_items_scroll)
    RelativeLayout cart_scrollview;
    @Bind(R.id.cart_recycler)
    RecyclerView cart_recycler;
    @Bind(R.id.checkout)
    Button checkout;

    private int ORDER_TOTAL, i = 0;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.cart);
        ButterKnife.bind(this);
        mView = new CatLoadingView();
        mView.setCancelable(false);
        apiObservable = new ApiObservable();
        final Intent intent = getIntent();

        USER_ID = intent.getStringExtra("user_id");
        AUTH_TOKEN = intent.getStringExtra("auth_token");

        adpter = new cart_adpter(Cart_ItemList, this, new cart_adpter.MyAdapterListener() {
            @Override
            public void deletefromCart(View v, int position) {
                String item_id = Cart_ItemList.get(position).getItemId();
                position_to_delete = position;
                delete_from_cart(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"), item_id);
            }

            @Override
            public void onImageSelected(View v, int pos) {
                //TODO detail trans
                detailtransition(v, Cart_ItemList.get(pos));
            }
        });

        //RecyclerView.setItemAnimator(RecyclerView.ItemAnimator animator)
        cart_recycler.setLayoutManager(new GridLayoutManager(this, 1));
        cart_recycler.setAdapter(adpter);
        cart_recycler.setNestedScrollingEnabled(false);
        //cart_scrollview.setVisibility(View.VISIBLE);
        //hidecartview.setVisibility(View.GONE);

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               mView.show(getSupportFragmentManager(), "LOADING..");
               loadCart(intent.getStringExtra("user_id"),intent.getStringExtra("auth_token"));
           }
       }, 700);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "");
                loadCart(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"));
            }
        });

        closecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
                //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder( Cart.this);
                final AlertDialog alert = dialog.create();
                final View view = getLayoutInflater().inflate( R.layout.checkout_view, null);
                dialog.setView(view);
                TextView tv = (TextView)view.findViewById(R.id.place_order);
                Button tv1 = (Button)view.findViewById(R.id.total);

                while (i<Cart_ItemList.size()){
                    ORDER_TOTAL += Cart_ItemList.get(i).getPrice();
                    i++;
                }
                tv1.setText("ORDER TOTAL: ₹" + ORDER_TOTAL);
                if (Cart_ItemList.isEmpty()) {
                    Snackbar.make(cart_scrollview, "Your Cart is Empty!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    dialog.show();
                }

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Cart_ItemList.isEmpty()) {
                            Snackbar.make(view, "Your Cart is Empty!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            alert.dismiss();
                            checkout(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"));
                        }
                    }
                });


            }
        });
    }

    public void detailtransition(View v,cart_Item_structure item){

        Intent transitionIntent = new Intent(this, Cart_DetailActivity.class);
        transitionIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        transitionIntent.putExtra("item_id", item.getItemId());
        transitionIntent.putExtra("item_name", item.getName());
        transitionIntent.putExtra("item_price", item.getPrice());
        transitionIntent.putExtra("item_description", item.getDescription());
        transitionIntent.putExtra("item_quality", item.getQuality());
        transitionIntent.putExtra("item_status",item.getStatus());
        transitionIntent.putExtra("item_image", item.getImage());
        transitionIntent.putExtra("user_id", USER_ID);
        transitionIntent.putExtra("auth_token", AUTH_TOKEN);

        ImageView placeImage = (ImageView) v.findViewById(R.id.item_image);
        TextView placeNameHolder = (TextView) v.findViewById(R.id.item_name);
        Pair<View, String> imagePair = Pair.create((View) v, "tImage");
        //Pair<View, String> holderPair = Pair.create((View) placeNameHolder, "tNameHolder");
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( Cart.this,
                imagePair);
        Log.d("aaya", "debug");
        ActivityCompat.startActivity(Cart.this, transitionIntent, options.toBundle());

    }

    private void setupWindowAnimations() {
        Slide fade = new Slide();
        fade.setDuration(500);
        fade.setSlideEdge(Gravity.BOTTOM);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.BOTTOM);

        slide.setDuration(700);
        getWindow().setEnterTransition(slide);
        getWindow().setReturnTransition(fade);
    }

    public void loadCart(String user_id, String auth_token){
        cart_item = apiObservable.getCartView(user_id,auth_token);
        cart_item.enqueue(new Callback<cart_Item_list_response>() {
            @Override
            public void onResponse(Call<cart_Item_list_response> call, Response<cart_Item_list_response> response) {
                mView.dismiss();
                if (response.isSuccessful()) {
                    cart_Item_list_response list_response = response.body();
                    Log.d("cart_response",response.body().getData().size()+"");
                    if (list_response.getStatusCode() == 200) {
                        Cart_ItemList = list_response.getData();
                        if (Cart_ItemList.size() == 0) {
                            remove_recycler();
                            Snackbar.make(cart_scrollview, "Your Cart is Empty!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        else {
                            adpter.removeAll();
                            adpter.notifyDataSetChanged();
                            adpter.set_new_list(Cart_ItemList);
                            adpter.notifyDataSetChanged();
                            show_recycler();
                        }
                    } else {
                        Snackbar.make(cart_scrollview, "Bad Request!!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(cart_scrollview, "Some error occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<cart_Item_list_response> call, Throwable t) {
                mView.dismiss();
                remove_recycler();
                Snackbar.make(cart_scrollview, "Network Error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void show_recycler(){
        hidecartview.setVisibility(View.GONE);
        reload.setVisibility(View.GONE);
        //cart_recycler.setVisibility(View.VISIBLE);
        cart_scrollview.setVisibility(View.VISIBLE);
    }

    public void remove_recycler(){
        Log.d("inside","remove recycler");
        //cart_recycler.setVisibility(View.GONE);
        cart_scrollview.setVisibility(View.GONE);
        hidecartview.setVisibility(View.VISIBLE);
        hidecartview.setVisibility(View.VISIBLE);
    }

    public void delete_from_cart(String user_id, String auth_token,String item_id){
        mView.show(getSupportFragmentManager(), "");
        cart_item_delete = apiObservable.delete_from_cart(user_id, auth_token, item_id);
        cart_item_delete.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mView.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("CART Response", object.toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            Cart_ItemList.remove(position_to_delete);
                            adpter.notifyDataSetChanged();
                            if(Cart_ItemList.size()==0){
                                remove_recycler();
                            }
                            Snackbar.make(cart_scrollview, "Successfully removed!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(cart_scrollview, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(cart_scrollview, "Network error!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void checkout(String user_id, String auth_token){
        mView.show(getSupportFragmentManager(), "");
        cart_checkout = apiObservable.checkout(user_id,auth_token);
        cart_checkout.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mView.dismiss();
                if (response.isSuccessful()) {
                    try {Log.d("checkout",response.body().toString());
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("CART Checkout Response", response.body().toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            //TODO: MOVE to pllaceorder activity and call clear cart here
                            Snackbar.make(cart_scrollview, "Order placed!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            finishAfterTransition();
                        } else {
                            Snackbar.make(cart_scrollview, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(cart_scrollview, "Network error!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mView.isVisible()) {
            mView.dismiss();
            cart_item.cancel();
        }
        else {
            super.onBackPressed();
            finishAfterTransition();
            //overridePendingTransition(R.anim.activity_slide_down, R.anim.activity_no_slide_tabitem);
        }
    }
}
