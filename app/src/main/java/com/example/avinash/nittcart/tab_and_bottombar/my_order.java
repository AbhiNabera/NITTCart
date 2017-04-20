package com.example.avinash.nittcart.tab_and_bottombar;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.avinash.nittcart.R;
import com.example.avinash.nittcart.display_item_files.Cart_DetailActivity;
import com.example.avinash.nittcart.response_classes.cart_Item_structure;
import com.example.avinash.nittcart.retrofit_api_calls.ApiObservable;
import com.google.gson.JsonObject;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by AVINASH on 4/14/2017.
 */
public class my_order extends AppCompatActivity {

    private ApiObservable apiObservable;
    Call<JsonObject> load_order;
    CatLoadingView mView;

    private ArrayList<cart_Item_structure> Cart_ItemList= new ArrayList<>();
    @Bind(R.id.closecart)
    ImageButton closecart;
    @Bind(R.id.home)
    Button home;
    @Bind(R.id.reload)
    TextView reload;
    @Bind(R.id.hidecartview)
    RelativeLayout hidecartview;
    @Bind(R.id.cart_items_scroll)
    ScrollView cart_scrollview;
    @Bind(R.id.order_recycler)
    RecyclerView cart_recycler;

    String USER_ID;
    String AUTH_TOKEN;
    myorder_adpter adapter;
    List<cart_Item_structure> MY_ORDER_LIST = Collections.emptyList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();
        setContentView(R.layout.my_order);
        ButterKnife.bind(this);

        mView = new CatLoadingView();
        apiObservable = new ApiObservable();
        final Intent intent = getIntent();

        USER_ID = intent.getStringExtra("user_id");
        AUTH_TOKEN= intent.getStringExtra("auth_token");

        adapter = new myorder_adpter(Cart_ItemList, my_order.this, new myorder_adpter.MyAdapterListener() {
            @Override
            public void deletefromCart(View v, int position) {

            }

            @Override
            public void onImageSelected(View v, int pos) {
                detailtransition(v,Cart_ItemList.get(pos));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.show(getSupportFragmentManager(), "LOADING..");
                load_my_items(intent.getStringExtra("user_id"),intent.getStringExtra("auth_token"));
            }
        }, 700);


        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView.show(getSupportFragmentManager(), "");
                load_my_items(intent.getStringExtra("user_id"), intent.getStringExtra("auth_token"));
            }
        });

        closecart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
    }

    public void load_my_items(String user_id, String auth_token){
        load_order = apiObservable.get_my_orders(user_id, auth_token);
        load_order.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mView.dismiss();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().toString());
                        Log.d("CART Response", response.body().toString());
                        int respone_code = (int) object.get("status_code");
                        if (respone_code == 200) {
                            //TODO: Set order Items here
                            Snackbar.make(cart_scrollview, "Data received!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        } else {
                            Snackbar.make(cart_scrollview, "Error Occured!!", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mView.dismiss();
                    Snackbar.make(cart_scrollview, "Some error Occured!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mView.dismiss();
                Snackbar.make(cart_scrollview, "Network error!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation( my_order.this,
                imagePair);
        Log.d("aaya", "debug");
        ActivityCompat.startActivity(my_order.this, transitionIntent, options.toBundle());

    }

    @Override
    public void onBackPressed(){
        if (mView.isVisible()) {
            mView.dismiss();
            load_order.cancel();
        }
        else {
            super.onBackPressed();
            finishAfterTransition();
        }
    }
}
